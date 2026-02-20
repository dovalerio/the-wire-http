package io.github.dovalerio.thewire.middleware.internal

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import io.github.dovalerio.thewire.middleware.HttpMiddleware
import io.github.dovalerio.thewire.middleware.MiddlewareChain

internal class DefaultMiddlewareChain(
    private val middlewares: List<HttpMiddleware>,
    private val index: Int,
    private val core: suspend (HttpRequest) -> HttpResponse
) : MiddlewareChain {

    override suspend fun proceed(
        request: HttpRequest
    ): HttpResponse {

        return if (index < middlewares.size) {
            val next = DefaultMiddlewareChain(
                middlewares = middlewares,
                index = index + 1,
                core = core
            )
            middlewares[index].intercept(request, next)
        } else {
            core(request)
        }
    }
}