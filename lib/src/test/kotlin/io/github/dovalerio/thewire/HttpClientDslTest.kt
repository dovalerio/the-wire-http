package io.github.dovalerio.thewire
import io.github.dovalerio.thewire.dsl.httpClient
import io.github.dovalerio.thewire.testinfra.FakeTransport
import io.github.dovalerio.thewire.testinfra.okResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

class HttpClientDslTest {

    @Test
    fun `dsl creates working client`() = runTest {

        val fake = FakeTransport { okResponse() }

        val client = httpClient {
            transport(fake)
            logging()
        }

        val response = client.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertEquals(200, response.statusCode)
        assertEquals(1, fake.callCount)
    }
}