package br.com.blifood.domain.exception

import br.com.blifood.core.message.Messages

open class InfrastructureException(message: String, cause: Throwable?) : Throwable(message, cause)

class EmailException(
    message: String = Messages.get("email.integration-exception"),
    cause: Throwable? = null
) : InfrastructureException(message, cause)

class StorageException(
    message: String = Messages.get("storage.integration-exception"),
    cause: Throwable? = null
) : InfrastructureException(message, cause)
