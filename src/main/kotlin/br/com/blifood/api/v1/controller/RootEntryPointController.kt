package br.com.blifood.api.v1.controller

import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.RootEntryPointModel
import br.com.blifood.api.v1.model.StateModel
import io.swagger.v3.oas.annotations.Operation
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

        rootEntryPointModel.add(StateModel.findAllLink())
        rootEntryPointModel.add(CityModel.findAllLink())
        rootEntryPointModel.add(CulinaryModel.findAllLink())
        rootEntryPointModel.add(PaymentMethodModel.findAllLink())
        rootEntryPointModel.add(RestaurantModel.findAllLink())
        // rootEntryPointModel.add(OrderModel.controllerLink())

        return rootEntryPointModel
    }
}
