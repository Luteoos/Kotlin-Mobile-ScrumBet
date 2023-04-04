package dev.luteoos.scrumbet.android.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.SplashFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>(SplashViewModel::class) {
    override val layoutId: Int = R.layout.splash_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> SplashFragmentBinding =
        { layoutInflater: LayoutInflater, viewGroup: ViewGroup?, attachToParent: Boolean ->
            SplashFragmentBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun initObservers() {
        model.onReady.observe(
            this,
            Observer {
                if (it)
                    activity?.toMainScreen()
            }
        )
    }

    override fun initBindingValues() {
    }

    override fun initFlowCollectors() {
    }
}
