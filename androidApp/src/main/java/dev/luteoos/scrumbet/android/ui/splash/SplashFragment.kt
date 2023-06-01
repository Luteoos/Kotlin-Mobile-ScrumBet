package dev.luteoos.scrumbet.android.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.SplashFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen
import dev.luteoos.scrumbet.android.ext.toRoomScreen
import dev.luteoos.scrumbet.android.ext.toUpdateScreen
import timber.log.Timber

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>(SplashViewModel::class) {
    override val layoutId: Int = R.layout.splash_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> SplashFragmentBinding =
        { layoutInflater: LayoutInflater, viewGroup: ViewGroup?, attachToParent: Boolean ->
            SplashFragmentBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initObservers() {
        model.onReady.observe(
            this
        ) { state ->
            Timber.w("isAuthorized : $state")
            when (state) {
                SplashUiState.AppVersionObsolete -> activity?.toUpdateScreen()
                SplashUiState.Loading -> { Timber.d("SplashUiState.Loading") }
                SplashUiState.ConnectionError -> { Toast.makeText(context, "Connection to server lost!", Toast.LENGTH_LONG).show() } // TODO make proper non-connection handling
                is SplashUiState.Success -> {
                    if (state.navigateToRoom)
                        activity?.toRoomScreen()
                    else
                        activity?.toMainScreen()
                }
            }
        }
    }

    override fun initBindingValues() {
    }

    override fun initFlowCollectors() {
//        viewLifecycleOwner.lifecycleScope.apply {
//            launch {
//                repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    model.isAuthorized.collect {
//                        Timber.w("isAuthorized : $it")
//                        if (it)
//                            activity?.toRoomScreen()
//                        else
//                            activity?.toMainScreen()
//                    }
//                }
//            }
//        }
    }
}
