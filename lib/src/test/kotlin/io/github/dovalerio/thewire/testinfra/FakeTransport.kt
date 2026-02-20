package io.github.dovalerio.thewire.testinfra

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import io.github.dovalerio.thewire.transport.HttpTransport

class FakeTransport(
    private val behavior: suspend (HttpRequest) -> HttpResponse
) : HttpTransport {

    var callCount: Int = 0
        private set

    val requests = mutableListOf<HttpRequest>()

    override suspend fun execute(request: HttpRequest): HttpResponse {
        callCount++
        requests += request
        return behavior(request)
    }
}