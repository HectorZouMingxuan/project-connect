# Project Connect

Project Connect is an Android mobile application that helps students discover projects, create teams, manage their personal profiles, and communicate with teammates through a project-based chat system.

The app is built with Kotlin, Jetpack Compose, Firebase Authentication, and Cloud Firestore. It focuses on student collaboration by allowing users to find suitable projects based on project descriptions, required skills, and available team spaces.

---

## Features

### 1. User Registration

Users can create a new account by entering their name, email address, password, and password confirmation.

The registration feature uses Firebase Authentication to create the user account. After registration, the app also creates a user profile document in Firestore using the Firebase user ID.

<img src="https://github.com/user-attachments/assets/d279efd2-2421-4889-89e9-725e354b4ac6" alt="Register Screen" width="280" />


---

### 2. User Login

Registered users can log in using their email and password.

The login screen includes validation, loading state handling, and error message display. After a successful login, the user is redirected to the project homepage.

<img src="https://github.com/user-attachments/assets/0398ef30-ac92-4e72-b0a9-7becd025125f" alt="Login Screen" width="280" />



---

### 3. Homepage and Project List

After logging in, users can view the homepage that displays all available student projects.

Each project card shows:

- Project title
- Project owner
- Required skills
- Current member count
- Maximum team size
- Project status

Project status can be:

- `Open`
- `Joined`
- `Owner`
- `Full`

The homepage also provides buttons for creating a project, opening the profile page, and logging out.

<img src="https://github.com/user-attachments/assets/cac3d87c-600b-463f-857d-fd9240fbdb21" alt="Homepage and Project List" width="280" />


---

### 4. Create Project

Users can create a new project by filling in a project form.

The project creation form includes:

- Project title
- Project description
- Required skills
- Team size

The app validates the form before allowing the project to be published. When a project is created, it is saved in Cloud Firestore and the current user is automatically added as the project owner and first member.

<img src="https://github.com/user-attachments/assets/30b3650d-8945-472a-a851-73e6bc78b7a3" alt="Create Project Screen" width="280" />


---

### 5. Project Detail

Users can tap a project card to view the project detail page.

The project detail page displays:

- Project title
- Owner name
- Project description
- Required skills
- Current members
- Maximum team size

The available buttons depend on the user's role:

- Project owner can delete the project.
- Project members can open the team chat.
- Non-members can join the project if it is not full.
- Joined non-owner users can quit the project.

<img src="https://github.com/user-attachments/assets/8b9d064c-4791-4fc1-8dc5-3da0d7d2f546" alt="Project Detail Screen" width="280" />


---

### 6. Join, Quit, and Delete Project

Project Connect supports project membership management.

Users can join a project if:

- They are logged in.
- They are not the project owner.
- They have not already joined the project.
- The project has available team space.

Users can quit a project if they are a member but not the owner. Project owners cannot quit their own project, but they can delete it.

When users join, quit, or delete a project, the data is updated in Firestore.

<img src="https://github.com/user-attachments/assets/3e44110f-1ce0-415b-9b1e-88c3b8293305" alt="Join Quit Delete Project Screen" width="280" />



---

### 7. Team Chat

Project members can open a team chat from the project detail page.

The chat feature allows users to:

- Send messages to project teammates
- View messages in real time
- See their own messages aligned differently from other users' messages
- View sender names and message times
- Return to the project detail page

Only users who are members of the project can access the team chat. Messages are stored in a Firestore subcollection under the selected project.

<img src="https://github.com/user-attachments/assets/71b17208-90fa-4578-aecc-b186a013daad" alt="Team Chat Screen" width="280" />


---

### 8. Profile Page

Each user has a personal profile page.

The profile page displays:

- Username
- Email
- Bio
- Faculty
- Year
- Skills

This allows users to show their background and technical skills to other students.

<img src="https://github.com/user-attachments/assets/1d44dd8e-c323-4284-b299-17940fb870af" alt="Profile Page" width="280" />


---

### 9. Edit Profile

Users can update their profile information from the Edit Profile page.

Editable profile fields include:

- Username
- Bio
- Faculty
- Year
- Skills

Skills are entered as comma-separated text and converted into a list before being saved to Firestore.

<img src="https://github.com/user-attachments/assets/be8c4b83-d16f-4d2c-a872-75791af0fa1f" alt="Edit Profile Screen" width="280" />


---

## Techniques Used

### Kotlin

Kotlin is used as the main programming language for the Android application. It is used to build the app logic, data models, repositories, ViewModel, and UI behavior.

### Jetpack Compose

Jetpack Compose is used to build the user interface with a declarative UI approach.

Compose techniques used in this project include:

- `@Composable` functions
- `remember`
- `mutableStateOf`
- `LaunchedEffect`
- `DisposableEffect`
- `LazyColumn`
- `OutlinedTextField`
- `Card`
- `AlertDialog`
- Material 3 buttons and typography

### Firebase Authentication

Firebase Authentication is used for account management.

