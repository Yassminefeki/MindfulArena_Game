# 🎮 Mindful Arena

> Classic minds, modern battles 

![Build](https://img.shields.io/badge/build-passing-brightgreen) ![License](https://img.shields.io/badge/license-MIT-blue) ![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android) ![Language](https://img.shields.io/badge/language-Java%2011-orange?logo=java) ![Min SDK](https://img.shields.io/badge/minSDK-24-blueviolet) ![Firebase](https://img.shields.io/badge/backend-Firebase-FFCA28?logo=firebase)


---

## 💡 What Is This?

Mindful Arena is a feature-rich Android application that transforms the timeless Tic-Tac-Toe into a full competitive experience — complete with a strategic AI opponent, real-time online multiplayer, tournament brackets, and persistent player statistics. Built for anyone who wants more than a casual game, it fuses offline-first local storage with live Firebase cloud sync to keep your data everywhere you go. The app is designed for players who crave mental sharpness, social competition, and measurable self-improvement.

**👉 Mindful Arena turns a 3×3 grid into a full-fledged gaming platform.**

---

## ✨ Core Features

**[AI Engine](./app/src/main/java/com/example/xo/AI/)** — Strategic opponent with smart decision-making; no two games feel the same.

**[Online Multiplayer](./app/src/main/java/com/example/xo/activities/OnlineGameActivity.java)** — Real-time head-to-head gameplay powered by Firebase Realtime Database.

**[Tournament Mode](./app/src/main/java/com/example/xo/activities/TournamentActivity.java)** — Bracket-style competition across multiple rounds with cloud-synced progress.

**[Player Statistics](./app/src/main/java/com/example/xo/activities/StatisticsActivity.java)** — Persistent tracking of wins, losses, draws, and full game history via Room DB.

**[Firebase Auth](./app/src/main/java/com/example/xo/activities/)** — Secure sign-up and login so your progress follows you across devices.

**[Offline-First Architecture](./app/src/main/java/com/example/xo/models/)** — Room database ensures the game works perfectly with zero connectivity.

---

## 🏗️ Architecture

### 🔹 System Overview

```
┌──────────────────────────────────────────────────┐
│                  Presentation Layer               │
│   Activities (UI) · Material Design · RecyclerView│
└───────────────────────┬──────────────────────────┘
                        │
┌───────────────────────▼──────────────────────────┐
│               Business Logic Layer                │
│       AI Engine · Game Rules · Tournament Logic   │
└──────────────┬────────────────────┬──────────────┘
               │                    │
┌──────────────▼──────┐  ┌──────────▼──────────────┐
│   Local Data Layer  │  │   Cloud Data Layer        │
│   Room (SQLite)     │  │   Firebase Realtime DB    │
│   Offline History   │  │   Online Sync · Auth      │
└─────────────────────┘  └──────────────────────────┘
```

### 🔹 Data Flow

```
User Action → Activity → AI/Game Logic → Room DB (local save)
                                       → Firebase (online sync)
                                       → LiveData → UI Update
```

### 🔹 Folder Structure

```
Mindful_Arena/
├── 📱 app/src/main/java/com/example/xo/
│   ├── 🚀 MainActivity.java              — App entry point & navigation
│   ├── 🎮 activities/
│   │   ├── SplashActivity.java           — Launch screen
│   │   ├── HomeActivity.java             — Main menu hub
│   │   ├── GameActivity.java             — Local game board
│   │   ├── OnlineGameActivity.java       — Real-time multiplayer
│   │   ├── TournamentActivity.java       — Bracket management
│   │   ├── ResultActivity.java           — Post-game results
│   │   ├── StatisticsActivity.java       — Player stats dashboard
│   │   └── SettingsActivity.java         — User preferences
│   ├── 🤖 AI/                            — Game intelligence engine
│   ├── 📦 models/                        — Player, Game, Statistics models
│   └── 🔧 utils/                         — Shared helpers & constants
├── 🔥 app/google-services.json           — Firebase configuration
├── 🛡️ app/proguard-rules.pro             — Release obfuscation rules
└── ⚙️ build.gradle                       — Root build configuration
```

---

## 🛠️ Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| UI Framework | Android SDK 36 + AndroidX | Activity lifecycle, modern APIs |
| Design System | Material Design Components | Cards, buttons, themes |
| Layout Engine | ConstraintLayout + GridLayout | Responsive game board UI |
| Lists | RecyclerView | Statistics & history rendering |
| AI Logic | Custom Java engine | Opponent strategy & game rules |
| Local Database | Room v2.6.1 (SQLite) | Offline game history & stats |
| Cloud Backend | Firebase Realtime Database | Online multiplayer sync |
| Authentication | Firebase Auth | User accounts & login |
| Build System | Gradle + Wrapper | Dependency management |
| Language | Java 11 | Core application logic |
| Unit Testing | JUnit 4 + Espresso | Logic & UI validation |

---

## ⚡ Quick Start

### 🔹 Prerequisites

- [Android Studio](https://developer.android.com/studio) — Hedgehog or later
- [JDK 11+](https://adoptium.net/) — Required for compilation
- Android SDK API Level 36 — Install via SDK Manager
- [Firebase Account](https://console.firebase.google.com/) — For online features

### 🔹 Clone & Run

```bash
git clone https://github.com/Yassminefeki/Mindful_Arena.git
cd Mindful_Arena
./gradlew assembleDebug
```

### 🔹 Firebase Setup

| Step | Action |
|---|---|
| 1 | Go to [Firebase Console](https://console.firebase.google.com/) and create a project |
| 2 | Add an Android app with package `com.example.xo` |
| 3 | Download `google-services.json` |
| 4 | Place it in the `app/` directory |
| 5 | Enable **Realtime Database** and **Authentication** in the console |

### 🔹 Run Options

**💻 Local Development**
```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Clean rebuild
./gradlew clean build
```

**📱 On Device / Emulator**

Open the project in Android Studio → Select your device (API 24+) → Click ▶ Run.

---

## 🗺️ Roadmap

```
Q2 2026 → ELO-based ranking system, player profiles with avatars
Q3 2026 → Push notifications for online match invites, dark mode theming
Q4 2026 → 5×5 grid variant, weekly leaderboard with global rankings
Q1 2027 → iOS port, cross-platform multiplayer support
```

---

## 🤝 Contributing

1. Fork → `git checkout -b feature/your-feature` → code → open a Pull Request
2. Run `./gradlew test && ./gradlew connectedAndroidTest` before submitting
3. Browse open tasks on [Issues](https://github.com/Yassminefeki/Mindful_Arena/issues)
4. Every PR must include at least one test covering the changed logic



---

## 📄 License

![MIT](https://img.shields.io/badge/license-MIT-blue) — Free to use, modify, and distribute. See [LICENSE](./LICENSE) for details.

---

<div align="center">

Built with 💙 by **Yassmine Feki** & **Nour Ben Slimene**

*Two developers, one board, infinite possibilities.*

</div>
