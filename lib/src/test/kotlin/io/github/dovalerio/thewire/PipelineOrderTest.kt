package io.github.dovalerio.thewire

import io.github.dovalerio.thewire.middleware.HttpMiddleware
import io.github.dovalerio.thewire.middleware.MiddlewareChain
import io.github.dovalerio.thewire.testinfra.FakeTransport
import io.github.dovalerio.thewire.testinfra.okResponse
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.URI

class PipelineOrderTest {

    @Test
    fun `middlewares execute in correct order`() = runTest {

        val log = mutableListOf<String>()

        val middlewareA = object : HttpMiddleware {
            override suspend fun intercept(
                request: HttpRequest,
                chain: MiddlewareChain
            ): HttpResponse {
                log += "before:A"
                val response = chain.proceed(request)
                log += "after:A"
                return response
            }
        }

        val middlewareB = object : HttpMiddleware {
            override suspend fun intercept(
                request: HttpRequest,
                chain: MiddlewareChain
            ): HttpResponse {
                log += "before:B"
                val response = chain.proceed(request)
                log += "after:B"
                return response
            }
        }

        val transport = FakeTransport { okResponse() }

        val client = PipelineHttpClient(
            transport = transport,
            middlewares = listOf(middlewareA, middlewareB)
        )

        client.execute(
            HttpRequest(
                method = HttpMethod.GET,
                uri = URI.create("https://test")
            )
        )

        assertEquals(
            listOf(
                "before:A",
                "before:B",
                "after:B",
                "after:A"
            ),
            log
        )
    }
}