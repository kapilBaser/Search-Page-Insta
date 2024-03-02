package com.example.instasearchpage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material.pullrefresh.PullRefreshDefaults
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import com.example.instasearchpage.ui.theme.InstaSearchPageTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val systemUiController = rememberSystemUiController()
            val useDarkIcons = !isSystemInDarkTheme()
            LaunchedEffect(Unit){
                systemUiController.setSystemBarsColor(
                    color = Color.White,
                    darkIcons = true
                ) {
                    Color.Black
                }
            }

            InstaSearchPageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {





                    Scaffold(topBar = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(12.dp)
                                .background(Color(236, 236, 236, 255), RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search, contentDescription = "",
                                tint = Color.Black,
                            )
                            BasicTextField(
                                value = "Search",
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                textStyle = TextStyle(
                                    color = Color(141, 141, 141, 255),
                                    fontSize = 16.sp,

                                ),
                                cursorBrush = Brush.linearGradient(colors = listOf(Color.Black,
                                    Color.Black
                                )),
                                singleLine = true


                            )

                        }

                    }) {

                        Column(modifier = Modifier.padding(it)) {
                            systemUiController.setSystemBarsColor(
                                color = Color.White,
                                darkIcons = true,
                            ) {
                                Color.Black
                            }
                            systemUiController.statusBarDarkContentEnabled = true
                            AppLayout()
                        }

                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun AppLayout() {
    val systemUiController = rememberSystemUiController()
    var isLongClick by remember {
        mutableStateOf(false)
    }

    var interaction = remember {
        MutableInteractionSource()
    }
    var isFullHeight by remember {
        mutableStateOf(false)
    }
    var clickedItem by remember {
        mutableStateOf(0)
    }
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    var progress by remember {
        mutableStateOf(0.0f)
    }
    var height by remember {
        mutableStateOf(0.0f)
    }
    var items by remember {
        mutableStateOf(5)
    }
    val scope = rememberCoroutineScope()
    var state = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {

            isRefreshing = true
            scope.launch {

                delay(1500)

                items += Random.nextInt(2, 6)
                isRefreshing = false

            }
        },
        refreshThreshold = 200.dp,
    )

    val viewConfiguration = LocalViewConfiguration.current
    LaunchedEffect(interaction, state.progress) {
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = true,
        )
        systemUiController.statusBarDarkContentEnabled = true
        progress = state.progress
        height = state.progress * 100
        interaction.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
//                    Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
                }

                is PressInteraction.Release -> {
                    isLongClick = false
//                    Toast.makeText(context, "Released", Toast.LENGTH_SHORT).show()
                }
            }


        }
    }
    Column {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
                .animateContentSize()
                .height(if (isRefreshing) 90.dp else height.dp)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            if (progress > 1f || isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(top = 30.dp),
                    color = Color(160, 159, 159, 255),
                    trackColor = Color(236, 236, 236, 255),
                    strokeWidth = 2.dp
                )
            } else {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(top = 30.dp),
                    color = Color(160, 159, 159, 255),
                    trackColor = Color(236, 236, 236, 255),
                    strokeWidth = 2.dp
                )
            }

        }

//        PullRefreshIndicator(
//            refreshing = isRefreshing, state = state,
//            modifier = Modifier.fillMaxWidth().animateContentSize().height(
//                height.dp
//            )
//        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.TopCenter,
        ) {

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalItemSpacing = 4.dp,
                modifier = Modifier.pullRefresh(
                    state
                )
            ) {
//            item {
//                PullRefreshIndicator(refreshing = isRefreshing, state = state)
//            }
                items(items) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (((it - 2) % 10 == 0 && it > 10) || it == 2) 270.dp else 134.dp)
                            .background(
                                Color(236, 236, 236, 255)
                            )
                            .combinedClickable(interactionSource = interaction,
                                indication = null,
                                onLongClick = {
                                    clickedItem = it
                                    isFullHeight = (((it - 2) % 10 == 0 && it > 10) || it == 2)
                                }) {},
                        contentAlignment = Alignment.Center,
                    )
                    {
                        Text(text = it.toString())
                    }
                }
            }

            if (isLongClick) {
                PopUpImage(text = clickedItem, isFullHeight = isFullHeight)
            }

        }
    }

}


@Composable
fun PopUpImage(
    text: Int,
    isFullHeight: Boolean = false
) {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(if (isFullHeight) 1f else .6f)
                    .padding(vertical = 24.dp, horizontal = 8.dp)
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    "@username",
                    color = Color.Black,
                    modifier = Modifier.padding(12.dp)
                )
                Divider()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "$text",
                        color = Color.Black,
                        modifier = Modifier.padding(12.dp),
                        fontSize = 32.sp
                    )
                }
                Divider()

                Icon(
                    imageVector = Icons.Rounded.ThumbUp, contentDescription = null,
                    modifier = Modifier.padding(12.dp)
                )

            }
        }
    }
}


