package com.example.guessingcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guessingcard.ui.theme.GuessingCardTheme

class MainActivity : ComponentActivity() {

    private val viewModel: CardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadCards()

        setContent {
            GuessingCardTheme() {
                Displayscreen()
            }
        }
    }

    @Composable
    private fun Displayscreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                )
            },
            content = {
                val cards: List<CardModel> by viewModel.getCards().observeAsState(listOf())
                CardsGrid(cards = cards)
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 200.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Restart Game",
                        fontSize = 25.sp,
                        color = Color.White,
                        modifier = Modifier
                            .clickable {
                                viewModel.loadCards()
                            }
                            .padding(1.dp)
                            .background(Color.Magenta.copy(0.7F), RoundedCornerShape(25))
                    )

                }
            }
        )
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun CardsGrid(cards: List<CardModel>) {
        val cardCount = cards.count()
        val columns = 4
        val rows = (cardCount + columns - 1) / columns

        LazyColumn(Modifier.fillMaxWidth()) {
            items(rows) { rowIndex ->
                Row(Modifier.fillMaxWidth()) {
                    for (colIndex in 0 until columns) {
                        val cardIndex = rowIndex * columns + colIndex
                        if (cardIndex < cardCount) {
                            CardItem(cards[cardIndex], Color.White)
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }


    @Composable
    private fun CardItem(numb: CardModel, color: Color) {
        Box(
            modifier = Modifier
                .padding(all = 5.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .size(width = 90.dp, height = 150.dp)
                    .background(
                        color = Color.Blue.copy(alpha = if (numb.isVisible) 0.4F else 0.0F),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable {
                        if (numb.isVisible) {
                            viewModel.updateShowVisibleCard(numb.id)
                        }
                    }

            ) {
                if (numb.isSelect) {
                    Text(
                        text = numb.char,
                        fontSize = 40.sp,
                        color = color
                    )
                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    private fun DefaultPreview() {
        GuessingCardTheme {
            Displayscreen()
        }
    }
}
