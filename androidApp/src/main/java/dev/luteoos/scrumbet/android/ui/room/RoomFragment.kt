package dev.luteoos.scrumbet.android.ui.room

import BottomSheetDefaultLayout
import LoadingView
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.ComposeFragmentBinding
import dev.luteoos.scrumbet.android.ext.toMainScreen
import dev.luteoos.scrumbet.android.util.composeUtil.KeepAlive
import dev.luteoos.scrumbet.android.util.composeUtil.Size
import dev.luteoos.scrumbet.android.util.composeUtil.TextSize
import dev.luteoos.scrumbet.android.util.encodeToBitmap
import dev.luteoos.scrumbet.data.entity.MultiUrl
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

    override fun onResume() {
        super.onResume()
        model.isAlive()
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
                                            customSheetContent = {
                                                RoomScreenShareSheet(
                                                    roomName,
                                                    (state as? RoomUiState.Success)?.config?.url,
                                                    (state as? RoomUiState.Success)?.config?.roomJoinCode
                                                )
                                            }
                                            toggleSheetState()
                                        }
                                    ) {
                                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                                    }
                                }
                            )
                        },
                        bottomBar = {
                            if (state !is RoomUiState.Success) {
                                // empty
                            } else {
                                BottomBarSuccess(state = state as RoomUiState.Success, showSheetContent = {
                                    customSheetContent = it
                                    toggleSheetState()
                                })
                            }
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
        KeepAlive()
        var currentPick = state.userVote // by remember { mutableStateOf<String?>(null) }
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            VoteScoreUi(isOwner = state.config.isOwner, list = state.userList)
            LazyVerticalGrid(
                modifier = Modifier
                    .scrollable(scrollState, orientation = Orientation.Vertical)
                    .fillMaxWidth(.75f),
                horizontalArrangement = Arrangement.spacedBy(Size.xSmall()),
                verticalArrangement = Arrangement.spacedBy(Size.xSmall()),
                columns = GridCells.Adaptive(
                    Size.buttonSize()
                ),
                content = {
                    val buttonModifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                    state.config.scale.forEach { value ->
                        item {
                            val onClick = {
                                currentPick = value
                                model.setVote(value)
                            }
                            if (currentPick == null || currentPick == value)
                                Button(
                                    modifier = buttonModifier,
                                    onClick = onClick,
//                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = if(value == "?") MaterialTheme.colors.secondary else MaterialTheme.colors.primary)
                                ) {
                                    Text(text = value)
                                }
                            else
                                OutlinedButton(modifier = buttonModifier, onClick = onClick) {
                                    Text(text = value)
                                }
                        }
                    }
                }
            )
        }
    }

    @Composable
    private fun VoteScoreUi(isOwner: Boolean, list: List<RoomUser>) {
        val score: String = if (list.any { it.vote == null })
            ""
        else
            list.mapNotNull { it.vote?.toIntOrNull() }.let { if (it.isNotEmpty()) it.sum() / it.size else "?" }.toString()

        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth(.75f)
                .padding(horizontal = Size.xSmall()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(Size.regular()),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val fontSize = TextSize.xSmall()
                Column(modifier = Modifier.padding(horizontal = Size.small())) {
                    Text(text = getString(R.string.label_users_in_room), fontSize = fontSize)
                    Text(text = getString(R.string.label_users_voted), fontSize = fontSize)
                }
                Column() {
                    Text(text = "${list.size}", fontWeight = FontWeight.Bold, fontSize = fontSize, fontFamily = FontFamily.Monospace)
                    Text(text = "${list.count { it.vote != null }}", fontWeight = FontWeight.Bold, fontSize = fontSize, fontFamily = FontFamily.Monospace)
                }
            }
            if (score.isNotEmpty())
                Column(
                    modifier = Modifier
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = score,
                            modifier = Modifier
                                .padding(Size.xSmall()),
                            fontSize = TextSize.huge(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colors.primaryVariant
                        )
                        Text(
                            text = getString(R.string.label_average_vote),
                            fontSize = TextSize.xxSmall()
                        )
                    }
                    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = list.mapNotNull { it.vote?.toIntOrNull() }.minOfOrNull { it }?.toString() ?: "-",
                                modifier = Modifier
                                    .padding(Size.xSmall()),
                                fontSize = TextSize.regular(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
//                                color = MaterialTheme.colors.primaryVariant
                            )
                            Text(
                                text = getString(R.string.label_lowest_vote),
                                fontSize = TextSize.xxSmall()
                            )
                        }
                        Spacer(modifier = Modifier.requiredWidth(Size.regular()))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = list.mapNotNull { it.vote?.toIntOrNull() }.maxOfOrNull { it }?.toString() ?: "-",
                                modifier = Modifier
                                    .padding(Size.xSmall()),
                                fontSize = TextSize.regular(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
//                                color = MaterialTheme.colors.primaryVariant
                            )
                            Text(
                                text = getString(R.string.label_highest_vote),
                                fontSize = TextSize.xxSmall()
                            )
                        }
                    }
                }
        }
    }

    @Composable
    private fun BottomBarSuccess(
        state: RoomUiState.Success,
        showSheetContent: (@Composable () -> Unit) -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Size.small()),
            horizontalArrangement = Arrangement.spacedBy(Size.small(), alignment = End)
        ) {
            IconButton(
                modifier = Modifier
                    .weight(1f)
                    .alpha(if (state.config.isOwner) 1f else 0.3f),
                enabled = state.config.isOwner,
                onClick = {
                    showSheetContent { RoomScreenSettingsSheet() }
                }
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            }
            IconButton(
                modifier = Modifier
                    .weight(2f)
                    .alpha(if (state.config.isOwner) 1f else 0.3f),
                enabled = state.config.isOwner && state.userList.any { it.vote != null },
                onClick = {
                    model.resetVote()
                }
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    showSheetContent { RoomScreenMemberListSheet() }
                }
            ) {
                Icon(imageVector = Icons.Default.List, contentDescription = null)
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun RoomScreenSettingsSheet() {
        val modelState by model.uiState.observeAsState()
        if (modelState !is RoomUiState.Success)
            return
        val config = (modelState as RoomUiState.Success).config
        var dropdownStateExpanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Size.xSmall()),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = getString(R.string.label_vote_visible_setting))
                Switch(
                    checked = config.alwaysVisibleVote,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary,
                        uncheckedThumbColor = MaterialTheme.colors.secondary
                    ),
                    onCheckedChange = {
                        if (it)
                            model.showVoteValues()
                        else
                            model.hideVoteValues()
                    }
                )
            }
            Spacer(modifier = Modifier.height(Size.small()))
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = dropdownStateExpanded,
                onExpandedChange = {
                    dropdownStateExpanded = !dropdownStateExpanded
                }
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = config.scaleType.toLowerCase(Locale.current).capitalize(Locale.current),
                    onValueChange = { },
                    label = {
                        Text(text = getString(R.string.label_choose_style), fontSize = TextSize.xSmall(),)
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = dropdownStateExpanded
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                        focusedLabelColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
                        focusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                        focusedTrailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity)
                    )
                )
                ExposedDropdownMenu(
                    expanded = dropdownStateExpanded,
                    onDismissRequest = {
                        dropdownStateExpanded = false
                    }
                ) {
                    config.scaleTypeList.sortedBy { it }.forEach { item ->
                        DropdownMenuItem(onClick = {
                            model.setScale(item)
                            dropdownStateExpanded = false
                        }) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = Size.small()),
                                text = item.toLowerCase(Locale.current).capitalize(Locale.current)
                            )
                        }
                    }
                }
            }
