package com.example.multigame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@Composable
fun GuessNumberGameScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    gussNumberGameModel: GuessNumberGameModel = viewModel()
) {
//    var randNum: Int = gussNumberGameModel.getRandomNumber();
    var numGuess by remember { mutableStateOf("") }
    var hint = gussNumberGameModel.state.value.hint;
//    val Guess = numGuess.toIntOrNull() ?: 0
//    val output = "";
//    println(output)

    Column (
        modifier = Modifier.padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = stringResource(R.string.title_game),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(194.dp))
        EnterNumber(
            value = numGuess, onValueChange = { numGuess = it})
        var hint_res = gussNumberGameModel.randomGuess(numGuess)
        Text(text = stringResource(R.string.guess_output, hint_res),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.grey)
        )
        Spacer(Modifier.height(160.dp))
        Row() {
            Button(
                onClick = {
                    gussNumberGameModel.restartGame();
                    numGuess = "";
                }) {
                Text(text = stringResource(R.string.try_again), fontSize = 10.sp)
            }
            Spacer(Modifier.width(10.dp))
            Button(
//            shape = RoundedCornerShape(23.dp),
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                onClick = { navController.navigate(ScreenPage.HomePage.route) }
            ) {
                Text(
                    text = "Go back",
                    fontSize = 10.sp,
                )
            }
        }
        Spacer(Modifier.height(10.dp))
    }
}

data class GuessNumberState(
    val initialNumber: Int = 0,
    val boundedNumber: Int = 1000,
    val isCorrect: Boolean = false,
    val isStarted: Boolean = false,
    val isOver: Boolean = false,
    val randomNumber: Int = 0,
    val guessNumber: Int = 0,
    val hint: String = ""
)
//val keyboardController = LocalSoftwareKeyboardController.current

@Composable
fun EnterNumber(
    value: String,
    onValueChange: (String) -> Unit,
){
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.placeholder))},
        modifier = Modifier.fillMaxWidth( ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = colorResource(R.color.white)
        )
    )
}
class GuessNumberGameModel : ViewModel() {
    val state = MutableStateFlow(GuessNumberState())
    val dataState: StateFlow<GuessNumberState> = state.asStateFlow()
    init {
        restartGame();
    }
//    fun getRandomNumber(): Int {
//        if(state.value.isStarted == false) {
//            val _rand  = (1..1000).random()
//            println("RANDOM -> ${_rand}")
//            state.update { currentState ->
//                currentState.copy(
//                    isCorrect = false,
//                    isOver = false,
//                    isStarted = true,
//                    randomNumber = _rand
//                );
//            }
//        }
//        return state.value.randomNumber;
//    }
    fun updateGuessingNumber(guessNum: String) {
        println(guessNum)
        state.update { currentState ->
            currentState.copy(
                guessNumber = guessNum.toIntOrNull() ?: 0
            );
        }
    }

    fun restartGame() : Int {
        val _rand  = (1 until 1000).random()
        state.update { currentState ->
            currentState.copy(
                isCorrect = false,
                isOver = false,
                isStarted = false,
                randomNumber = _rand
            );
        }
//        state.value.randomNumber = (1..1000).random();
        return state.value.randomNumber;
    }
    fun randomGuess(
        Guess: String,
//        randNum: Int
    ) : String{
        var result: String = ""
        val randNum = state.value.randomNumber;
        val guessNumber = Guess.toIntOrNull() ?: 0;
        println("guessNumber -> ${guessNumber}")
        println("randNum -> ${randNum}")
        if (guessNumber < randNum){
            result = " lower!"
        }else if (guessNumber > randNum){
            result = " higher!"
        }else{
            result = " correct!"
            restartGame();
        }
        println("result -> ${result}")
        state.update { currentState ->
            currentState.copy(
                hint = result
            );
        }
        return result;
    }
}