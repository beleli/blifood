package br.com.blifood.core.io

import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ProtocolResolver
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader

class StringProtocolResolver : ProtocolResolver, ApplicationListener<ApplicationContextInitializedEvent> {

    companion object {
        private const val STRING_PREFIX = "string:"
    }

    override fun resolve(location: String, resourceLoader: ResourceLoader): Resource? {
        if (location.startsWith(STRING_PREFIX)) {
            return ByteArrayResource(location.substring(STRING_PREFIX.length).toByteArray())
        }
        return null
    }

    override fun onApplicationEvent(event: ApplicationContextInitializedEvent) {
        event.applicationContext.addProtocolResolver(this)
    }
}
