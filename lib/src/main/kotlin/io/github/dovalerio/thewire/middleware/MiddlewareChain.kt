package io.github.dovalerio.thewire.middleware

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse

public interface MiddlewareChain {

    public suspend fun proceed(
        request: HttpRequest
    ): HttpResponse
}
