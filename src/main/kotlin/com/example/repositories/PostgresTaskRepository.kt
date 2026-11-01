package com.example.repositories

import com.example.database.Tasks
import com.example.models.Task
import com.example.models.TaskRepository
import org.jetbrains.exposed.sql.*
// Fix: Explicitly import 'eq' to resolve the deleteWhere scoping issue
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresTaskRepository : TaskRepository {
    override fun findAll(): List<Task> = transaction {
        Tasks.selectAll().orderBy(Tasks.createdAt to SortOrder.DESC).map { it.toTask() }
    }

    override fun findById(id: Int): Task? = transaction {
        Tasks.select { Tasks.id eq id }.map { it.toTask() }.singleOrNull()
    }

    override fun create(title: String, description: String, priority: Int): Task = transaction {
        val insertStatement = Tasks.insert {
            it[Tasks.title] = title
            it[Tasks.description] = description
            it[Tasks.priority] = priority
        }
        Task(
            id = insertStatement[Tasks.id],
            title = title,
            description = description,
            priority = priority
        )
    }

    override fun update(id: Int, title: String, description: String, priority: Int): Task? = transaction {
        val updatedRows = Tasks.update({ Tasks.id eq id }) {
            it[Tasks.title] = title
            it[Tasks.description] = description
            it[Tasks.priority] = priority
        }
        if (updatedRows > 0) findById(id) else null
    }

    override fun toggleCompleted(id: Int): Task? = transaction {
        val current = Tasks.select { Tasks.id eq id }.singleOrNull() ?: return@transaction null
        Tasks.update({ Tasks.id eq id }) {
            it[completed] = !current[Tasks.completed]
        }
        findById(id)
    }

    override fun delete(id: Int): Boolean = transaction {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }

    override fun findByCompleted(completed: Boolean): List<Task> = transaction {
        Tasks.select { Tasks.completed eq completed }.map { it.toTask() }
    }

    override fun findByPriorityGreaterThan(priority: Int): List<Task> = transaction {
        Tasks.select { Tasks.priority greater priority }.map { it.toTask() }
    }

    private fun ResultRow.toTask(): Task = Task(
        id = this[Tasks.id],
        title = this[Tasks.title],
        description = this[Tasks.description] ?: "",
        completed = this[Tasks.completed],
        priority = this[Tasks.priority],
        createdAt = this[Tasks.createdAt]
    )
}