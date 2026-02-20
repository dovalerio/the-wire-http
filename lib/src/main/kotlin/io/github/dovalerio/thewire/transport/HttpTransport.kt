package io.github.dovalerio.thewire.transport

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse

public fun interface HttpTransport {

    public suspend fun execute(
        request: HttpRequest
    ): HttpResponse
}
