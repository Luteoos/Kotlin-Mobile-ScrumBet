@file:OptIn(ExperimentalMaterial3Api::class)

package dev.luteoos.scrumbet.android.ui.room

import DefaultModalSheet
import LoadingView
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseComposeFragment
import dev.luteoos.scrumbet.android.ext.toMainScreen
import dev.luteoos.scrumbet.android.util.IntegerLabelFormatter
import dev.luteoos.scrumbet.android.util.composeUtil.KeepAlive
import dev.luteoos.scrumbet.android.util.composeUtil.Size
import dev.luteoos.scrumbet.android.util.composeUtil.TextSize
import dev.luteoos.scrumbet.android.util.encodeToBitmap
import dev.luteoos.scrumbet.data.entity.MultiUrl
import dev.luteoos.scrumbet.data.state.room.RoomUser
import kotlinx.coroutines.launch

class RoomFragment : BaseComposeFragment<RoomViewModel>(RoomViewModel::class) {
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

    @Composable
    override fun ComposeLayout() {
        RoomScreenUi(model)
    }

    override fun initFlowCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                model.connect()
            }
        }
    }
}

@Preview(
    widthDp = 320, heightDp = 320, showSystemUi = false, showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun VoteResultUi_Preview() {
    Mdc3Theme() {
        VoteResultUi(list = listOf(RoomUser("", "preview", true, "2")), scale = listOf("1", "2", "?"))
    }
}

@Composable
private fun RoomScreenUi(model: RoomViewModel) {
    val state by model.uiState.observeAsState(RoomUiState.Loading)
    val shareSheetState = rememberModalBottomSheetState(true)
    val scope = rememberCoroutineScope()

    val title = when (state) {
        RoomUiState.Disconnect, RoomUiState.Loading -> stringResource(R.string.label_connecting)
        is RoomUiState.Error -> stringResource(R.string.label_error)
        is RoomUiState.Success -> stringResource(R.string.label_owner, (state as RoomUiState.Success).userList.firstOrNull { it.isOwner }?.username ?: "")
    }
    val roomName = when (state) {
        is RoomUiState.Success -> (state as RoomUiState.Success).connectionName
        else -> null
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background),
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
                            scope.launch {
                                shareSheetState.show()
                            }
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
                BottomBarSuccess(
                    model = model,
                    state = state as RoomUiState.Success,
                    resetVote = { model.resetVote() }
                )
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
                is RoomUiState.Error -> RoomScreenUiError(
                    state = state as RoomUiState.Error,
                    retry = { model.connect() }
                )
                RoomUiState.Loading -> RoomScreenUiLoading()
                is RoomUiState.Success -> RoomScreenUiConnected(
                    state = state as RoomUiState.Success,
                    setVote = { model.setVote(it) }
                )
            }
        }
        if (shareSheetState.isVisible)
            DefaultModalSheet(scope, shareSheetState) {
                RoomScreenShareSheet(
                    roomName,
                    (state as? RoomUiState.Success)?.config?.url,
                    (state as? RoomUiState.Success)?.config?.roomJoinCode
                )
            }
    }
}

@Composable
private fun RoomScreenUiConnected(
    state: RoomUiState.Success,
    setVote: (String) -> Unit
) {
    KeepAlive()
    var currentPick = state.userVote // by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        VoteResultUi(list = state.userList, scale = state.config.scale)
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
                val shape = ShapeDefaults.Large
                state.config.scale.forEach { value ->
                    item {
                        val onClick = {
                            currentPick = value
                            setVote(value)
                        }
                        if (currentPick == null || currentPick == value)
                            Button(
                                modifier = buttonModifier,
                                onClick = onClick,
                                shape = shape
//                                    colors = ButtonDefaults.buttonColors(
//                                        backgroundColor = if(value == "?") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary)
                            ) {
                                Text(text = value)
                            }
                        else
                            OutlinedButton(
                                modifier = buttonModifier,
                                onClick = onClick,
                                shape = shape
                            ) {
                                Text(text = value)
                            }
                    }
                }
            }
        )
    }
}

@Composable
fun VoteResultUi(list: List<RoomUser>, scale: List<String>) {
    val score: String = if (list.any { it.vote == null })
        ""
    else
        list.mapNotNull { it.vote?.toIntOrNull() }.let { if (it.isNotEmpty()) it.sum() / it.size else "?" }.toString()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth(.85f)
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
                Text(text = stringResource(R.string.label_users_in_room), fontSize = fontSize)
                Text(text = stringResource(R.string.label_users_voted), fontSize = fontSize)
            }
            Column() {
                Text(text = "${list.size}", fontWeight = FontWeight.Bold, fontSize = fontSize, fontFamily = FontFamily.Monospace)
                Text(text = "${list.count { it.vote != null }}", fontWeight = FontWeight.Bold, fontSize = fontSize, fontFamily = FontFamily.Monospace)
            }
        }
        if (score.isNotEmpty())
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .verticalScroll(scrollState),
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
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                    Text(
                        text = stringResource(R.string.label_average_vote),
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
                        )
                        Text(
                            text = stringResource(R.string.label_lowest_vote),
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
                        )
                        Text(
                            text = stringResource(R.string.label_highest_vote),
                            fontSize = TextSize.xxSmall()
                        )
                    }
                }
                val chartData = scale.map { voteValue ->
                    BarChartData.Bar(list.count { it.vote == voteValue }.toFloat(), MaterialTheme.colorScheme.primary, voteValue)
                }
                Column {
                    val formatter = remember {
                        IntegerLabelFormatter()
                    }
                    BarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .padding(bottom = Size.large(), top = Size.regular()),
                        barChartData = BarChartData(chartData),
                        animation = simpleChartAnimation(),
                        xAxisDrawer = SimpleXAxisDrawer(axisLineColor = MaterialTheme.colorScheme.onSurface),
                        yAxisDrawer = SimpleYAxisDrawer(
                            labelTextColor = MaterialTheme.colorScheme.onBackground,
                            labelRatio = 10,
                            labelValueFormatter = {
                                formatter.format(it)
                            },
                            axisLineColor = MaterialTheme.colorScheme.onSurface
                        ),
                        labelDrawer = SimpleValueDrawer(SimpleValueDrawer.DrawLocation.XAxis, labelTextColor = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
    }
}

