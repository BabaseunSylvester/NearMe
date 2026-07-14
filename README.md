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
| Home | Detail | Favorites |
|---|---|---|
| 

![Home](
<img width="1080" height="2265" alt="Screenshot_20260713_141231_NearMe" src="https://github.com/user-attachments/assets/bf44908a-1648-43b0-9a4a-1d57331345bf" />
<img width="1080" height="2265" alt="Screenshot_20260713_141306_NearMe" src="https://github.com/user-attachments/assets/0f185ae9-10a8-4968-a6d8-a198c1a50fe8" />
<img width="1080" height="2265" alt="Screenshot_20260713_141320_NearMe" src="https://github.com/user-attachments/assets/4e296894-bb59-457e-8bf3-54a2ac80626d" />
)

 | 

![Detail](
<img width="1080" height="2265" alt="Screenshot_20260713_141333_NearMe" src="https://github.com/user-attachments/assets/44d03023-c9f0-4612-808d-40c26bbd276b" />

)

 | 

![Favorites](
<img width="1080" height="2265" alt="Screenshot_20260713_141352_NearMe" src="https://github.com/user-attachments/assets/a7ec24ec-ec96-4553-85a8-6404d9b5138f" />

)

 |

## Demo Video
https://youtube.com/shorts/5g_k54BT3RU?feature=share

## Getting Started
1. Clone the repo
2. Add your Foursquare API key to `local.properties`: API_SERVICE_KEY=your_key_here
3. Build and run on Android Studio Hedgehog or later

## Requirements
- Android 8.0 (API 26)+
- Android Studio Hedgehog+
