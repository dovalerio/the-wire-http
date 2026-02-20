package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.middleware.HttpMiddleware
import io.github.dovalerio.thewire.middleware.internal.DefaultMiddlewareChain
import io.github.dovalerio.thewire.transport.HttpTransport

public class PipelineHttpClient(
    private val transport: HttpTransport,
    private val middlewares: List<HttpMiddleware> = emptyList()
) : HttpClient {

    override suspend fun execute(
        request: HttpRequest
    ): HttpResponse {

        val chain = DefaultMiddlewareChain(
            middlewares = middlewares,
            index = 0,
            core = transport::execute
        )

        return chain.proceed(request)
    }
}
