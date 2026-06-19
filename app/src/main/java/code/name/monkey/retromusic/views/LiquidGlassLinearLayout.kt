/*
 * Copyright (c) 2026 Metro Music Player contributors.
 *
 * Licensed under the GNU General Public License v3
 */
package code.name.monkey.retromusic.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.view.ViewCompat
import code.name.monkey.retromusic.util.PreferenceUtil

class LiquidGlassLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val defaultBackground: Drawable? = background
    private val defaultElevation = elevation
    private val defaultTranslationZ = ViewCompat.getTranslationZ(this)
    private var glassDrawable: LiquidGlassDrawable? = null

    init {
        setLiquidGlassEnabled(PreferenceUtil.liquidGlass)
    }

    fun setLiquidGlassEnabled(enabled: Boolean) {
        if (enabled) {
            glassDrawable = LiquidGlassDrawable(context)
            background = glassDrawable
            clipToOutline = false
            elevation = 10f * resources.displayMetrics.density
            ViewCompat.setTranslationZ(this, 10f * resources.displayMetrics.density)
        } else {
            glassDrawable = null
            background = defaultBackground
            elevation = defaultElevation
            ViewCompat.setTranslationZ(this, defaultTranslationZ)
        }
    }

    override fun drawableHotspotChanged(x: Float, y: Float) {
        super.drawableHotspotChanged(x, y)
        glassDrawable?.setHotspot(x, y)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        glassDrawable?.setHotspot(event.x, event.y)
        return super.dispatchTouchEvent(event)
    }
}
