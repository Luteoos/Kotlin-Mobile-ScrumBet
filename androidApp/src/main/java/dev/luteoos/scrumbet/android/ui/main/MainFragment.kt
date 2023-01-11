package dev.luteoos.scrumbet.android.ui.main

import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.MainFragmentBinding

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>(MainViewModel::class) {

    override val layoutId: Int = R.layout.main_fragment

    override fun initObservers() {
    }

    override fun initBindingValues() {
    }

    override fun initFlowCollectors() {
    }
}
