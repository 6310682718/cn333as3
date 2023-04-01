package com.example.multigame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
fun GuessAnswerGameScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    guessAnswerGameModel: GuessAnswerGameModel = viewModel()
) {
    val gameState by guessAnswerGameModel.State.collectAsState()
    val currentQuestion = gameState.currentQuestion
    val quizCount = gameState.currentCount
    val score = gameState.score
    val randomQuestion = remember(guessAnswerGameModel.State.value.currentQuestion) {
        guessAnswerGameModel.State.value.currentQuestion.slice(1..4).shuffled()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .background(color = Color.LightGray)
                .fillMaxSize()
                .padding(all = 12.dp),

            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(Alignment.End),
                text = stringResource(R.string.score, score),
                fontSize = 18.sp,
            )
            Text(
                modifier = Modifier.wrapContentWidth(Alignment.Start),
                text = stringResource(R.string.quiz_count, quizCount),
                fontSize = 18.sp,
                maxLines = 1,
            )
        }
        Text(
            text = (currentQuestion[0]),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 8.dp, top = 25.dp),
        )
        Button(
            shape = RoundedCornerShape(23.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 15.dp),

            onClick = { guessAnswerGameModel.getAnswer(randomQuestion[0]);guessAnswerGameModel.checkUserAnswer() }
        ) {
            Text(
                text = randomQuestion[0],
                fontSize = 20.sp,
            )
        }
        Button(
            shape = RoundedCornerShape(23.dp),
            modifier = modifier
                .fillMaxWidth()

                .padding(start = 8.dp, top = 12.dp),
            onClick = { guessAnswerGameModel.getAnswer(randomQuestion[1]);guessAnswerGameModel.checkUserAnswer() }
        ) {
            Text(
                text = randomQuestion[1],
                fontSize = 20.sp,
            )
        }
        Button(
            shape = RoundedCornerShape(23.dp),
            modifier = modifier
                .fillMaxWidth()

                .padding(start = 8.dp, top = 12.dp),
            onClick = { guessAnswerGameModel.getAnswer(randomQuestion[2]);guessAnswerGameModel.checkUserAnswer() }
        ) {
            Text(
                text = randomQuestion[2],
                fontSize = 20.sp,
            )
        }
        Button(
            shape = RoundedCornerShape(23.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 12.dp),

            onClick = { guessAnswerGameModel.getAnswer(randomQuestion[3]);guessAnswerGameModel.checkUserAnswer() }
        ) {
            Text(
                text = randomQuestion[3],
                fontSize = 20.sp,
            )
        }
        if (gameState.isOver) {
            LaunchedEffect(Unit) {
                navController.navigate(ScreenPage.GuessAnswerResult.route + "/$score")
            }
        }
    }
}

data class GuessAnswerState(
    val currentQuestion: List<String> = listOf(),
    val currentCount: Int = 1,
    val score: Int = 0,
    val isWrong: Boolean = false,
    val isOver: Boolean = false
)

class GuessAnswerGameModel : ViewModel() {
    val state = MutableStateFlow(GuessAnswerState())
    val State: StateFlow<GuessAnswerState> = state.asStateFlow()
    var userAnswer by mutableStateOf("")
    var usedQuestion: MutableSet<List<String>> = mutableSetOf()
    lateinit var currentQuiz: List<String>

    fun shuffleQuestion(question: List<String>): List<String> {
        val quiz = question
        val quizQuestion = question[0].toCharArray()
        quizQuestion.shuffle()
        while (String(quizQuestion).equals(question)) {
            quizQuestion.shuffle()
        }
        return quiz
    }

    fun randomQuestion(): List<String> {
        currentQuiz = questions.random()
        if (usedQuestion.contains(currentQuiz)) {
            return randomQuestion()
        } else {
            usedQuestion.add(currentQuiz)
            return shuffleQuestion(currentQuiz)
        }
    }

    init {
        resetGame()
    }

    fun resetGame() {
        usedQuestion.clear()
        state.value = GuessAnswerState(currentQuestion = randomQuestion())
    }

    fun getAnswer(answer: String) {
        userAnswer = answer
    }

    fun nextQuestion() {
        updateGameState(state.value.score)
        getAnswer("")
    }

    fun checkUserAnswer() {
        if (userAnswer == currentQuiz[1]) {
            val updatedScore = state.value.score.plus(score)
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            state.update { currentState ->
                currentState.copy(isWrong = true)

            }
            nextQuestion()
        }
        // Reset user guess
        getAnswer("")
    }

    fun updateGameState(updatedScore: Int) {
        if (usedQuestion.size == total_question) {
            //Last round in the game, update isGameOver to true, don't pick a new word
            state.update { currentState ->
                currentState.copy(
                    isWrong = false,
                    score = updatedScore,
                    isOver = true
                )
            }
        } else {
            // Normal round in the game
            state.update { currentState ->
                currentState.copy(
                    isWrong = false,
                    currentQuestion = randomQuestion(),
                    currentCount = currentState.currentCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

}

@Composable
fun GuessAnswerResultScreen(score: Int, navController: NavController, modifier: Modifier = Modifier)
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = stringResource(R.string.result),
            fontSize = 54.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all = 25.dp),
        )

        Text(
            text = stringResource(R.string.you_scored, score),
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(all = 25.dp),
        )


        Button(
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            onClick = { navController.navigate(ScreenPage.GuessAnswer.route) }
        ) {
            Text(
                text = "Play Again",
                fontSize = 20.sp,
            )
        }

        Button(
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            onClick = { navController.navigate(ScreenPage.HomePage.route) }
        ) {
            Text(
                text = "Go back",
                fontSize = 20.sp,
            )
        }
    }
}

const val total_question = 10
const val score = 1

val questions: Set<List<String>> =
    setOf(
        listOf("When did Salt Lake City host the Winter Olympics?", "2002", "1992", "1998", "2008"),
        listOf("In which city can you find the Prado Museum?", "Madrid", "Buenos Aires", "Barcelona", "Santiago"),
        listOf("How long is the border between the United States and Canada?", "5,525 miles", "3,525 miles", "4,525 miles", "6,525 miles"),
        listOf("When was the book “To Kill a Mockingbird” by Harper Lee published?", "1960", "1950", "1970", "1980"),
        listOf("What is the largest active volcano in the world?", "Mouna Loa", "Mount Vesuvius", "Mount Etna", "Mount Batur"),
        listOf("What is the largest continent in size?", "Asia", "Africa", "Europe", "North America"),
        listOf("When was the first Harry Potter book published?", "1997", "1999", "2001", "2003"),
        listOf("Apart from water, what is the most popular drink in the world?", "Tea", "Coffee", "Beer", "Orange Juice"),
        listOf("Which one of the following companies has a mermaid in its logo?", "Starbucks", "HSBC", "Twitter", "Target"),
        listOf("What is the longest river in the world?", "Nile", "Amazon River", "Yellow River", "Congo River"),
        listOf("How many sides has a Hexagon?", "6", "5", "7", "8"),
        listOf("What is the national animal of England?", "Lion", "Puffin", "Rabbit", "Fox"),
        listOf("What colour is the “m” from the McDonald’s logo?", "Yellow", "Blue", "Black", "Red"),
        listOf("Who is the CEO of Amazon?", "Jeff Bezos", "Elon Musk", "Tim Cook", "Mark Zuckerberg"),
        listOf("How many players are in a cricket team?", "11", "9", "10", "8"),
    )