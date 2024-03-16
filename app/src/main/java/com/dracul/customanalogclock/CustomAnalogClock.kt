package com.dracul.customanalogclock

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import java.util.Calendar
import java.util.TimeZone
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val TIME_ZONE_KEY = "timeZone"

class CustomAnalogClock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(
    context,
    attrs,
    defStyleAttr,
), ValueAnimator.AnimatorUpdateListener {
    private val animator = ValueAnimator.ofInt(0)
    private val paint = Paint()
    private var hourHandLength = 0f
    private var minuteHandLength = 0f
    private var secondHandLength = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f


    private var backgroundColor: Int
    var centerDotColor: Int
    var centerDotRadius: Int
    var hourHandColor: Int
    var hourHandWidth: Int
    var hourTextSize: Int
    var hoursDotColor: Int
    var hoursDotRadius: Int
    var minuteHandColor: Int
    var minuteHandWidth: Int
    var minutesDotColor: Int
    var minutesDotRadius: Int
    var outerCircleColor: Int
    var outerCircleWidth: Int
    var radiusToHourNumbers: Float
    var secondHandColor: Int
    var secondHandWidth: Int
    var showCenterDot: Boolean
    var showMinutesDots: Boolean
    var showOuterCircle: Boolean
    var textColor: Int
    var calendar = Calendar.getInstance()
    var timeZone: TimeZone
        get() = calendar.timeZone
        set(value) {
            calendar.timeZone = value
        }


    init {
        isSaveEnabled = true
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomAnalogClock, defStyleAttr, 0)


        backgroundColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_backgroundColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorSurface)
        )
        centerDotColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_centerDotColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorTertiary)
        )
        hourHandColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_hourHandColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorOnTertiary)
        )
        minuteHandColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_minuteHandColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorTertiary)
        )
        hoursDotColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_hoursDotColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorOnTertiaryContainer)
        )
        minutesDotColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_minutesDotColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorOnTertiary)
        )
        outerCircleColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_outerCircleColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorSurfaceVariant)
        )
        secondHandColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_secondHandColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorPrimary)
        )
        textColor = typedArray.getColor(
            R.styleable.CustomAnalogClock_textColor,
            context.getColorByAttr(com.google.android.material.R.attr.colorOnSurface)
        )
        centerDotRadius = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_centerDotRadius,
            resources.getDimensionPixelSize(R.dimen.center_dot_radius)
        )

        hourHandWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_minuteHandWidth,
            resources.getDimensionPixelSize(R.dimen.hour_hand)
        )
        hourTextSize = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_hourTextSize,
            resources.getDimensionPixelSize(R.dimen.text_size)
        )

        hoursDotRadius = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_hoursDotRadius,
            resources.getDimensionPixelSize(R.dimen.radius_hour_dot)
        )

        minuteHandWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_minuteHandWidth,
            resources.getDimensionPixelSize(R.dimen.minut_hand)
        )

        minutesDotRadius = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_minutesDotRadius,
            resources.getDimensionPixelSize(R.dimen.radius_minut_dot)
        )

        outerCircleWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_outerCircleWidth,
            resources.getDimensionPixelSize(R.dimen.stroke_width_outer_circle)
        )
        radiusToHourNumbers = typedArray.getFloat(
            R.styleable.CustomAnalogClock_radiusToHourNumbers,
            0.8F
        )

        secondHandWidth = typedArray.getDimensionPixelSize(
            R.styleable.CustomAnalogClock_minuteHandWidth,
            resources.getDimensionPixelSize(R.dimen.second_hand)
        )
        showCenterDot = typedArray.getBoolean(
            R.styleable.CustomAnalogClock_showCenterDot,
            true
        )
        showMinutesDots = typedArray.getBoolean(
            R.styleable.CustomAnalogClock_showMinutesDot,
            true
        )
        showOuterCircle = typedArray.getBoolean(
            R.styleable.CustomAnalogClock_showOuterCircle,
            true
        )

        typedArray.recycle()

        animator.addUpdateListener(this)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }

    @SuppressLint("MissingSuperCall")
    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(null)
        calendar.timeZone.rawOffset = (state as Bundle).getInt(TIME_ZONE_KEY)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val state = super.onSaveInstanceState()
        if (id == NO_ID)
            return state
        val bundle = Bundle()
        bundle.putInt(TIME_ZONE_KEY, calendar.timeZone.rawOffset)
        return bundle
    }

    fun getBackgroundClockColor() = backgroundColor

    fun setBackgroundClockColor(color: Int) {
        backgroundColor = color
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        calendar.timeInMillis = System.currentTimeMillis()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val milisecond = calendar.get(Calendar.MILLISECOND)
        paint.drawBackground(canvas)
        paint.drawDots(canvas)

        paint.drawNumbersText(canvas)

        val hourDegree = (hour + minute / 60f) * 30f
        paint.strokeWidth = resources.getDimension(R.dimen.hour_hand)
        paint.drawHand(canvas, hourDegree, hourHandLength, hourHandColor)

        val minuteDegree = (minute + second / 60f) * 6f
        paint.strokeWidth = minuteHandWidth.toFloat()
        paint.drawHand(canvas, minuteDegree, minuteHandLength, minuteHandColor)

        val secondDegree = (second.toFloat() + (milisecond / 1000f)) * 6f
        paint.strokeWidth = resources.getDimension(R.dimen.second_hand)
        paint.drawHand(canvas, secondDegree, secondHandLength, secondHandColor)

        if (showCenterDot)
            paint.drawCenterDot(canvas)

        if (showOuterCircle)
            paint.drawOuterCircle(canvas)

    }

    private fun Paint.drawBackground(canvas: Canvas) {
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = resources.getDimension(R.dimen.stroke_width_outer_circle)
        canvas.drawCircle(centerX, centerY, radius - 1, paint)
        paint.reset()
    }

    private fun Paint.drawHand(canvas: Canvas, degree: Float, length: Float, color: Int) {
        paint.isAntiAlias = true
        val handRadians = Math.toRadians(degree.toDouble())
        val x = centerX + sin(handRadians) * length
        val y = centerY - cos(handRadians) * length
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = color
        canvas.drawLine(centerX, centerY, x.toFloat(), y.toFloat(), paint)
    }

    private fun Paint.drawOuterCircle(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.color = outerCircleColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = outerCircleWidth.toFloat()
        canvas.drawCircle(centerX, centerY, radius - (outerCircleWidth / 2) - 1, paint)
        paint.reset()
    }

    private fun Paint.drawCenterDot(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.color = centerDotColor
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, centerDotRadius.toFloat(), paint)
        paint.reset()
    }

    private fun Paint.drawDots(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        for (i in 0..59) {
            val angle = Math.toRadians((i * 6).toDouble())
            val hourCircleX = centerX + ((radius * 0.9f) * sin(angle)).toFloat()
            val hourCircleY = centerY - (radius * 0.9f * cos(angle)).toFloat()

            if (i % 5 == 0) {
                paint.color = hoursDotColor
                canvas.drawCircle(hourCircleX, hourCircleY, hoursDotRadius.toFloat(), paint)
            } else if (showMinutesDots) {
                paint.color = minutesDotColor
                canvas.drawCircle(hourCircleX, hourCircleY, minutesDotRadius.toFloat(), paint)
            }
        }
        paint.reset()
    }

    private fun Paint.drawNumbersText(canvas: Canvas) {
        paint.isAntiAlias = true
        paint.color = textColor
        paint.textSize = hourTextSize.toFloat()
        for (i in 1..12) {
            val angle = Math.toRadians((i * 30).toDouble())
            val bounds = Rect()
            val text = i.toString()
            val textWidth = paint.measureText(text)
            paint.getTextBounds(i.toString(), 0, text.length, bounds)

            val hourCircleX = centerX + (radius * radiusToHourNumbers * sin(angle) - textWidth / 2).toFloat()
            val hourCircleY = centerY - (radius * radiusToHourNumbers * cos(angle) - bounds.height() / 2).toFloat()
            canvas.drawText(text, hourCircleX, hourCircleY, paint)
        }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = right - left - paddingStart - paddingEnd
        val height = bottom - top - paddingBottom - paddingTop
        centerX = width / 2f + paddingLeft
        centerY = height / 2f + paddingTop
        radius = min(width, height) / 2f



        hourHandLength = radius * 0.5f
        minuteHandLength = radius * 0.65f
        secondHandLength = radius * 0.8f
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        invalidate()
    }

    fun Context.getColorByAttr(@AttrRes attr: Int): Int = ContextCompat.getColor(this, findResIdByAttr(attr))
    fun Context.findResIdByAttr(@AttrRes attr: Int): Int = findResIdsByAttr(attr)[0]

    fun Context.findResIdsByAttr(@AttrRes vararg attrs: Int): IntArray {
        @SuppressLint("ResourceType")
        val array = obtainStyledAttributes(attrs)

        val values = IntArray(attrs.size)
        for (i in attrs.indices) {
            values[i] = array.getResourceId(i, 0)
        }
        array.recycle()

        return values
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = context.dpToPx(300).toInt()
        val desiredHeight = context.dpToPx(300).toInt()
        val resolvedWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolvedHeight = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }


    fun Context.dpToPx(dp: Int): Float {
        return dp.toFloat() * this.resources.displayMetrics.density
    }
}