package io.github.dovalerio.thewire.middleware

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse

public fun interface HttpMiddleware {

    public suspend fun intercept(
        request: HttpRequest,
        chain: MiddlewareChain
    ): HttpResponse
}
