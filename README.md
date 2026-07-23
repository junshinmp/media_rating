# Media Rater

A full-stack web application designed for browsing movies and media, submitting user reviews, and managing user profiles. Built with a **Spring Boot** backend integrated with **TMDB (The Movie Database) API** and a **React + Vite** frontend.

---

## Tech Stack

* **Frontend:** React, Vite, JavaScript (JSX), CSS
* **Backend:** Java, Spring Boot (REST API), Spring Data JPA, Maven
* **Database:** SQL (configured via `schema.sql`)
* **External Services:** TMDB API (The Movie Database)

---

## Project Structure

```text
media_rater/
├── media_rater_backend/          # Java Spring Boot Service
│   ├── src/main/java/com/db_connector/rating/backend/
│   │   ├── controller/           # REST API Endpoints (Rating, User, TMDB)
│   │   ├── model/                # JPA Entities (AppUser, Rating)
│   │   ├── repositories/         # Spring Data JPA Interfaces
│   │   ├── service/              # Business Logic & External API Calls
│   │   └── interaction/          # CLI Utilities & Launchers
│   └── src/main/resources/       # App Properties & Database Schema
│
└── media_rating_frontend/        # React + Vite Client Application
    ├── src/
    │   ├── Components/           # Header, MediaInfo, ReviewForm, WelcomeDash
    │   └── Pages/                # ReviewPage, SearchMediaPage, UserPage, UserSettings
    └── vite.config.js            # Vite Configuration
```

---

## Features

* **User Management:** Create accounts, authenticate (login), view profile stats, update passwords, and delete accounts.
* **Media Search:** Search movies/media dynamically by title or TMDB ID via backend TMDB integration.
* **Ratings & Reviews:**
  * Post star ratings and comments for specific media items.
  * Update or delete existing ratings.
  * Fetch media-specific average ratings and user-specific rating histories.

---

## API Reference (Backend)

### **1. User Endpoints (`/`)**

| Method | Endpoint | Description | Parameters / Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/createUser` | Register a new user | Body: `AppUser` object |
| `POST` | `/login` | Authenticate a user | Body: `{ username, password }` |
| `GET` | `/userById` | Fetch user details by ID | Query: `userId` |
| `GET` | `/findUsers` | Fetch user details by username | Query: `username` |
| `GET` | `/checkUsername` | Check if username exists | Query: `username` |
| `GET` | `/allUsers` | Get list of all users | *None* |
| `PUT` | `/changePassword` | Change user password | Query: `userID`, `currentPass`, `newPass` |
| `DELETE` | `/deleteUser` | Delete user account | Query: `username` |

### **2. Rating Endpoints (`/`)**

| Method | Endpoint | Description | Parameters / Body |
| :--- | :--- | :--- | :--- |
| `POST` | `/createRating` | Create a new rating/review | Body: `Rating` object |
| `GET` | `/ratingId` | Get rating details by ID | Query: `ratingId` |
| `GET` | `/ratingUserId` | Get all ratings by a user | Query: `userId` |
| `GET` | `/ratingsMediaId` | Get all ratings for a media | Query: `mediaId` |
| `GET` | `/ratingUserAndMedia` | Check user's rating for specific media | Query: `mediaId`, `userId` |
| `GET` | `/averageRating` | Get aggregate average score for media | Query: `mediaId` |
| `PUT` | `/changeRating` | Edit an existing rating | Query: `ratingId`, `stars`, `comments` |
| `DELETE` | `/deleteRating` | Delete rating by ID | Query: `ratingId` |
| `DELETE` | `/deleteRatingByUser` | Bulk delete ratings by user ID | Query: `userId` |

### **3. TMDB Endpoints (`/`)**

| Method | Endpoint | Description | Parameters |
| :--- | :--- | :--- | :--- |
| `GET` | `/search` | Search TMDB catalog by title | Query: `title` |
| `GET` | `/movieFromId` | Get detailed media information by ID | Query: `id` |

---

## Setup & Installation

### **Prerequisites**
* JDK 17+ installed
* Node.js & npm installed
* Maven installed

---

### **1. Backend Setup**

1. Navigate to the backend directory:
   ```bash
   cd media_rater_backend
   ```
2. Configure your environment variables in `.env` or standard `application.properties`:
   ```properties
   # Setup database connections and your TMDB API Key here
   TMDB_API_KEY=your_tmdb_api_key_here
   ```
3. Run the Spring Boot application:
   ```bash
   mvn spring-boot:run
   ```
   > Backend runs on `http://localhost:8080` by default.

---

### **2. Frontend Setup**

1. Navigate to the frontend directory:
   ```bash
   cd media_rating_frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the Vite development server:
   ```bash
   npm run dev
   ```
   > Frontend runs on `http://localhost:5173`.

---

## Status & Roadmap (Incomplete Project)

* [x] Basic CRUD REST API structure for Users and Ratings
* [x] External TMDB Service integration
* [x] Basic React UI Routing and Page Components
* [ ] Implement JWT / Spring Security for stateless session management
* [ ] Complete UI styling and error handling feedback on React components
* [ ] Write comprehensive unit and integration tests
