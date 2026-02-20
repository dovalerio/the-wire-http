package io.github.dovalerio.thewire

public enum class HttpMethod {
    GET,
    POST,
    PUT,
    PATCH,
    DELETE,
    HEAD,
    OPTIONS
}

public val HttpMethod.isIdempotent: Boolean
    get() = when (this) {
        HttpMethod.GET,
        HttpMethod.PUT,
        HttpMethod.DELETE,
        HttpMethod.HEAD,
        HttpMethod.OPTIONS -> true

        HttpMethod.POST,
        HttpMethod.PATCH -> false
    }


