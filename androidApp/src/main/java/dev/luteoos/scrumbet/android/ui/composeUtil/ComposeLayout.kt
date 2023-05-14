import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.android.ext.notify
import dev.luteoos.scrumbet.android.ui.composeUtil.Size
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetDefaultLayout(model: BaseViewModel, toggleSheetVisibility: Boolean, confirmSheetState: () -> Boolean = { true }, sheetContent: @Composable () -> Unit, content: @Composable (ModalBottomSheetState) -> Unit) {
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = false,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden)
                model.hideKeyboard.notify()
            confirmSheetState()
        }
    )
    val scope = rememberCoroutineScope()
    var isFirstShow by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = toggleSheetVisibility, block = {
        if (isFirstShow) {
            isFirstShow = false
            return@LaunchedEffect
        }
        if (!modalSheetState.isVisible)
            scope.launch { modalSheetState.show() }
        else
            scope.launch { modalSheetState.hide() }
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
        content(modalSheetState)
    }
}
