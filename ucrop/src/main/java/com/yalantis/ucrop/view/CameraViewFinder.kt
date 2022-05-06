package com.yalantis.ucrop.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class CameraViewFinder : View {

    var viewFinderRect = RectF()
        private set

    private var finderSize: Int = 0
    private var cornerLength: Float = 0f
    private var cornerThickness: Float = 0f
    private var cornerColor: Int = 0
    private var shadowColor: Int = 0
    private var finderMarginBottom: Int = 0

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs, defStyleAttr)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun initView(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) {
        context.withStyledAttributes(attrs, R.styleable.CameraViewFinder) {
            finderSize = getDimensionPixelSize(
                R.styleable.CameraViewFinder_cameraViewFinderSize,
                DEFAULT_FINDER_SIZE_DP.dpToPx
            )
            cornerLength = getDimensionPixelSize(
                R.styleable.CameraViewFinder_cameraViewFinderCornerLength,
                DEFAULT_CORNER_LENGTH_DP.dpToPx
            ).toFloat()
            cornerThickness = getDimensionPixelSize(
                R.styleable.CameraViewFinder_cameraViewFinderCornerThickness,
                DEFAULT_CORNER_THICKNESS_DP.dpToPx
            ).toFloat()
            cornerColor =
                getColor(
                    R.styleable.CameraViewFinder_cameraViewFinderCornerColor,
                    DEFAULT_CORNER_COLOR
                )
            shadowColor =
                getColor(
                    R.styleable.CameraViewFinder_cameraViewShadowColor,
                    DEFAULT_SHADOW_COLOR
                )
            finderMarginBottom = getDimensionPixelSize(
                R.styleable.CameraViewFinder_cameraViewFinderBottomMargin,
                DEFAULT_FINDER_BOTTOM_MARGIN_DP.dpToPx
            )
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val newLayoutParams = layoutParams
        newLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        newLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams = newLayoutParams

        // положение, размер видоискателя
        val parentHalfWidth = MeasureSpec.getSize(widthMeasureSpec) / 2
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)
        val parentHalfHeight = parentHeight / 2
        val finderHalfSize = finderSize / 2

        val marginBottom =
            if (parentHalfHeight - finderHalfSize - finderMarginBottom >= 0 &&
                parentHalfHeight + finderHalfSize - finderMarginBottom < parentHeight
            ) finderMarginBottom else throw IndexOutOfBoundsException("Margin Bottom is out of View.")

        viewFinderRect.set(
            parentHalfWidth.toFloat() - finderHalfSize.toFloat(),
            parentHalfHeight.toFloat() - finderHalfSize.toFloat() - marginBottom.toFloat(),
            parentHalfWidth.toFloat() + finderHalfSize.toFloat(),
            parentHalfHeight.toFloat() + finderHalfSize.toFloat() - marginBottom.toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawShadedArea(canvas, viewFinderRect)
        drawCorners(canvas, viewFinderRect)
    }

    private fun drawShadedArea(canvas: Canvas, frameRect: RectF) {
        paint.color = shadowColor

        // лево
        canvas.drawRect(
            0f,
            frameRect.top,
            frameRect.left,
            frameRect.bottom,
            paint
        )
        // право
        canvas.drawRect(
            frameRect.right,
            frameRect.top,
            width.toFloat(),
            frameRect.bottom,
            paint
        )
        // верх
        canvas.drawRect(
            0f,
            0f,
            width.toFloat(),
            frameRect.top,
            paint
        )
        // низ
        canvas.drawRect(
            0f,
            frameRect.bottom,
            width.toFloat(),
            height.toFloat(),
            paint
        )
    }

    private fun drawCorners(canvas: Canvas, rect: RectF) {
        paint.color = cornerColor

        val halfThickness = cornerThickness / 2f
        val radius = halfThickness

        // верхний левый
        // |
        canvas.drawRoundRect(
            rect.left - halfThickness,
            rect.top - halfThickness,
            rect.left + halfThickness,
            rect.top + cornerLength,
            radius, radius,
            paint
        )
        // --
        canvas.drawRoundRect(
            rect.left - halfThickness,
            rect.top - halfThickness,
            rect.left + cornerLength,
            rect.top + halfThickness,
            radius, radius,
            paint
        )
        // *
        canvas.drawRect(
            rect.left - halfThickness,
            rect.top - halfThickness,
            rect.left + halfThickness,
            rect.top + halfThickness,
            paint
        )

        // верхний правый
        // |
        canvas.drawRoundRect(
            rect.right - halfThickness,
            rect.top - halfThickness,
            rect.right + halfThickness,
            rect.top + cornerLength,
            radius, radius,
            paint
        )
        // --
        canvas.drawRoundRect(
            rect.right - cornerLength,
            rect.top - halfThickness,
            rect.right,
            rect.top + halfThickness,
            radius, radius,
            paint
        )
        // *
        canvas.drawRect(
            rect.right - halfThickness,
            rect.top - halfThickness,
            rect.right + halfThickness,
            rect.top + halfThickness,
            paint
        )

        // нижний левый
        // |
        canvas.drawRoundRect(
            rect.left - halfThickness,
            rect.bottom - cornerLength,
            rect.left + halfThickness,
            rect.bottom + halfThickness,
            radius, radius,
            paint
        )
        // --
        canvas.drawRoundRect(
            rect.left - halfThickness,
            rect.bottom - halfThickness,
            rect.left + cornerLength,
            rect.bottom + halfThickness,
            radius, radius,
            paint
        )
        // *
        canvas.drawRect(
            rect.left - halfThickness,
            rect.bottom - halfThickness,
            rect.left + halfThickness,
            rect.bottom + halfThickness,
            paint
        )

        // нижний правый
        // |
        canvas.drawRoundRect(
            rect.right - halfThickness,
            rect.bottom - cornerLength,
            rect.right + halfThickness,
            rect.bottom + halfThickness,
            radius, radius,
            paint
        )
        // --
        canvas.drawRoundRect(
            rect.right - cornerLength,
            rect.bottom - halfThickness,
            rect.right,
            rect.bottom + halfThickness,
            radius, radius,
            paint
        )
        // *
        canvas.drawRect(
            rect.right - halfThickness,
            rect.bottom - halfThickness,
            rect.right + halfThickness,
            rect.bottom + halfThickness,
            paint
        )
    }

    companion object {
        private const val DEFAULT_FINDER_SIZE_DP = 285
        private const val DEFAULT_FINDER_BOTTOM_MARGIN_DP = 0
        private const val DEFAULT_CORNER_LENGTH_DP = 30
        private const val DEFAULT_CORNER_THICKNESS_DP = 4
        private const val DEFAULT_CORNER_COLOR = Color.WHITE
        private const val DEFAULT_SHADOW_COLOR = Color.TRANSPARENT
    }
}