It handles:

- User registration
- User login
- User logout
- Current user ID
- Current user email

The Firebase user ID is used to connect each account to its own Firestore profile document.

### Cloud Firestore

Cloud Firestore is used as the backend database.

The app stores:

- User profile data
- Project data
- Project member IDs
- Team chat messages

Firestore data paths used:

```text
users/{uid}
projects/{projectId}
projects/{projectId}/messages/{messageId}
```

### Real-Time Updates

Firestore snapshot listeners are used to update data in real time.

Real-time updates are used for:

- Project list changes
- Project membership changes
- Team chat messages

When Firestore data changes, the UI updates automatically.

### Repository Pattern

Firebase operations are separated into repository classes.

Repositories used:

```text
AuthRepository
UserRepository
ProjectRepository
ChatRepository
```

This keeps Firebase logic separate from UI code and makes the project easier to maintain.

### ViewModel State Management

`AuthViewModel` manages the authentication screen state.

It controls:

- Login email
- Login password
- Register name
- Register email
- Register password
- Confirm password
- Loading state
- Error messages
- Login success state
- Registration success state

### Screen-Based Navigation

The app uses an enum called `AppScreen` to manage navigation between screens.

Screens include:

```text
LOGIN
REGISTER
PROJECT_LIST
PROJECT_DETAIL
TEAM_CHAT
CREATE_PROJECT
PROFILE
EDIT_PROFILE
```

### Data Models

The app uses Kotlin data classes to represent structured data.

Data models used:

```text
UserProfile
Project
ChatMessage
```

These models are mapped to Firestore documents.

### Custom UI Theme

The app uses a custom dark neon theme.

UI design techniques include:

- Dark gradient background
- Neon accent colors
- Material 3 color scheme
- Custom reusable buttons
- Glitch-style title text
- Rounded cards
- Different button styles for primary, secondary, quiet, and danger actions

Reusable UI components:

```text
PrimaryActionButton
SecondaryActionButton
DangerActionButton
QuietActionButton
PunkTitle
```

---

## Tech Stack

| Category | Technology |
| --- | --- |
| Programming Language | Kotlin |
| UI Framework | Jetpack Compose |
| UI Components | Material 3 |
| Authentication | Firebase Authentication |
| Database | Cloud Firestore |
| Architecture Style | Repository Pattern with ViewModel state |
| IDE | Android Studio |
| Build Tool | Gradle Kotlin 
## Project Structure

```text
app/src/main/java/com/example/projectconnect
|-- MainActivity.kt
|-- data
|   |-- model
|   |   |-- ChatMessage.kt
|   |   |-- Project.kt
|   |   `-- UserProfile.kt
|   `-- repository
|       |-- AuthRepository.kt
|       |-- ChatRepository.kt
|       |-- ProjectRepository.kt
|       `-- UserRepository.kt
|-- navigation
|   `-- AppScreen.kt
|-- ui
|   |-- component
|   |   |-- AppButton.kt
|   |   `-- PunkTitle.kt
|   |-- screen
|   |   |-- CreateProjectScreen.kt
|   |   |-- EditProfileScreen.kt
|   |   |-- ProfileScreen.kt
|   |   |-- ProjectDetailScreen.kt
|   |   |-- ProjectListScreen.kt
|   |   |-- TeamChatScreen.kt
|   |   `-- auth
|   |       |-- LoginScreen.kt
|   |       `-- RegisterScreen.kt
|   |-- theme
|   |   |-- Color.kt
|   |   |-- Theme.kt
|   |   `-- Type.kt
|   `-- viewmodel
|       `-- AuthViewModel.kt
```

---

## Firebase Setup

To run the application, Firebase must be configured.

Steps:

1. Create a Firebase project.
2. Add an Android app to the Firebase project.
3. Use the package name:

```text
com.example.projectconnect
```

4. Download the `google-services.json` file.
5. Place `google-services.json` inside the `app` folder.
6. Enable Firebase Authentication.
7. Enable Email/Password sign-in.
8. Enable Cloud Firestore.

---

## How to Run

1. Open the project in Android Studio.
2. Add `google-services.json` into the `app` folder.
3. Sync the Gradle project.
4. Run the app on an Android emulator or Android device.
5. Register a new account.
6. Log in and test the project features.

---

## Testing Flow

1. Register a new user account.
2. Log in with the registered account.
3. Edit the user profile.
4. Create a new project.
5. View the project on the homepage.
6. Open the project detail page.
7. Join or manage the project.
8. Open the team chat.
9. Send chat messages.
10. Check Firestore to confirm that users, projects, and messages are saved.

---

## Conclusion

Project Connect demonstrates a complete Android student collaboration app using modern Android development techniques. It combines Jetpack Compose UI, Firebase Authentication, Cloud Firestore, real-time updates, profile management, project management, and team chat into one mobile application.

---

## Author

Developed as an Android mobile application project using Kotlin, Jetpack Compose, Firebase Authentication, and Cloud Firestore.
