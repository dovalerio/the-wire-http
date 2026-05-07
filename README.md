# The Wire HTTP

![CI](https://github.com/dovalerio/the-wire-http/actions/workflows/ci.yml/badge.svg)
![Coverage](https://img.shields.io/badge/coverage-65%25-yellow)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-purple)
![Maven Central](https://img.shields.io/badge/Maven%20Central-0.1.0-blue)

A lightweight, middleware-driven HTTP client for Kotlin.

Stop rewriting HTTP configuration in every project.
The Wire provides a simple, extensible pipeline for building robust HTTP clients with logging, retry, and custom behavior — without heavy frameworks.

---

## ✨ Why The Wire?

Most projects end up repeating the same boilerplate:

- Configure Java HttpClient
- Add logging
- Add retry logic
- Handle timeouts
- Wrap responses
- Repeat

The Wire centralizes all of that in a clean, composable middleware pipeline.

You focus on:
- Base URL
- Routes
- Request/response types

Everything else becomes infrastructure.

---

## 🚀 Features

- Kotlin-first API
- Middleware pipeline (Chain of Responsibility)
- Java 21 HttpClient under the hood
- Structured logging support
- Retry middleware support
- Lightweight and framework-free
- Fully testable
- No reflection magic

---

## 🧱 Architecture

HttpClient
↓
Middleware (Logging, Retry, Custom...)
↓
CoreHttpClient (Java 21)
yaml
Copiar

Each middleware decides:
- Inspect request
- Modify request
- Retry
- Log
- Short-circuit
- Or delegate to the next layer

---

## 📦 Installation

_(Coming soon — publish to Maven Central or GitHub Packages)_

---

## 🧪 Example Usage

```kotlin
val client = HttpClient(
    middlewares = listOf(
        LoggingMiddleware(),
        RetryMiddleware()
    ),
    core = CoreHttpClient(java.net.http.HttpClient.newHttpClient())::execute
)

val request = HttpRequest(
    method = HttpMethod.GET,
    uri = URI.create("https://api.example.com/users")
)

val response = client.execute(request)
println(response.statusCode)
 
🎯 Philosophy
The Wire is not a framework.
It is not an annotation engine.
It is not magic.
It is:
• 
Simple
• 
Predictable
• 
Explicit
• 
Extensible
Infrastructure should be boring.
And reliable.
 