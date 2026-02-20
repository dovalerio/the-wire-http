package io.github.dovalerio.thewire.middleware

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

public class LoggingMiddleware(
    private val logHeaders: Boolean = false
) : HttpMiddleware {

    private val logger = LoggerFactory.getLogger(LoggingMiddleware::class.java)

    override suspend fun intercept(
        request: HttpRequest,
        chain: MiddlewareChain
    ): HttpResponse {

        if (logger.isInfoEnabled) {
            logger.info(
                "the-wire.request method={} uri={}",
                request.method,
                request.uri
            )

            if (logHeaders) {
                logger.debug(
                    "the-wire.request.headers {}",
                    request.headers
                )
            }
        }

        var response: HttpResponse
        val duration = measureTimeMillis {
            response = chain.proceed(request)
        }

        if (logger.isInfoEnabled) {
            logger.info(
                "the-wire.response status={} durationMs={}",
                response.statusCode,
                duration
            )
        }

        return response
    }
}
