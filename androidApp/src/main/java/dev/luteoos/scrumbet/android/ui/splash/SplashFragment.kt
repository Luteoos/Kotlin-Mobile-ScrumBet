package dev.luteoos.scrumbet.android.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.SplashFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen
import dev.luteoos.scrumbet.android.ui.auth.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel
import timber.log.Timber

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>(SplashViewModel::class) {
    private lateinit var authModel: AuthViewModel
    override val layoutId: Int = R.layout.splash_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> SplashFragmentBinding =
        { layoutInflater: LayoutInflater, viewGroup: ViewGroup?, attachToParent: Boolean ->
            SplashFragmentBinding.inflate(layoutInflater, viewGroup, attachToParent)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authModel = requireActivity().getViewModel()
    }

    override fun initObservers() {
//        model.onReady.observe(
//            this,
//            Observer {
//                if (it)
//                    activity?.toMainScreen()
//            }
//        )
    }

    override fun initBindingValues() {
    }

    override fun initFlowCollectors() {
        viewLifecycleOwner.lifecycleScope.apply {
            launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    authModel.isAuthorized.collect {
                        Timber.w("isAuthorized : $it")
                        if (it)
                            TODO("toChatScreen")
                        else
                            activity?.toMainScreen()
                    }
                }
            }
        }
    }
}
