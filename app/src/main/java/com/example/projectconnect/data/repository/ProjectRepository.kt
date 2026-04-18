package com.example.projectconnect.data.repository

import com.example.projectconnect.data.model.Project
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ProjectRepository {

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
        projectsCollection
            .document(project.projectId)
            .set(project)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to create project.")
            }
    }

    fun joinProject(
        project: Project,
        userId: String,
        onSuccess: (Project) -> Unit,
        onError: (String) -> Unit
    ) {
        val updatedProject = project.copy(
            memberIds = (project.memberIds + userId).distinct()
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