# OTT Sample - README

## Overview
This project is a sample OTT streaming application designed to support both TV and Mobile platforms. The app follows a scalable architecture and is developed using modern Android development practices. It includes various features like video streaming, search functionality, account management for TV and Mobile devices.

## Project Structure
- **Architecture**: MVVM with Clean Architecture (Separation of Concerns: Data, Domain, Presentation layers)
- **Modular Structure**: The project is organized into feature-level modules to promote reusability and scalability.
- **Build Variants**: Supports Gradle build variants for compiling the app for different platforms (TV and Mobile) from the same codebase.

## Tech Stack
- **UI**: Jetpack Compose (Mobile), Leanback + Jetpack Compose (TV)
- **Networking**: Ktor with Coroutines
- **Dependency Injection**: Hilt
- **Image Loading**: Coil
- **Data Persistence**: Jetpack DataStore
- **Media Playback**: ExoPlayer with HLS/DASH support

## Features

### Mobile App
- **Welcome Page**: Splash screen with the app name centered.
- **Main Page**: Contains a bottom navigation bar with items: Home, Search, and Account.
- **Home Page**: Displays a list of video cards (Thumbnail, Title, Duration).
- **Search Page**: Includes a search bar with recommended results and a separate search results page.
- **Account Page**: Displays user details (Name, Email, Phone Number)(it is common between mobile and TV).

### TV App
- **Welcome Page**: Splash screen with the app name centered.
- **Main Page**: Left-hand side menu with options: Home, Search, and Account.
- **Home Page**: Features a list of video cards using Leanback.
- **Search Page**: Includes a search bar with recommended results and a separate search results page using jetpack compose
- **Account Page**: Displays user details (it is common between mobile and TV).

## API Endpoints
- **GET /films**: get list of Lists media items for the home page.
- **GET /people/1**: Fetches user account details.

## Development Guidelines
- **Clean Architecture**: Follow Clean Architecture principles for separation of concerns.
- **Atomic Design**: UI elements are built following atomic design principles.
- **Coroutines**: UI-network callbacks are handled using Coroutines.
- **Gradle**: Supports build variants for compiling TV and Mobile apps.
- **Versioning**: Use trunk-based development with proper commit history in GitLab.

### Core Libraries
- **AndroidX Core KTX**: for Kotlin extensions for common framework APIs.
- **AppCompat**: for backward-compatible features.
- **Material Components**: for Material Design UI components.
- **ConstraintLayout**: for complex UI layouts.

### UI
- **Jetpack Compose**: for modern declarative UI.
    - **Compose BOM**: to manage versions.
    - **Material**: for Material Design.
    - **Activity Compose**: for Compose integration with activities.
    - **Navigation Compose**: for Compose navigation.
    - **TV Material**: for TV-specific UI components.
- **Leanback**: for Android TV UI.
- **Coil**: for image loading in Compose.
- **Glide**: for image loading in XML.

### Dependency Injection
- **Hilt**: for dependency injection.
    - **Hilt Compiler**: for dependency injection
    - **Hilt Navigation Compose**:  for integrating Hilt with Jetpack Compose.

### Networking & Serialization
- **Ktor**:for networking.
    - **JSON**: for JSON parsing.
    - **Serialization**: for JSON serialization.
    - **Logging**: for logging HTTP requests.
    - **Content Negotiation**: for handling content types.

### Media Playback
- **ExoPlayer**: for media playback.
    - **UI**: for media controls.
    - **DASH/HLS**: [ExoPlayer DASH](https://exoplayer.dev/dash.html), [HLS](https://exoplayer.dev/hls.html) support.

## Setup Instructions
1. **Clone the Repository**: Use `git clone` to clone the project repository.
2. **Build the Project**: Use Gradle to sync and build the project.
3. **Run the Application**: Select the desired build variant (TV or Mobile) and run the app on an emulator or physical device.
