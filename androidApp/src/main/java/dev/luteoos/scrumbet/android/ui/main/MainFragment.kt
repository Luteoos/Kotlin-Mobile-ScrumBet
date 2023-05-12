package dev.luteoos.scrumbet.android.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material.MdcTheme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.MainFragmentBinding

class MainFragment : BaseFragment<MainViewModel, MainFragmentBinding>(MainViewModel::class) {

    override val layoutId: Int = R.layout.main_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> MainFragmentBinding = { inflater, viewGroup, attachToParrent ->
        MainFragmentBinding.inflate(inflater, viewGroup, attachToParrent)
    }

    override fun initObservers() {
        model.uiState.observe(this) {
            when (it) {
                is UserUiState.Error -> {}
                UserUiState.Loading -> {}
                is UserUiState.Success -> {}
            }
        }
    }

    override fun initBindingValues() {
        binding.composeView.setContent {
            MdcTheme() {
                Scaffold(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) { padding ->
                    val state = model.uiState.observeAsState()
                    val uiState = state.value ?: UserUiState.Loading
                    when (uiState) {
                        is UserUiState.Success -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Center) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(.6f)
                                        .padding(padding),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "Hello, ${uiState.data.username}", modifier = Modifier.padding(2.dp), fontSize = TextUnit(34f, TextUnitType.Sp), textAlign = TextAlign.Center)
                                    TextButton(onClick = { /*TODO*/ }, contentPadding = PaddingValues(4.dp)) {
                                        Row(verticalAlignment = CenterVertically) {
                                            Icon(modifier = Modifier.size(14.dp), painter = painterResource(id = com.google.android.material.R.drawable.material_ic_edit_black_24dp), contentDescription = "", tint = Color.White)
                                            Text(text = "Edit", fontSize = TextUnit(15f, TextUnitType.Sp), fontWeight = FontWeight.Light, color = Color.White)
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                                        Text(text = "Join", fontSize = TextUnit(17f, TextUnitType.Sp))
                                    }
                                    Text(text = "or", fontSize = TextUnit(17f, TextUnitType.Sp))
                                    Button(modifier = Modifier.fillMaxWidth(), onClick = { /*TODO*/ }) {
                                        Text(text = "Create", fontSize = TextUnit(17f, TextUnitType.Sp))
                                    }
                                }
                            }
                        }
                        is UserUiState.Error -> {
                            Text(text = "Error")
                        }
                        UserUiState.Loading -> {
                            Column(
                                Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    Text(text = "")
                }
            }
        }
    }

    override fun initFlowCollectors() {
    }
}
