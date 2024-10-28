package br.com.blifood.infrastructure.metric

import br.com.blifood.domain.entity.OrderStatus
import br.com.blifood.domain.service.MetricService
import io.micrometer.core.instrument.ImmutableTag
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OtlpMetricAdapter(private val meterRegistry: MeterRegistry) : MetricService {
    private val changeOrderMetric = "blifood.order"
    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun sendOrderStatusChanged(status: OrderStatus) {
        logger.info("metric name:$changeOrderMetric, status=${status.toString().lowercase()}")
        kotlin.runCatching {
            val tags = setOf(ImmutableTag("status", status.toString().lowercase()))
            meterRegistry.counter(changeOrderMetric, tags).increment()
        }.onFailure {
            logger.error("failed to send the metric: $changeOrderMetric", it)
        }
    }
}
