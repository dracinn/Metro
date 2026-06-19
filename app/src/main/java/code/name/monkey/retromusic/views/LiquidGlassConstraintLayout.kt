/*
 * Copyright (c) 2026 Metro Music Player contributors.
 *
 * Licensed under the GNU General Public License v3
 */
package code.name.monkey.retromusic.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import code.name.monkey.retromusic.util.PreferenceUtil

class LiquidGlassConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val defaultBackground: Drawable? = background
    private val defaultElevation = elevation
    private val defaultTranslationZ = ViewCompat.getTranslationZ(this)

    init {
        setLiquidGlassEnabled(PreferenceUtil.liquidGlass)
    }

    fun setLiquidGlassEnabled(enabled: Boolean) {
        if (enabled) {
            background = LiquidGlassDrawable(context)
            clipToOutline = false
            elevation = 10f * resources.displayMetrics.density
            ViewCompat.setTranslationZ(this, 10f * resources.displayMetrics.density)
        } else {
            background = defaultBackground
            elevation = defaultElevation
            ViewCompat.setTranslationZ(this, defaultTranslationZ)
        }
    }
}
