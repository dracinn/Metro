/*
 * Copyright (c) 2026 Metro Music Player contributors.
 *
 * Licensed under the GNU General Public License v3
 */
package code.name.monkey.retromusic.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import androidx.core.graphics.ColorUtils
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.retromusic.extensions.addAlpha

class LiquidGlassDrawable(
    context: Context,
    private val cornerRadius: Float = 28f * context.resources.displayMetrics.density,
) : Drawable() {

    private val boundsRect = RectF()
    private val outlinePath = Path()
    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = context.resources.displayMetrics.density
    }
    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2f * context.resources.displayMetrics.density
    }
    private val rimPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val touchPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val pressedOverlayPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val surfaceColor =
        ATHUtil.resolveColor(context, com.google.android.material.R.attr.colorSurface, Color.BLACK)
    private val accentColor =
        ATHUtil.resolveColor(context, androidx.appcompat.R.attr.colorAccent, Color.WHITE)
    private var touchX = 0f
    private var touchY = 0f
    private var pressed = false
    private var alphaOverride = 255

    init {
        fillPaint.color = ColorUtils.blendARGB(surfaceColor, Color.WHITE, 0.18f).addAlpha(0.62f)
        strokePaint.color = Color.WHITE.addAlpha(0.34f)
        shadowPaint.color = Color.BLACK.addAlpha(0.14f)
        pressedOverlayPaint.color = Color.WHITE.addAlpha(0.12f)
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(outlinePath)
        canvas.drawPath(outlinePath, fillPaint)
        canvas.drawPath(outlinePath, rimPaint)
        canvas.drawPath(outlinePath, touchPaint)
        if (pressed) {
            canvas.drawPath(outlinePath, pressedOverlayPaint)
        }
        canvas.restore()
        canvas.drawPath(outlinePath, shadowPaint)
        canvas.drawPath(outlinePath, strokePaint)
    }

    override fun onBoundsChange(bounds: Rect) {
        boundsRect.set(bounds)
        outlinePath.reset()
        outlinePath.addRoundRect(boundsRect, cornerRadius, cornerRadius, Path.Direction.CW)
        touchX = boundsRect.centerX()
        touchY = boundsRect.top + boundsRect.height() * 0.28f
        fillPaint.shader = LinearGradient(
            boundsRect.left,
            boundsRect.top,
            boundsRect.right,
            boundsRect.bottom,
            intArrayOf(
                ColorUtils.blendARGB(surfaceColor, Color.WHITE, 0.34f).addAlpha(0.74f),
                ColorUtils.blendARGB(surfaceColor, accentColor, 0.22f).addAlpha(0.56f),
                ColorUtils.blendARGB(surfaceColor, Color.BLACK, 0.16f).addAlpha(0.70f),
            ),
            floatArrayOf(0f, 0.58f, 1f),
            Shader.TileMode.CLAMP
        )
        rimPaint.shader = LinearGradient(
            boundsRect.left,
            boundsRect.top,
            boundsRect.left,
            boundsRect.bottom,
            intArrayOf(
                Color.WHITE.addAlpha(0.30f),
                Color.TRANSPARENT,
                Color.BLACK.addAlpha(0.18f),
            ),
            floatArrayOf(0f, 0.34f, 1f),
            Shader.TileMode.CLAMP
        )
        updateTouchShader()
        applyAlpha()
    }

    override fun setAlpha(alpha: Int) {
        alphaOverride = alpha
        applyAlpha()
        invalidateSelf()
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        fillPaint.colorFilter = colorFilter
        strokePaint.colorFilter = colorFilter
        shadowPaint.colorFilter = colorFilter
        invalidateSelf()
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun isStateful(): Boolean = true

    override fun onStateChange(state: IntArray): Boolean {
        val wasPressed = pressed
        pressed = state.any { it == android.R.attr.state_pressed }
        if (pressed != wasPressed) {
            invalidateSelf()
            return true
        }
        return false
    }

    override fun setHotspot(x: Float, y: Float) {
        touchX = x.coerceIn(boundsRect.left, boundsRect.right)
        touchY = y.coerceIn(boundsRect.top, boundsRect.bottom)
        updateTouchShader()
        invalidateSelf()
    }

    private fun updateTouchShader() {
        if (boundsRect.isEmpty) return
        val radius = boundsRect.width().coerceAtLeast(boundsRect.height()) * 0.72f
        touchPaint.shader = RadialGradient(
            touchX,
            touchY,
            radius,
            intArrayOf(
                Color.WHITE.addAlpha(if (pressed) 0.34f else 0.22f),
                ColorUtils.blendARGB(accentColor, Color.WHITE, 0.45f).addAlpha(0.12f),
                Color.TRANSPARENT,
            ),
            floatArrayOf(0f, 0.34f, 1f),
            Shader.TileMode.CLAMP
        )
        applyAlpha()
    }

    private fun applyAlpha() {
        fillPaint.alpha = alphaOverride
        strokePaint.alpha = (alphaOverride * 0.76f).toInt()
        shadowPaint.alpha = (alphaOverride * 0.70f).toInt()
        rimPaint.alpha = alphaOverride
        touchPaint.alpha = alphaOverride
        pressedOverlayPaint.alpha = (alphaOverride * 0.48f).toInt()
    }
}
