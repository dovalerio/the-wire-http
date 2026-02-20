package io.github.dovalerio.thewire.dsl

import io.github.dovalerio.thewire.HttpMethod
import io.github.dovalerio.thewire.HttpRequest
import java.net.URI

public class RequestDsl(
    private val method: HttpMethod,
    private val url: String
) {

    private val headers = mutableMapOf<String, String>()
    private var body: ByteArray? = null
    private var timeoutMillis: Long? = null

    public fun header(
        name: String,
        value: String
    ) {
        headers[name] = value
    }

    public fun body(
        value: String
    ) {
        body = value.toByteArray()
    }

    public fun body(
        value: ByteArray
    ) {
        body = value
    }

    public fun timeout(
        millis: Long
    ) {
        timeoutMillis = millis
    }

    internal fun build(): HttpRequest {
        return HttpRequest(
            method = method,
            uri = URI.create(url),
            headers = headers.toMap(),
            body = body,
            timeoutMillis = timeoutMillis
        )
    }
}