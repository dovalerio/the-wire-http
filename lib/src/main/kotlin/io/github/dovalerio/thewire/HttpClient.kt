package io.github.dovalerio.thewire

public interface HttpClient {

    public suspend fun execute(
        request: HttpRequest
    ): HttpResponse
}
