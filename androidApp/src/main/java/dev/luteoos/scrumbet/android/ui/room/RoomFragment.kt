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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import dev.luteoos.scrumbet.android.ui.composeUtil.VisibilityToggle
import dev.luteoos.scrumbet.data.state.room.RoomUser
import kotlinx.coroutines.launch

class RoomFragment : BaseFragment<RoomViewModel, ComposeFragmentBinding>(RoomViewModel::class) {
    override val layoutId: Int = R.layout.compose_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ComposeFragmentBinding = { inflater, viewGroup, attachToParent ->
        ComposeFragmentBinding.inflate(inflater, viewGroup, attachToParent)
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
                                is RoomUiState.Success -> RoomScreenUiConnected(
                                    state = state as RoomUiState.Success,
                                    showSheetContent = {
                                        customSheetContent = it
                                        toggleSheetState()
                                    }
                                )
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
    private fun RoomScreenUiConnected(
        state: RoomUiState.Success,
        showSheetContent: (@Composable () -> Unit) -> Unit
    ) {
        var currentPick by remember { mutableStateOf<String?>(null) } // todo = state.userVote

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${state.userList.count { it.vote != null }}/${state.userList.size}", fontSize = TextSize.regular())
                Spacer(modifier = Modifier.width(Size.regular()))
                Button(
                    onClick = { showSheetContent { RoomScreenMemberListSheet(state.userList, state.config.isOwner, state.config.anonymousVote, state.config.alwaysVisibleVote) } },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(text = getString(R.string.label_list))
                }
            }
            AvgVoteUi(list = state.userList)
            LazyVerticalGrid(modifier = Modifier.fillMaxWidth(.75f), columns = GridCells.Adaptive(Size.xxLarge()), content = {
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
    private fun AvgVoteUi(list: List<RoomUser>) {
        val score: String = if (list.any { it.vote == null })
            " "
        else
            list.mapNotNull { it.vote?.toIntOrNull() }.let { it.sum() / it.size }.toString()

        Text(
            text = score,
            modifier = Modifier.padding(Size.regular()),
            fontSize = TextSize.xLarge(),
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colors.primaryVariant
        )
    }

    @Composable
    private fun RoomScreenMemberListSheet(
        list: List<RoomUser>,
        isOwner: Boolean,
        isVoteAnonymous: Boolean,
        isVoteVisible: Boolean
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            Text(
                text = getString(R.string.label_member_list),
                fontSize = TextSize.xSmall(),
                color = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.height(Size.small()))
            if (isOwner) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.height(40.dp),
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                    ) {
                        Text(text = getString(R.string.label_reset))
                    }
                    Spacer(modifier = Modifier.width(Size.regular()))
                    VisibilityToggle(
                        modifier = Modifier.height(40.dp),
                        initialState = isVoteVisible, onClick = {
                            // show/hide votes
                        }
                    )
                }
                Spacer(modifier = Modifier.height(Size.small()))
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(list.sortedBy { !it.isOwner }) { user ->
                        MemberListRow(user = user, isVoteVisible)
                    }
                }
            )
        }
    }

    @Composable
    private fun MemberListRow(user: RoomUser, isVoteVisible: Boolean) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Size.xSmall()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = user.username, fontSize = TextSize.small())
            if (user.isOwner)
                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.Yellow) // TODO change icon to Crown
            Spacer(modifier = Modifier.weight(1f))
            if (user.vote != null)
                Button(onClick = {}) {
                    if (isVoteVisible)
                        Text(text = user.vote!!, fontSize = TextSize.xSmall())
                    else
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colors.onPrimary)
                }
            else
                OutlinedButton(onClick = {}) {
                    Text(text = " ", fontSize = TextSize.xSmall())
                }
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
