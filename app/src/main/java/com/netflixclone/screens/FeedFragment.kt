package com.netflixclone.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.netflixclone.network.models.Movie
import com.netflixclone.network.models.RetrofitClient
import com.netflixclone.screens.feed.FeedScreen
import retrofit2.Call

class FeedFragment : BottomNavFragment() {
    lateinit var rootView: ComposeView

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        rootView = ComposeView(requireContext()).apply {
            setContent {
                var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
                MaterialTheme(colors = darkColors(background = Color.Black)) {
//                    FeedScreen()
                    LaunchedEffect(Unit) {
                        RetrofitClient.instance.getMovies().enqueue(object : retrofit2.Callback<List<Movie>> {
                            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                                if (response.isSuccessful) {
                                    val movie = response.body()
                                    if (movie != null) {
                                        movies = movie
                                    }
                                } else {
                                    println("Error: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                                println("Failure: ${t.message}")
                            }
                        })
                    }
                    Column {
                        Button(
                            onClick = {
                                val intent=Intent(context, CategoryActivity::class.java)
                                context.startActivity(intent)
                            }
                        ) {
                            Text("Search by category")
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        LazyColumn {
                            items(movies) { movie ->
                                MovieComponent(movie)
                            }
                        }
                    }

                }
            }
        }
        return rootView
    }


    override fun onFirstDisplay() {
    }
}
@Composable
fun MovieComponent(movie: Movie) {
    val context = LocalContext.current
    Row(modifier = Modifier
        .background(Color.White)
        .fillMaxWidth()
        .clickable {
            VideoPlayerActivity.start(context, movie.video_url)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(120.dp),
            painter = rememberAsyncImagePainter(model = movie.image_url),
            contentDescription = "movie image"
        )
        Text(movie.title)
    }
}
@Preview
@Composable
fun MovieComponentPreview() {
    MovieComponent(Movie(image_url = "https://picsum.photos/200/300", title = "abc", video_url = "", id = 1, category = "aba"))
}
