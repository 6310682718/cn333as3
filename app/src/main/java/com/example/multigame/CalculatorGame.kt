package com.example.multigame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun CalculatorGameScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    calculatorGameModel: CalculatorGameModel = viewModel()
) {
    val gameState by calculatorGameModel.gameState.collectAsState()
    var userInput by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Calculator Game :)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(194.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = calculatorGameModel.getQuestion(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = colorResource(R.color.grey)
            );
            TextField(
                value = userInput,
                onValueChange = {
                    userInput = it;
                    calculatorGameModel.validateAnswer(userInput.toIntOrNull() ?: 0);
                },
                label = {
                    calculatorGameModel.getQuestion()
                },
                modifier = Modifier.fillMaxWidth( ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = colorResource(R.color.white)
                )
            );
        }
        Text(text = stringResource(R.string.alert_answer, gameState.alertText),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(R.color.grey)
        )
        Spacer(Modifier.height(80.dp))
        Spacer(Modifier.height(20.dp))
        Row() {
            Button(
                onClick = {
                    calculatorGameModel.restartGame();
                    userInput = ""
//                    numGuess = "";
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
        println(gameState.userInput);
    }
}

data class CalculatorState(
   var isStarted : Boolean = false,
   var isCorrect : Boolean = false,
   var alertText: String = "",
   var answer: Int = 0,
   var operation: Int = 0, // 0 -> +, 1 -> -, 2 -> *
   var userInput: String = "",
   var question: String = "",
   var firstN: Int = 0,
   var secondN: Int = 0
)

class CalculatorGameModel : ViewModel() {
    val state = MutableStateFlow(CalculatorState());
    val gameState: StateFlow<CalculatorState> = state.asStateFlow();

    init {
        restartGame()
    }

    fun restartGame() {
        gameState.value.question = generateQuestion();
        gameState.value.isCorrect = false;
        gameState.value.alertText = "";
    }
    fun randomOperation(): Int {
        var randomId = (0 until 3).random();
        return randomId;
    }
    fun getAnswer(): Int {
        var firstN: Int = gameState.value.firstN;
        var secondN: Int = gameState.value.secondN;
       return when(gameState.value.operation) {
           0 -> firstN + secondN;
           1 -> firstN - secondN;
           2 -> firstN * secondN;
           else -> firstN + secondN;
       }
    }
    fun generateNumber(): Int {
        return (1 until 100).random();
    }
    fun getOperationString(): String {
        var operationStr: String = when(gameState.value.operation) {
            0 -> "+";
            1 -> "-";
            2 -> "*";
            else -> "+";
        }
        return operationStr;
    }
    fun generateQuestion(): String {
        var operation = randomOperation();
        gameState.value.operation = operation;
        var firstN = generateNumber();
        gameState.value.firstN = firstN;
        var secondN = generateNumber();
        gameState.value.secondN = secondN;
        var operationStr = getOperationString();
        var answer = getAnswer();
        gameState.value.answer = answer;
        var fullQuestion = "${firstN} ${operationStr} ${secondN} =  ";
        return fullQuestion;
    }
    fun getQuestion(): String {
        var operationStr = getOperationString();
        return "${gameState.value.firstN} ${operationStr} ${gameState.value.secondN} = ";
    }

    fun validateAnswer(answer: Int): Boolean {
        println("Answer ${answer}");
        println("GameState answer ${gameState.value.answer}")
        if(gameState.value.answer == answer) {
            gameState.value.isCorrect = true;
            gameState.value.alertText = "Correct !";
            println("Correct !!");
            return true;
        } else {
            gameState.value.alertText = "Incorrect !";
            println("Incorrect !!");
            return false;
        }
    }


}
