package com.eegets.measureview

import android.content.Context

/**
 * @packageName: com.eegets.measureview
 * @author: eegets
 * @date: 2020/11/12
 * @description: TODO
 */

private var scale = 0f
fun dpToPixel(dp: Float, context: Context): Float {
    if (scale == 0f) {
        scale = context.resources.displayMetrics.density
    }
    return (dp * scale)
}