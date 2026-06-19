/*
 * Copyright (c) 2026 Metro Music Player contributors.
 *
 * Licensed under the GNU General Public License v3
 */
package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat

class LiquidGlassConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        background = LiquidGlassDrawable(context)
        clipToOutline = false
        elevation = 10f * resources.displayMetrics.density
        ViewCompat.setTranslationZ(this, 10f * resources.displayMetrics.density)
    }
}
