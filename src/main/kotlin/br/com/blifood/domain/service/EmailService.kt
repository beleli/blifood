package br.com.blifood.domain.service

interface EmailService {

    fun send(message: Message)

    data class Message(
        val to: Set<String>,
        val subject: String,
        val template: String,
        val variables: Map<String, Any>
    )
}
