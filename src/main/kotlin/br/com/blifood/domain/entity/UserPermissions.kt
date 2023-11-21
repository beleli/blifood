package br.com.blifood.domain.entity

enum class UserPermissions(val authority: String) {
    STATE_READ(Authority.STATE_READ),
    STATE_WRITE(Authority.STATE_WRITE),
    CITY_READ(Authority.CITY_READ),
    CITY_WRITE(Authority.CITY_WRITE),
    CULINARY_READ(Authority.CULINARY_READ),
    CULINARY_WRITE(Authority.CULINARY_WRITE),
    PAYMENT_METHOD_READ(Authority.PAYMENT_METHOD_READ),
    PAYMENT_METHOD_WRITE(Authority.PAYMENT_METHOD_WRITE),
    RESTAURANT_READ(Authority.RESTAURANT_READ),
    RESTAURANT_WRITE(Authority.RESTAURANT_WRITE),
    PRODUCT_READ(Authority.PRODUCT_READ),
    PRODUCT_WRITE(Authority.PRODUCT_WRITE),
    USER_READ(Authority.USER_READ),
    USER_WRITE(Authority.USER_WRITE),
    ORDER_READ(Authority.ORDER_READ),
    ORDER_WRITE(Authority.ORDER_WRITE)
}

object Authority {
    const val STATE_READ = "STATE.READ"
    const val STATE_WRITE = "STATE.WRITE"
    const val CITY_READ = "CITY.READ"
    const val CITY_WRITE = "CITY.WRITE"
    const val CULINARY_READ = "CULINARY.READ"
    const val CULINARY_WRITE = "CULINARY.WRITE"
    const val PAYMENT_METHOD_READ = "PAYMENT_METHOD.READ"
    const val PAYMENT_METHOD_WRITE = "PAYMENT_METHOD.WRITE"
    const val RESTAURANT_READ = "RESTAURANT.READ"
    const val RESTAURANT_WRITE = "RESTAURANT.WRITE"
    const val PRODUCT_READ = "PRODUCT.READ"
    const val PRODUCT_WRITE = "PRODUCT.WRITE"
    const val USER_READ = "USER.READ"
    const val USER_WRITE = "USER.WRITE"
    const val ORDER_READ = "ORDER.READ"
    const val ORDER_WRITE = "ORDER.WRITE"
}
