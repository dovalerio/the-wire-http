package io.github.dovalerio.thewire.dsl

import io.github.dovalerio.thewire.HttpClient
import io.github.dovalerio.thewire.HttpMethod
import io.github.dovalerio.thewire.HttpResponse

public suspend fun HttpClient.get(
    url: String,
    block: RequestDsl.() -> Unit = {}
): HttpResponse {
    return execute(buildRequest(HttpMethod.GET, url, block))
}

public suspend fun HttpClient.post(
    url: String,
    block: RequestDsl.() -> Unit = {}
): HttpResponse {
    return execute(buildRequest(HttpMethod.POST, url, block))
}

public suspend fun HttpClient.put(
    url: String,
    block: RequestDsl.() -> Unit = {}
): HttpResponse {
    return execute(buildRequest(HttpMethod.PUT, url, block))
}

public suspend fun HttpClient.delete(
    url: String,
    block: RequestDsl.() -> Unit = {}
): HttpResponse {
    return execute(buildRequest(HttpMethod.DELETE, url, block))
}

private fun buildRequest(
    method: HttpMethod,
    url: String,
    block: RequestDsl.() -> Unit
) = RequestDsl(method, url).apply(block).build()