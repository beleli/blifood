package br.com.blifood.domain

import br.com.blifood.domain.entity.Address
import br.com.blifood.domain.entity.City
import br.com.blifood.domain.entity.Culinary
import br.com.blifood.domain.entity.Order
import br.com.blifood.domain.entity.OrderItem
import br.com.blifood.domain.entity.PaymentMethod
import br.com.blifood.domain.entity.Product
import br.com.blifood.domain.entity.ProductImage
import br.com.blifood.domain.entity.Restaurant
import br.com.blifood.domain.entity.State
import br.com.blifood.domain.entity.User
import br.com.blifood.domain.entity.UserProfile
import br.com.blifood.domain.service.EmailService
import br.com.blifood.domain.service.ImageStorageService
import io.mockk.mockk
import java.io.InputStream
import java.math.BigDecimal

fun createState(
    id: Long = 1,
    name: String = "ST"
) = State(
    id = id,
    name = name
)

fun createCity(
    id: Long = 1,
    name: String = "City",
    state: State = createState()
) = City(
    id = id,
    name = name,
    state = state
)

fun createCulinary(
    id: Long = 1,
    name: String = "Culinary"
) = Culinary(
    id = id,
    name = name
)

fun createPaymentMethod(
    id: Long = 1,
    description: String = "Payment Method 1"
) = PaymentMethod(
    id = id,
    description = description
)

fun createAddress(
    city: City = createCity(),
    zipCode: String = "99999999",
    street: String = "Street",
    number: String = "1",
    complement: String? = null,
    district: String = "district"
) = Address(
    city = city,
    zipCode = zipCode,
    street = street,
    number = number,
    complement = complement,
    district = district
)
fun createRestaurant(
    id: Long = 1,
    culinary: Culinary = createCulinary(),
    address: Address = createAddress(),
    name: String = "Restaurant 1",
    deliveryFee: BigDecimal = BigDecimal.valueOf(10.0)
) = Restaurant(
    id = id,
    culinary = culinary,
    address = address,
    name = name,
    deliveryFee = deliveryFee
)

fun createProduct(
    id: Long = 1,
    restaurant: Restaurant = createRestaurant(),
    name: String = "Product 1",
    description: String = "Description",
    price: BigDecimal = BigDecimal.valueOf(10.0),
    active: Boolean = true
) = Product(
    id = id,
    restaurant = restaurant,
    name = name,
    description = description,
    price = price,
    active = active
)

fun createProductImage(
    id: Long = 1,
    fileName: String = "test_image.jpg",
    description: String = "Test Image",
    contentType: String = "image/jpeg",
    size: Long = 1024
) = ProductImage(
    id = id,
    fileName = fileName,
    description = description,
    contentType = contentType,
    size = size
)

fun createUser(
    id: Long = 1,
    name: String = "Test User",
    email: String = "test@example.com",
    password: String = "password",
    profile: UserProfile = UserProfile.ADMIN
) = User(
    id = id,
    name = name,
    email = email,
    password = password,
    profile = profile
)

fun createOrder(
    id: Long = 1,
    user: User = createUser(),
    restaurant: Restaurant = createRestaurant(),
    paymentMethod: PaymentMethod = createPaymentMethod(),
    deliveryAddress: Address = createAddress(),
    items: List<OrderItem> = listOf(createOrderItem())
) = Order(
    id = id,
    user = user,
    restaurant = restaurant,
    paymentMethod = paymentMethod,
    deliveryAddress = deliveryAddress,
    items = items
)

fun createOrderItem(
    id: Long = 1,
    product: Product = createProduct(),
    amount: Int = 1,
    unitPrice: BigDecimal = BigDecimal.valueOf(10.0),
    observation: String? = null
) = OrderItem(
    id = id,
    product = product,
    amount = amount,
    unitPrice = unitPrice,
    observation = observation
)

fun createMessage(
    to: Set<String> = setOf("user@example.com"),
    subject: String = "Test Subject",
    template: String = "test-template",
    variables: Map<String, Any> = mapOf("key" to "value")
) = EmailService.Message(
    to = to,
    subject = subject,
    template = template,
    variables = variables
)

fun createImage(
    fileName: String = "test.jpg",
    contentType: String = "jpg",
    inputStream: InputStream = mockk<InputStream>(relaxed = true)
) = ImageStorageService.Image(
    fileName = fileName,
    contentType = contentType,
    inputStream = inputStream
)
