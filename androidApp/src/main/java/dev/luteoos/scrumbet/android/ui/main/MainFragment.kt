@file:OptIn(ExperimentalMaterial3Api::class)

package dev.luteoos.scrumbet.android.ui.main

import LoadingView
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseComposeFragment
import dev.luteoos.scrumbet.android.ext.notify
import dev.luteoos.scrumbet.android.ext.toRoomScreen
import dev.luteoos.scrumbet.android.util.composeUtil.Size
import dev.luteoos.scrumbet.android.util.composeUtil.TextSize
import dev.luteoos.scrumbet.shared.Log
import kotlinx.coroutines.launch

class MainFragment : BaseComposeFragment<MainViewModel>(MainViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initObservers() {
        model.uiState.observe(this) {
            when (it) {
                is UserUiState.Error -> {}
                UserUiState.Loading -> {}
                is UserUiState.Success -> {}
            }
        }
        model.isAuthorized.observe(this) {
            Log.d("isAuthorized: $it")
            if (it)
                activity?.toRoomScreen()
        }
    }

    override fun initFlowCollectors() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                authModel.isAuthorized.collect {
//                    if (it)
//                        TODO("Navigate to roomScreen")
//                }
//            }
//        }
    }

    @Composable
    override fun ComposeLayout() {
        MainScreenUi(model)
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun MainScreenUi(model: MainViewModel) {
    val state = model.uiState.observeAsState()
    Scaffold(
        Modifier
            .fillMaxSize()
    ) { _ ->
        when (val uiState = state.value ?: UserUiState.Loading) {
            is UserUiState.Success -> {
                MainScreenSuccess(
                    model = model,
                    username = uiState.data.username,
                    padding = PaddingValues(Size.regular())
                )
            }

            is UserUiState.Error -> {
                Text(text = "Error")
            }

            UserUiState.Loading -> {
                LoadingView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun MainScreenSuccess(
    model: MainViewModel,
    username: String,
    padding: PaddingValues
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Center) {
        val scrollState = rememberScrollState()

        val scope = rememberCoroutineScope()
        val usernameSheetState = rememberModalBottomSheetState()
        val joinSheetState = rememberModalBottomSheetState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.6f)
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(R.string.label_hello, username),
                fontSize = TextSize.xLarge(),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                maxLines = 2,
                lineHeight = TextSize.xLarge()
            )
            TextButton(
                onClick = {
                    scope.launch {
                        usernameSheetState.show()
                    }
                },
                contentPadding = PaddingValues(Size.xSmall()),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
            ) {
                Row(verticalAlignment = CenterVertically) {
                    Icon(
                        modifier = Modifier.size(Size.xRegular()),
                        painter = painterResource(id = com.google.android.material.R.drawable.material_ic_edit_black_24dp),
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.label_edit),
                        fontSize = TextSize.xxxSmall(),
                        fontWeight = FontWeight.Light
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    scope.launch {
                        joinSheetState.show()
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.label_join),
                    fontSize = TextSize.small()
                )
            }
            Text(text = stringResource(R.string.divider_label_or), fontSize = TextSize.small())
            Button(modifier = Modifier.fillMaxWidth(), onClick = {
                model.createNewRoom()
            }) {
                Text(text = stringResource(R.string.label_create), fontSize = TextSize.small(), fontWeight = FontWeight.Bold)
            }
        }
        if (usernameSheetState.isVisible)
            ModalBottomSheet(
                onDismissRequest = { scope.launch { usernameSheetState.hide() } },
                sheetState = usernameSheetState
            ) {
                MainScreenUserEditSheet(username = username, onSave = { newUsername ->
                    model.updateUsername(newUsername)
                    model.hideKeyboard.notify()
                    scope.launch {
                        usernameSheetState.hide()
                    }
                })
            }
        if (joinSheetState.isVisible)
            ModalBottomSheet(
                onDismissRequest = { scope.launch { joinSheetState.hide() } },
                sheetState = joinSheetState
            ) {
                MainScreenJoinSheet { roomId ->
                    scope.launch { joinSheetState.hide() }
                    model.setRoomId(roomId)
                }
            }
    }
}

@Composable
private fun MainScreenUserEditSheet(username: String, onSave: (username: String) -> Unit) {
    var name by remember { mutableStateOf(username) }
    var isSaveEnabled by remember { mutableStateOf(true) }

    Column(
        Modifier.fillMaxWidth().padding(Size.regular()),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Bottom
    ) {
        OutlinedTextField(
            value = name,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.Text),
            singleLine = true,
            onValueChange = {
                name = it
                isSaveEnabled = name.isNotBlank()
            },
            label = {
                Text(text = stringResource(R.string.label_name))
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Size.xLarge()),
            enabled = isSaveEnabled,
            onClick = {
                onSave(name)
            }
        ) {
            Text(text = stringResource(R.string.label_save))
        }
    }
}

@Composable
private fun MainScreenJoinSheet(onJoin: (id: String) -> Unit) {
    var state: JoinSheetState by remember { mutableStateOf(JoinSheetState.NONE) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Size.regular(), horizontal = Size.regular())
            .animateContentSize(tween()),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        when (state) {
            JoinSheetState.NONE -> {
                Button(onClick = {
                    state = JoinSheetState.QR
                }) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.label_scan_qr_code),
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    modifier = Modifier.padding(vertical = Size.small()),
                    text = stringResource(R.string.divider_label_or)
                )
                Button(onClick = {
                    state = JoinSheetState.ROOM_NAME
                }) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.label_enter_room_name),
                        textAlign = TextAlign.Center
                    )
                }
            }
            JoinSheetState.QR -> MainScreenJoinQrCodeSheet(onJoin = onJoin)
            JoinSheetState.ROOM_NAME -> MainScreenJoinRoomNameSheet(onJoin = onJoin)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MainScreenJoinQrCodeSheet(onJoin: (id: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val cameraPermissionState = rememberPermissionState(
            android.Manifest.permission.CAMERA
        )

        SideEffect {
            println("SideEffectMainFragmentJoinQR")
            if (!cameraPermissionState.status.isGranted)
                cameraPermissionState.launchPermissionRequest()
        }

        if (cameraPermissionState.status.isGranted) {
            val lifecycleOwner = LocalLifecycleOwner.current
            AndroidView(modifier = Modifier.padding(bottom = Size.xLarge()), factory = { context ->
                io.github.luteoos.qrx.QrXScanner(context).also {
                    it.initialize(lifecycleOwner, { }, { })
                    it.onBarcodeScannedListener = { list ->
                        list.firstOrNull()?.let { barcode ->
                            barcode.rawValue?.let { value ->
                                onJoin(value)
                            }
                        }
                    }
                    it.onPermission(cameraPermissionState.status.isGranted)
                }
            })
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(.75f),
                text = stringResource(R.string.label_grant_permission_camera)
            )
        }
    }
}

@Composable
private fun MainScreenJoinRoomNameSheet(onJoin: (id: String) -> Unit) {
    Column() {
        var roomId by remember { mutableStateOf("") }
        var isConnectEnabled by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = roomId, block = {
            isConnectEnabled = roomId.isNotBlank()
        })

        OutlinedTextField(
            value = roomId,
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.NumberPassword),
            onValueChange = {
                roomId = it
            },
            label = {
                Text(text = stringResource(R.string.label_room_code))
            }
        )
        Spacer(modifier = Modifier.fillMaxHeight(.25f))
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isConnectEnabled,
            onClick = {
                onJoin(roomId)
            }
        ) {
            Text(text = stringResource(R.string.label_connect))
        }
    }
}