@Composable
private fun BottomBarSuccess(
    model: RoomViewModel,
    state: RoomUiState.Success,
    resetVote: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val settingsSheetState = rememberModalBottomSheetState()
    val membersSheetState = rememberModalBottomSheetState()
    BottomAppBar(containerColor = MaterialTheme.colorScheme.background) {
        IconButton(
            modifier = Modifier
                .weight(1f)
                .alpha(if (state.config.isOwner) 1f else 0.3f),
            enabled = state.config.isOwner,
            onClick = {
                scope.launch {
                    settingsSheetState.show()
                }
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
                resetVote()
            }
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
        }
        IconButton(
            modifier = Modifier.weight(1f),
            onClick = {
                scope.launch {
                    membersSheetState.show()
                }
            }
        ) {
            Icon(imageVector = Icons.Default.List, contentDescription = null)
        }
    }
    if (settingsSheetState.isVisible)
        DefaultModalSheet(scope, settingsSheetState) {
            RoomScreenSettingsSheet(model)
        }
    if (membersSheetState.isVisible)
        DefaultModalSheet(scope, membersSheetState) {
            RoomScreenMemberListSheet(model)
        }
}

@Composable
private fun RoomScreenSettingsSheet(model: RoomViewModel) {
    val modelState by model.uiState.observeAsState()
    if (modelState !is RoomUiState.Success)
        return
    val config = (modelState as RoomUiState.Success).config
    var dropdownStateExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = Size.regular())
            .padding(horizontal = Size.regular()),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Bottom
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(R.string.label_vote_visible_setting))
            Switch(
                checked = config.alwaysVisibleVote,
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
            modifier = Modifier
                .fillMaxWidth()
                .focusable(false),
            expanded = dropdownStateExpanded,
            onExpandedChange = {
                dropdownStateExpanded = !dropdownStateExpanded
            }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                readOnly = true,
                value = config.scaleType.toLowerCase(Locale.current).capitalize(Locale.current),
                onValueChange = { },
                label = {
                    Text(text = stringResource(R.string.label_choose_style), fontSize = TextSize.xSmall(),)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = dropdownStateExpanded
                    )
                },
                // https://developer.android.com/jetpack/compose/designsystems/material2-material3#emphasis-and
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                    focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                    focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            ExposedDropdownMenu(
                expanded = dropdownStateExpanded,
                onDismissRequest = {
                    dropdownStateExpanded = false
                }
            ) {
                config.scaleTypeList.sortedBy { it }.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = Size.small()),
                                text = item.toLowerCase(Locale.current).capitalize(Locale.current)
                            )
                        },
                        onClick = {
                            model.setScale(item)
                            dropdownStateExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RoomScreenMemberListSheet(model: RoomViewModel) {
    val modelState by model.uiState.observeAsState()
    if (modelState !is RoomUiState.Success)
        return
    val state = modelState as RoomUiState.Success
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Size.regular()),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Bottom
    ) {
        Text(
            text = stringResource(R.string.label_member_list),
            fontSize = TextSize.xSmall()
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
        val shape = ShapeDefaults.Large
        if (user.vote != null)
            Button(modifier = sizeModifier, shape = shape, contentPadding = PaddingValues(0.dp), onClick = {}) {
                if (isVoteVisible)
                    Text(text = user.vote!!, fontSize = TextSize.xSmall())
                else
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
            }
        else
            OutlinedButton(modifier = sizeModifier, shape = shape, onClick = {}) {
                Text(text = " ", fontSize = TextSize.xSmall())
            }
    }
}

@Composable
private fun RoomScreenUiError(state: RoomUiState.Error, retry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.label_connection_failed))
        Button(onClick = retry) {
            Text(text = stringResource(R.string.label_retry))
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
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Bottom
    ) {
        Text(text = stringResource(R.string.label_share), fontSize = TextSize.xSmall())
        Spacer(modifier = Modifier.height(Size.regular()))
        Image(modifier = Modifier.fillMaxWidth(0.75f), bitmap = qrCode, contentDescription = null, contentScale = ContentScale.FillWidth)
        Spacer(modifier = Modifier.height(Size.regular()))
        if (roomCode != null)
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size.small()),
                onClick = { copyToClipboard(context, roomCode) }
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.label_room_code),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
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
                    }
                }
            }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Size.small()),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            onClick = { copyToClipboard(context, url.appSchema) }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.label_room_app_url))
                Icon(modifier = Modifier.size(Size.large()), painter = painterResource(id = R.drawable.ic_copy_content), contentDescription = null)
            }
        }

        if(false){
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size.small()),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                onClick = { copyToClipboard(context, url.httpSchema) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.label_room_web_url))
                    Icon(modifier = Modifier.size(Size.large()), painter = painterResource(id = R.drawable.ic_copy_content), contentDescription = null)
                }
            }
        }
    }
}

private fun copyToClipboard(context: Context, value: String) {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
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
