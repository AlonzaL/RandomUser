package com.example.randomuser.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onGenerateClick: (gender: String, nationalities: String) -> Unit
) {
    // --- Состояния для хранения ВЫБРАННЫХ значений ---
    // Устанавливаем начальные значения по умолчанию.
    var selectedGender by remember { mutableStateOf(genderOptions.first()) }
    var selectedNationality by remember { mutableStateOf(nationalityOptions.first()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Параметры генерации", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(32.dp))

            // --- Выпадающий список для пола ---
            ExposedDropdownMenu(
                label = "Пол",
                options = genderOptions,
                selectedOption = selectedGender,
                onOptionSelected = { selectedGender = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // --- Выпадающий список для национальности ---
            ExposedDropdownMenu(
                label = "Национальность",
                options = nationalityOptions,
                selectedOption = selectedNationality,
                onOptionSelected = { selectedNationality = it }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // --- Кнопка "Сгенерировать" ---
            Button(
                onClick = {
                    // API ожидает "male" или "female", а для "Любой" - отсутствие параметра.
                    // Мы адаптируем это здесь.
                    val genderToSend = selectedGender.second

                    // Вызываем колбэк, передавая в него КОДЫ, а не отображаемые имена.
                    onGenerateClick(genderToSend, selectedNationality.second)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Сгенерировать пользователей")
            }
        }
    }
}

/**
 * Переиспользуемый Composable для выпадающего списка.
 * @param label Текст метки над списком.
 * @param options Список пар (отображаемое имя, код значения).
 * @param selectedOption Текущая выбранная пара.
 * @param onOptionSelected Лямбда, вызываемая при выборе нового элемента.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExposedDropdownMenu(
    label: String,
    options: List<Pair<String, String>>,
    selectedOption: Pair<String, String>,
    onOptionSelected: (Pair<String, String>) -> Unit
) {
    // Состояние для отслеживания, открыто ли меню
    var expanded by remember { mutableStateOf(false) }

    Box {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            // Текстовое поле, которое отображает текущий выбор и открывает меню по клику
            OutlinedTextField(
                modifier = Modifier.menuAnchor(), // Важно для правильной работы
                readOnly = true,
                value = selectedOption.first, // Показываем имя, а не код
                onValueChange = {},
                label = { Text(label) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            // Само выпадающее меню
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.first) },
                        onClick = {
                            onOptionSelected(selectionOption)
                            expanded = false // Закрываем меню после выбора
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onGenerateClick = { gender, nationalities -> }
    )
}