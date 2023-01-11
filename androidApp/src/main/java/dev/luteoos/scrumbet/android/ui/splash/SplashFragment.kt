package dev.luteoos.scrumbet.android.ui.splash

import androidx.lifecycle.Observer
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.SplashFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>(SplashViewModel::class) {
    override val layoutId: Int = R.layout.splash_fragment

    override fun initObservers() {
    }

    override fun initBindingValues() {
        model.onReady.observe(
            this,
            Observer {
                if (it)
                    activity?.toMainScreen()
            }
        )
    }

    override fun initFlowCollectors() {
    }
}
