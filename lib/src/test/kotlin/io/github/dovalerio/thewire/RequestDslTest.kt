package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.dsl.RequestDsl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RequestDslTest {

    @Test
    fun `dsl builds correct HttpRequest`() {

        val dsl = RequestDsl(HttpMethod.POST, "https://test").apply {
            header("X-Test", "123")
            body("abc")
            timeout(1000)
        }

        val request = dsl.build()

        assertEquals(HttpMethod.POST, request.method)
        assertEquals("123", request.headers["X-Test"])
        assertEquals("abc", request.body?.toString(Charsets.UTF_8))
        assertEquals(1000, request.timeoutMillis)
    }
}