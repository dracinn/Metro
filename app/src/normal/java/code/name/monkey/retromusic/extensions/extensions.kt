package code.name.monkey.retromusic.extensions

import android.content.Context
import android.view.Menu
import androidx.fragment.app.FragmentActivity
import code.name.monkey.retromusic.R
import com.google.android.gms.cast.framework.CastButtonFactory

fun Context.setUpMediaRouteButton(menu: Menu) {
    CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.action_cast)
}

fun FragmentActivity.installLanguageAndRecreate(code: String, onInstallComplete: () -> Unit) {
    recreate()
}

fun Context.goToProVersion() {
}

fun Context.installSplitCompat() {
}
