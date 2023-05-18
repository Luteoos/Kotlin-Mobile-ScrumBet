package dev.luteoos.scrumbet.android.ui.main

import BottomSheetDefaultLayout
import CustomTextField
import LoadingView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.themeadapter.material.MdcTheme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.MainFragmentBinding
import dev.luteoos.scrumbet.android.ext.notify
import dev.luteoos.scrumbet.android.ext.toRoomScreen
import dev.luteoos.scrumbet.android.ui.composeUtil.Size
import dev.luteoos.scrumbet.android.ui.composeUtil.TextSize

@OptIn(
    ExperimentalPermissionsApi::class
)
class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>(MainViewModel::class) {

    override val layoutId: Int = R.layout.main_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> MainFragmentBinding =
        { inflater, viewGroup, attachToParrent ->
            MainFragmentBinding.inflate(inflater, viewGroup, attachToParrent)
        }

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
            println("isAuthorized: $it")
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

    override fun initBindingValues() {
        binding.composeView.setContent {
            MdcTheme {
                var customSheetContent by remember { mutableStateOf<@Composable (() -> Unit)>({ }) }
                val state = model.uiState.observeAsState()
                BottomSheetDefaultLayout(
                    model,
                    sheetContent = customSheetContent
                ) { toggleSheetState ->
                    Scaffold(
                        Modifier
                            .fillMaxSize()
                            .padding(Size.regular())
                    ) { scaffoldPadding ->
                        when (val uiState = state.value ?: UserUiState.Loading) {
                            is UserUiState.Success -> {
                                MainScreenUi(
                                    padding = scaffoldPadding,
                                    username = uiState.data.username,
                                    updateSheetContent = { customSheetContent = it },
                                    toggleSheetVisibility = toggleSheetState
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
            }
        }
    }

    @Composable
    private fun MainScreenUi(
        padding: PaddingValues,
        username: String,
        updateSheetContent: (@Composable () -> Unit) -> Unit,
        toggleSheetVisibility: () -> Unit
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Center) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.6f)
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = getString(R.string.label_hello, username),
                    fontSize = TextSize.xLarge(),
                    textAlign = TextAlign.Center
                )
                TextButton(
                    onClick = {
                        updateSheetContent {
                            MainScreenUserEditSheet(username = username, onSave = { newUsername ->
                                model.updateUsername(newUsername)
                                model.hideKeyboard.notify()
                                toggleSheetVisibility()
                            })
                        }
                        toggleSheetVisibility()
                    },
                    contentPadding = PaddingValues(Size.xSmall())
                ) {
                    Row(verticalAlignment = CenterVertically) {
                        Icon(
                            modifier = Modifier.size(Size.xRegular()),
                            painter = painterResource(id = com.google.android.material.R.drawable.material_ic_edit_black_24dp),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground
                        )
                        Text(
                            text = getString(R.string.label_edit),
                            fontSize = TextSize.xSmall(),
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        updateSheetContent {
                            MainScreenJoinSheet(onJoin = { roomId ->
                                toggleSheetVisibility()
                                model.setRoomId(roomId)
                            })
                        }
                        toggleSheetVisibility()
                    }
                ) {
                    Text(
                        text = getString(R.string.label_join),
                        fontSize = TextSize.small()
                    )
                }
                Text(text = getString(R.string.divider_label_or), fontSize = TextSize.small())
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    model.createNewRoom()
                }) {
                    Text(text = getString(R.string.label_create), fontSize = TextSize.small(), fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    @Composable
    private fun MainScreenUserEditSheet(username: String, onSave: (username: String) -> Unit) {
        var name by remember { mutableStateOf(username) }
        var isSaveEnabled by remember { mutableStateOf(true) }
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Bottom
        ) {
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                initialValue = name,
                singleLine = true,
                onValueChange = {
                    name = it
                    isSaveEnabled = name.isNotBlank()
                },
                leadingIcon = {
                    Text(text = getString(R.string.label_name))
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
                Text(text = getString(R.string.label_save))
            }
        }
    }

    @Composable
    private fun MainScreenJoinSheet(onJoin: (id: String) -> Unit) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            var roomId by remember { mutableStateOf("") }
            var isConnectEnabled by remember { mutableStateOf(false) }
            val cameraPermissionState = rememberPermissionState(
                android.Manifest.permission.CAMERA
            )

            LaunchedEffect(key1 = roomId, block = {
                isConnectEnabled = roomId.isNotBlank()
            })

            Text(
                modifier = Modifier.padding(bottom = Size.large()),
                text = getString(R.string.label_join_by),
                fontSize = TextSize.regular()
            )

            Text(
                modifier = Modifier.padding(bottom = Size.xSmall()),
                text = getString(R.string.label_scan_qr_code), fontSize = TextSize.small()
            )
            if (cameraPermissionState.status.isGranted) {
                val lifecycleOwner = LocalLifecycleOwner.current
                AndroidView(modifier = Modifier.fillMaxHeight(.45f), factory = { context ->
                    io.github.luteoos.qrx.QrXScanner(context).also {
                        it.initialize(
                            lifecycleOwner, { barcode ->
                            barcode.rawValue?.let { value ->
                                roomId = value
                            }
                        }, {
                        }
                        )
                        it.onPermission(cameraPermissionState.status.isGranted)
                    }
                })
            } else {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                    onClick = {
                        cameraPermissionState.launchPermissionRequest()
                    }
                ) {
                    Text(text = getString(R.string.label_grant_permission_camera))
                }
            }
            Text(
                modifier = Modifier.padding(vertical = Size.regular()),
                text = getString(R.string.divider_label_or)
            )
            CustomTextField(
                modifier = Modifier.fillMaxWidth(),
                initialValue = roomId,
                singleLine = true,
                onValueChange = {
                    roomId = it
                },
                leadingIcon = {
                    Text(text = getString(R.string.label_room_name))
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
                Text(text = getString(R.string.label_connect))
            }
        }
    }
}
