package com.example.randomuser.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.randomuser.R
import com.example.randomuser.data.Coordinates
import com.example.randomuser.data.Location
import com.example.randomuser.data.Login
import com.example.randomuser.data.Name
import com.example.randomuser.data.Picture
import com.example.randomuser.data.Street
import com.example.randomuser.data.Timezone
import com.example.randomuser.data.User
import com.example.randomuser.viewModel.UserListUiState
import com.example.randomuser.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: UserViewModel,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    // 1. Подписываемся на ЕДИНЫЙ поток состояния UI
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Сгенерированные пользователи") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            // 2. Используем `when` для обработки ВСЕХ состояний
            when (val state = uiState) {
                is UserListUiState.Loading -> {
                    // Показываем индикатор загрузки
                    CircularProgressIndicator()
                }
                is UserListUiState.Success -> {
                    // Если данные успешно загружены, показываем список
                    val users = state.users
                    if (users.isEmpty()) {
                        Text(
                            text = "Список пуст.\nСгенерируйте пользователей на главном экране.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(
                                items = users,
                                key = { user -> user.login?.uuid ?: user.email ?: "" }
                            ) { user ->
                                UserListItem(
                                    user = user,
                                    onClick = { onUserClick(user) }
                                )
                            }
                        }
                    }
                }
                is UserListUiState.Error -> {
                    // Если произошла ошибка, показываем сообщение
                    Text(
                        text = state.message,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is UserListUiState.Idle -> {
                    // Начальное состояние. Можно ничего не показывать или дать подсказку.
                    Text(
                        text = "Список пуст.\nСгенерируйте пользователей на главном экране.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

/**
 * Отдельный Composable для одного элемента в списке.
 */
@Composable
private fun UserListItem(user: User, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) // Делаем весь элемент кликабельным
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.picture?.thumbnail ?: "",
                contentDescription = "Аватар пользователя",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "${user.name?.first ?: "Имя"} ${user.name?.last ?: "Фамилия"}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = user.email ?: "Email не указан",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Divider(modifier = Modifier.padding(start = 16.dp + 48.dp + 16.dp)) // Отступ для разделителя
    }
}


// Превью для быстрой разработки UI одного элемента списка
@Preview(showBackground = true)
@Composable
private fun UserListItemPreview() {
    val fakeUser = User(
        login = Login("hvkh5b5b879x"),
        gender = "female",
        name = Name("Lina", "Larsen"),
        email = "lina.larsen@example.com",
        picture = Picture("", "https://randomuser.me/api/portraits/thumb/women/27.jpg"),
        location = Location(
            Street(0, ""),
            "",
            "",
            "",
            "",
            Coordinates("", ""),
            Timezone("", "")
        ),
        phone = "",
        nat = "NO"
    )
    MaterialTheme {
        UserListItem(user = fakeUser, onClick = {})
    }
}