# Flight Shooting Game Development

This **AGENTS.md** provides high‑level instructions for anyone working on the Flight Shooting Game project.  All build, development, and testing steps are summarized here to help keep the workflow consistent across contributors.

## 1. Project Overview
* **Target platform** – Android (API 24+ supported)
* **Language** – Kotlin 2.3.0
* **UI toolkit** – Jetpack Compose 1.6
* **Game loop** – Coroutine‑driven tick at ~60 FPS
* **Architecture** – MVVM (`GameViewModel` exposes `StateFlow`)
* **Features**
    * Player aircraft movement via drag gestures.
    * Player shooting mechanism.
    * Collision detection between projectiles and enemies, with score tracking.
    * Shield power-up that protects from one enemy collision.
    * Basic enemy AI with randomized horizontal movement and screen-edge bouncing.
    * Game-over condition with a restart option.
    * Sound effects for shooting, explosions, and power-ups.

## 2. Build & Setup
1. Clone the repo and run `./gradlew build –info` to verify that the Gradle Wrapper works.
2. Open the project in Android Studio 2024.1+ (the Gradle plugin 8.4 is required).
3. In *gradle.properties* enable Jetpack Compose: `android.useAndroidX=true` and `android.enableJetpackCompose=true`.
4. Build a debug APK with `./gradlew assembleDebug` or by clicking **Run**.

## 3. Directory Structure
```
└── app
    ├── src
    │   ├── main
    │   │   ├── java/com/flightgame
    │   │   │   ├── ui          // Compose UI & screens
    │   │   │   ├── model       // Data classes for aircraft, enemies
    │   │   │   ├── game        // Game loop, physics, spawner
    │   │   │   └── utils       // Helpers, extensions
    │   └── androidTest
    │   └── test
    └── build.gradle
```

## 4. Development Workflow
1. **Branching** – Always create feature branches from `main`.
2. **Code style** – Use Kotlin `detekt` lint; follow the style defined in `.editorconfig`.
3. **Unit tests** – Kotlin `junit` under `src/test/java`. Run with `./gradlew test`.
4. **Instrumented tests** – Place under `src/androidTest/java`. Run with `./gradlew connectedAndroidTest`.
5. **CI** – The project uses GitHub Actions (`ci.yml`) to run lint, unit tests, and build the release APK.

## 5. Asset Management
* Drawables and sound clips should live in `src/main/res/`.
* Vector drawables for UI elements; PNG/WEBP for sprite textures.
* All audio is MP3 or OGG and referenced via `MediaPlayer` or ExoPlayer.

## 6. Common Tasks
* **Add a new enemy type** – Create a data class in `model`, add physics logic in `game`, and register the type in `EnemySpawner`.
* **Add a new weapon type** – Implement damage scaling and animation.
* **Change input method** – Edit `InputHandler` in `game` and update the test harness accordingly.
* **Upgrade Compose version** – Update dependency in `build.gradle` and run `./gradlew resync`.

## 7. Troubleshooting
* If the app crashes on launch, run **adb logcat** and look for `IllegalStateException` in `GameViewModel`.
* For performance issues, profile the game loop using Android Studio Profiler and check for frame drops.

## 8. Licensing
* This project is licensed under Apache License 2.0 – see the `LICENSE` file.
