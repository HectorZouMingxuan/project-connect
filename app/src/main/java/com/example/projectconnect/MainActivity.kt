package com.example.projectconnect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projectconnect.data.model.Project
import com.example.projectconnect.data.model.UserProfile
import com.example.projectconnect.data.repository.ProjectRepository
import com.example.projectconnect.data.repository.UserRepository
import com.example.projectconnect.navigation.AppScreen
import com.example.projectconnect.ui.screen.CreateProjectScreen
import com.example.projectconnect.ui.screen.EditProfileScreen
import com.example.projectconnect.ui.screen.ProfileScreen
import com.example.projectconnect.ui.screen.ProjectDetailScreen
import com.example.projectconnect.ui.screen.ProjectListScreen
import com.example.projectconnect.ui.theme.ProjectConnectTheme
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProjectConnectTheme {
                AppEntry()
            }
        }
    }
}

@Composable
fun AppEntry() {
    val userRepository = remember { UserRepository() }
    val projectRepository = remember { ProjectRepository() }

    var currentScreen by remember {
        mutableStateOf(AppScreen.PROJECT_LIST)
    }

    var fakeUsers by remember {
        mutableStateOf(
            listOf(
                UserProfile(
                    userId = "user_001",
                    username = "Hector",
                    bio = "Software Engineering student looking for project teammates",
                    faculty = "FSKTM",
                    year = "Year 2",
                    skills = listOf("Kotlin", "Java", "Firebase")
                ),
                UserProfile(
                    userId = "user_002",
                    username = "Adam",
                    bio = "UI-focused student interested in app projects",
                    faculty = "FSKTM",
                    year = "Year 2",
                    skills = listOf("UI Design", "Figma", "Testing")
                )
            )
        )
    }

    var currentUserIndex by remember {
        mutableIntStateOf(0)
    }

    var userProfile by remember {
        mutableStateOf(fakeUsers[currentUserIndex])
    }

    var projects by remember {
        mutableStateOf<List<Project>>(emptyList())
    }

    var selectedProject by remember {
        mutableStateOf<Project?>(null)
    }

    DisposableEffect(Unit) {
        val registration: ListenerRegistration = projectRepository.listenToProjects(
            onResult = { loadedProjects ->
                projects = loadedProjects
                selectedProject = selectedProject?.let { current ->
                    loadedProjects.find { it.projectId == current.projectId }
                }
            },
            onError = { errorMessage ->
                Log.e("Firestore", errorMessage)
            }
        )

        onDispose {
            registration.remove()
        }
    }

    LaunchedEffect(currentUserIndex) {
        val requestedProfile = fakeUsers[currentUserIndex]
        val requestedUserId = requestedProfile.userId

        userRepository.loadOrCreateUserProfile(
            defaultProfile = requestedProfile,
            onResult = { loadedProfile ->
                if (loadedProfile.userId == requestedUserId) {
                    userProfile = loadedProfile
                }
            },
            onError = { errorMessage ->
                Log.e("Firestore", errorMessage)
                userProfile = requestedProfile
            }
        )
    }

    when (currentScreen) {
        AppScreen.PROJECT_LIST -> {
            ProjectListScreen(
                projects = projects,
                currentUserId = userProfile.userId,
                currentUsername = userProfile.username,
                onSelectProject = { project ->
                    selectedProject = project
                    currentScreen = AppScreen.PROJECT_DETAIL
                },
                onGoToCreateProject = {
                    currentScreen = AppScreen.CREATE_PROJECT
                },
                onGoToProfile = {
                    currentScreen = AppScreen.PROFILE
                },
                onSwitchUser = {
                    currentUserIndex = if (currentUserIndex == 0) 1 else 0
                }
            )
        }

        AppScreen.PROJECT_DETAIL -> {
            selectedProject?.let { project ->
                ProjectDetailScreen(
                    project = project,
                    currentUserId = userProfile.userId,
                    onJoinProject = {
                        projectRepository.joinProject(
                            project = project,
                            userId = userProfile.userId,
                            onSuccess = { updatedProject ->
                                selectedProject = updatedProject
                            },
                            onError = { errorMessage ->
                                Log.e("Firestore", errorMessage)
                            }
                        )
                    },
                    onDeleteProject = {
                        projectRepository.deleteProject(
                            projectId = project.projectId,
                            onSuccess = {
                                selectedProject = null
                                currentScreen = AppScreen.PROJECT_LIST
                            },
                            onError = { errorMessage ->
                                Log.e("Firestore", errorMessage)
                            }
                        )
                    },
                    onBack = {
                        currentScreen = AppScreen.PROJECT_LIST
                    }
                )
            }
        }

        AppScreen.CREATE_PROJECT -> {
            CreateProjectScreen(
                onCreateProject = { title, description, requiredSkills, teamSize ->
                    val newProject = Project(
                        projectId = "project_${System.currentTimeMillis()}",
                        title = title,
                        description = description,
                        ownerId = userProfile.userId,
                        ownerName = userProfile.username,
                        requiredSkills = requiredSkills,
                        teamSize = teamSize,
                        memberIds = listOf(userProfile.userId)
                    )

                    projectRepository.createProject(
                        project = newProject,
                        onSuccess = {
                            currentScreen = AppScreen.PROJECT_LIST
                        },
                        onError = { errorMessage ->
                            Log.e("Firestore", errorMessage)
                        }
                    )
                },
                onBack = {
                    currentScreen = AppScreen.PROJECT_LIST
                }
            )
        }

        AppScreen.PROFILE -> {
            ProfileScreen(
                userProfile = userProfile,
                onGoToEditProfile = {
                    currentScreen = AppScreen.EDIT_PROFILE
                },
                onBack = {
                    currentScreen = AppScreen.PROJECT_LIST
                }
            )
        }

        AppScreen.EDIT_PROFILE -> {
            EditProfileScreen(
                userProfile = userProfile,
                onSave = { updatedProfile ->
                    userProfile = updatedProfile

                    fakeUsers = fakeUsers.mapIndexed { index, profile ->
                        if (index == currentUserIndex) updatedProfile else profile
                    }

                    userRepository.saveUserProfile(
                        profile = updatedProfile,
                        onSuccess = {
                            Log.d("Firestore", "Profile saved successfully.")
                        },
                        onError = { errorMessage ->
                            Log.e("Firestore", errorMessage)
                        }
                    )

                    currentScreen = AppScreen.PROFILE
                },
                onBack = {
                    currentScreen = AppScreen.PROFILE
                }
            )
        }
    }
}