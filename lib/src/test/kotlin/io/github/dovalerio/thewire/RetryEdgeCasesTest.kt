package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.middleware.RetryMiddleware
import io.github.dovalerio.thewire.transport.HttpTransport
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.URI

class RetryEdgeCasesTest {

    @Test
    fun `should throw after exceeding max attempts`() = runTest {

        val transport = mockk<HttpTransport>()

        coEvery {
            transport.execute(any())
        } throws RuntimeException("network error")

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(
                RetryMiddleware(maxAttempts = 3)
            )
        )

        assertThrows<RuntimeException> {
            client.execute(
                HttpRequest(
                    method = HttpMethod.GET,
                    uri = URI.create("https://test")
                )
            )
        }

        coVerify(exactly = 3) {
            transport.execute(any())
        }
    }
    @Test
    fun `retry with maxAttempts 1 should not retry`() = runTest {

        val transport = mockk<HttpTransport>()

        coEvery { transport.execute(any()) } returns HttpResponse(
            statusCode = 500,
            headers = emptyMap(),
            body = ByteArray(0)
        )

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(RetryMiddleware(maxAttempts = 1))
        )

        client.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        coVerify(exactly = 1) { transport.execute(any()) }
    }

    }