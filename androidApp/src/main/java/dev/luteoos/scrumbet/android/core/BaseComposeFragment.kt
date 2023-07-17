package dev.luteoos.scrumbet.android.core

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.databinding.ComposeFragmentBinding
import kotlin.reflect.KClass

abstract class BaseComposeFragment<T : BaseViewModel>(clazz: KClass<T>) : BaseFragment<T, ComposeFragmentBinding>(clazz) {
    override val layoutId: Int = R.layout.compose_fragment

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ComposeFragmentBinding = { inflater, viewGroup, attachToParent ->
        ComposeFragmentBinding.inflate(inflater, viewGroup, attachToParent)
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun initBindingValues() {
        binding.composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed) // critical for proper lifecycleManagement
        binding.composeView.setContent {
            Mdc3Theme {
                ComposeLayout()
            }
        }
    }

    @Composable
    abstract fun ComposeLayout()
}
