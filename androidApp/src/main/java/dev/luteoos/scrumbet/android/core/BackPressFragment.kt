package dev.luteoos.scrumbet.android.core

/**
 * Deprecated Interface, see Sample for solution
 *
 * @sample
 * requireActivity().onBackPressedDispatcher.addCallback(
 * this,
 * object : OnBackPressedCallback(true) {
 *     override fun handleOnBackPressed() {
 *         Log.d(TAG, "Fragment back pressed invoked")
 *         // Do custom work here
 *         // if you want onBackPressed() to be called as normal afterwards
 *         if (isEnabled) {
 *             isEnabled = false
 *             requireActivity().onBackPressed()
 *         }
 *     }
 * }
 * )
 */
@Deprecated("Deprecated `OnBackPressed() See Sample for potential solution`")
interface BackPressFragment {

    /**
     *
     * If true call activity's onBackPressed(), else do nothing
     *
     * @sample
     * if(currentFragment !is BackPressFragment || (currentFragment as BackPressFragment).onBackPressed())
     *             super.onBackPressed()
     }
     */
    fun onBackPressed(): Boolean
}
