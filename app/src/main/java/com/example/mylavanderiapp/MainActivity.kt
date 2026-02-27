package com.example.mylavanderiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mylavanderiapp.core.navigation.NavigationWrapper
import com.example.mylavanderiapp.core.ui.theme.MylavanderiappTheme
import com.example.mylavanderiapp.features.auth.presentation.navigation.AuthNavGraph
import com.example.mylavanderiapp.features.laundry_reservation.presentation.navigation.ReservationNavGraph
import com.example.mylavanderiapp.features.machines.presentation.navigation.MachinesNavGraph
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authNavGraph: AuthNavGraph
    @Inject
    lateinit var machinesNavGraph: MachinesNavGraph
    @Inject
    lateinit var reservationNavGraph: ReservationNavGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MylavanderiappTheme {
                NavigationWrapper(
                    navGraphs = listOf(authNavGraph, machinesNavGraph, reservationNavGraph)
                )
            }
        }
    }
}