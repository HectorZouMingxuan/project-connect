# Project Connect

Project Connect is a student collaboration Android application for discovering projects, creating teams, managing student profiles, and chatting with project members. The app is built with Kotlin, Jetpack Compose, Firebase Authentication, and Cloud Firestore.

The main goal of the app is to make it easier for students to find teammates based on project needs, skills, and team availability.

---

## Main Features

### 1. User Registration and Login

- Register with name, email, password, and password confirmation.
- Log in with Firebase Email/Password Authentication.
- Show loading states while authentication requests are running.
- Display validation and Firebase error messages.
- Log out from the project list or profile page.
- Automatically connect each user account to a Firestore profile document using the Firebase `uid`.

### 2. Student Profile Management

- View the current user's profile.
- Edit username, bio, faculty, year, and skills.
- Store profile data in the Firestore `users` collection.
- Load an existing profile after login.
- Create a default profile automatically if a signed-in user does not have one yet.
- Save skills as a list by converting comma-separated input into structured data.

### 3. Project Discovery

- Display all available student projects in a scrollable project list.
- Show project title, owner name, required skills, member count, team size, and current status.
- Status labels are calculated in the UI:
  - `Owner`
  - `Joined`
  - `Full`
  - `Open`
- Use real-time Firestore listeners so project changes appear automatically.

### 4. Project Creation

- Create a new project with:
  - project title
  - description
  - required skills
  - team size
- Validate that all required fields are filled before publishing.
- Save new projects to the Firestore `projects` collection.
- Automatically set the logged-in user as the project owner.
- Automatically add the owner to the project member list.

### 5. Join and Quit Project

- Join open projects when the user is not the owner, not already a member, and the team is not full.
- Quit a project after joining.
- Prevent project owners from quitting their own project.
- Update Firestore member lists when users join or quit.
- Disable the join button when the user cannot join.

### 6. Project Detail Page

- View full project information:
  - title
  - owner
  - description
  - required skills
  - current member count
  - team size
- Show different actions depending on user role:
  - owners can delete the project
  - joined members can open team chat
  - joined non-owners can quit
  - non-members can join if space is available
- Confirm project deletion with an alert dialog.

### 7. Team Chat

- Open a team chat from the project detail page.
- Restrict chat access to project members only.
- Send messages to a Firestore `messages` subcollection under each project.
- Listen for chat updates in real time.
- Sort messages by creation time.
- Show different chat bubble styling for the current user and other members.
- Auto-scroll to the newest message.
- Prevent empty messages from being sent.

### 8. App Navigation

- Use a simple screen-state navigation flow with the `AppScreen` enum.
- Supported screens:
  - Login
  - Register
  - Project List
  - Project Detail
  - Team Chat
  - Create Project
  - Profile
  - Edit Profile
- Keep selected project, current user, profile, project list, and chat messages in Compose state.

### 9. Custom User Interface

- Built fully with Jetpack Compose.
- Uses Material 3 components such as cards, text fields, dialogs, buttons, and typography.
- Uses a custom dark cyberpunk-inspired theme.
- Includes reusable button components:
  - `PrimaryActionButton`
  - `SecondaryActionButton`
  - `DangerActionButton`
  - `QuietActionButton`
- Includes a reusable `PunkTitle` composable with layered text, color offsets, and shadow effects.
- Uses edge-to-edge layout, status bar padding, keyboard padding, and LazyColumn lists.

---

## Techniques Used

### Android Development

- Kotlin programming language
- Android Studio project structure
- Jetpack Compose declarative UI
- Material 3 design components
- Compose state management with `remember`, `mutableStateOf`, and `LaunchedEffect`
- Lifecycle-aware listener cleanup with `DisposableEffect`
- Scrollable lists using `LazyColumn`
- Keyboard-aware layout using `imePadding`
- Edge-to-edge layout support

### Firebase Integration

- Firebase Authentication for email/password login and registration
- Cloud Firestore for user profiles, projects, and chat messages
- Firestore collections:
  - `users`
  - `projects`
  - `projects/{projectId}/messages`
- Firestore document mapping to Kotlin data classes
- Snapshot listeners for real-time project and chat updates
- Firestore subcollections for project-specific chat messages
- Firebase `uid` used as the main user identity

### App Architecture

- Data models for structured app data:
  - `UserProfile`
  - `Project`
  - `ChatMessage`
- Repository classes to separate Firebase logic from UI logic:
  - `AuthRepository`
  - `UserRepository`
  - `ProjectRepository`
  - `ChatRepository`
- `AuthViewModel` for authentication form state, validation, loading state, and error handling.
- Screen-based navigation controlled from `MainActivity`.
- Callback-based communication between screens and repositories.

### Data Handling

- Form validation before submitting login, registration, project, and chat data.
- Comma-separated text parsing for skills.
- Member list updates using distinct user ids.
- Defensive checks for blank ids, blank messages, missing login state, and unauthorized chat access.
- Timestamp-based message ordering.

---

## Tech Stack

