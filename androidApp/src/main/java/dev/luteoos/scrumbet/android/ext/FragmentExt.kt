package dev.luteoos.scrumbet.android.ext

import android.view.View
import androidx.fragment.app.Fragment
import dev.luteoos.scrumbet.android.ui.MainActivity

fun Fragment.hideKeyboard(view: View? = this.view) {
    activity?.let {
        if (it is MainActivity)
            it.hideKeyboard(view)
    }
}
