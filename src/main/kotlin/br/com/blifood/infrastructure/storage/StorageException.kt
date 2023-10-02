package br.com.blifood.infrastructure.storage

class StorageException : Throwable {
    constructor(message: String, cause: Throwable) : super(message, cause)
}
