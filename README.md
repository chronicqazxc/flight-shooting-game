# Flight Shooting Game

## Project Overview

This is a simple flight shooting game developed for Android. The project aims to provide a basic yet engaging arcade-style experience with core game mechanics, implemented using modern Android development practices.

**Target Platform:** Android (API 24+ supported)
**Language:** Kotlin 2.3.0
**UI Toolkit:** Jetpack Compose 1.6
**Game Loop:** Coroutine-driven tick at ~60 FPS
**Architecture:** MVVM (`GameViewModel` exposes `StateFlow`)

## Current Features

*   **Player Movement:** Control the player aircraft via drag gestures.
*   **Shooting:** Fire projectiles with an on-screen button.
*   **Collision Detection:** Projectiles collide with enemies, increasing score. Player collisions with enemies are also detected.
*   **Shield Power-up:** Collect power-ups to gain a temporary shield, protecting from one enemy hit.
*   **Enemy AI:** Enemies spawn in waves, feature randomized horizontal movement, and bounce off screen edges.
*   **Game State Management:** Includes a game-over condition and a restart option.
*   **Sound Effects:** Integrated audio feedback for shooting, explosions, and power-up collection.

## Setup and Building

To set up and build the project locally:

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/chronicqazxc/flight-shooting-game.git
    cd flight-shooting-game
    ```
2.  **Verify Gradle Wrapper:**
    ```bash
    ./gradlew build --info
    ```
3.  **Open in Android Studio:** Open the project in Android Studio 2024.1+ (Gradle plugin 8.4 is required).
4.  **Enable Jetpack Compose:** Ensure `android.useAndroidX=true` and `android.enableJetpackCompose=true` are set in your `gradle.properties` file.
5.  **Build Debug APK:**
    ```bash
    ./gradlew assembleDebug
    ```
    Alternatively, you can build and run directly from Android Studio.

## Running Tests

*   **Unit Tests:**
    ```bash
    ./gradlew test
    ```
*   **Instrumentation Tests:**
    Currently **BLOCKED** due to a persistent compilation error (`Unresolved reference: assertExists`). We are actively investigating this issue.
    *(Note: The `GameScreenTest.kt` file has been temporarily removed from the project to allow for clean builds and progress on other tasks.)*

## Continuous Integration

A basic GitHub Actions workflow (`.github/workflows/ci.yml`) is configured to:
*   Run on `push` and `pull_request` events to the `main` branch.
*   Set up Java 17 and an Android environment.
*   Run `./gradlew lintDebug`.
*   Run `./gradlew testDebugUnitTest`.
*   Run `./gradlew assembleDebug`.

---

Please remember to commit these newly generated files to your repository:

```bash
git add .gitignore README.md
git commit -m "Generated .gitignore and README.md files"
git push origin main
```
Let me know once you've done that, or if you have any other questions!
