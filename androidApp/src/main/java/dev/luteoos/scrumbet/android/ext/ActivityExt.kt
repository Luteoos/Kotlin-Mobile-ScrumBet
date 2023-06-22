package dev.luteoos.scrumbet.android.ext

import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import dev.luteoos.scrumbet.android.R
import timber.log.Timber
import java.lang.Exception

/**
 * example of how to handle navigation
 */
fun FragmentActivity.navigate() {
    navigateWithTryCatch {
        with(Navigation.findNavController(this, R.id.navHost)) {
//            navigate()
        }
    }
}

fun FragmentActivity.toMainScreen() {
    navigateWithTryCatch {
        with(Navigation.findNavController(this, R.id.navHost)) {
            navigate(R.id.action_to_mainFragment)
        }
    }
}

fun FragmentActivity.toRoomScreen() {
    navigateWithTryCatch {
        with(Navigation.findNavController(this, R.id.navHost)) {
            navigate(R.id.action_to_roomFragment)
        }
    }
}

fun FragmentActivity.toUpdateScreen() {
    navigateWithTryCatch {
        with(Navigation.findNavController(this, R.id.navHost)) {
            navigate(R.id.action_to_updateFragment)
        }
    }
}

private fun navigateWithTryCatch(method: () -> Unit) {
    try {
        method.invoke()
    } catch (e: Exception) {
        Timber.e(e)
    }
}
