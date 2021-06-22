package br.com.iupp.buildingwarriors.controller.handler.response

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class DefaultErrorResponse(
    val statusCode: Int,
    val messages: List<String>,
    val path: String,
    val error: String,
    timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:MM:ss")
    }

    val timestamp = formatter.format(timestamp)
}