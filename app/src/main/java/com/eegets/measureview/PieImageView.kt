package com.eegets.measureview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.ViewCompat

/**
 * @packageName: com.eegets.measureview
 * @author: eegets
 * @date: 2020/11/11
 * @description: TODO
 */
class PieImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var progress: Int = 0
    private val MAX_PROGRESS: Int = 100
    private var arcPaint: Paint? = null
    private var circlePaint: Paint? = null
    private var bound: RectF? = RectF()

    fun setProgress(progress: Int) {
        this.progress = progress
        ViewCompat.postInvalidateOnAnimation(this)
    }

    init {
        arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arcPaint?.style = Paint.Style.FILL_AND_STROKE
        arcPaint?.strokeWidth = dpToPixel(0.1f, context)
        arcPaint?.color = Color.RED

        circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint?.style = Paint.Style.STROKE
        circlePaint?.strokeWidth = dpToPixel(2f, context)
        circlePaint?.color = Color.argb(120, 0xff, 0xff, 0xff)
    }

    //布局加载完成执行
    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.d("TAG", "onFinishInflate")
    }

    //布局控件大小发生变化时调用，只在初始化执行一次
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val min = Math.min(w, h)
        val max = w + h - min
        val r = Math.min(w, h) / 2
        Log.d(
            "TAG",
            "onSizeChanged w = $w, h = $h, oldW = $oldw, oldH = $oldh, min = $min, max = $max, r = $r"
        )
        val left = ((max shr 1) - r).toFloat()
        val top = ((min shr 1) - r).toFloat()
        val right = ((max shr 1) + r).toFloat()
        val bottom = ((min shr 1) + r).toFloat()
        bound?.set(left, top, right, bottom)
        Log.d(
            "TAG",
            "onSizeChanged bound left = $left, top = $top, right = $right, bottom = $bottom"
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //宽度测量模式
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        Log.d(
            "TAG",
            "MeasureSpecMode MeasureSpec.AT_MOST = ${MeasureSpec.AT_MOST}, MeasureSpec.EXACTLY = ${MeasureSpec.EXACTLY}, MeasureSpec.UNSPECIFIED = ${MeasureSpec.UNSPECIFIED}"
        )

        Log.d(
            "TAG",
            "MeasureSpecMode MeasureSpec.AT_MOST = ${MeasureSpec.AT_MOST}, MeasureSpec.EXACTLY = ${MeasureSpec.EXACTLY}, MeasureSpec.UNSPECIFIED = ${MeasureSpec.UNSPECIFIED}"
        )
        Log.d(
            "TAG",
            "widthMode widthMode = $widthMode, heightMode = $heightMode"
        )

        // 判断是wrap_content的测量模式
        if (MeasureSpec.AT_MOST == widthMode || MeasureSpec.AT_MOST == heightMode) {
            val measuredWidth = MeasureSpec.getSize(widthMeasureSpec)
            val measuredHeight = MeasureSpec.getSize(heightMeasureSpec)
            // 将宽高设置为传入宽高的最小值
            val size = if (measuredWidth > measuredHeight) measuredHeight else measuredWidth
            // 调用setMeasuredDimension设置View实际大小
            setMeasuredDimension(size, size)
            Log.d(
                "TAG",
                "onMeasure +++++ measuredWidth = $size, measureHeight = $size"
            )
        } else { //如果不是wrap_content则还是使用父布局的实现方式
            setMeasuredDimension(
                getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
                getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
            )
            Log.d(
                "TAG",
                "onMeasure ----- defaultMeasuredWidth = ${
                    getDefaultSize(
                        suggestedMinimumWidth,
                        widthMeasureSpec
                    )
                }, defaultMeasuredHeight = ${
                    getDefaultSize(
                        suggestedMinimumHeight,
                        heightMeasureSpec
                    )
                }"
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("TAG", "onDraw")
        if (progress != MAX_PROGRESS && progress != 0) {
            val angle = progress * 360f / MAX_PROGRESS
            canvas?.drawArc(bound!!, 270f, angle, true, arcPaint!!)
            canvas?.drawCircle(
                bound?.centerX()!!,
                bound?.centerY()!!,
                bound?.height()!! / 2,
                circlePaint!!
            )
        }
    }
}