package dev.luteoos.scrumbet.android.ui.room

import BottomSheetDefaultLayout
import LoadingView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
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
        binding.composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed) // critical for proper lifecycleManagement
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
                                    Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis, softWrap = true)
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
        var currentPick = state.userVote // by remember { mutableStateOf<String?>(null) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "${state.userList.count { it.vote != null }}/${state.userList.size}", fontSize = TextSize.regular())
                Spacer(modifier = Modifier.width(Size.regular()))
                Button(
                    onClick = { showSheetContent { RoomScreenMemberListSheet() } },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(text = getString(R.string.label_list))
                }
            }
            if (state.config.isOwner)
                TextButton(
                    onClick = { showSheetContent { RoomScreenStyleListSheet() } }, colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                ) {
                    Text(text = getString(R.string.label_choose_style))
                }
            AvgVoteUi(isOwner = state.config.isOwner, list = state.userList)
            LazyVerticalGrid(modifier = Modifier.fillMaxWidth(.75f), columns = GridCells.Adaptive(Size.xxLarge()), content = {
                val buttonModifier = Modifier
                    .fillMaxWidth()
                    .padding(Size.xSmall())
                state.config.scale.forEach { value ->
                    item {
                        val onClick = {
                            currentPick = value
                            model.setVote(value)
                        }
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
    private fun AvgVoteUi(isOwner: Boolean, list: List<RoomUser>) {
        val score: String = if (list.any { it.vote == null })
            " "
        else
            list.mapNotNull { it.vote?.toIntOrNull() }.let { if (it.isNotEmpty()) it.sum() / it.size else " " }.toString()
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isOwner)
                IconButton(
                    enabled = list.any { it.vote != null },
                    onClick = {
                        model.resetVote()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
                }
            Text(
                text = score,
                modifier = Modifier
                    .padding(Size.regular())
                    .width(108.dp),
                fontSize = TextSize.xLarge(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colors.primaryVariant
            )
        }
    }

    @Composable
    private fun RoomScreenStyleListSheet() {
        val modelState by model.uiState.observeAsState()
        if (modelState !is RoomUiState.Success)
            return
        val config = (modelState as RoomUiState.Success).config

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            Text(
                text = getString(R.string.label_choose_style),
                fontSize = TextSize.xSmall(),
//                color = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.height(Size.small()))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(config.scaleTypeList.sortedBy { it }) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Size.small())
                                .clickable { model.setScale(item) },
                            backgroundColor = if (item == config.scaleType) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = Size.small()),
                                    text = item.toLowerCase(Locale.current).capitalize(Locale.current)
                                )
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun RoomScreenMemberListSheet() {
        val modelState by model.uiState.observeAsState()
        if (modelState !is RoomUiState.Success)
            return
        val state = modelState as RoomUiState.Success
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            Text(
                text = getString(R.string.label_member_list),
                fontSize = TextSize.xSmall(),
//                color = MaterialTheme.colors.secondary
            )
            Spacer(modifier = Modifier.height(Size.small()))
            if (state.config.isOwner) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        modifier = Modifier.height(40.dp),
                        onClick = { model.resetVote() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
                    ) {
                        Text(text = getString(R.string.label_reset))
                    }
                    Spacer(modifier = Modifier.width(Size.regular()))
                    VisibilityToggle(
                        modifier = Modifier.height(40.dp),
                        initialState = state.config.alwaysVisibleVote, onClick = {
                            if (it)
                                model.showVoteValues()
                            else
                                model.hideVoteValues()
                        }
                    )
                }
                Spacer(modifier = Modifier.height(Size.small()))
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(state.userList.sortedBy { !it.isOwner }) { user ->
                        MemberListRow(user = user, state.config.alwaysVisibleVote)
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
            Text(text = user.username, fontSize = TextSize.small(), maxLines = 1, overflow = TextOverflow.Ellipsis, softWrap = true)
            if (user.isOwner)
                Icon(modifier = Modifier.padding(Size.xSmall()).size(Size.regular()), painter = painterResource(id = R.drawable.ic_crown), contentDescription = null, tint = Color(android.graphics.Color.parseColor("#FFD43E")))
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
            Text(text = getString(R.string.label_connection_failed))
            Button(onClick = { model.connect() }) {
                Text(text = getString(R.string.label_retry))
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
