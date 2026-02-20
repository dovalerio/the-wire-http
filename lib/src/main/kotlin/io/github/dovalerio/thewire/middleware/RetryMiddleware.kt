package io.github.dovalerio.thewire.middleware

import io.github.dovalerio.thewire.HttpRequest
import io.github.dovalerio.thewire.HttpResponse
import io.github.dovalerio.thewire.isIdempotent
import kotlinx.coroutines.delay
import kotlin.math.pow

public class RetryMiddleware(
    private val maxAttempts: Int = 3,
    private val initialDelayMillis: Long = 200,
    private val backoffMultiplier: Double = 2.0
) : HttpMiddleware {

    override suspend fun intercept(
        request: HttpRequest,
        chain: MiddlewareChain
    ): HttpResponse {

        if (!request.method.isIdempotent) {
            return chain.proceed(request)
        }

        var lastException: Throwable? = null

        for (attempt in 1..maxAttempts) {

            try {
                val response = chain.proceed(request)

                if (response.statusCode < 500) {
                    return response
                }

                if (attempt == maxAttempts) {
                    return response
                }

            } catch (ex: Throwable) {
                lastException = ex

                if (attempt == maxAttempts) {
                    throw ex
                }
            }

            val delayMillis =
                (initialDelayMillis * Math.pow(backoffMultiplier, (attempt - 1).toDouble())).toLong()

            if (delayMillis > 0) {
                kotlinx.coroutines.delay(delayMillis)
            }
        }

        throw lastException ?: IllegalStateException("Unreachable retry state")
    }
}