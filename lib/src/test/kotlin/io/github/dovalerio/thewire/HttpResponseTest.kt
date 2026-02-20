package io.github.dovalerio.thewire

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HttpResponseTest {

    @Test
    fun `bodyAsString should return correct string`() {

        val response = HttpResponse(
            statusCode = 200,
            headers = emptyMap(),
            body = "hello world".toByteArray()
        )

        assertEquals("hello world", response.bodyAsString())
    }

    @Test
    fun `bodyAsString should return empty when body is empty`() {

        val response = HttpResponse(
            statusCode = 200,
            headers = emptyMap(),
            body = ByteArray(0)
        )

        assertEquals("", response.bodyAsString())
    }
}