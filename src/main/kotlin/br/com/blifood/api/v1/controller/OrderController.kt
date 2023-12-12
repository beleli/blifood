package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.addUriInResponseHeader
import br.com.blifood.api.v1.getSecurityContextHolderUserId
import br.com.blifood.api.v1.model.OrderModel
import br.com.blifood.api.v1.model.input.OrderInputModel
import br.com.blifood.api.v1.model.input.toEntity
import br.com.blifood.api.v1.model.toModel
import br.com.blifood.api.v1.openapi.OrderControllerOpenApi
import br.com.blifood.core.log.logRequest
import br.com.blifood.core.log.logResponse
import br.com.blifood.domain.entity.Authority
import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.exception.BusinessException
import br.com.blifood.domain.exception.EntityNotFoundException
import br.com.blifood.domain.service.OrderService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/orders", produces = [MediaType.APPLICATION_JSON_VALUE])
class OrderController(
    private val orderService: OrderService
) : OrderControllerOpenApi {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PreAuthorize("hasAuthority('${Authority.ORDER_READ}')")
    @GetMapping("/{orderCode}")
    override fun findByCode(@PathVariable orderCode: String): OrderModel {
        return orderService.findOrThrow(orderCode).toModel()
    }

    @PreAuthorize("hasAuthority('${Authority.ORDER_WRITE}')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    override fun create(
        @Valid @RequestBody
        orderInputModel: OrderInputModel
    ): OrderModel {
        logger.logRequest("create", orderInputModel)
        val userId = getSecurityContextHolderUserId()
        val order = orderInputModel.toEntity(userId)
        return issue(order).toModel().also {
            addUriInResponseHeader(it.code)
            logger.logResponse("create", it)
        }
    }

    @PreAuthorize("hasAuthority('${Authority.ORDER_WRITE}')")
    @PutMapping("/{orderCode}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun confirm(@PathVariable orderCode: String): ResponseEntity<Void> {
        orderService.confirm(orderCode)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('${Authority.ORDER_WRITE}')")
    @PutMapping("/{orderCode}/delivery")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun delivery(@PathVariable orderCode: String): ResponseEntity<Void> {
        orderService.delivery(orderCode)
        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('${Authority.ORDER_WRITE}')")
    @PutMapping("/{orderCode}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    override fun cancel(@PathVariable orderCode: String): ResponseEntity<Void> {
        orderService.cancel(orderCode)
        return ResponseEntity.noContent().build()
    }

    private fun issue(order: Order): Order {
        return try {
            orderService.issue(order)
        } catch (ex: EntityNotFoundException) {
            throw throw BusinessException(ex.message)
        } catch (ex: Throwable) {
            throw ex
        }
    }
}
