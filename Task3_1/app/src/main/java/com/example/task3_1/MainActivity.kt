package com.example.task3_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task3_1.products.Product
import com.example.task3_1.products.productList
import com.example.task3_1.ui.theme.Task3_1Theme
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task3_1Theme {

            }
        }
    }
}

@Preview
@Composable
fun GroceryCatalog(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = productList) { item ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp, horizontal = 12.dp)) {
                Row {
                    Image(
                        painter = painterResource(id = item.image),
                        contentDescription = item.title,
                        modifier = Modifier.clip(MaterialTheme.shapes.medium)
                            .height(128.dp)
                            .width(128.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = item.description,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 13.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}

