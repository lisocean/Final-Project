package com.lisocean.musicplayer.ui.search

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.google.android.flexbox.*
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.databinding.ActivitySearchBinding
import com.lisocean.musicplayer.extension.MultipleStatusView
import com.lisocean.musicplayer.helper.utils.CleanLeakUtils
import com.lisocean.musicplayer.helper.constval.ErrorStatus
import com.lisocean.musicplayer.helper.StatusBarUtil
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.base.adapter.SingleTypeAdapter
import com.lisocean.musicplayer.ui.base.adapter.animators.ScaleInItemAnimator
import com.lisocean.musicplayer.ui.musicplaying.MusicPlayingActivity
import com.lisocean.musicplayer.ui.presenter.ItemClickPresenter
import com.lisocean.musicplayer.ui.search.dependencies.SearchContract
import com.lisocean.musicplayer.ui.search.dependencies.ViewAnimUtils
import com.lisocean.musicplayer.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity(),  SearchContract.View, ItemClickPresenter<SongInfo> {

    override fun onItemClick(v: View?, item: SongInfo) {
        val intent = Intent(this, MusicPlayingActivity::class.java)
        intent.putExtra("list", mViewModel.songs)
        intent.putExtra("isPlaying",false)
        startActivity(intent)
    }

    /**
     * 多种状态的 View 的切换
     */
    private var mLayoutStatusView: MultipleStatusView? = null
    private val mViewModel by viewModel<SearchViewModel>()
    private val mBinding by lazy {
        DataBindingUtil.setContentView<ActivitySearchBinding>(this, R.layout.activity_search)
    }

    private val mAdapter by lazy {
        SingleTypeAdapter<SongInfo>(
            this,
            R.layout.item_searchresult,
            mViewModel.songs).apply {
            itemPresenter = this@SearchActivity
        }
    }
    private val hotAdapter by lazy {
        SingleTypeAdapter<String>(
            this,
            R.layout.item_flow_text,
            mViewModel.searchHot).apply {
            onBindItem { v, item ->
                v.setOnClickListener {
                    mViewModel.text.set(item)
                    start()
                }
            }
            itemAnimator = ScaleInItemAnimator(
                from = 0.5f,
                duration = 1000L,
                interpolator = DecelerateInterpolator())
        }
    }
    val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vm = mViewModel

        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
        setUpEnterAnimation() // 入场动画
        setUpExitAnimation() // 退场动画
        tv_cancel.setOnClickListener { onBackPressed() }


        et_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeSoftKeyboard()
                    if (mViewModel.text.get().isNullOrEmpty()) {
                        showHotWordView()
                    } else {
                        start()
                    }
                }
                return false
            }

        })
        mRecyclerView_result.apply {
            layoutManager =LinearLayoutManager(this@SearchActivity)
            adapter = mAdapter

        }

        //set search hot
        showHotWordView()

        mRecyclerView_hot.layoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP      //按正常方向换行
            flexDirection = FlexDirection.ROW   //主轴为水平方向，起点在左端
            alignItems = AlignItems.CENTER    //定义项目在副轴轴上如何对齐
            justifyContent = JustifyContent.FLEX_START  //多个轴对齐方式
        }
        mRecyclerView_hot.adapter = hotAdapter


        mLayoutStatusView = multipleStatusView

        //状态栏透明和间距处理
        StatusBarUtil.darkMode(this)
        StatusBarUtil.darkModeForM(window, true)
        StatusBarUtil.setPaddingSmart(this, toolbar)

    }

    fun start(){
        setSearchResult()
        showLoading()
        mViewModel.text.get()?.let {
            mViewModel.search(it){error ->
                dismissLoading()
                error?.let {trueError->
                    showError(trueError.message ?: "", -1)
                }
            }
        }
    }

    override fun setSearchResult() {
        hideHotWordView()

    }

    override fun closeSoftKeyboard() {
        closeKeyBord(et_search_view, applicationContext)
    }


    /**
     * 设置热门关键词
     */
    override fun setHotWordData(string: ArrayList<String>) {}
    override fun showError(errorMsg: String, errorCode: Int) {
        toast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }
    /**
     * 没有找到相匹配的内容
     */
    override fun setEmptyView() {
        toast("抱歉，没有找到相匹配的内容")
        hideHotWordView()
        mLayoutStatusView?.showEmpty()
    }
    /**
     * 隐藏热门关键字的 View
     */
    private fun hideHotWordView(){
        layout_hot_words.visibility = View.GONE
        layout_content_result.visibility = View.VISIBLE
    }

    /**
     * 显示热门关键字的 流式布局
     */
    private fun showHotWordView(){
        layout_hot_words.visibility = View.VISIBLE
        layout_content_result.visibility = View.GONE

    }
    /**
     * 退场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExitAnimation() {
        val fade = Fade()
        window.returnTransition = fade
        fade.duration = 300
    }

    /**
     * 进场动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpEnterAnimation() {
        val transition = TransitionInflater.from(this)
            .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        })
    }

    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
    /**
     * 关闭软键盘
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
    private fun setUpView() {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animation.duration = 300
        rel_container.startAnimation(animation)
        rel_container.visibility = View.VISIBLE
        //打开软键盘
        openKeyBord(et_search_view, applicationContext)
    }
    /**
     * 展示动画
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow() {
        ViewAnimUtils.animateRevealShow(
            this, rel_frame,
            fab_circle.width / 2, R.color.backgroundColor,
            object : ViewAnimUtils.OnRevealAnimationListener {
                override fun onRevealHide() {}
                override fun onRevealShow() = setUpView()
            })
    }


    // 返回事件
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimUtils.animateRevealHide(
                this, rel_frame,
                fab_circle.width / 2, R.color.backgroundColor,
                object : ViewAnimUtils.OnRevealAnimationListener {
                    override fun onRevealHide() {
                        closeKeyBord(et_search_view, applicationContext)
                    }

                    override fun onRevealShow() {

                    }
                })
            super.onBackPressed()
        } else {
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()

    }
}