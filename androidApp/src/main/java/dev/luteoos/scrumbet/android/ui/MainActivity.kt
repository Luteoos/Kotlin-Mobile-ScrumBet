package dev.luteoos.scrumbet.android.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dev.luteoos.scrumbet.android.R
import dev.luteoos.scrumbet.android.core.BackPressFragment
import dev.luteoos.scrumbet.android.databinding.MainActivityBinding
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import org.koin.android.ext.android.get
import org.koin.core.qualifier.named

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        // In Manifest we implement Theme.CallingCard.Splash, here we set proper app-wide theme for activity
        setTheme(R.style.Theme_AppTheme)
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fixes crazy insets in M3 ModalBottomSheet
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val intentData: Uri? = intent?.data
        if (intentData != null)
            handleIntent(intentData)

//        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
//            // smooth brain moment
//            if (exception is SocketException && thread.name.contains("DefaultDispatcher-worker")) {
//                binding.navHost.getFragment<RoomFragment>().get<RoomViewModel>().disconnect()
//                Timber.e(exception)
//            } else
//                throw exception
//        }
    }

    private fun handleIntent(uri: Uri) {
        if (uri.host?.contains(get<String>(named("BASE_URL")).split(":").first()) == true) {
            uri.pathSegments?.first()?.let { pathRoomName ->
                get<AuthControllerInterface>().setRoomConnectionId(pathRoomName)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        supportFragmentManager.fragments[0].childFragmentManager.fragments[0].let { fragment ->
            if (fragment !is BackPressFragment || (fragment as BackPressFragment).onBackPressed())
                super.onBackPressed()
        }
    }

    /**
     * true to portrait <-> default
     * false to landscape
     */
    fun setPortraitOrientation(isPortrait: Boolean = true) {
        requestedOrientation = when (isPortrait) {
            true -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            false -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    /**
     * call for hide keyboard from this activity
     */
    fun hideKeyboard(view: View?) {
        view?.let {
            (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}

// @Composable
// fun Greeting(text: String) {
//    Text(text = text)
// }
//
// @Preview
// @Composable
// fun DefaultPreview() {
//    MyApplicationTheme {
//        Greeting("Hello, Android!")
//    }
// }
// import androidx.compose.material.* 1.2.1 for colors and shapes?
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApplicationTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting(Greeting().greeting())
//                }
//            }
//        }
//    }
//
// @Composable
// fun MyApplicationTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
//    content: @Composable () -> Unit
// ) {
//    val colors = if (darkTheme) {
//        darkColors(
//            primary = Color(0xFFBB86FC),
//            primaryVariant = Color(0xFF3700B3),
//            secondary = Color(0xFF03DAC5)
//        )
//    } else {
//        lightColors(
//            primary = Color(0xFF6200EE),
//            primaryVariant = Color(0xFF3700B3),
//            secondary = Color(0xFF03DAC5)
//        )
//    }
//    val typography = Typography(
//        body1 = TextStyle(
//            fontFamily = FontFamily.Default,
//            fontWeight = FontWeight.Normal,
//            fontSize = 16.sp
//        )
//    )
//    val shapes = Shapes(
//        small = RoundedCornerShape(4.dp),
//        medium = RoundedCornerShape(4.dp),
//        large = RoundedCornerShape(0.dp)
//    )
//
//    MaterialTheme(
//        colors = colors,
//        typography = typography,
//        shapes = shapes,
//        content = content
//    )
// }
