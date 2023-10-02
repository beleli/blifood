package br.com.blifood.domain.service

import br.com.blifood.domain.entity.ProductImage
import br.com.blifood.domain.exception.ProductImageNotFoundException
import br.com.blifood.domain.repository.ProductImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream

@Service
class ProductImageService(
    private val productImageRepository: ProductImageRepository,
    private val imageStorageService: ImageStorageService
) {

    @Transactional(readOnly = true)
    fun findOrThrow(productId: Long): ProductImage {
        return productImageRepository.findById(productId).orElseThrow { ProductImageNotFoundException() }
    }

    @Transactional
    fun save(productImage: ProductImage, fileData: InputStream): ProductImage {
        val existentImage = productImageRepository.findById(productImage.id)
        if (existentImage.isPresent) imageStorageService.remove(existentImage.get().fileName)
        imageStorageService.upload(ImageStorageService.Image(productImage.fileName, productImage.contentType, fileData))
        return productImageRepository.save(productImage)
    }

    @Transactional
    fun delete(restaurantId: Long, productId: Long) {
        val image = findOrThrow(productId)
        productImageRepository.delete(image)
        imageStorageService.remove(image.fileName)
    }

    fun recoverImage(fileName: String): Any {
        return imageStorageService.recover(fileName)
    }
}
