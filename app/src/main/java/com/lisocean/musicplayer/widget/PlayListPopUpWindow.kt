package com.lisocean.musicplayer.widget

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.ObservableArrayList
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.PopupWindow
import com.lisocean.musicplayer.R
import com.lisocean.musicplayer.model.data.local.SongInfo
import com.lisocean.musicplayer.ui.base.adapter.SingleTypeAdapter
import com.lisocean.musicplayer.ui.presenter.ItemClickPresenter
import org.jetbrains.anko.find
import org.jetbrains.anko.linearLayout

class PlayListPopUpWindow (private val window : Window, context : Context, listTemp: List<SongInfo>) : PopupWindow(),
    ItemClickPresenter<SongInfo> {
    override fun onItemClick(v: View?, item: SongInfo) {
        when(v?.id){

        }
    }


    val list = ObservableArrayList<SongInfo>()

    //inner recycler view adapter
    private val adapter by lazy {
        SingleTypeAdapter<SongInfo>(
            context,
            R.layout.item_popupmusic,
            list).apply {
            itemPresenter = this@PlayListPopUpWindow
        }
    }
    //记录当前应用程序窗体透明度
    var alpha: Float = 1.0f
    init {

        contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow, null , false)

        alpha = window.attributes.alpha
        animationStyle = R.style.pop
        //width and height(3/5 of screen)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        manager.defaultDisplay.getSize(point)
        height = (point.y * 3) / 5
        //focusable
        isFocusable = true
        //outside click
        isOutsideTouchable = true
        //TODO : List adapter
        list.addAll(listTemp)
        contentView.find<RecyclerView>(R.id.popUp_recyclerView).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PlayListPopUpWindow.adapter
        }
        contentView.find<View>(R.id.del_all)

    }

    override fun showAsDropDown(anchor: View?, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        // alpha
        val attributes = window.attributes
        attributes.alpha = 0.3f
        window.attributes = attributes
    }

    override fun dismiss() {
        super.dismiss()
        //hide popwindows change windows alpha
        val attributes = window.attributes
        attributes.alpha = alpha
        window.attributes = attributes
    }
}