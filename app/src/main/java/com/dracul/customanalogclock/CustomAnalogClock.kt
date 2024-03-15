package com.dracul.customanalogclock

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


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
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f


    private val hourHandColor: Int
    private val hourHandWidth: Int
    private val secondHandColor: Int
    private val secondHandWidth: Int
    private val minuteHandColor: Int
    private val minuteHandWidth: Int
    private val showCenterDot: Boolean
    private val showMinutesDots: Boolean
    private val centerDotColor: Int
    private val minutesDotColor: Int
    private val hoursDotColor: Int
    private val backgroundColor: Int
    private val textColor: Int
    private val hourTextSize: Int
    private val radiusToHourNumbers: Float
    private val minutesDotRadius: Int
    private val hoursDotRadius: Int
    private val outerCircleColor: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomAnalogClock, defStyleAttr, 0)
        hourHandColor = typedArray.getColor(R.styleable.CustomAnalogClock_hourHandColor, Color.WHITE)
        minuteHandColor = typedArray.getColor(R.styleable.CustomAnalogClock_minuteHandColor, Color.BLACK)
        secondHandColor = typedArray.getColor(R.styleable.CustomAnalogClock_secondHandColor, Color.BLACK)
        hourHandWidth = typedArray.getDimensionPixelSize(R.styleable.CustomAnalogClock_minuteHandWidth, resources.getDimensionPixelSize(R.dimen.hour_hand))
        minuteHandWidth = typedArray.getDimensionPixelSize(R.styleable.CustomAnalogClock_minuteHandWidth, resources.getDimensionPixelSize(R.dimen.minut_hand))
        secondHandWidth = typedArray.getDimensionPixelSize(R.styleable.CustomAnalogClock_minuteHandWidth, resources.getDimensionPixelSize(R.dimen.second_hand))
        centerDotColor = typedArray.getColor(R.styleable.CustomAnalogClock_centerDotColor, Color.RED)
        minutesDotColor = typedArray.getColor(R.styleable.CustomAnalogClock_minutesDotColor, Color.WHITE)
        hoursDotColor = typedArray.getColor(R.styleable.CustomAnalogClock_hoursDotColor, Color.WHITE)
        showCenterDot = typedArray.getBoolean(R.styleable.CustomAnalogClock_showCenterDot, true)
        showMinutesDots = typedArray.getBoolean(R.styleable.CustomAnalogClock_showMinutesDot, true)
        backgroundColor = typedArray.getColor(R.styleable.CustomAnalogClock_backgroundColor, Color.TRANSPARENT)
        hourTextSize = typedArray.getDimensionPixelSize(R.styleable.CustomAnalogClock_hourTextSize, resources.getDimensionPixelSize(R.dimen.text_size))
        radiusToHourNumbers = typedArray.getFloat(R.styleable.CustomAnalogClock_radiusToHourNumbers, 0.8F)
        minutesDotRadius = typedArray.getDimensionPixelSize(R.styleable.CustomAnalogClock_minutesDotRadius, resources.getDimensionPixelSize(R.dimen.radius_minut_dot))
        hoursDotRadius = typedArray.getDimensionPixelSize(R.styleable.CustomAnalogClock_hoursDotRadius, resources.getDimensionPixelSize(R.dimen.radius_hour_dot))
        textColor = typedArray.getColor(R.styleable.CustomAnalogClock_textColor, Color.WHITE)
        outerCircleColor = typedArray.getColor(R.styleable.CustomAnalogClock_outerCircleColor, Color.WHITE)


        typedArray.recycle()

        animator.addUpdateListener(this)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY) % 12
        val minute = calendar.get(Calendar.MINUTE)
        val second = calendar.get(Calendar.SECOND)
        val milisecond = calendar.get(Calendar.MILLISECOND)

        drawBackground(canvas)
        drawDots(canvas)

        drawNumbersText(canvas)

        paint.reset()
        val hourDegree = (hour + minute / 60f) * 30f
        paint.strokeWidth = resources.getDimension(R.dimen.hour_hand)
        drawHand(canvas, hourDegree, hourHandLength, hourHandColor)
        paint.reset()

        val minuteDegree = (minute + second / 60f) * 6f
        paint.strokeWidth = minuteHandWidth.toFloat()
        drawHand(canvas, minuteDegree, minuteHandLength, minuteHandColor)

        paint.reset()
        val secondDegree = (second.toFloat() + (milisecond / 1000f)) * 6f
        paint.strokeWidth = resources.getDimension(R.dimen.second_hand)
        drawHand(canvas, secondDegree, minuteHandLength, secondHandColor)

        paint.reset()
        if (showCenterDot)
            drawCenterDot(canvas)
        drawClockFace(canvas)

    }

    private  fun drawBackground(canvas: Canvas){
        paint.color = backgroundColor
        paint.style = Paint.Style.FILL
        paint.strokeWidth = resources.getDimension(R.dimen.stroke_width_outer_circle)
        canvas.drawCircle(centerX, centerY, radius, paint)
    }
    private fun drawHand(canvas: Canvas, degree: Float, length: Float, color: Int) {
        val handRadians = Math.toRadians(degree.toDouble())
        val x = centerX + sin(handRadians) * length
        val y = centerY - cos(handRadians) * length
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = color
        canvas.drawLine(centerX, centerY, x.toFloat(), y.toFloat(), paint)
    }

    private fun drawClockFace(canvas: Canvas) {
        paint.color = outerCircleColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.getDimension(R.dimen.stroke_width_outer_circle)
        canvas.drawCircle(centerX, centerY, radius, paint)
    }

    private fun drawDots(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        for (i in 0..59) {
            val angle = Math.toRadians((i * 6).toDouble())
            val hourCircleX = centerX + ((radius * 0.9f) * sin(angle)).toFloat()
            val hourCircleY = centerY - (radius * 0.9f * cos(angle)).toFloat()



            if (i % 5 == 0) {
                paint.color = hoursDotColor
                canvas.drawCircle(hourCircleX, hourCircleY,hoursDotRadius.toFloat() , paint)
            } else if (showMinutesDots) {
                paint.color = minutesDotColor
                canvas.drawCircle(hourCircleX, hourCircleY, minutesDotRadius.toFloat(), paint)
            }
        }
    }

    private fun drawNumbersText(canvas: Canvas) {
        for (i in 1..12) {
            val angle = Math.toRadians((i * 30).toDouble())

            paint.color = textColor
            paint.textSize = hourTextSize.toFloat()

            val bounds = Rect()
            val text = i.toString()
            val textWidth = paint.measureText(text)
            paint.getTextBounds(i.toString(), 0, text.length, bounds)

            val hourCircleX = centerX + (radius * radiusToHourNumbers * sin(angle) - textWidth / 2).toFloat()
            val hourCircleY = centerY - (radius * radiusToHourNumbers * cos(angle) - bounds.height() / 2).toFloat()
            canvas.drawText(text, hourCircleX, hourCircleY, paint)
        }
    }


    private fun drawCenterDot(canvas: Canvas) {
        paint.color = centerDotColor
        paint.strokeWidth = resources.getDimension(R.dimen.stroke_width_radius)
        canvas.drawCircle(centerX, centerY, radius / 25, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
        radius = min(w, h) / 2f - 20

        hourHandLength = radius * 0.5f
        minuteHandLength = radius * 0.8f
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        invalidate()
    }

}