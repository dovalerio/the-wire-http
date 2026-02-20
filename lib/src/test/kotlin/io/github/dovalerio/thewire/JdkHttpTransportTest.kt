package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.transport.JdkHttpTransport
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI
import java.net.http.HttpClient
import java.time.Duration
import java.net.http.HttpRequest as JdkRequest
import java.net.http.HttpResponse as JdkResponse

class JdkHttpTransportTest {

    @Test
    fun `should call underlying HttpClient send`() = runTest {

        val httpClient = mockk<HttpClient>()
        val jdkResponse = mockk<JdkResponse<ByteArray>>()

        every { jdkResponse.statusCode() } returns 200
        every { jdkResponse.headers() } returns
                java.net.http.HttpHeaders.of(emptyMap()) { _, _ -> true }
        every { jdkResponse.body() } returns "ok".toByteArray()

        every {
            httpClient.send(any<JdkRequest>(), any<JdkResponse.BodyHandler<ByteArray>>())
        } returns jdkResponse

        val transport = JdkHttpTransport(httpClient)

        val response = transport.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertEquals(200, response.statusCode)
        assertEquals("ok", response.bodyAsString())

        verify(exactly = 1) {
            httpClient.send(any(), any<JdkResponse.BodyHandler<ByteArray>>())
        }
    }

    @Test
    fun `should apply headers correctly`() = runTest {

        val httpClient = mockk<HttpClient>()
        val jdkResponse = mockk<JdkResponse<ByteArray>>()

        every { jdkResponse.statusCode() } returns 200
        every { jdkResponse.headers() } returns
                java.net.http.HttpHeaders.of(emptyMap()) { _, _ -> true }
        every { jdkResponse.body() } returns ByteArray(0)

        val slot = slot<JdkRequest>()

        every {
            httpClient.send(capture(slot), any<JdkResponse.BodyHandler<ByteArray>>())
        } returns jdkResponse

        val transport = JdkHttpTransport(httpClient)

        transport.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test"),
                headers = mapOf("X-Test" to "123")
            )
        )

        assertEquals(
            "123",
            slot.captured.headers().firstValue("X-Test").orElse(null)
        )
    }

    @Test
    fun `should apply timeout when provided`() = runTest {

        val httpClient = mockk<HttpClient>()
        val jdkResponse = mockk<JdkResponse<ByteArray>>()

        every { jdkResponse.statusCode() } returns 200
        every { jdkResponse.headers() } returns
                java.net.http.HttpHeaders.of(emptyMap()) { _, _ -> true }
        every { jdkResponse.body() } returns ByteArray(0)

        val slot = slot<JdkRequest>()

        every {
            httpClient.send(capture(slot), any<JdkResponse.BodyHandler<ByteArray>>())
        } returns jdkResponse

        val transport = JdkHttpTransport(httpClient)

        transport.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test"),
                timeoutMillis = 1500
            )
        )

        assertEquals(Duration.ofMillis(1500), slot.captured.timeout().orElse(null))
    }

    @Test
    fun `should map null body to empty byte array`() = runTest {

        val httpClient = mockk<HttpClient>()
        val jdkResponse = mockk<JdkResponse<ByteArray>>()

        every { jdkResponse.statusCode() } returns 204
        every { jdkResponse.headers() } returns
                java.net.http.HttpHeaders.of(emptyMap()) { _, _ -> true }
        every { jdkResponse.body() } returns null

        every {
            httpClient.send(any<JdkRequest>(), any<JdkResponse.BodyHandler<ByteArray>>())
        } returns jdkResponse

        val transport = JdkHttpTransport(httpClient)

        val response = transport.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertArrayEquals(ByteArray(0), response.body)
    }
}