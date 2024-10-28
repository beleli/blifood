package br.com.blifood.infrastructure.metric

import br.com.blifood.domain.entity.OrderStatus
import io.kotest.core.spec.style.StringSpec
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Tag
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class OtlpMetricAdapterTest : StringSpec({

    val meterRegistry = mockk<MeterRegistry>()
    val counter = mockk<Counter>(relaxed = true)
    val metricName = "blifood.order"
    val otlpMetricAdapter = OtlpMetricAdapter(meterRegistry)

    beforeTest { clearMocks(counter) }

    "should send metric when order status changes" {
        val status = OrderStatus.CREATED
        val tags = setOf(ImmutableTag("status", status.toString().lowercase()))

        every { meterRegistry.counter(metricName, tags) } returns counter

        otlpMetricAdapter.sendOrderStatusChanged(status)

        verify { meterRegistry.counter(metricName, tags) }
        verify { counter.increment() }
    }

    "should log error when metric sending fails" {
        val status = OrderStatus.CREATED
        val tags = setOf(Tag.of("status", status.toString().lowercase()))
        val exception = RuntimeException("Counter error")

        every { meterRegistry.counter(metricName, tags) } throws exception

        otlpMetricAdapter.sendOrderStatusChanged(status)

        verify { meterRegistry.counter(metricName, tags) }
        verify(exactly = 0) { counter.increment() }
    }
})
