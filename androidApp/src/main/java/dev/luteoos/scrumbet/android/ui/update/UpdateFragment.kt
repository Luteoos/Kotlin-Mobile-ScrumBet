package dev.luteoos.scrumbet.android.ui.update

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.result.IntentSenderRequest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseComposeFragment
import dev.luteoos.scrumbet.android.ui.MainActivity
import dev.luteoos.scrumbet.android.util.composeUtil.Size
import dev.luteoos.scrumbet.android.util.composeUtil.TextSize


class UpdateFragment : BaseComposeFragment<UpdateViewModel>(UpdateViewModel::class) {
    private val contactEmail = "mateusz.lutecki@fujitsu.com"

    override fun initObservers() {
        // empty
    }

    override fun initFlowCollectors() {
        // empty
    }

    override fun onResume() {
        super.onResume()
        activity?.let {activity ->
            if(activity is MainActivity)
                model.requestAppUpdate(activity)
        }
    }

    @Composable
    override fun ComposeLayout() {
        UpdateFragmentUI(contactEmail = contactEmail, appStoreUrl = model.appStoreUrl)
    }
}

@Composable
private fun UpdateFragmentUI(contactEmail: String, appStoreUrl: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally
    ) {
        Text(text = stringResource(R.string.label_new_version_available), fontSize = TextSize.regular(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(Size.small()))
        ElevatedButton(onClick = {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(appStoreUrl)
                setPackage("com.android.vending")
            }
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(intent.apply {
                    setPackage(null)
                })
            }
        }) {
            Text(stringResource(R.string.label_open_in_google_play))
        }
    }
}

@Preview(
    showSystemUi = false, showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun UpdateFragment_Preview(){
    Mdc3Theme() {
        UpdateFragmentUI(contactEmail = "", appStoreUrl = "http://play.google.com/store/apps/details?id=dev.luteoos.scrumbet")
    }
}
