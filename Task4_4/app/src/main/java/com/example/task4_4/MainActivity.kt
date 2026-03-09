package com.example.task4_4

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.task4_4.data.Comment
import com.example.task4_4.data.PostState
import com.example.task4_4.data.SocialPost
import com.example.task4_4.data.User
import com.example.task4_4.ui.theme.Task4_4Theme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.json.JSONArray


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task4_4Theme {
                SocialFeedScreen()
            }
        }
    }
}

suspend fun readJson(context: Context, name: String): String = withContext(Dispatchers.IO) {
    context.assets.open(name).bufferedReader().use { it.readText() }
}

fun parsePosts(json: String) : List<SocialPost>{
    return buildList {
        val arr = JSONArray(json)
        for(i in 0 .. arr.length() - 1){
            arr.getJSONObject(i).let { o -> add(SocialPost(o.getLong("id"), o.getLong("userId"), o.getString("title"),o.getString("body"), ""))
            }
        }
    }
}

fun parseUsers(json: String): List<User> {
    return buildList {
        val arr = JSONArray(json)
        for(i in 0 .. arr.length() - 1){
            arr.getJSONObject(i).let {
                o -> add(User(o.getLong("id"), o.getString("firstName"), o.getString("lastName"), o.getString("avatarUrl")))
            }
        }
    }
}

fun parseComment(json: String): List<Comment>{
    return buildList {
        val arr = JSONArray(json)
        for(i in 0 .. arr.length() - 1){
            arr.getJSONObject(i).let {
                    o -> add(Comment(o.getLong("id"), o.getLong("postId"), o.getString("userName"), o.getString("content")))
            }
        }
    }
}

suspend fun fetchUser(users: List<User>, userId: Long): User{
    delay((200L..800L).random())
    return users.first{it.id == userId}
}

suspend fun fetchComment(comments: List<Comment>, postId: Long): List<Comment>{
    delay((300L..900L).random())
    return comments.filter {it.postId == postId}
}

@Composable
@Preview(
    device = "spec:parent=pixel_5",
    showBackground = true,
    showSystemUi = true
)
fun SocialFeedScreen(){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var states by remember { mutableStateOf <List<PostState>>(emptyList()) }
    var loadingJob by remember { mutableStateOf<Job?>(null) }

    fun load(){
        loadingJob?.cancel()
        loadingJob = scope.launch {
            val postsJson = async { readJson(context, "social_posts.json") }
            val usersJson = async { readJson(context, "users.json") }
            val commentsJson = async { readJson(context, "comments.json") }

            val posts = parsePosts(postsJson.await())
            val users = parseUsers(usersJson.await())
            val comments = parseComment(commentsJson.await())

            states = posts.map{
                PostState.Loading
            }

            supervisorScope {
                posts.forEachIndexed { i, post ->
                    launch {
                        try{
                            val userDeferred = async {
                                try{ fetchUser(users, post.userId)}
                                catch (e: Exception){null}
                            }
                            val commentsDeferred = async {
                                try { fetchComment(comments, post.id)}
                                catch (e: Exception){null}
                            }

                            val user = userDeferred.await()
                            val postComments = commentsDeferred.await()

                            states = states.toMutableList().also {
                                it[i] = PostState.Ready(post, user, postComments)
                            }
                        }catch (e: CancellationException){
                            throw e
                        }catch (e: Exception){
                            states = states.toMutableList().also{it[i] = PostState.Error(post)}
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) { load() }

    Column(modifier = Modifier.fillMaxSize().padding(top = 10.dp)){
        Row (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
             verticalAlignment = Alignment.CenterVertically
        ){
            Text("Лента",fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Button(
                onClick = {load()}
            ) {
                Text("Обновить")
            }
        }
        LazyColumn (
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ){
            items(states.size) {
                PostCard(states[it])
            }
        }
    }
}

@Composable
fun PostCard(state: PostState){
    Card(modifier = Modifier.fillMaxWidth()){
        when (state){
            is PostState.Loading -> {
                Row(verticalAlignment = Alignment.CenterVertically){
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        "Загрузка...", color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            is PostState.Ready -> {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.user != null) {
                            AsyncImage(
                                model = state.user.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("${state.user.firstName} ${state.user.lastName}",
                                fontWeight = FontWeight.SemiBold)
                        } else {
                            Box(
                                Modifier.size(36.dp).clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.errorContainer),
                                contentAlignment = Alignment.Center
                            ) { Text("?", fontWeight = FontWeight.Bold) }
                            Spacer(Modifier.width(8.dp))
                            Text("Неизвестный автор",
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Text(state.post.title, fontWeight = FontWeight.Bold)
                    Text(state.post.body, fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(Modifier.height(8.dp))

                    when {
                        state.comment == null ->
                            Text(" Комментарии не загрузились", fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.error)
                        state.comment.isEmpty() ->
                            Text("Нет комментариев", fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        else -> state.comment.take(2).forEach {
                            Text("• ${it.userName}: ${it.content}", fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            is PostState.Error -> {
                Text(" Ошибка загрузки: ${state.post.title}",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
