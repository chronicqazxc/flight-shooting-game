// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Root build – keeps plugin versions in libs.versions.toml
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
