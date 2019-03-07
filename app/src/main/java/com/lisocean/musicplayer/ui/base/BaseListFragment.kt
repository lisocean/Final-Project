package com.lisocean.musicplayer.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseListFragment<ITEMBEAN, ITEMVIEW> : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView()
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initListener()
        initData()
    }
    protected open fun init(){}

    /**
     * 获取布局view
     */
    abstract fun initView(): View?

    /**
     * 数据初始化
     */
    protected open fun initData(){}

    /**
     * adapter和 listener 初始化
     */
    protected open fun initListener() {}
}