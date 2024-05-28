package com.example.movieapplicationjetpackcompose.ui

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.content.getSystemService
import androidx.navigation.compose.rememberNavController
import com.example.easy_connectivity.ConnectionState
import com.example.easy_connectivity.EasyConnectivity
import com.example.movieapplicationjetpackcompose.navigation.AppNavHost
import com.example.movieapplicationjetpackcompose.shared.BottomNavigationBar
import com.example.movieapplicationjetpackcompose.ui.theme.MovieApplicationjetpackComposeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val easyConnectivity =
                EasyConnectivity.getInstance(applicationContext.getSystemService() as ConnectivityManager?)

            val scope = rememberCoroutineScope()
            val snackBarHostState = remember { SnackbarHostState() }
            scope.launch {
                easyConnectivity.getNetworkStateFlow().collectLatest {
                    val connectionState = it.connectionState
                    when (connectionState) {
                        ConnectionState.Available -> {
                            snackBarHostState.showSnackbar(
                                "you are online",
                                withDismissAction = true
                            )

                        }

                        ConnectionState.AvailableWithOutInternet -> {
                            snackBarHostState.showSnackbar(
                                "you are currently in offline",
                                withDismissAction = true
                            )

                        }

                        ConnectionState.Lost -> {
                            snackBarHostState.showSnackbar(
                                "you are currently in offline",
                                withDismissAction = true
                            )

                        }

                        ConnectionState.Unavailable -> {
                            snackBarHostState.showSnackbar(
                                "you are currently in offline",
                                withDismissAction = true
                            )

                        }

                        ConnectionState.Losing -> {
                            snackBarHostState.showSnackbar(
                                "you are currently in offline",
                                withDismissAction = true
                            )
                        }
                    }

                }
            }
            MovieApplicationjetpackComposeTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(navController = navController)
                    },
                    snackbarHost = { SnackbarHost(snackBarHostState) },
                ) { innerPadding ->
                    AppNavHost(
                        navController = navController,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
            }
        }
    }
}

