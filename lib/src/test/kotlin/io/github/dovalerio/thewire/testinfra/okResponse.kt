package io.github.dovalerio.thewire.testinfra

import io.github.dovalerio.thewire.HttpResponse

fun okResponse(
    body: String = "ok"
): HttpResponse =
    HttpResponse(
        statusCode = 200,
        headers = emptyMap(),
        body = body.toByteArray()
    )

fun errorResponse(
    status: Int = 500
): HttpResponse =
    HttpResponse(
        statusCode = status,
        headers = emptyMap(),
        body = ByteArray(0)
    )