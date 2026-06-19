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
    private val surfaceColor =
        ATHUtil.resolveColor(context, com.google.android.material.R.attr.colorSurface, Color.BLACK)
    private val accentColor =
        ATHUtil.resolveColor(context, androidx.appcompat.R.attr.colorAccent, Color.WHITE)

    init {
        fillPaint.color = ColorUtils.blendARGB(surfaceColor, Color.WHITE, 0.18f).addAlpha(0.62f)
        strokePaint.color = Color.WHITE.addAlpha(0.34f)
        shadowPaint.color = Color.BLACK.addAlpha(0.14f)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(outlinePath, fillPaint)
        canvas.drawPath(outlinePath, shadowPaint)
        canvas.drawPath(outlinePath, strokePaint)
    }

    override fun onBoundsChange(bounds: Rect) {
        boundsRect.set(bounds)
        outlinePath.reset()
        outlinePath.addRoundRect(boundsRect, cornerRadius, cornerRadius, Path.Direction.CW)
        fillPaint.shader = LinearGradient(
            boundsRect.left,
            boundsRect.top,
            boundsRect.right,
            boundsRect.bottom,
            intArrayOf(
                ColorUtils.blendARGB(surfaceColor, Color.WHITE, 0.28f).addAlpha(0.72f),
                ColorUtils.blendARGB(surfaceColor, accentColor, 0.16f).addAlpha(0.54f),
                ColorUtils.blendARGB(surfaceColor, Color.BLACK, 0.08f).addAlpha(0.68f),
            ),
            floatArrayOf(0f, 0.58f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    override fun setAlpha(alpha: Int) {
        fillPaint.alpha = alpha
        strokePaint.alpha = alpha
        shadowPaint.alpha = alpha
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
}
