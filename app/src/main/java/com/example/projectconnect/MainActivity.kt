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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projectconnect.data.model.ChatMessage
import com.example.projectconnect.data.model.Project
import com.example.projectconnect.data.model.UserProfile
import com.example.projectconnect.data.repository.AuthRepository
import com.example.projectconnect.data.repository.ChatRepository
import com.example.projectconnect.data.repository.ProjectRepository
import com.example.projectconnect.data.repository.UserRepository
import com.example.projectconnect.navigation.AppScreen
import com.example.projectconnect.ui.screen.CreateProjectScreen
import com.example.projectconnect.ui.screen.EditProfileScreen
import com.example.projectconnect.ui.screen.ProfileScreen
import com.example.projectconnect.ui.screen.ProjectDetailScreen
import com.example.projectconnect.ui.screen.ProjectListScreen
import com.example.projectconnect.ui.screen.TeamChatScreen
import com.example.projectconnect.ui.screen.auth.LoginScreen
import com.example.projectconnect.ui.screen.auth.RegisterScreen
import com.example.projectconnect.ui.theme.ProjectConnectTheme
import com.example.projectconnect.ui.viewmodel.AuthViewModel
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
    val authRepository = remember { AuthRepository() }
    val authViewModel = remember { AuthViewModel() }
    val userRepository = remember { UserRepository() }
    val projectRepository = remember { ProjectRepository(authRepository) }
    val chatRepository = remember { ChatRepository() }

    var currentUserId by remember {
        mutableStateOf<String?>(null)
    }

    var currentScreen by remember {
        mutableStateOf(AppScreen.LOGIN)
    }

    var userProfile by remember {
        mutableStateOf<UserProfile?>(null)
    }

    var projects by remember {
        mutableStateOf<List<Project>>(emptyList())
    }

    var selectedProject by remember {
        mutableStateOf<Project?>(null)
    }

    var chatMessages by remember {
        mutableStateOf<List<ChatMessage>>(emptyList())
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

    DisposableEffect(currentScreen, selectedProject?.projectId) {
        val project = selectedProject
        if (currentScreen != AppScreen.TEAM_CHAT || project == null) {
            chatMessages = emptyList()
            onDispose {}
        } else {
            val registration: ListenerRegistration = chatRepository.listenToProjectMessages(
                projectId = project.projectId,
                onResult = { loadedMessages ->
                    chatMessages = loadedMessages
                },
                onError = { errorMessage ->
                    Log.e("Firestore", errorMessage)
                }
            )

            onDispose {
                registration.remove()
            }
        }
    }

    LaunchedEffect(currentUserId) {
        val requestedUserId = currentUserId
        if (requestedUserId == null) {
            userProfile = null
            return@LaunchedEffect
        }

        val currentUserEmail = authRepository.getCurrentUserEmail()
        val defaultProfile = UserProfile(
            userId = requestedUserId,
            email = currentUserEmail,
            username = currentUserEmail
                .substringBefore("@")
                .ifBlank { "Student" }
        )

        userRepository.loadOrCreateUserProfile(
            defaultProfile = defaultProfile,
            onResult = { loadedProfile ->
                if (loadedProfile.userId == requestedUserId) {
                    userProfile = loadedProfile.copy(
                        email = loadedProfile.email.ifBlank { currentUserEmail }
                    )
                }
            },
            onError = { errorMessage ->
                Log.e("Firestore", errorMessage)
                userProfile = defaultProfile
            }
        )
    }

    when (currentScreen) {
        AppScreen.LOGIN -> {
            LoginScreen(
                authViewModel = authViewModel,
                onGoToRegister = {
                    authViewModel.clearLoginForm()
                    currentScreen = AppScreen.REGISTER
                },
                onLoginSuccess = {
                    currentUserId = authRepository.getCurrentUserId()
                    authViewModel.clearAuthForms()
                    currentScreen = AppScreen.PROJECT_LIST
                }
            )
        }

        AppScreen.REGISTER -> {
            RegisterScreen(
                authViewModel = authViewModel,
                onGoToLogin = {
                    authViewModel.clearRegisterForm()
                    currentScreen = AppScreen.LOGIN
                },
                onRegisterSuccess = {
                    currentUserId = authRepository.getCurrentUserId()
                    authViewModel.clearAuthForms()
                    currentScreen = AppScreen.PROJECT_LIST
                }
            )
        }

        AppScreen.PROJECT_LIST -> {
            userProfile?.let { profile ->
                ProjectListScreen(
                    projects = projects,
                    currentUserId = profile.userId,
                    currentUsername = profile.username,
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
                    onLogout = {
                        authRepository.logout()
                        currentUserId = null
                        userProfile = null
                        selectedProject = null
                        authViewModel.clearAuthForms()
                        currentScreen = AppScreen.LOGIN
                    }
                )
            }
        }

        AppScreen.PROJECT_DETAIL -> {
            val profile = userProfile
            val project = selectedProject

            if (profile != null && project != null) {
                ProjectDetailScreen(
                    project = project,
                    currentUserId = profile.userId,
                    onJoinProject = {
                        projectRepository.joinProject(
                            project = project,
                            onSuccess = { updatedProject ->
                                selectedProject = updatedProject
                            },
                            onError = { errorMessage ->
                                Log.e("Firestore", errorMessage)
                            }
                        )
                    },
                    onQuitProject = {
                        projectRepository.quitProject(
                            project = project,
                            onSuccess = { updatedProject ->
                                selectedProject = updatedProject
                            },
                            onError = { errorMessage ->
                                Log.e("Firestore", errorMessage)
                            }
                        )
                    },
                    onOpenTeamChat = {
                        currentScreen = AppScreen.TEAM_CHAT
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

        AppScreen.TEAM_CHAT -> {
            val profile = userProfile
            val project = selectedProject

            if (profile != null && project != null) {
                val canUseChat = profile.userId in project.memberIds
                if (canUseChat) {
                    TeamChatScreen(
                        project = project,
                        messages = chatMessages,
                        currentUserId = profile.userId,
                        onSendMessage = { messageText, onMessageSent ->
                            chatRepository.sendMessage(
                                projectId = project.projectId,
                                senderId = profile.userId,
                                senderName = profile.username,
                                memberIds = project.memberIds,
                                text = messageText,
                                onSuccess = {
                                    onMessageSent()
                                    Log.d("Firestore", "Message sent successfully.")
                                },
                                onError = { errorMessage ->
                                    Log.e("Firestore", errorMessage)
                                }
                            )
                        },
                        onBack = {
                            currentScreen = AppScreen.PROJECT_DETAIL
                        }
                    )
                } else {
                    LaunchedEffect(project.projectId, profile.userId) {
                        currentScreen = AppScreen.PROJECT_DETAIL
                    }
                }
            }
        }

        AppScreen.CREATE_PROJECT -> {
            userProfile?.let { profile ->
                CreateProjectScreen(
                    onCreateProject = { title, description, requiredSkills, teamSize ->
                        val newProject = Project(
                            projectId = "project_${System.currentTimeMillis()}",
                            title = title,
                            description = description,
                            ownerName = profile.username,
                            requiredSkills = requiredSkills,
                            teamSize = teamSize
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
        }

        AppScreen.PROFILE -> {
            userProfile?.let { profile ->
                ProfileScreen(
                    userProfile = profile,
                    onGoToEditProfile = {
                        currentScreen = AppScreen.EDIT_PROFILE
                    },
                    onLogout = {
                        authRepository.logout()
                        currentUserId = null
                        userProfile = null
                        selectedProject = null
                        authViewModel.clearAuthForms()
                        currentScreen = AppScreen.LOGIN
                    },
                    onBack = {
                        currentScreen = AppScreen.PROJECT_LIST
                    }
                )
            }
        }

        AppScreen.EDIT_PROFILE -> {
            userProfile?.let { profile ->
                EditProfileScreen(
                    userProfile = profile,
                    onSave = { updatedProfile ->
                        val uid = authRepository.getCurrentUserId()
                        if (uid == null) {
                            Log.e("Auth", "No logged-in user found.")
                            userProfile = null
                            currentScreen = AppScreen.LOGIN
                        } else {
                            val profileToSave = updatedProfile.copy(
                                userId = uid,
                                email = updatedProfile.email.ifBlank {
                                    profile.email.ifBlank { authRepository.getCurrentUserEmail() }
                                }
                            )

                            userProfile = profileToSave

                            userRepository.saveUserProfile(
                                profile = profileToSave,
                                onSuccess = {
                                    Log.d("Firestore", "Profile saved successfully.")
                                },
                                onError = { errorMessage ->
                                    Log.e("Firestore", errorMessage)
                                }
                            )

                            currentScreen = AppScreen.PROFILE
                        }
                    },
                    onBack = {
                        currentScreen = AppScreen.PROFILE
                    }
                )
            }
        }
    }
}
