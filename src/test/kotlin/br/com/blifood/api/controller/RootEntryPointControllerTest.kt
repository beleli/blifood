package br.com.blifood.api.controller

import br.com.blifood.api.v1.DEFAULT_PAGE_SIZE
import br.com.blifood.api.v1.controller.RootEntryPointController
import br.com.blifood.api.v1.model.CityModel
import br.com.blifood.api.v1.model.CulinaryModel
import br.com.blifood.api.v1.model.PaymentMethodModel
import br.com.blifood.api.v1.model.RestaurantModel
import br.com.blifood.api.v1.model.StateModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import org.springframework.data.domain.Pageable

class RootEntryPointControllerTest : StringSpec({

    "root should return RootEntryPointModel with correct links" {
        val controller = RootEntryPointController()
        val rootEntryPointModel = controller.root()

        rootEntryPointModel.links shouldContain StateModel.findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE))
        rootEntryPointModel.links shouldContain CityModel.findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE))
        rootEntryPointModel.links shouldContain CulinaryModel.findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE))
        rootEntryPointModel.links shouldContain PaymentMethodModel.findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE))
        rootEntryPointModel.links shouldContain RestaurantModel.findAllLink(Pageable.ofSize(DEFAULT_PAGE_SIZE))
    }
})
