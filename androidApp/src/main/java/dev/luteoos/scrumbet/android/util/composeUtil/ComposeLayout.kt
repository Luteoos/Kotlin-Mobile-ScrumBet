import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.android.ext.notify
import dev.luteoos.scrumbet.android.ext.toggle
import dev.luteoos.scrumbet.android.util.composeUtil.Size
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetDefaultLayout(model: BaseViewModel, confirmSheetState: () -> Boolean = { true }, sheetContent: @Composable () -> Unit, content: @Composable (() -> Unit) -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = if (isVisible) ModalBottomSheetValue.Expanded else ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden)
                model.hideKeyboard.notify()
            isVisible = it != ModalBottomSheetValue.Hidden
            confirmSheetState()
        },
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    var isFirstShow by remember { mutableStateOf(true) }
    var sheetVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = sheetVisibility, block = {
        if (isFirstShow) {
            isFirstShow = false
            return@LaunchedEffect
        }
        if (!isVisible)
            scope.launch {
                modalSheetState.show()
                isVisible = true
            }
        else
            scope.launch {
                modalSheetState.hide()
                isVisible = false
            }
    })

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetContent = {
            Column(
                Modifier
                    .padding(Size.regular())
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = Size.minHeight())
            ) {
                sheetContent()
            }
        },
        sheetShape = RoundedCornerShape(topEnd = Size.regular(), topStart = Size.regular())
    ) {
        content {
            sheetVisibility = sheetVisibility.toggle()
        }
    }
}

@Composable
fun LoadingView() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    paddingLeadingIconEnd: Dp = Size.regular(),
    paddingTrailingIconStart: Dp = Size.regular(),
    singleLine: Boolean = true,
    leadingIcon: (@Composable() () -> Unit)? = null,
    trailingIcon: (@Composable() () -> Unit)? = null,
    initialValue: String? = null,
    textFieldColor: Color = MaterialTheme.colors.onSurface,
    onValueChange: (String) -> Unit
) {
    var state by rememberSaveable { mutableStateOf(initialValue ?: "") }

    LaunchedEffect(key1 = initialValue, block = {
        state = initialValue ?: ""
    })

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(percent = 10)
            )
            .padding(Size.regular()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            leadingIcon()
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = paddingLeadingIconEnd, end = paddingTrailingIconStart)
        ) {
            BasicTextField(
                textStyle = LocalTextStyle.current.copy(textFieldColor),
                value = state,
                onValueChange = {
                    state = it
                    onValueChange(it)
                },
                singleLine = singleLine
            )
            if (state.isEmpty()) {
                Text(
                    text = ""
                )
            }
        }
        if (trailingIcon != null) {
            trailingIcon()
        }
    }
}
