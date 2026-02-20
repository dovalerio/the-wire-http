package io.github.dovalerio.thewire.transport

import io.github.dovalerio.thewire.HttpMethod
import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.http.HttpClient
import java.net.http.HttpRequest as JdkRequest
import java.net.http.HttpResponse as JdkResponse
import java.time.Duration

public class JdkHttpTransport(
    private val client: HttpClient = HttpClient.newBuilder().build()
) : HttpTransport {

    override suspend fun execute(
        request: HttpRequest
    ): HttpResponse = withContext(Dispatchers.IO) {

        val builder = JdkRequest.newBuilder()
            .uri(request.uri)

        request.timeoutMillis?.let {
            builder.timeout(Duration.ofMillis(it))
        }

        applyHeaders(builder, request)
        applyMethod(builder, request)

        val response = client.send(
            builder.build(),
            JdkResponse.BodyHandlers.ofByteArray()
        )

        HttpResponse(
            statusCode = response.statusCode(),
            headers = response.headers().map(),
            body = response.body() ?: ByteArray(0)
        )
    }

    private fun applyHeaders(
        builder: JdkRequest.Builder,
        request: HttpRequest
    ) {
        request.headers.forEach { (key, value) ->
            builder.header(key, value)
        }
    }

    private fun applyMethod(
        builder: JdkRequest.Builder,
        request: HttpRequest
    ) {
        when (request.method) {
            HttpMethod.GET -> builder.GET()
            HttpMethod.DELETE -> builder.DELETE()
            HttpMethod.POST -> builder.POST(bodyPublisher(request.body))
            HttpMethod.PUT -> builder.PUT(bodyPublisher(request.body))
            HttpMethod.PATCH -> builder.method("PATCH", bodyPublisher(request.body))
            HttpMethod.HEAD -> builder.method("HEAD", bodyPublisher(request.body))
            HttpMethod.OPTIONS -> builder.method("OPTIONS", bodyPublisher(request.body))
        }
    }

    private fun bodyPublisher(
        body: ByteArray?
    ): JdkRequest.BodyPublisher =
        body?.let { JdkRequest.BodyPublishers.ofByteArray(it) }
            ?: JdkRequest.BodyPublishers.noBody()
}