//            Spacer(modifier = Modifier.height(Size.small()))
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                content = {
//                    items(config.scaleTypeList.sortedBy { it }) { item ->
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(Size.small())
//                                .clickable { model.setScale(item) },
//                            backgroundColor = if (item == config.scaleType) MaterialTheme.colors.primary else MaterialTheme.colors.secondary
//                        ) {
//                            Column(
//                                modifier = Modifier.fillMaxWidth(),
//                                horizontalAlignment = Alignment.CenterHorizontally
//                            ) {
//                                Text(
//                                    modifier = Modifier
//                                        .padding(vertical = Size.small()),
//                                    text = item.toLowerCase(Locale.current).capitalize(Locale.current)
//                                )
//                            }
//                        }
//                    }
//                }
//            )
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
                .padding(horizontal = Size.xSmall(), vertical = Size.xxSmall()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = user.username, fontSize = TextSize.small(), maxLines = 1, overflow = TextOverflow.Ellipsis, softWrap = true)
            if (user.isOwner)
                Icon(
                    modifier = Modifier
                        .padding(Size.xSmall())
                        .size(Size.regular()),
                    painter = painterResource(id = R.drawable.ic_crown), contentDescription = null, tint = Color(android.graphics.Color.parseColor("#FFD43E"))
                )
            Spacer(modifier = Modifier.weight(1f))
            val sizeModifier = Modifier.size(Size.xLarge())
            if (user.vote != null)
                Button(modifier = sizeModifier, contentPadding = PaddingValues(0.dp), onClick = {}) {
                    if (isVoteVisible)
                        Text(text = user.vote!!, fontSize = TextSize.xSmall())
                    else
                        Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colors.onPrimary)
                }
            else
                OutlinedButton(modifier = sizeModifier, onClick = {}) {
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
    private fun RoomScreenShareSheet(roomName: String?, url: MultiUrl?, roomCode: String?) {
        if (roomName == null || url == null)
            return
        val qrCode = remember { getQrCode(url.appSchema) }
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier.fillMaxWidth()
                .scrollable(scrollState, orientation = Orientation.Vertical),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            Text(text = getString(R.string.label_share), fontSize = TextSize.xSmall())
            Spacer(modifier = Modifier.height(Size.regular()))
            Image(modifier = Modifier.fillMaxWidth(0.75f), bitmap = qrCode, contentDescription = null, contentScale = ContentScale.FillWidth)
            Spacer(modifier = Modifier.height(Size.regular()))
            if (roomCode != null)
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Size.small()),
//                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = { copyToClipboard(roomCode) }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = getString(R.string.label_room_code),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colors.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = roomCode,
                                textAlign = TextAlign.Center,
                                fontSize = TextSize.large(),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
//                            Icon(modifier = Modifier.size(Size.large()), painter = painterResource(id = R.drawable.ic_copy_content), contentDescription = null)
                        }
                    }
                }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size.small()),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                onClick = { copyToClipboard(url.appSchema) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = getString(R.string.label_room_app_url))
                    Icon(modifier = Modifier.size(Size.large()), painter = painterResource(id = R.drawable.ic_copy_content), contentDescription = null)
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size.small()),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                onClick = { copyToClipboard(url.httpSchema) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = getString(R.string.label_room_web_url))
                    Icon(modifier = Modifier.size(Size.large()), painter = painterResource(id = R.drawable.ic_copy_content), contentDescription = null)
                }
            }
        }
    }

    private fun copyToClipboard(value: String) {
        val manager = this.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(ClipData.newPlainText("ScrumBet", value))
    }

    private fun getQrCode(value: String): ImageBitmap {
        val QR_BITMAP_SIZE = 512
        return QRCodeWriter()
            .encode(
                value,
                BarcodeFormat.QR_CODE,
                QR_BITMAP_SIZE,
                QR_BITMAP_SIZE,
                mutableMapOf(EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.H)
            )
            .encodeToBitmap()
            .asImageBitmap()
    }

    @Preview(widthDp = 320, heightDp = 320)
    @Composable
    fun AvgVoteUtil_Preview() {
        MdcTheme() {
            VoteScoreUi(isOwner = true, list = listOf(RoomUser("", "preview", true, "2")))
        }
    }
}
