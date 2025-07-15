package space.mori.chzzk_bot.webserver.routes

import io.ktor.server.application.ApplicationStopped
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.java.KoinJavaComponent.inject
import space.mori.chzzk_bot.common.events.CoroutinesEventBus
import space.mori.chzzk_bot.common.events.UserRegisterEvent
import space.mori.chzzk_bot.common.metrics.Metrics
import kotlin.getValue


val metricScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
fun Routing.metricRoutes() {
    environment.monitor.subscribe(ApplicationStopped) {
        metricScope.cancel()
    }

    val dispatcher: CoroutinesEventBus by inject(CoroutinesEventBus::class.java)
    val registry: PrometheusMeterRegistry by inject(PrometheusMeterRegistry::class.java)

    dispatcher.subscribe(UserRegisterEvent::class) {
        Metrics.streamer++
    }

    get("/metrics") {
        call.respondText(registry.scrape())
    }

}