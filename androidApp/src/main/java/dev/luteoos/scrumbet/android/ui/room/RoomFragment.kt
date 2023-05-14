package dev.luteoos.scrumbet.android.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import com.google.accompanist.themeadapter.material.MdcTheme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.ComposeFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen

class RoomFragment : BaseFragment<RoomViewModel, ComposeFragmentBinding>(RoomViewModel::class) {
    override val layoutId: Int = R.layout.compose_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ComposeFragmentBinding = { inflater, viewGroup, attachToParrent ->
        ComposeFragmentBinding.inflate(inflater, viewGroup, attachToParrent)
    }

    override fun initObservers() {
    }

    override fun initBindingValues() {
        binding.composeView.setContent {
            MdcTheme() {
                Column() {
                    Text(text = "Room")
                    Button(onClick = {
                        model.disconnect()
                        activity?.toMainScreen()
                    }) {
                        Text(text = "disconnect")
                    }
                }
            }
        }
    }

    override fun initFlowCollectors() {
    }
}
