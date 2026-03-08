package com.example.task4_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task4_3.data.GitHubRepo
import com.example.task4_3.ui.theme.Task4_3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task4_3Theme {
                    GithubSearchScreen()
                }
            }
        }
    }

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

fun CoroutineScope.debounce(waitMS: Long = 500L, action: suspend(String) -> Unit): (String) -> Unit {
    var job: Job? = null
    return { query -> job?.cancel()
            job = launch {
                delay(waitMS)
                action(query)
            }}
}

fun searchRepos(jsonText: String, query: String) : List<GitHubRepo> {
    if (query.isBlank()) {
        return emptyList()
    }

    val array = JSONArray(jsonText)
    return buildList {
        for (i in 1..array.length() - 1) {
            val obj = array.getJSONObject(i)
            val fullName = obj.optString("full_name")
            val description = obj.optString("description")
            val language = obj.optString("language")

            if (fullName.contains(query, ignoreCase = true) || description.contains(query, ignoreCase = true) || language.contains(query, ignoreCase = true)
            ) {
                add(
                    GitHubRepo(
                        id = obj.optLong("id"),
                        fullName = fullName,
                        description = description,
                        stargazersCount = obj.optInt("stargazers_count"),
                        language = language
                    )
                )
            }
        }
    }
}

@Composable
@Preview(
    device = "spec:parent=pixel_5",
    showBackground = true,
    showSystemUi = true
)
fun GithubSearchScreen(){
    val context = LocalContext.current

    val jsonText = remember {
        context.assets.open("repos.json").bufferedReader().use {it.readText()}
    }

    var query by remember { mutableStateOf("") }
    var repos by remember { mutableStateOf <List<GitHubRepo>> (emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val debouncedSearch = remember {
        scope.debounce(waitMS = 500L) {
            q -> isLoading = true
            repos = searchRepos(jsonText, q)
            isLoading = false
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).padding(top = 16.dp)
    ){
        Text(
            "Github Search", style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { newValue ->
                query = newValue
                if (newValue.isBlank()) {
                    repos = emptyList()
                    isLoading = false
                } else {
                    isLoading = true
                    debouncedSearch(newValue)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {Text("Поиск репозиториев...")},
            singleLine = true
        )

        Spacer(Modifier.height(8.dp))

        if(isLoading){
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }else{
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)){
                items(repos, key = {it.id}){
                    repo -> RepoCard(repo)
                }
            }
        }

    }
}


@Composable
fun RepoCard(repo: GitHubRepo){
    Card (modifier = Modifier.fillMaxWidth()){
        Column(modifier = Modifier.padding(12.dp)) {
            Text(repo.fullName, fontWeight = FontWeight.Bold)
            if(repo.description.isNotBlank()) {
                Text(repo.description , style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }
            Text("${repo.language} - ${repo.stargazersCount}", style = MaterialTheme.typography.bodySmall, maxLines = 2)
        }
    }
}
