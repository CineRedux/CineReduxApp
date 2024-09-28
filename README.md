

# CineRedux

CineRedux is an Android application designed to provide users with a comprehensive movie database experience. It allows users to explore trending movies, add them to a watchlist, view detailed information, and manage their profiles seamlessly. The app integrates with external movie databases like TMDB and Rotten Tomatoes to fetch real-time movie data and offers a smooth user interface using Kotlin and Android architecture components.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Usage](#usage)
- [App Architecture](#app-architecture)
- [Database Schema](#database-schema)
- [Contributing](#contributing)
- [Programmers/Members](#Programmers/Creators)

## Features

- **User Authentication**: Sign in with Google for a personalized experience.
- **Movie Database**: Browse and search for movies using real-time data from TMDB and Rotten Tomatoes.
- **Watchlist Management**: Add, view, and remove movies from a personal watchlist.
- **Movie Details View**: Click on any movie to see detailed information, including the trailer.
- **Responsive UI**: Modern design with intuitive navigation.
- **Settings**: Edit and delete user profiles.

## Technologies Used

- **Kotlin**: The primary programming language for Android development.
- **Android Studio**: IDE for building the app.
- **Glide**: Image loading library for handling movie posters.
- **Retrofit**: Networking library for API calls to TMDB and Rotten Tomatoes.
- **SQLite**: Database for storing user watchlists and profiles.
- **Material Design**: For a clean and modern UI/UX.

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/yourusername/cineredux.git
   cd cineredux
   ```

2. Open the project in Android Studio.

3. Update the `google-services.json` file with your Firebase configuration. You can obtain this file from the Firebase console.

4. Sync your project with Gradle files to download the necessary dependencies.

5. Build and run the application on an Android device or emulator.

## Usage

- **Sign In**: Launch the app and sign in using your Google account.
- **Home Screen**: Browse trending movies and search for specific titles.
- **Watchlist**: Add movies to your watchlist by clicking the "Add to Watchlist" button.
- **View Movie Details**: Click on any movie to view more information, including trailers.
- **Settings**: Access settings to edit or delete your profile.

## App Architecture

CineRedux follows the MVVM (Model-View-ViewModel) architecture pattern, which promotes a clean separation of concerns and improves code maintainability. 

- **Model**: Represents the data structure, including the `Movie` data class and `WatchlistDatabaseHelper` for managing SQLite operations.
  
  ```kotlin
  data class Movie(
      val id: Int,
      val title: String?,
      val overview: String?,
      val poster: String?,
      val tomatometer: String?,
      val trailer: String?
  )
  ```

- **View**: The XML layouts and fragments that define the user interface. For example, `fragment_watchlist.xml` for the watchlist screen.

- **ViewModel**: Manages the UI-related data and interacts with the Model to fetch or update data. The ViewModel retains the data during configuration changes.

## Database Schema

The app uses an SQLite database to store user watchlist data. The database schema includes the following fields:

### Movies Table (`movies`)

| Column Name     | Data Type | Description                       |
|------------------|-----------|-----------------------------------|
| id               | INTEGER   | Primary key, auto-incremented ID. |
| title            | TEXT      | Movie title.                      |
| overview         | TEXT      | Movie overview.                   |
| poster_url       | TEXT      | URL of the movie poster image.    |
| tmdbScore        | TEXT      | Tomatometer score.                |
| trailer_url      | TEXT      | URL for the movie trailer.        |

## Contributing

Contributions are welcome! If you have suggestions for improvements or want to report bugs, please open an issue or submit a pull request.

### Steps to Contribute

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/YourFeature`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature/YourFeature`.
5. Open a pull request.

## Programmers/Creators
This project was developed by the following group members:

Ted Ngobeni - ST10027949.
Tshiamo Thekiso – ST10132516.
Motjoka Fanana – ST10089515.
