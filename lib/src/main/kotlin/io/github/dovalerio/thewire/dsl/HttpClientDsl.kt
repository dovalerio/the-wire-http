package io.github.dovalerio.thewire.dsl

import io.github.dovalerio.thewire.HttpClient
import io.github.dovalerio.thewire.PipelineHttpClient
import io.github.dovalerio.thewire.middleware.HttpMiddleware
import io.github.dovalerio.thewire.middleware.LoggingMiddleware
import io.github.dovalerio.thewire.middleware.RetryMiddleware
import io.github.dovalerio.thewire.transport.HttpTransport
import io.github.dovalerio.thewire.transport.JdkHttpTransport

public class HttpClientDsl {

    private var transport: HttpTransport? = null
    private val middlewares = mutableListOf<HttpMiddleware>()

    public fun transport(
        value: HttpTransport
    ) {
        transport = value
    }

    public fun logging(
        logHeaders: Boolean = false
    ) {
        middlewares += LoggingMiddleware(logHeaders)
    }

    public fun retry(
        block: RetryConfigDsl.() -> Unit = {}
    ) {
        val config = RetryConfigDsl().apply(block)

        middlewares += RetryMiddleware(
            maxAttempts = config.maxAttempts,
            initialDelayMillis = config.initialDelayMillis,
            backoffMultiplier = config.backoffMultiplier
        )
    }

    internal fun build(): HttpClient {
        val resolvedTransport = transport ?: JdkHttpTransport()

        return PipelineHttpClient(
            transport = resolvedTransport,
            middlewares = middlewares.toList()
        )
    }
}
    public fun httpClient(
        block: HttpClientDsl.() -> Unit
    ): HttpClient {

        val dsl = HttpClientDsl()
        dsl.block()
        return dsl.build()
    }

