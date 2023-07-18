import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultModalSheet(scope: CoroutineScope, sheetState: SheetState, content: @Composable () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = { scope.launch { sheetState.hide() } },
        sheetState = sheetState
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Deprecated("Deprecated in favor of M3 ModalBottomSheet", level = DeprecationLevel.ERROR)
@Composable
fun ModalBottomSheetDefaultLayout(model: BaseViewModel, confirmSheetState: () -> Boolean = { true }, sheetContent: @Composable () -> Unit, content: @Composable (() -> Unit) -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    val modalSheetState = rememberModalBottomSheetState(
//        initialValue = if (isVisible) SheetValue.Expanded else SheetValue.Hidden,
        confirmValueChange = {
            if (it == SheetValue.Hidden)
                model.hideKeyboard.notify()
            isVisible = it != SheetValue.Hidden
            confirmSheetState()
        },
        skipPartiallyExpanded = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = modalSheetState
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

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column(
                Modifier
                    .animateContentSize(animationSpec = tween(100))
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
    textFieldColor: Color = MaterialTheme.colorScheme.onSurface,
    onValueChange: (String) -> Unit
) {
    var state by rememberSaveable { mutableStateOf(initialValue ?: "") }

    LaunchedEffect(key1 = initialValue, block = {
        state = initialValue ?: ""
    })

    Row(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
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
