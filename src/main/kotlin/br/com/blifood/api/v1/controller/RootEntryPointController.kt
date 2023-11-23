package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.RootEntryPointModel
import br.com.blifood.api.v1.model.StateModel
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1", produces = [MediaType.APPLICATION_JSON_VALUE])
class RootEntryPointController {

    @GetMapping
    @Operation(hidden = true)
    fun root(): RootEntryPointModel {
        val rootEntryPointModel = RootEntryPointModel()
        val pageable = Pageable.ofSize(DEFAULT_PAGE_SIZE)

        rootEntryPointModel.add(StateModel.findAllLink(pageable))
        rootEntryPointModel.add(CityModel.findAllLink(pageable))
        rootEntryPointModel.add(CulinaryModel.findAllLink(pageable))
        rootEntryPointModel.add(PaymentMethodModel.findAllLink(pageable))
        rootEntryPointModel.add(RestaurantModel.findAllLink(pageable))
        // rootEntryPointModel.add(OrderModel.controllerLink())

        return rootEntryPointModel
    }
}
