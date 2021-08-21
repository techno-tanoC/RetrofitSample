package dev.tanoc.android.retrofitsample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dev.tanoc.android.retrofitsample.ui.theme.RetrofitSampleTheme
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

data class Album(
    val id: Int,
    val userId: Int,
    val title: String,
)

interface Api {
    @GET("/albums")
    suspend fun index(): Response<List<Album>>
}

class Repository(url: String) {
    private var api: Api = Retrofit
        .Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(Api::class.java)

    suspend fun index(): Response<List<Album>> {
        return api.index()
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RetrofitSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }

        lifecycleScope.launch {
            Log.i("RetrofitSample", "start")
            val response = Repository("https://jsonplaceholder.typicode.com/").index()
            response.body()?.forEach {
                Log.i("RetrofitSample", it.toString())
            }
            Log.i("RetrofitSample", "finish")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RetrofitSampleTheme {
        Greeting("Android")
    }
}