# Flight Shooting Game Development Plan

The following step‑by‑step plan lays out the work required to build a fully functional Flight Shooting Game in Android using Kotlin and Jetpack Compose.  Each step is intentionally short‑form to make it easy to track and measure progress.

## 1. Project Initialization
* Import the existing repo into Android Studio (Gradle 8.13.2).
* Verify Gradle wrapper works: `./gradlew build –info`.
* Ensure `build.gradle.kts` uses explicit dependency coordinates (no `plugins { alias(libs.*) }`).
* Run `./gradlew assembleDebug` to confirm a successful debug build.

## 2. Architecture & Core Modules (DONE)
* Define **MVVM** structure: `GameViewModel`, `GameRepository`, `GameScreen`.
* Install a coroutine‑based **GameRunner** that maintains a consistent tick rate.
* Create core **data models**: `Aircraft`, `Enemy`, `Projectile`, `GameState`.

## 3. Rendering & UI (DONE)
* Design Compose UI for:
  * Cockpit overlay with HUD elements.
  * Game canvas using `AndroidCanvas` for custom drawing.
  * Main menu, pause dialog, and game‑over screens.
* Wire UI to `GameState` via `StateFlow`.

## 4. Game Mechanics (DONE)
* Implement **physics** for aircraft motion using simple kinematic equations.
* Create **EnemySpawner** to introduce waves based on difficulty.
* Add **collision detection** between projectiles and enemies; update score accordingly.
* Provide **power‑ups** and **shield** mechanic.
* **AI Behaviour Enhanced**: Implemented wave-based enemy spawning with random horizontal movement.

## 5. Input Handling (DONE)
* Map **touch** gestures to aircraft controls (drag for pitch/roll). 
* Support device **accelerometer** as an alternative control source.
* Add **on‑screen fire button**.

## 6. Audio & Visual Assets (DONE)
* Add background music using `MediaPlayer` (or ExoPlayer if streaming).
* Load sound effects for shooting, explosion, power‑up.
* Import sprite assets as PNGs; vector drawables for UI elements.

## 7. Testing (DONE - Unit tests; BLOCKED - Instrumentation tests)
* **Unit tests** for physics calculations, enemy spawning logic, and state transitions. (DONE)
* **Instrumentation tests** for UI flows (menu navigation, pause, resume). (PENDING: compilation issues resolved, but tests need to be implemented/fixed)
* Verify smooth frame rate (60 fps target) in Android Studio Profiler.

## 8. Build & Release (DONE)
* Configure release build signing with keystore.
* Generate APK/AAB for distribution.
* Write release notes and build manifest for Play Store (if publishing).

## 9. Documentation & CI (DONE)
* Document public API in `README` and inline KDoc.
* Add **GitHub Actions** for lint, unit tests, and build.
* Ensure `detekt` analysis passes without critical issues.

---
**Note**: Each section should be tracked in a Git branch to keep changes isolated and reviewable.
