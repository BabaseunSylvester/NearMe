# NearMe 📍

A local places and events discovery Android app built with modern Android 
development best practices.

## Features
- 🔍 Search nearby places by name or keyword
- 🏷️ Filter by category — Food, Entertainment, Outdoors, Shopping
- 📄 Detailed place info — photos, address, ratings, contact details
- ❤️ Save favorites locally for quick access
- 📶 Offline support — cached results available without internet
- ⚠️ Proper loading, error, and empty states throughout

## Tech Stack
| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| Networking | Retrofit + OkHttp |
| Local Storage | Room Database |
| DI | Hilt |
| State Management | ViewModel + StateFlow |
| Image Loading | Coil |
| API | Foursquare Places API |

## Architecture
The app follows Clean Architecture with three layers:
- **Data** — API calls, Room database, repository implementation
- **Domain** — models and repository interface
- **Presentation** — ViewModels and Compose UI screens

## Screenshots
_Coming soon_

## Getting Started
1. Clone the repo
2. Add your Foursquare API key to `local.properties`: API_SERVICE_KEY=your_key_here
3. Build and run on Android Studio Hedgehog or later

## Requirements
- Android 8.0 (API 26)+
- Android Studio Hedgehog+
