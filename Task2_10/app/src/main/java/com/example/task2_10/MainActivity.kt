package com.example.task2_10

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task2_10.db.Database
import com.example.task2_10.model.User
import com.example.task2_10.ui.theme.Task2_10Theme

class MainActivity : ComponentActivity() {
    private val database = Database()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Task2_10Theme {
                loginScreen(database = database)
            }
        }
    }

    fun login(email: String, password: String, database: Database): Boolean {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            val user = database.login(email, password)
            return user != null
        }
        return false
    }

    fun register(name: String, email: String, password: String, database: Database): Boolean {
        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            val user = User(name, email, password)
            return database.addUser(user)
        }
        return false
    }
}

@Composable
fun loginScreen(database: Database? = null) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginResult by remember { mutableStateOf<String?>(null) }
    var showRegistration by remember { mutableStateOf(false) }

    val context = LocalContext.current

    if (showRegistration) {
        registrationScreen(
            database = database,
            onBackToLogin = { showRegistration = false }
        )
    } else {
        Task2_10Theme {
            Box(
                modifier = Modifier.padding(0.dp, 100.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.welcome_login),
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.welcome_substring),
                        fontWeight = FontWeight.Light
                    )

                    // Показываем результат авторизации
                    if (loginResult != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = loginResult!!,
                            color = if (loginResult!!.contains("успешно")) colorResource(R.color.teal_200) else colorResource(R.color.violet),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        value = email,
                        onValueChange = {
                            email = it
                            loginResult = null // Сбрасываем сообщение при изменении email
                        },
                        label = { Text("email") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        value = password,
                        onValueChange = {
                            password = it
                            loginResult = null // Сбрасываем сообщение при изменении пароля
                        },
                        label = { Text("password") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        )
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    Text(
                        text = stringResource(R.string.forgot_password),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding().align(Alignment.End)
                    )
                    Spacer(modifier = Modifier.height(36.dp))
                    Button(
                        modifier = Modifier.padding(8.dp).fillMaxWidth(0.85f).height(50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.violet)),
                        onClick = {
                            if (database != null) {
                                val mainActivity = context as? MainActivity
                                if (mainActivity != null) {
                                    val success = mainActivity.login(email, password, database)
                                    loginResult = if (success) {
                                        "Вход выполнен успешно!"
                                    } else {
                                        "Ошибка: неверный email или пароль"
                                    }
                                } else {
                                    loginResult = "Ошибка: база данных недоступна"
                                }
                            } else {
                                loginResult = "Ошибка: база данных не инициализирована"
                            }
                        }
                    ) {
                        Text(
                            text = "Log In",
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(36.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.not_account),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        TextButton(
                            onClick = {
                                showRegistration = true
                            }
                        ) {
                            Text(
                                text = "Sign Up"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun registrationScreen(database: Database?, onBackToLogin: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registrationResult by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    Task2_10Theme {
        Box(
            modifier = Modifier.padding(0.dp, 100.dp).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registration",
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Create your account",
                    fontWeight = FontWeight.Light
                )

                // Показываем результат регистрации
                if (registrationResult != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = registrationResult!!,
                        color = if (registrationResult!!.contains("успешно")) colorResource(R.color.teal_200) else colorResource(R.color.violet),
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    value = name,
                    onValueChange = {
                        name = it
                        registrationResult = null
                    },
                    label = { Text("name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(36.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    value = email,
                    onValueChange = {
                        email = it
                        registrationResult = null
                    },
                    label = { Text("email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(modifier = Modifier.height(36.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(0.85f),
                    value = password,
                    onValueChange = {
                        password = it
                        registrationResult = null
                    },
                    label = { Text("password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(modifier = Modifier.height(36.dp))
                Button(
                    modifier = Modifier.padding(8.dp).fillMaxWidth(0.85f).height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(colorResource(R.color.violet)),
                    onClick = {
                        if (database != null) {
                            val mainActivity = context as? MainActivity
                            if (mainActivity != null) {
                                val success = mainActivity.register(name, email, password, database)
                                registrationResult = if (success) {
                                    "Регистрация выполнена успешно!"
                                } else {
                                    "Ошибка при регистрации"
                                }
                            } else {
                                registrationResult = "Ошибка: база данных недоступна"
                            }
                        } else {
                            registrationResult = "Ошибка: база данных не инициализирована"
                        }
                    }
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.height(36.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account?",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    TextButton(
                        onClick = onBackToLogin
                    ) {
                        Text(
                            text = "Log In"
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=pixel_9_pro,orientation=portrait"
)
@Composable
fun LoginScreenPreview() {
    Task2_10Theme {
        loginScreen(database = Database())
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=pixel_9_pro,orientation=portrait"
)
@Composable
fun RegistrationScreenPreview() {
    Task2_10Theme {
        registrationScreen(
            database = Database(),
            onBackToLogin = {}
        )
    }
}