package br.com.blifood.core.io

import org.springframework.boot.context.event.ApplicationContextInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ProtocolResolver
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import java.util.Base64

class Base64ProtocolResolver : ProtocolResolver, ApplicationListener<ApplicationContextInitializedEvent> {
    override fun resolve(location: String, resourceLoader: ResourceLoader): Resource? {
        if (location.startsWith("base64:")) {
            val decodedResource = Base64.getDecoder().decode(location.substring(7))
            return ByteArrayResource(decodedResource)
        }
        return null
    }

    override fun onApplicationEvent(event: ApplicationContextInitializedEvent) {
        event.applicationContext.addProtocolResolver(this)
    }
}
