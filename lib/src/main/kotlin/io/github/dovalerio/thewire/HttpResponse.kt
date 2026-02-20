package io.github.dovalerio.thewire

public class HttpResponse(
    public val statusCode: Int,
    public val headers: Map<String, List<String>>,
    public val body: ByteArray
) {
    public fun bodyAsString(): String =
        body.toString(Charsets.UTF_8)

    public val isSuccessful: Boolean
        get() = statusCode in 200..299
}
