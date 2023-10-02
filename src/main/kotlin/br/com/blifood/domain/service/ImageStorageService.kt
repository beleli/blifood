package br.com.blifood.domain.service

import java.io.InputStream

interface ImageStorageService {
    fun upload(image: Image)
    fun remove(fileName: String)
    fun recover(fileName: String): Any
    fun isURL(): Boolean

    data class Image(
        val fileName: String,
        val contentType: String,
        val inputStream: InputStream
    )
}
