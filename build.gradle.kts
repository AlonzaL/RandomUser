// build.gradle.kts (Project-level)
// ЗАВЕДОМО РАБОЧАЯ ВЕРСИЯ

// ВАЖНО: Применяем плагины ко всему проекту, но с `apply false`,
// чтобы каждый модуль сам решал, что ему нужно.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
}