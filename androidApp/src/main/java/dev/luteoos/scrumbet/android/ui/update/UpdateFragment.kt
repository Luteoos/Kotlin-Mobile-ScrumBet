package dev.luteoos.scrumbet.android.ui.update

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.themeadapter.material.MdcTheme
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BaseFragment
import dev.luteoos.scrumbet.android.databinding.ComposeFragmentBinding
import dev.luteoos.scrumbet.android.ui.composeUtil.Size
import dev.luteoos.scrumbet.android.ui.composeUtil.TextSize

class UpdateFragment : BaseFragment<UpdateViewModel, ComposeFragmentBinding>(UpdateViewModel::class) {
    override val layoutId: Int = R.layout.compose_fragment
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ComposeFragmentBinding = { inflater, viewGroup, attachToParent ->
        ComposeFragmentBinding.inflate(inflater, viewGroup, attachToParent)
    }

    private val contactEmail = "mateusz.lutecki@fujitsu.com"

    override fun initObservers() {
    }

    override fun initBindingValues() {
        binding.composeView.setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        binding.composeView.setContent {
            MdcTheme() {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally
                ) {
                    Text(text = getString(R.string.label_new_version_available), fontSize = TextSize.large(), textAlign = TextAlign.Center)
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
                            startActivity(intent)
                        }
                    ) {
                        Text(text = "Send email", fontSize = TextSize.small())
                    }
                }
            }
        }
    }

    override fun initFlowCollectors() {
    }
}
