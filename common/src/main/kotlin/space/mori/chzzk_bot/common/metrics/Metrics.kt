package space.mori.chzzk_bot.common.metrics

import io.micrometer.core.instrument.Gauge
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import space.mori.chzzk_bot.common.services.UserService

object Metrics {
    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    var streamer =  0.0
    val streamerGauge: Gauge = Gauge.builder("streamer_gauge", this) { streamer }
        .description("Current All Streamer Count")
        .register(registry)

    var activeStreamer = 0.0
    val activateGauge: Gauge = Gauge.builder("active_streamer_gauge", this) { activeStreamer }
        .description("Current Active Streamer Count")
        .register(registry)

    var streaming: Double = 0.0
    val streamingGauge: Gauge = Gauge.builder("streaming_gauge", this) { streaming }
        .description("Current Streaming Streamer Count")
        .register(registry)

    fun refreshStreamerMetrics() {
        val streamers = UserService.getAllUsers()

        streamer = streamers.size.toDouble()
        activeStreamer = streamers.filter { !it.isDisabled }.size.toDouble()
    }

    fun increaseStreaming(inc: Int = 1) {
        streaming += inc
    }
    fun decreaseStreaming(dec: Int = 1) {
        streaming -= dec
    }

    init {
        refreshStreamerMetrics()
    }
}