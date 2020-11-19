package com.eegets.measureview

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_flow.*

/**
 * @packageName: com.eegets.measureview
 * @author: eegets
 * @date: 2020/11/17
 * @description: TODO
 */
class FlowActivity :Activity() {

    private val list = mutableListOf("阿迪达斯", "李林", "耐克", "361", "海蓝之迷面霜", "coach", "fendi", "亚历山大短靴", "二手中古", "Ariete阿丽亚特", "ASH", "阿玛尼牛仔")
//    private val list = mutableListOf("阿迪达斯", "李林", "耐克", "361", "海蓝之迷面霜", "coach", "fendi", "亚历山大短靴")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
        addView()
    }

    private fun addView() {
        flowLayout.removeAllViews()
        list.forEach {
            val view = LayoutInflater.from(this).inflate(R.layout.item_flow, flowLayout, false) as TextView
            view.text = it
            flowLayout.addView(view)
        }
    }
}