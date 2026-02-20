package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.middleware.HttpMiddleware
import io.github.dovalerio.thewire.transport.HttpTransport
import io.github.dovalerio.thewire.transport.JdkHttpTransport

public class HttpClientBuilder {

    private var transport: HttpTransport? = null
    private val middlewares = mutableListOf<HttpMiddleware>()

    public fun transport(
        transport: HttpTransport
    ): HttpClientBuilder {
        this.transport = transport
        return this
    }

    public fun addMiddleware(
        middleware: HttpMiddleware
    ): HttpClientBuilder {
        middlewares += middleware
        return this
    }

    public fun build(): HttpClient {

        val resolvedTransport = transport ?: JdkHttpTransport()

        return PipelineHttpClient(
            transport = resolvedTransport,
            middlewares = middlewares.toList()
        )
    }
}
