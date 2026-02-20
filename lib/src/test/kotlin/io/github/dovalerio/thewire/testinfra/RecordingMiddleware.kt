package io.github.dovalerio.thewire.testinfra

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import io.github.dovalerio.thewire.middleware.HttpMiddleware
import io.github.dovalerio.thewire.middleware.MiddlewareChain

class RecordingMiddleware(
    private val name: String,
    private val log: MutableList<String>
) : HttpMiddleware {

    override suspend fun intercept(
        request: HttpRequest,
        chain: MiddlewareChain
    ): HttpResponse {

        log += "before:$name"
        val response = chain.proceed(request)
        log += "after:$name"

        return response
    }
}