package com.example.repositories

import com.example.models.Task
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

object Tasks : Table() {
    val id = integer("id").autoIncrement()
    val title = varchar("title", 128)
    val description = varchar("description", 256)
    val isCompleted = bool("is_completed").default(false)

    override val primaryKey = PrimaryKey(id)
}

class PostgresTaskRepository : TaskRepository {
    init {
        transaction {
            SchemaUtils.create(Tasks)
        }
    }

    private fun resultRowToTask(row: ResultRow) = Task(
        id = row[Tasks.id],
        title = row[Tasks.title],
        description = row[Tasks.description],
        completed = row[Tasks.isCompleted]
    )

    override fun getAllTasks(): List<Task> = transaction {
        Tasks.selectAll().map(::resultRowToTask)
    }

    override fun getTaskById(id: Int): Task? = transaction {
        Tasks.selectAll().where { Tasks.id eq id }
            .map(::resultRowToTask)
            .singleOrNull()
    }

    override fun addTask(task: Task): Unit = transaction {
        Tasks.insert {
            it[title] = task.title
            it[description] = task.description
            it[isCompleted] = task.completed
        }
    }

    override fun updateTask(id: Int, task: Task): Boolean = transaction {
        Tasks.update({ Tasks.id eq id }) {
            it[title] = task.title
            it[description] = task.description
            it[isCompleted] = task.completed
        } > 0
    }

    override fun deleteTask(id: Int): Boolean = transaction {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }
}