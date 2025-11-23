package com.example.randomuser.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomuser.R
import com.example.randomuser.presentation.viewModel.NavigationEvent
import com.example.randomuser.presentation.viewModel.UserListUiState
import com.example.randomuser.presentation.viewModel.UserViewModel

private val genderOptions = listOf("Мужской" to "male", "Женский" to "female")
private val nationalityOptions = listOf(
    "Австралия" to "AU",
    "Бразилия" to "BR",
    "Великобритания" to "GB",
    "Германия" to "DE",
    "Швейцария" to "CH",
    "Дания" to "DK",
    "Испания" to "ES",
    "Финляндия" to "FI",
    "Ирландия" to "IE",
    "Индия" to "IN",
    "Иран" to "IR",
    "Канада" to "CA",
    "Мексика" to "MX",
    "Нидерланды" to "NL",
    "Норвегия" to "NO",
    "Новая Зеландия" to "NZ",
    "Сербия" to "RS",
    "США" to "US",
    "Турция" to "TR",
    "Украина" to "UA",
    "Франция" to "FR"
)

@Composable
fun HomeScreen(
    viewModel: UserViewModel,
    onNavigateToList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoading = uiState is UserListUiState.Loading

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToUserList -> {
                    onNavigateToList()
                }
            }
        }
    }

    HomeScreenContent(
        isLoading = isLoading,
        onGenerateClick = { gender, nat ->
            viewModel.loadUser(gender, nat)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    isLoading: Boolean,
    onGenerateClick: (gender: String, nat: String) -> Unit
) {
    var selectedGender by remember { mutableStateOf(genderOptions.first()) }
    var selectedNationality by remember {
        mutableStateOf(nationalityOptions.find { it.second == "US" } ?: nationalityOptions.first())
    }

    Scaffold(
        containerColor = colorResource(R.color.screenBackground)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            ) {
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Generate user",
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                DropdownWithLabel(
                    label = "Select Gender :",
                    options = genderOptions,
                    selectedOption = selectedGender,
                    onOptionSelected = { selectedGender = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                DropdownWithLabel(
                    label = "Select Nationality :",
                    options = nationalityOptions,
                    selectedOption = selectedNationality,
                    onOptionSelected = { selectedNationality = it }
                )
            }
            Button(
                onClick = {
                    onGenerateClick(selectedGender.second, selectedNationality.second)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.BottomCenter),
                enabled = !isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.buttonColor)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("GENERATE", letterSpacing = 1.2.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownWithLabel(
    label: String,
    options: List<Pair<String, String>>,
    selectedOption: Pair<String, String>,
    onOptionSelected: (Pair<String, String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.buttonColor),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                value = selectedOption.first,
                onValueChange = {},
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.first) },
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        isLoading = false,
        onGenerateClick = { _, _ -> }
    )

}