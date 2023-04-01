package com.example.multigame

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text(
            text = "Multiple Game Play naja",
            fontSize = 54.sp,
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
                text = "Quiz Game",
                fontSize = 20.sp,
            )
        }
        Button(
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            onClick = { navController.navigate(ScreenPage.GuessNumber.route) }
        ) {
            Text(
                text = "Guess Number Game",
                fontSize = 20.sp,
            )
        }
        Button(
            shape = RoundedCornerShape(23.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
            modifier = modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            onClick = { navController.navigate(ScreenPage.CalculatorGame.route) }
        ) {
            Text(
                text = "Calculator Game",
                fontSize = 20.sp,
            )
        }
    }
}