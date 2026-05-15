package com.example.projectconnect.data.repository

import com.example.projectconnect.data.model.Project
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ProjectRepository(
    private val authRepository: AuthRepository = AuthRepository()
) {

    private val db = FirebaseFirestore.getInstance()
    private val projectsCollection = db.collection("projects")

    fun listenToProjects(
        onResult: (List<Project>) -> Unit,
        onError: (String) -> Unit
    ): ListenerRegistration {
        return projectsCollection
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    onError(e.message ?: "Failed to listen to projects.")
                    return@addSnapshotListener
                }

                val projects = snapshots?.documents?.mapNotNull { document ->
                    document.toObject(Project::class.java)
                } ?: emptyList()

                onResult(projects)
            }
    }

    fun createProject(
        project: Project,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUserId = authRepository.getCurrentUserId()
        if (currentUserId == null) {
            onError("You must be logged in to create a project.")
            return
        }

        val projectToCreate = project.copy(
            ownerId = currentUserId,
            memberIds = (project.memberIds + currentUserId).distinct()
        )

        projectsCollection
            .document(projectToCreate.projectId)
            .set(projectToCreate)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to create project.")
            }
    }

    fun joinProject(
        project: Project,
        onSuccess: (Project) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUserId = authRepository.getCurrentUserId()
        if (currentUserId == null) {
            onError("You must be logged in to join a project.")
            return
        }

        val updatedProject = project.copy(
            memberIds = (project.memberIds + currentUserId).distinct()
        )

        projectsCollection
            .document(project.projectId)
            .set(updatedProject)
            .addOnSuccessListener {
                onSuccess(updatedProject)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to join project.")
            }
    }

    fun quitProject(
        project: Project,
        onSuccess: (Project) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUserId = authRepository.getCurrentUserId()
        if (currentUserId == null) {
            onError("You must be logged in to quit a project.")
            return
        }

        if (project.ownerId == currentUserId) {
            onError("Project owners cannot quit their own project.")
            return
        }

        val updatedProject = project.copy(
            memberIds = project.memberIds.filterNot { it == currentUserId }
        )

        projectsCollection
            .document(project.projectId)
            .set(updatedProject)
            .addOnSuccessListener {
                onSuccess(updatedProject)
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to quit project.")
            }
    }

    fun deleteProject(
        projectId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        projectsCollection
            .document(projectId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to delete project.")
            }
    }
}
