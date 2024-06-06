package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class InfrastructureException(message: String?, cause: Throwable?) : Throwable(message, cause)

class EmailException(
    message: String = Messages.get("email.integrationError"),
    cause: Throwable? = null
) : InfrastructureException(message, cause)

class StorageException(
    message: String = Messages.get("storage.integrationError"),
    cause: Throwable? = null
) : InfrastructureException(message, cause)
