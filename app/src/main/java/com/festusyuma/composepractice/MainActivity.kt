package com.festusyuma.composepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.festusyuma.composepractice.models.Person
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      val color = remember {
        mutableStateOf(Color.Black)
      }

      val painter = painterResource(id = R.drawable.card_img)
      val description = "abstract portrait image"
      val title = "Abstract face portrait"
      val subTitle = "This is an image also"
      val scaffoldState = rememberScaffoldState()

      val textFieldState = remember {
        mutableStateOf("")
      }

      val people = remember {
        mutableStateOf(mutableListOf<Person>())
      }

      val snackBarScope = rememberCoroutineScope()
      val peopleScrollState = rememberScrollState()

      Box(
        modifier = Modifier
          .padding(10.dp)
          .fillMaxSize()
          .background(Color.White),
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          ImageCard(
            painter = painter,
            contentDescription = description,
            title = title,
            subTitle = subTitle,
          ) {
            color.value = it
          }

          Spacer(modifier = Modifier.padding(vertical = 10.dp))

          Text(
            text = "Chameleon text",
            style = TextStyle(
              textAlign = TextAlign.Center,
              color = color.value,
              fontSize = 16.sp
            ),
            modifier = Modifier
              .fillMaxWidth()
          )

          Spacer(modifier = Modifier.padding(vertical = 10.dp))

          Scaffold(
            scaffoldState = scaffoldState,
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .fillMaxWidth()
            ) {

              TextField(
                value = textFieldState.value,
                onValueChange = {
                  textFieldState.value = it
                },
                label = { Text("Enter your username") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(15.dp))

              Button(onClick = {
                people.value.add(
                  Person(
                    textFieldState.value,
                    Date()
                  )
                )

                snackBarScope.launch {
                  scaffoldState
                    .snackbarHostState
                    .showSnackbar("${textFieldState.value} Added to list")
                }
              }) {
                Text("Click me now")
              }

              Spacer(
                modifier = Modifier
                  .padding(vertical = 10.dp)
                  .background(Color.Red)
              )

              Column(
                modifier = Modifier
                  .fillMaxSize()
                  .verticalScroll(peopleScrollState),
              ) {
                for (person in people.value) {
                  PersonListItem(
                    name = person.name,
                    created = person.createdAt.toString()
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ImageCard(
  painter: Painter,
  contentDescription: String,
  title: String,
  modifier: Modifier = Modifier,
  subTitle: String = "",
  updateColor: (color: Color) -> Unit
) {
  val fontFamily =
    FontFamily(Font(R.font.lexend_variable_font_wght))

  Card(
    modifier = modifier
      .clickable {
        updateColor(
          Color(
            Random.nextFloat(),
            Random.nextFloat(),
            Random.nextFloat(),
          )
        )
      },
    shape = RoundedCornerShape(10.dp),
    elevation = 5.dp,
  ) {
    Box(
      modifier = Modifier
        .height(300.dp)
        .width(300.dp),
    ) {
      Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Transparent,
                Color.Black
              ),
              startY = 300f
            )
          )
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(12.dp),
        contentAlignment = Alignment.BottomStart
      ) {
        Column {
          val defaultTextStyle =
            TextStyle(
              color = Color.White,
              fontFamily = fontFamily,
              textAlign = TextAlign.Center
            )

          Text(
            text = title,
            style = defaultTextStyle
              .merge(
                TextStyle(
                  fontSize = 16.sp
                )
              )
          )

          Text(
            text = subTitle,
            style = defaultTextStyle
              .merge(
                TextStyle(
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraLight,
                  textDecoration = TextDecoration.Underline,
                )
              )
          )
        }
      }
    }
  }
}

@Composable
fun PersonListItem(
  name: String,
  created: String
) {
  val fontFamily =
    FontFamily(Font(R.font.lexend_variable_font_wght))
  val defaultTextStyle = TextStyle(
    fontFamily = fontFamily
  )

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(bottom = 10.dp)
      .background(color = Color.White)
  ) {
    Row(
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = name,
        style = defaultTextStyle.merge(
          TextStyle(
            fontSize = 14.sp,
          )
        )
      )

      Spacer(modifier = Modifier.width(10.dp))

      Text(
        text = created,
        style = defaultTextStyle.merge(
          TextStyle(
            fontSize = 11.sp,
            color = Color.DarkGray
          )
        )
      )
    }
  }
}

@Composable
@Preview
fun PreviewImageCard() {
  val painter = painterResource(id = R.drawable.card_img)
  val description = "abstract portrait image"
  val title = "Abstract face portrait"
  val subTitle = "This is an image also"

  ImageCard(
    painter = painter,
    contentDescription = description,
    title = title,
    subTitle = subTitle
  ) {

  }
}

@Composable
@Preview
fun PreviewPersonListItem() {
  PersonListItem(
    name = "Festus Agboma",
    created = "4pm 22/08/2022"
  )
}