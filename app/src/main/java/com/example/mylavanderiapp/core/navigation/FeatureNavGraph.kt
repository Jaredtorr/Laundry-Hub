package com.example.mylavanderiapp.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.example.mylavanderiapp.core.auth.GoogleSignInHelper

interface FeatureNavGraph {
    fun registerGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController,
        googleSignInHelper: GoogleSignInHelper? = null
    )
}