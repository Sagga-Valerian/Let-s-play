package com.example.letsplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TicTacToeApp() }
    }
}

@Composable
fun TicTacToeApp() {
    var board by remember { mutableStateOf(List(9) { "" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var winner by remember { mutableStateOf("") }
    var xScore by remember { mutableStateOf(0) }
    var oScore by remember { mutableStateOf(0) }
    var drawScore by remember { mutableStateOf(0) }

    fun resetBoard() {
        board = List(9) { "" }
        winner = ""
        currentPlayer = "X"
    }

    fun checkWinner(): String? {
        val winPositions = listOf(
            listOf(0, 1, 2), listOf(3, 4, 5), listOf(6, 7, 8), // rows
            listOf(0, 3, 6), listOf(1, 4, 7), listOf(2, 5, 8), // cols
            listOf(0, 4, 8), listOf(2, 4, 6) // diagonals
        )
        for (pos in winPositions) {
            val (a, b, c) = pos
            if (board[a].isNotEmpty() && board[a] == board[b] && board[a] == board[c]) {
                return board[a]
            }
        }
        return if (board.none { it.isEmpty() }) "Draw" else null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF101010))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Lets Play Tic-Tac-Toe", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)

        // Scoreboard
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            ScoreBox("X", xScore, Color.Cyan, currentPlayer == "X" && winner.isEmpty())
            ScoreBox("Draws", drawScore, Color.Gray, false)
            ScoreBox("O", oScore, Color.Magenta, currentPlayer == "O" && winner.isEmpty())
        }

        // Game Board
        Column(modifier = Modifier.size(300.dp)) {
            for (i in 0..2) {
                Row(modifier = Modifier.weight(1f)) {
                    for (j in 0..2) {
                        val index = i * 3 + j
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .padding(4.dp)
                                .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
                                .border(2.dp, Color.LightGray, RoundedCornerShape(12.dp))
                                .clickable(enabled = board[index].isEmpty() && winner.isEmpty()) {
                                    val newBoard = board.toMutableList()
                                    newBoard[index] = currentPlayer
                                    board = newBoard
                                    val result = checkWinner()
                                    if (result != null) {
                                        winner = result
                                        when (result) {
                                            "X" -> xScore++
                                            "O" -> oScore++
                                            "Draw" -> drawScore++
                                        }
                                    } else {
                                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = board[index],
                                color = if (board[index] == "X") Color.Cyan else Color.Magenta,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Status message
        if (winner.isNotEmpty()) {
            Text(
                text = if (winner == "Draw") "It's a Draw!" else "$winner Wins!",
                color = Color.Yellow,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp)
            )

            Button(onClick = { resetBoard() }, modifier = Modifier.padding(top = 16.dp)) {
                Text("Play Again")
            }
        }
    }
}

@Composable
fun ScoreBox(label: String, score: Int, color: Color, isTurn: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(score.toString(), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        if (isTurn) Text("Your Turn", color = Color.Green, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}