| Category | Technology |
| --- | --- |
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Design System | Material 3 |
| Authentication | Firebase Authentication |
| Database | Cloud Firestore |
| Build Tool | Gradle Kotlin DSL |
| Minimum SDK | 24 |
| Target SDK | 36 |
| IDE | Android Studio |

---

## Screenshots and Photos to Insert

Place all images inside the `screenshots/` folder and link them from this README.

### Required App Screenshots

| Section | File Name | What Photo/Screenshot Should Be Inserted |
| --- | --- | --- |
| Login | `screenshots/login.png` | Screenshot of the login screen showing the Project Connect title, email field, password field, login button, and create account link. |
| Register | `screenshots/register.png` | Screenshot of the registration screen showing name, email, password, confirm password, and register button. |
| Project List | `screenshots/project-list.png` | Screenshot of the main project list with several project cards and visible project statuses such as Open, Joined, Full, or Owner. |
| Project Detail | `screenshots/project-detail.png` | Screenshot of one project detail page showing title, owner, description, required skills, members, and available action buttons. |
| Create Project | `screenshots/create-project.png` | Screenshot of the create project form with title, description, required skills, team size, and publish button. |
| Team Chat | `screenshots/team-chat.png` | Screenshot of a project team chat showing at least two messages, the message input field, and the send button. |
| Profile | `screenshots/profile.png` | Screenshot of the profile page showing username, email, bio, faculty, year, and skills. |
| Edit Profile | `screenshots/edit-profile.png` | Screenshot of the edit profile form with editable profile fields and save button. |
| Delete Confirmation | `screenshots/delete-project-dialog.png` | Screenshot of the delete project confirmation dialog. |

### Required Firebase Console Screenshots

| Section | File Name | What Photo/Screenshot Should Be Inserted |
| --- | --- | --- |
| Firebase Authentication | `screenshots/firebase-auth-users.png` | Screenshot of Firebase Authentication showing registered test users. Hide or blur sensitive emails if needed. |
| Firestore Users | `screenshots/firestore-data-user.png` | Screenshot of the Firestore `users` collection showing a user profile document with fields such as username, email, faculty, year, bio, and skills. |
| Firestore Projects | `screenshots/firestore-data-project.png` | Screenshot of the Firestore `projects` collection showing project fields such as title, ownerId, ownerName, requiredSkills, teamSize, and memberIds. |
| Firestore Chat Messages | `screenshots/firestore-chat-messages.png` | Screenshot of a `projects/{projectId}/messages` subcollection showing messageId, senderId, senderName, text, and createdAt. |

### Current Screenshot Links

Some screenshots already exist in the project. Add the missing images listed above when available.

#### Project List

![Project List](screenshots/project-list.png)

#### Project Detail

![Project Detail](screenshots/project-detail.png)

#### Create Project

![Create Project](screenshots/create-project.png)

#### Profile

![Profile](screenshots/profile.png)

#### Edit Profile

![Edit Profile](screenshots/edit-profile.png)

#### Firestore User Data

![Firestore User Data](screenshots/firestore-data-user.png)

#### Firestore Project Data

![Firestore Project Data](screenshots/firestore-data-project.png)

---

## Firebase Setup

1. Create a Firebase project.
2. Add an Android app using this package name:

```text
com.example.projectconnect
```

3. Download `google-services.json`.
4. Place `google-services.json` inside the `app/` folder.
5. Enable Firebase Authentication.
6. Enable the Email/Password sign-in provider.
7. Enable Cloud Firestore.
8. Create or allow access to these Firestore paths:
   - `users/{uid}`
   - `projects/{projectId}`
   - `projects/{projectId}/messages/{messageId}`

Important: `google-services.json` contains project-specific Firebase configuration. Do not commit a private production Firebase configuration to a public repository.

---

## How to Run

1. Clone or download the project.
2. Open the folder in Android Studio.
3. Add your Firebase `google-services.json` file to the `app/` folder.
4. Sync Gradle.
5. Run the app on an Android emulator or physical Android device.
6. Register a new account.
7. Create, join, quit, delete, and chat inside projects to test the full flow.

---

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

## Example Test Flow

1. Register a new account with name, email, and password.
2. Confirm the new account appears in Firebase Authentication.
3. Confirm a matching profile document appears in `users/{uid}`.
4. Edit the profile and confirm the Firestore profile document updates.
5. Create a project and confirm it appears in the project list.
6. Confirm the new project appears in the Firestore `projects` collection.
7. Log in as another user and join the project.
8. Confirm the project `memberIds` field updates in Firestore.
9. Open team chat and send messages as project members.
10. Confirm messages appear in `projects/{projectId}/messages`.
11. Quit the project as a non-owner.
12. Delete the project as the owner.

---

## Future Improvements

- Add search and filtering by skills, faculty, or project status.
- Add project editing for project owners.
- Add profile photos or avatars.
- Add push notifications for chat messages.
- Add stronger Firestore security rules.
- Add Compose UI tests for login, project creation, and chat flows.
---

## Author

Developed as an Android mobile application project using Kotlin, Jetpack Compose, Firebase Authentication, and Cloud Firestore.
