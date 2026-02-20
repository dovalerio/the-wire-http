package io.github.dovalerio.thewire

import java.net.URI

public class HttpRequest(
    public val method: HttpMethod,
    public val uri: URI,
    public val headers: Map<String, String> = emptyMap(),
    public val body: ByteArray? = null,
    public val timeoutMillis: Long? = null
)
