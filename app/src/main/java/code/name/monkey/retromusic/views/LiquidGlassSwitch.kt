/*
 * Copyright (c) 2026 Metro Music Player contributors.
 *
 * Licensed under the GNU General Public License v3
 */
package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.isVisible
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.materialswitch.MaterialSwitch

class LiquidGlassSwitch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
) : MaterialSwitch(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode && !ThemeStore.isMD3Enabled(context)) {
            ATH.setTint(this, ThemeStore.accentColor(context))
        }
        if (PreferenceUtil.liquidGlass) {
            val density = resources.displayMetrics.density
            background = LiquidGlassDrawable(context, 24f * density)
            setPadding(
                (6f * density).toInt(),
                (2f * density).toInt(),
                (6f * density).toInt(),
                (2f * density).toInt(),
            )
        }
    }

    override fun isShown(): Boolean {
        return parent != null && isVisible
    }
}
