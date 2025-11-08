package com.example.task2_9

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.task2_9.model.Cart
import com.example.task2_9.model.Product
import com.example.task2_9.ui.theme.Task2_9Theme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task2_9Theme {
                    ShoppingCartScreen()
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

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=pixel_9_pro, orientation=portrait"
)
@Composable
fun ShoppingCartScreen(){
    val context = LocalContext.current
    var products by remember { mutableStateOf(
            listOf(
                Product(0,"Товар #1",100),
                Product(1,"Товар #2",200),
                Product(2,"Товар #3",300),
            )
        )
    }
    val cart = remember(products ) { Cart(products) }
    var totalSum = cart.total

    Box(
        modifier = Modifier.padding(0.dp, 80.dp).fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            for (product in products) {
                Text(text = product.name + " - " + product.price + " рублей")
                Spacer(modifier = Modifier.height(10.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Товар на сумму: $totalSum рублей"
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row {
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        val beforeSum = products.sumOf { it.price }
                        products = AddProductSection(products)
                        val afterSum = products.sumOf { it.price }
                        if(beforeSum < 500 && afterSum >= 500){
                            Toast.makeText(context, "Доставка бесплатная", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        text = "Добавить"
                    )
                }
                Button(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        if (products.isNotEmpty()){
                            products = RemoveProductSection(products)
                        }
                        else{
                            Toast.makeText(context, "Корзина пустая !", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) {
                    Text(
                        text = "Удалить"
                    )
                }
            }
        }
    }
}


fun AddProductSection(products: List<Product>): List<Product>{
    val newId = products.size
    val name = "Товар #${products.size + 1}"
    val price = Random.nextInt(10, 200)
    val product: Product = Product(newId, name, price)
    return products + product
}

fun RemoveProductSection(products: List<Product>): List<Product>{
    if(products.isNotEmpty()){
        return products.dropLast(1)
    }
    else{
        return products
    }
}


