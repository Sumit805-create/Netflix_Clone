package com.netflixclone.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.netflixclone.network.models.Movie
import com.netflixclone.network.models.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : ComponentActivity() {
    lateinit var rootView: ComposeView

    private val categories = listOf("Action", "Sci-Fi", "Thriller", "Superhero", "Western", "Drama", "Romance")

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
            setContent {
                CategoryListScreen(categories)
            }
        }

}

@Composable
fun CategoryListScreen(categories: List<String>) {
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }

    MaterialTheme(colors = darkColors(background = Color.Black)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text("Categories", color = Color.White, modifier = Modifier.padding(8.dp))

            // **Horizontal Scrollable Categories**
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(categories) { category ->
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                selectedCategory = category
                                fetchMoviesByCategory(category) { movies = it }
                            }
                            .background(Color.Gray)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(text = category, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            selectedCategory?.let {
                Text("Movies in $it", color = Color.White, modifier = Modifier.padding(8.dp))
            }

            LazyColumn {
                items(movies) { movie ->
                    MovieComponent(movie)
                }
            }
        }
    }
}

private fun fetchMoviesByCategory(category: String, callback: (List<Movie>) -> Unit) {
    RetrofitClient.instance.getMoviesByCategory(category).enqueue(object : Callback<List<Movie>> {
        override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
            if (response.isSuccessful) {
                callback(response.body() ?: emptyList())
            }
        }

        override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
            callback(emptyList())
        }
    })
}
