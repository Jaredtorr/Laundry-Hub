package com.example.mylavanderiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mylavanderiapp.core.di.AppContainer
import com.example.mylavanderiapp.core.navigation.LaundryNavGraph
import com.example.mylavanderiapp.core.navigation.Routes
import com.example.mylavanderiapp.core.ui.theme.MylavanderiappTheme

class MainActivity : ComponentActivity() {

    // Cambiamos la inicialización para pasar 'this'
    private lateinit var appContainer: AppContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos aquí el container pasando el contexto de la Activity
        appContainer = AppContainer(this)

        setContent {
            MylavanderiappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    LaundryNavGraph(
                        navController = navController,
                        appContainer = appContainer,
                        startDestination = Routes.Login.route
                    )
                }
            }
        }
    }
}