package dev.luteoos.scrumbet.android.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.ComposeFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen
import kotlinx.coroutines.launch

class RoomFragment : BaseFragment<RoomViewModel, ComposeFragmentBinding>(RoomViewModel::class) {
    override val layoutId: Int = R.layout.compose_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ComposeFragmentBinding = { inflater, viewGroup, attachToParrent ->
        ComposeFragmentBinding.inflate(inflater, viewGroup, attachToParrent)
    }

    override fun initObservers() {
        model.uiState.observe(this) {
            if (it is RoomUiState.Disconnect)
                activity?.toMainScreen()
        }
    }

    override fun initBindingValues() {
        binding.composeView.setContent {
            MdcTheme {
                val state = model.uiState.observeAsState()
                Column() {
                    Text(text = "Room")
                    Button(onClick = {
                        model.disconnect()
                    }) {
                        Text(text = "disconnect")
                    }
                }
            }
        }
    }

    override fun initFlowCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                model.connect()
            }
        }
    }
}
