package com.example.randomuser.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import com.example.randomuser.presentation.viewModel.UserViewModel

@Composable
fun DetailScreen(
    viewModel: UserViewModel,
    onBackClick: () -> Unit
) {
    val selectedUser by viewModel.selectedUser.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedUser()
        }
    }

    selectedUser?.let { user ->
        UserProfileContent(user = user, onBackClick = onBackClick)
    } ?: run {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun UserProfileContent(user: User, onBackClick: () -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Surface(
        color = colorResource(R.color.screenBackground)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailHeader(user = user, onBackClick = onBackClick)
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Hi, how are you today?",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "I'm fine!",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = "${user.name?.first} ${user.name?.last}",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = colorResource(R.color.tabBackground),
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                InfoTabs(
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )
                when (selectedTabIndex) {
                    0 -> PersonalInfoContent(user)
                    1 -> ContactInfoContent(user)
                    2 -> ContactInfoContent(user)
                    3 -> LocationInfoContent(user)
                }
            }
        }
    }
}


@Composable
private fun DetailHeader(user: User, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        // Синий фон хедера
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(colorResource(R.color.headerBlue))
        ) {
            // Кнопка назад
            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    painter = painterResource(R.drawable.arrow_back),
                    contentDescription = "Назад",
                    tint = Color.White,
                    modifier = Modifier
                        .size(25.dp)
                )
            }
        }
        AsyncImage(
            model = user.picture?.large,
            contentDescription = "Аватар",
            modifier = Modifier
                .offset(y = 60.dp)
                .size(120.dp)
                .clip(CircleShape)
                .border(4.dp, Color.White, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun InfoTabs(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf(
        painterResource(R.drawable.account),
        painterResource(R.drawable.phone),
        painterResource(R.drawable.email),
        painterResource(R.drawable.location)
    )

    SecondaryTabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = colorResource(R.color.tabBackground),
        contentColor = Color.White,
        indicator = {},
        divider = {},
        tabs = {
            tabs.forEachIndexed { index, icon ->
                val isSelected = selectedTabIndex == index
                Tab(
                    selected = isSelected,
                    onClick = { onTabSelected(index) },
                    modifier = if (isSelected) Modifier.background(Color.White) else Modifier,
                    icon = {
                        Icon(
                            painter = icon,
                            contentDescription = "Tab $index",
                            tint = if (isSelected) colorResource(R.color.tabBackground) else Color.White,
                            modifier = Modifier
                                .size(25.dp)
                        )
                    }
                )
            }
        })
}


@Composable
private fun PersonalInfoContent(user: User) {

    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        DetailInfoRow("First name", user.name?.first)
        DetailInfoRow("Last name", user.name?.last)
        DetailInfoRow("Gender", user.gender)
        DetailInfoRow("Age", user.dob?.age?.toString())
        DetailInfoRow("Date of birth", user.dob?.date ?: "Неизвестно")
    }
}

@Composable
private fun ContactInfoContent(user: User) {
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        DetailInfoRow("Email", user.email)
        DetailInfoRow("Phone", user.phone)
    }
}

@Composable
private fun LocationInfoContent(user: User) {
    val location = user.location
    Column(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
    ) {
        DetailInfoRow("Address", "${location?.street?.number} ${location?.street?.name}")
        DetailInfoRow("City", location?.city)
        DetailInfoRow("State", location?.state)
        DetailInfoRow("Country", location?.country)
        DetailInfoRow("Postcode", location?.postcode.toString())
    }
}


@Composable
private fun DetailInfoRow(label: String, value: String?) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label :",
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value ?: "Не указано",
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.tabBackground)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
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
    UserProfileContent(user = fakeUser, onBackClick = {})
}