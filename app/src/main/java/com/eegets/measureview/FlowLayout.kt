package com.eegets.measureview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

/**
 * @packageName: com.eegets.measureview
 * @author: eegets
 * @date: 2020/11/17
 * @description: TODO
 */
class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr){
    //存放容器中所有的View[N行M列]
    private val mAllViews: MutableList<MutableList<View>> = mutableListOf()

    //存放每一行最高View的高度
    private val mPerLineMaxHeight = mutableListOf<Int>()

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams? {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return MarginLayoutParams(context, attrs)
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        //获取容器中子View的个数
        val childCount = childCount
        //记录每一行View的总宽度
        var totalLineWidth = 0
        //记录每一行最高View的高度
        var perLineMaxHeight = 0
        //记录当前ViewGroup的总高度
        var totalHeight = 0

        Log.d("TAG", "onMeasure childCount = $childCount")

        for (index in 0 until childCount) {
            val childView = getChildAt(index)
            measureChild(childView, widthMeasureSpec, heightMeasureSpec)
            val lp = childView.layoutParams as MarginLayoutParams
            //获得子View的测量宽度
            val childWidth = childView.measuredWidth + lp.leftMargin + lp.rightMargin
            //获得子VIew的测量高度
            val childHeight = childView.measuredHeight + lp.topMargin + lp.bottomMargin
//            Log.d("TAG", "onMeasure totalLineWidth=$totalLineWidth, childWidth=$childWidth, totalLineWidth + childWidth = ${totalLineWidth + childWidth}, widthSize=$widthSize")
            if (totalLineWidth + childWidth > widthSize) {
                //统计总高度
                totalHeight += perLineMaxHeight
                //开启新的一行
                totalLineWidth = childWidth
                perLineMaxHeight = childHeight
                Log.d("TAG", "onMeasure true totalLineWidth=$totalLineWidth, perLineMaxHeight=$perLineMaxHeight, childHeight=$childHeight")
            } else {
                //记录每一行的总宽度
                totalLineWidth += childWidth
                //比较每一行最高的View
                perLineMaxHeight = Math.max(perLineMaxHeight, childHeight)
                Log.d("TAG", "onMeasure false totalLineWidth=$totalLineWidth, perLineMaxHeight=$perLineMaxHeight, childHeight=$childHeight")
            }
            //当该View已是最后一个View时，将该行最大高度添加到totalHeight中
            if (index == childCount - 1) {
                totalHeight += perLineMaxHeight
            }

            //如果高度的测量模式是EXACTLY，则高度用测量值，否则用计算出来的总高度（这时高度的设置为wrap_content）
            heightSize = if (heightMode == MeasureSpec.EXACTLY) heightSize else totalHeight
//            Log.d(
//                "TAG",
//                "onMeasure childMargin measuredWidth = $childWidth, leftMargin = ${lp.leftMargin}, rightMargin = ${lp.rightMargin}, totalHeight=$totalHeight, heightSize=$heightSize"
//            )
            setMeasuredDimension(widthSize, heightSize)
        }
    }

    /**
     * 摆放控件
     * 通过循环并通过‘totalLineWidth + childWidth > width’进行宽度比较将我们的子View存储到lineViews中，也就是一列能装几个子View
     * 同样通过循环将每一行显示的子View的lineViews存储到MAllViews中，mAllViews中存储了n行lineViews列(每列的个数可能不一致)组成的数组
     *
     * 最后通过遍历mAllViews和lineViews得到子View并通过`childView.layout(leftChild, topChild, rightChild, bottomChild)`摆放到合适的位置
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAllViews.clear()
        mPerLineMaxHeight.clear()

        //存放每一行的子View
        var lineViews = mutableListOf<View>()
        //记录每一行已存放View的总宽度
        var totalLineWidth = 0

        //记录每一行最高View的高度
        var lineMaxHeight = 0

        /*************遍历所有View，将View添加到List<List></List><View>>集合中</View> */
        Log.d("TAG", "onLayout ")
        //获得子View的总个数
        val childCount = childCount
        for (i in 0 until childCount) {
            val childView: View = getChildAt(i)
            val lp = childView.layoutParams as MarginLayoutParams
            val childWidth: Int = childView.measuredWidth + lp.leftMargin + lp.rightMargin
            val childHeight: Int = childView.measuredHeight + lp.topMargin + lp.bottomMargin
            Log.d("TAG", "onLayout width=$width, totalLineWidth=$totalLineWidth, childWidth=$childWidth, totalLineWidth + childWidth=${totalLineWidth + childWidth}")
            if (totalLineWidth + childWidth > width) {
                mAllViews.add(lineViews)
                mPerLineMaxHeight.add(lineMaxHeight)
                //开启新的一行
                totalLineWidth = childWidth
                lineMaxHeight = childHeight
                lineViews = mutableListOf()
                Log.d("TAG", "onLayout true lineViews size=${lineViews.size}, mAllViews size=${mAllViews.size}")
            } else {
                totalLineWidth += childWidth
                Log.d("TAG", "onLayout false lineViews size=${lineViews.size}, mAllViews size=${mAllViews.size}")
            }
            lineViews.add(childView)
            lineMaxHeight = Math.max(lineMaxHeight, childHeight)
        }
        //单独处理最后一行
        mAllViews.add(lineViews)
        mPerLineMaxHeight.add(lineMaxHeight)
        Log.d(
            "TAG",
            "onLayout mAllViews size=${mAllViews.size}, mPerLineMaxHeight size=${mPerLineMaxHeight.size}, lineViews size=${lineViews.size}"
        )

        /************遍历集合中的所有View并显示出来 */
        //表示一个View和父容器左边的距离
        var mLeft = 0
        //表示View和父容器顶部的距离
        var mTop = 0
        for (i in 0 until mAllViews.size) {
            //获得每一行的所有View
            lineViews = mAllViews[i]
            lineMaxHeight = mPerLineMaxHeight[i]
            for (j in lineViews.indices) {
                val childView: View = lineViews[j]
                val lp = childView.layoutParams as MarginLayoutParams
                val leftChild = mLeft + lp.leftMargin
                val topChild = mTop + lp.topMargin
                val rightChild: Int = leftChild + childView.measuredWidth
                val bottomChild: Int = topChild + childView.measuredHeight
                //四个参数分别表示View的左上角和右下角
                childView.layout(leftChild, topChild, rightChild, bottomChild)
                mLeft += lp.leftMargin + childView.measuredWidth + lp.rightMargin
            }
            mLeft = 0
            mTop += lineMaxHeight
        }
    }
}