package dev.luteoos.scrumbet.android.ui.room

import BottomSheetDefaultLayout
import LoadingView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.ComposeFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen
import dev.luteoos.scrumbet.android.ui.composeUtil.Size
import dev.luteoos.scrumbet.android.ui.composeUtil.TextSize
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
                val state by model.uiState.observeAsState(RoomUiState.Loading)
                var customSheetContent by remember { mutableStateOf<@Composable (() -> Unit)>({ }) }

                val title = when (state) {
                    RoomUiState.Disconnect, RoomUiState.Loading -> getString(R.string.label_connecting)
                    is RoomUiState.Error -> getString(R.string.label_error)
                    is RoomUiState.Success -> getString(R.string.label_owner, (state as RoomUiState.Success).userList.firstOrNull { it.isOwner }?.username ?: "")
                }
                val roomName = when (state) {
                    is RoomUiState.Success -> (state as RoomUiState.Success).connectionName
                    else -> null
                }

                BottomSheetDefaultLayout(
                    model,
                    sheetContent = customSheetContent
                ) { toggleSheetState ->
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            TopAppBar(
                                backgroundColor = MaterialTheme.colors.background,
                                title = {
                                    Text(text = title)
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        model.disconnect()
                                    }) {
                                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                                    }
                                },
                                actions = {
                                    IconButton(
                                        enabled = state is RoomUiState.Success,
                                        onClick = {
                                            customSheetContent = { RoomScreenShareSheet(roomName) }
                                            toggleSheetState()
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                                    }
                                }
                            )
                        }
                    ) { padding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            when (state) {
                                RoomUiState.Disconnect -> {}
                                is RoomUiState.Error -> RoomScreenUiError(state = state as RoomUiState.Error)
                                RoomUiState.Loading -> RoomScreenUiLoading()
                                is RoomUiState.Success -> RoomScreenUiConnected(state = state as RoomUiState.Success)
                            }
                        }
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

    @Composable
    private fun RoomScreenUiConnected(state: RoomUiState.Success) {
        var currentPick by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${state.userList.count { it.vote != null }}/${state.userList.size}", fontSize = TextSize.regular())
                Spacer(modifier = Modifier.width(Size.small()))
                Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)) {
                    Text(text = getString(R.string.label_list))
                }
            }
            LazyVerticalGrid(modifier = Modifier.fillMaxWidth(.65f), columns = GridCells.Adaptive(Size.xLarge()), content = { // TODO - bug - GridCellsAdaptive multiline Text()
                val buttonModifier = Modifier
                    .fillMaxWidth()
                    .padding(Size.xSmall())
                state.config.scale.forEach { value ->
                    item {
                        val onClick = { currentPick = value }
                        if (currentPick == null || currentPick == value)
                            Button(modifier = buttonModifier, onClick = onClick) {
                                Text(text = value)
                            }
                        else
                            OutlinedButton(modifier = buttonModifier, onClick = onClick) {
                                Text(text = value)
                            }
                    }
                }
            })
        }
    }

    @Composable
    private fun RoomScreenUiError(state: RoomUiState.Error) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Failed to connect")
            Button(onClick = { model.connect() }) {
                Text(text = "Retry")
            }
        }
    }

    @Composable
    private fun RoomScreenUiLoading() {
        LoadingView()
    }

    @Composable
    private fun RoomScreenShareSheet(roomName: String?) {
        if (roomName == null)
            return
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            Text(text = getString(R.string.label_share), fontSize = TextSize.xSmall())
        }
    }
}
