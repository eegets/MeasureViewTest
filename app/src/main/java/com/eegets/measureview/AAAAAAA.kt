package com.eegets.measureview

import android.util.Log

/**
 * @packageName: com.eegets.measureview
 * @author: eegets
 * @date: 2020/11/18
 * @description: TODO
 */

inline fun sum(a: Int, b: Int, crossinline lambda: (result: Int) -> Unit): Int {
    val r = a + b
    lambda.invoke(r)
    Log.d("TAG", "BBBBBBBBBB")
    return r
}

fun main(args: Array<String>) {
    sum(1, 2) {

        Log.d("TAG", "Result is: $it")
//        return  // 编译错误: return is not allowed here
    }
}