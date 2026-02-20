package io.github.dovalerio.thewire.testinfra

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import io.github.dovalerio.thewire.transport.HttpTransport

class SequenceTransport(
    private val responses: MutableList<suspend (HttpRequest) -> HttpResponse>
) : HttpTransport {

    var callCount: Int = 0
        private set

    override suspend fun execute(request: HttpRequest): HttpResponse {
        callCount++

        if (responses.isEmpty()) {
            error("No more responses configured")
        }

        return responses.removeAt(0).invoke(request)
    }
}