package dev.luteoos.scrumbet.android.ui.update

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseComposeFragment
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

    @Composable
    override fun ComposeLayout() {
        UpdateFragmentUI(contactEmail = contactEmail)
    }
}

@Composable
private fun UpdateFragmentUI(contactEmail: String) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally
    ) {
        Text(text = stringResource(R.string.label_new_version_available), fontSize = TextSize.large(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(Size.small()))
        TextButton(onClick = { }) {
            Text("Lorem Ipsum link to PlayStore update here")
        }
        Spacer(modifier = Modifier.height(Size.small()))
        Text(text = "For new version contact $contactEmail", textAlign = TextAlign.Center)
        TextButton(
            modifier = Modifier.padding(Size.regular()),
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("mailto:$contactEmail?subject=Scrumbet%20application%20feedback")
                intent.data = data
                context.startActivity(intent)
            }
        ) {
            Text(text = "Send email", fontSize = TextSize.small())
        }
    }
}
