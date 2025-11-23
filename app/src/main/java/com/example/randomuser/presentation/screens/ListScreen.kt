package com.example.randomuser.presentation.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.randomuser.R
import com.example.randomuser.data.model.Dob
import com.example.randomuser.data.model.Location
import com.example.randomuser.data.model.Name
import com.example.randomuser.data.model.Picture
import com.example.randomuser.data.model.Street
import com.example.randomuser.data.model.User
import com.example.randomuser.presentation.viewModel.UserListUiState
import com.example.randomuser.presentation.viewModel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    viewModel: UserViewModel,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = colorResource(R.color.screenBackground),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onBackClick,
                containerColor = colorResource(R.color.primaryTextColor),
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.plus),
                    contentDescription = "Добавить пользователя",
                    modifier = Modifier
                        .size(25.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopStart
        ) {
            when (val state = uiState) {
                is UserListUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UserListUiState.Success -> {
                    val users = state.users
                    if (users.isEmpty()) {
                        Text(
                            text = "Список пуст.\nНажмите '+' чтобы сгенерировать пользователя.",
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
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
                    Text(
                        text = state.message,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                is UserListUiState.Idle -> {
                    Text(
                        text = "Список пуст.\nНажмите '+' чтобы сгенерировать пользователя.",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun UserListItem(user: User, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.cardColor)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Аватар
            AsyncImage(
                model = user.picture?.thumbnail,
                contentDescription = "Аватар",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.name?.first} ${user.name?.last}",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.primaryTextColor),
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.phone ?: "Нет телефона",
                    color = colorResource(R.color.secondaryTextColor),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = user.nat ?: "",
                        color = colorResource(R.color.secondaryTextColor),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F2F5)
@Composable
private fun UserListItemPreview() {
    val fakeUser = User(
        name = Name("Ritthy", "Lopez"),
        email = "ritthy.lopez@example.com",
        gender = "male",
        picture = Picture(
            "",
            ""
        ),
        phone = "123-456-7890",
        location = Location(
            street = Street(123, "Main St"),
            city = "Anytown",
            state = "Anystate",
            country = "USA",
            postcode = "12345",
            coordinates = null,
            timezone = null
        ),
        login = null,
        nat = "US",
        dob = Dob("sedf", 32)
    )
    UserListItem(user = fakeUser, onClick = {})
}