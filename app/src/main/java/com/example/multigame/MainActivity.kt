package com.example.multigame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.multigame.ui.theme.MultiGameTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class ScreenPage (val route: String){
    object GuessNumber: ScreenPage("guess-number-game")
    object GuessAnswer: ScreenPage("guess-answer-game")
    object HomePage: ScreenPage("home-page")
    object GuessAnswerResult: ScreenPage("result-guess-answer")
    object CalculatorGame: ScreenPage("calculate-game")

}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultiGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = ScreenPage.HomePage.route) {
                        composable(ScreenPage.GuessNumber.route) {
                            GuessNumberGameScreen(navController = navController);
                        }
                        composable(ScreenPage.GuessAnswer.route) {
                            GuessAnswerGameScreen(navController = navController);
                        }
                        composable(ScreenPage.CalculatorGame.route) {
                            CalculatorGameScreen(navController = navController);
                        }

                        composable(ScreenPage.HomePage.route) {
                            HomePage(navController = navController);
                        }
                        composable(ScreenPage.GuessAnswerResult.route+"/{score}",arguments = listOf(navArgument("score"){
                            type = NavType.IntType
                        })){
                            val score = requireNotNull(it.arguments).getInt("score")
                            if( score != null)
                                GuessAnswerResultScreen(score, navController = navController)
                        }
                    }
                }
            }
        }
    }
}


    // -------------------------------------


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MultiGameTheme {
            val navController = rememberNavController()
            HomePage(navController, modifier = Modifier)
        }
    }
