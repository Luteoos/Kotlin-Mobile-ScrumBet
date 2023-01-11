package dev.luteoos.scrumbet.android.core

interface BackPressFragment {
    /**
     * If true call activity's onBackPressed(), else do nothing
     *
     * @sample
     * if(currentFragment !is BackPressFragment || (currentFragment as BackPressFragment).onBackPressed())
     *             super.onBackPressed()
     }
     */
    fun onBackPressed(): Boolean
}
