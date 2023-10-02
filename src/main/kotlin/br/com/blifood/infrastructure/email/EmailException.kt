package br.com.blifood.infrastructure.email

class EmailException : Throwable {
    constructor(message: String, cause: Throwable) : super(message, cause)
}
