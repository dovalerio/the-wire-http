package io.github.dovalerio.thewire.dsl

public class RetryConfigDsl {

    public var maxAttempts: Int = 3
    public var initialDelayMillis: Long = 200
    public var backoffMultiplier: Double = 2.0
}