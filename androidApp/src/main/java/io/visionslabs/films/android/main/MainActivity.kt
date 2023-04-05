package io.visionslabs.films.android.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.visionslabs.films.android.R
import io.visionslabs.films.android.main.screen.DetailScreen
import io.visionslabs.films.android.main.screen.FavoriteScreen
import io.visionslabs.films.android.main.screen.MainScreen
import io.visionslabs.films.android.main.screen.Screen
import io.visionslabs.films.android.theme.AppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainView(viewModel) { startSettingActivity() }
                }
            }
        }
    }

    private fun startSettingActivity() {
//        val intent = Intent(this, SettingsActivity::class.java)
//        startActivity(intent)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(viewModel: MainViewModel, startSettingActivity: () -> Unit) {
    val navController = rememberNavController()
    var canPop by remember { mutableStateOf(false) }

    val mainScreens = listOf(
        Screen.MainScreen,
        Screen.FavoriteScreen
    )

    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, dest, _ ->
            canPop =
                (controller.previousBackStackEntry != null) && dest.route?.equals(Screen.FavoriteScreen.route) == false
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.activity_topbar_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    if (canPop) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { startSettingActivity() }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Go to settings"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                mainScreens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { contentPadding ->
        NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
            composable(Screen.MainScreen.route) {
                MainScreen(
                    navController = navController,
                    contentPadding = contentPadding,
                    viewModel = viewModel
                )
            }
            composable(Screen.FavoriteScreen.route) {
                FavoriteScreen(
                    navController = navController,
                    contentPadding = contentPadding,
                    viewModel = viewModel
                )
            }
            composable(
                "${Screen.DetailScreen.route}/{id}",
                arguments = listOf(navArgument("id") {
                    type = NavType.IntType
                })
            ) {
                val id = requireNotNull(it.arguments).getInt("id", 0)
                DetailScreen(
                    detailId = id,
                    navController = navController,
                    contentPadding = contentPadding,
                    viewModel = viewModel
                )
            }
        }
    }

}
