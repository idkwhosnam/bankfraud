# TrustLog Architecture Overview

## Stack
- **Platform:** Android, minSdk 26, targetSdk 34.
- **Language:** Kotlin with Coroutines.
- **Architecture:** MVVM + Repository pattern.
- **Data store:** Firebase Cloud Firestore (`trustlog_sessions` collection).

## Modules
- **UI Layer (Fragments + Activity):**
  - `MainActivity` hosts a Navigation Component graph of four fragments that mirror the required flow.
  - Each fragment owns only UI concerns and delegates behavior to its ViewModel.
- **ViewModels:**
  - `SessionViewModel` (activity scoped) stores session state (user identity, transfer amount, OTP input, score) and orchestrates transitions.
  - Screen specific ViewModels expose LiveData/StateFlow for view binding.
- **Repository Layer:**
  - `SessionRepository` (singleton) is responsible for packaging `SessionData` models and pushing them to Firestore using Kotlin coroutines.
- **Sensor Layer:**
  - `SensorCollectionManager` coordinates lifecycle between the UI and `SensorCollectorService` (foreground service).
  - `SensorCollectorService` subscribes to accelerometer, gyroscope, and touch events, and records contextual security flags (root, overlay, accessibility, injected/paste events).
  - Data is buffered inside an in-memory `SessionBuffer` and flushed to the ViewModel on demand.
- **Domain Utilities:**
  - `FraudDetector` consumes buffered sensor data/security flags and computes the rule-based score described in the brief.
  - `RootChecker`, `OverlayDetector`, `AccessibilityChecker`, `OtpPasteWatcher`, etc. isolate platform-specific checks.

## Data Flow
1. **TermsFragment**: verifies consent, navigates to Login.
2. **LoginFragment**: starts `SensorCollectorService` via `SensorCollectionManager.start()` and collects user identity.
3. **TransferFragment**: displays account stub, handles transfer action and pops up OTP dialog (`OtpDialogFragment`). OTP digit inputs are streamed back to `SessionViewModel` (detects paste events).
4. **ResultFragment**: stops the sensor service, pulls current sensor buffer, runs `FraudDetector`, updates UI state, and triggers `SessionRepository.uploadSession(sessionData)`.
5. **Firestore**: receives a JSON document per session with nested touch/sensor/security payloads.

## Foreground Service Strategy
- Uses `NotificationCompat` channel `trustlog_session` to run persistently during login + transfer screens.
- Touch events are captured globally via `MainActivity.dispatchTouchEvent` and forwarded to the manager.
- Physical sensors rely on `SensorManager` listeners registered inside the service.

## Threading
- Coroutines with `Dispatchers.Default` for sensor processing, `Dispatchers.IO` for Firestore.
- LiveData/StateFlow to expose states to UI.

## Firebase Integration
- App expects a `google-services.json` file in `app/`.
- Firestore configured via BoM and `firebase-firestore-ktx` dependency.

## Data Export Script
- Repository ships a Python helper (`tools/export_sessions.py`) that connects via Google Service Account credentials and dumps `trustlog_sessions` into CSV for analysis.
