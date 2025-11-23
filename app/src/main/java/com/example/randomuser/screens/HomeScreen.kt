package com.example.randomuser.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.randomuser.viewModel.NavigationEvent
import com.example.randomuser.viewModel.UserListUiState
import com.example.randomuser.viewModel.UserViewModel

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

/**
 * Экран для выбора параметров генерации пользователей с выпадающими списками.
 * @param onGenerateClick Лямбда, которая вызывается при нажатии на кнопку "Сгенерировать".
 */
@Composable
fun HomeScreen(
    viewModel: UserViewModel,
    onNavigateToList: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    // Подписка на состояния из ViewModel
    val isLoading = uiState is UserListUiState.Loading

    // Эффект для прослушивания одноразовых навигационных событий
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateToUserList -> {
                    Log.d("ViewModelDebug", "3. Событие навигации ПОЛУЧЕНО в HomeScreen")
                    onNavigateToList()
                }
            }
        }
    }

    // Передаем все состояния и лямбды в "глупый" UI-компонент
    HomeScreenContent(
        isLoading = isLoading,
        onGenerateClick = { gender, nat ->
            viewModel.loadUser(gender, nat)
        }
    )
}

/**
 * "Глупый" UI-компонент, который отвечает только за отрисовку.
 * Он не знает о ViewModel и полностью управляется извне.
 */
@Composable
private fun HomeScreenContent(
    isLoading: Boolean,
    onGenerateClick: (gender: String, nat: String) -> Unit
) {
    // Локальное состояние для UI (выбранные опции в списках)
    var selectedGender by remember { mutableStateOf(genderOptions.first()) }
    var selectedNationality by remember { mutableStateOf(nationalityOptions.first()) }

    Scaffold() { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Параметры генерации", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))

            // Выпадающий список для пола
            ExposedDropdownMenu(
                label = "Пол",
                options = genderOptions,
                selectedOption = selectedGender,
                onOptionSelected = { selectedGender = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Выпадающий список для национальности
            ExposedDropdownMenu(
                label = "Национальность",
                options = nationalityOptions,
                selectedOption = selectedNationality,
                onOptionSelected = { selectedNationality = it }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Единственная кнопка "Сгенерировать и перейти"
            Button(
                onClick = {
                    onGenerateClick(selectedGender.second, selectedNationality.second)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Сгенерировать и перейти к списку")
                }
            }
        }
    }
}


/**
 * Переиспользуемый Composable для выпадающего списка (взят из вашего оригинального кода).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenu(
    label: String,
    options: List<Pair<String, String>>,
    selectedOption: Pair<String, String>,
    onOptionSelected: (Pair<String, String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                readOnly = true,
                value = selectedOption.first,
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
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

/**
 * Превью для HomeScreen.
 * Мы вызываем "глупый" HomeScreenContent с фейковыми данными.
 */
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreenContent(
            isLoading = false,
            onGenerateClick = { _, _ -> }
        )
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
fun HomeScreenLoadingPreview() {
    MaterialTheme {
        HomeScreenContent(
            isLoading = true, // Показываем состояние загрузки
            onGenerateClick = { _, _ -> }
        )
    }
}