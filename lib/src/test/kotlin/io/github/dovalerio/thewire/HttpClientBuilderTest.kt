package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.transport.HttpTransport
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

class HttpClientBuilderTest {

    @Test
    fun `builder should use provided transport`() = runTest {

        val transport = mockk<HttpTransport>()

        coEvery {
            transport.execute(any())
        } returns HttpResponse(
            statusCode = 200,
            headers = emptyMap(),
            body = "ok".toByteArray()
        )

        val client = HttpClientBuilder()
            .transport(transport)
            .build()

        val response = client.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertEquals(200, response.statusCode)

        coVerify(exactly = 1) {
            transport.execute(any())
        }
    }
}