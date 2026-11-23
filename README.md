# DevFinder

**Discover, explore and save GitHub repositories.**

DevFinder is a native Android application built with Kotlin and Jetpack Compose. It allows users to discover GitHub repositories, search for projects using advanced filters, explore repository details and save their favorites for offline access.

This project demonstrates modern Android development practices, clean code organization and a scalable application architecture.

## Features

- **Explore repositories:** Discover popular GitHub projects.
- **Smart search:** Search repositories automatically with a 600 ms debounce.
- **Advanced filters:** Filter results by programming language and minimum star count.
- **Repository details:** View information about individual repositories.
- **Favorites:** Save and remove repositories from a personal collection.
- **Offline access:** Browse saved repositories without an internet connection.
- **Modern UI:** A dark-themed interface built entirely with Jetpack Compose.
- **Developer profile:** Access the project's source code and developer GitHub profile.

## Screenshots

*Screenshots will be added soon.*

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Main programming language |
| Jetpack Compose | Declarative UI |
| Material 3 | UI components and theming |
| MVVM | Presentation architecture |
| Clean Architecture | Separation of application responsibilities |
| Hilt | Dependency injection |
| Retrofit | GitHub REST API integration |
| OkHttp | HTTP client |
| Room | Local persistence |
| Kotlin Coroutines & Flow | Asynchronous operations and reactive state |
| Navigation Compose | Navigation between screens |
| JUnit & Coroutines Test | Unit testing |

## Architecture

DevFinder follows MVVM and Clean Architecture principles, separating the application into three main layers.

**Presentation Layer**
- Jetpack Compose screens
- ViewModels
- UI state management

**Domain Layer**
- Domain models
- Repository interfaces
- Use cases

**Data Layer**
- Retrofit API services
- DTOs and mappers
- Repository implementations
- Room database, entities and DAOs

### Project Structure

```text
com.devfinder.app/
├── core/
│   ├── designsystem/
│   ├── di/
│   └── navigation/
├── data/
│   ├── local/
│   ├── mapper/
│   ├── remote/
│   └── repository/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── feature/
    ├── home/
    ├── search/
    ├── detail/
    ├── saved/
    └── profile/
```

## Testing

The project includes **20 unit tests** covering repository mapping, use cases, ViewModel behavior, search debounce and filter combinations.

Run the tests with:

```bash
./gradlew testDebugUnitTest
```

On Windows:

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

## Getting Started

### Requirements

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle configuration
- Android device or emulator running Android 8.0 (API 26) or later

### Installation

1. Clone the repository:

```bash
git clone https://github.com/Felixcl91/DevFinder.git
```

2. Open the project in Android Studio.
3. Allow Gradle to synchronize and download the required dependencies.
4. Select an emulator or connected Android device.
5. Run the `app` configuration.

The application uses the public GitHub REST API. No API key is required for its current functionality.

## Roadmap

- [x] Repository discovery
- [x] Repository search
- [x] Language and star filters
- [x] Repository details
- [x] Offline favorites
- [x] Unit tests
- [x] Developer profile
- [x] Custom launcher icon

## Developer

Developed by **Felixcl91**, an Android developer focused on building native applications with Kotlin, Jetpack Compose and modern Android architecture.

**GitHub:** https://github.com/Felixcl91

## Project Status

**Version 1.0 — Portfolio project**

The main features are implemented and have been manually tested. The project is available as a demonstration of native Android development skills.