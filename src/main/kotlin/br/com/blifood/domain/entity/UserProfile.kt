package br.com.blifood.domain.entity

enum class UserProfile(val permissions: Set<UserPermissions>) {
    ADMIN(UserPermissions.values().toHashSet()),
    MANAGER(
        setOf(
            UserPermissions.STATE_READ,
            UserPermissions.CITY_READ,
            UserPermissions.CULINARY_READ,
            UserPermissions.PAYMENT_METHOD_READ,
            UserPermissions.RESTAURANT_READ,
            UserPermissions.RESTAURANT_WRITE,
            UserPermissions.PRODUCT_READ,
            UserPermissions.PRODUCT_WRITE,
            UserPermissions.USER_WRITE,
            UserPermissions.ORDER_READ,
            UserPermissions.ORDER_WRITE
        )
    ),
    CUSTOMER(
        setOf(
            UserPermissions.RESTAURANT_READ,
            UserPermissions.ORDER_READ,
            UserPermissions.ORDER_WRITE,
            UserPermissions.PRODUCT_READ,
            UserPermissions.USER_WRITE
        )
    )
}
