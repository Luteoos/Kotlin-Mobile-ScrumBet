package dev.luteoos.scrumbet.android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.MainFragmentBinding

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>(MainViewModel::class) {

    override val layoutId: Int = R.layout.main_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> MainFragmentBinding = { inflater, viewGroup, attachToParrent ->
        MainFragmentBinding.inflate(inflater, viewGroup, attachToParrent)
    }

    override fun initObservers() {
    }

    override fun initBindingValues() {
//        binding.composeView.setContent {
//            MdcTheme() {
//            }
//        }
    }

    override fun initFlowCollectors() {
    }
}
