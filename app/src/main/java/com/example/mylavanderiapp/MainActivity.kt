package com.example.mylavanderiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mylavanderiapp.core.navigation.NavigationWrapper
import com.example.mylavanderiapp.core.ui.theme.MylavanderiappTheme
import com.example.mylavanderiapp.features.auth.presentation.navigation.AuthNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authNavGraph: AuthNavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MylavanderiappTheme {
                NavigationWrapper(
                    navGraphs = listOf(authNavGraph)
                )
            }
        }
    }
}