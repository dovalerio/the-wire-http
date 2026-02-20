package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.middleware.RetryMiddleware
import io.github.dovalerio.thewire.testinfra.SequenceTransport
import io.github.dovalerio.thewire.testinfra.errorResponse
import io.github.dovalerio.thewire.testinfra.okResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

class RetryMiddlewareTest {
    @Test
    fun `should throw after max attempts`() = runTest {

        val transport = SequenceTransport(
            mutableListOf(
                { throw RuntimeException("error1") },
                { throw RuntimeException("error2") },
                { throw RuntimeException("error3") }
            )
        )

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(
                RetryMiddleware(maxAttempts = 3)
            )
        )

        var thrown: Throwable? = null

        try {
            client.execute(
                HttpRequest(
                    method = HttpMethod.GET,
                    uri = URI.create("https://test")
                )
            )
        } catch (ex: Throwable) {
            thrown = ex
        }

        assertEquals(3, transport.callCount)
        assertEquals("error3", thrown?.message)
    }

    @Test
    fun `post should not retry on exception`() = runTest {

        val transport = SequenceTransport(
            mutableListOf(
                { throw RuntimeException("network error") }
            )
        )

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(
                RetryMiddleware(maxAttempts = 3)
            )
        )

        try {
            client.execute(
                HttpRequest(
                    method = HttpMethod.POST,
                    uri = URI.create("https://test")
                )
            )
        } catch (_: RuntimeException) {
            // esperado
        }

        assertEquals(1, transport.callCount)
    }

    @Test
    fun `retry should attempt again on exception`() = runTest {

        var attempts = 0

        val transport = SequenceTransport(
            mutableListOf(
                {
                    attempts++
                    throw RuntimeException("network error")
                },
                {
                    attempts++
                    okResponse()
                }
            )
        )

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(
                RetryMiddleware(maxAttempts = 3)
            )
        )

        val response = client.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertEquals(2, transport.callCount)
        assertEquals(200, response.statusCode)
    }

    @Test
    fun `retry should attempt again on 500`() = runTest {

        val transport = SequenceTransport(
            mutableListOf(
                { errorResponse(500) },
                { okResponse() }
            )
        )

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(
                RetryMiddleware(maxAttempts = 3)
            )
        )

        val response = client.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertEquals(2, transport.callCount)
        assertEquals(200, response.statusCode)
    }
}