<div align="center">

# 🧭 Live Auction Service — Real-Time Bidding Backend API

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green.svg)](https://spring.io/projects/spring-boot)
[![WebSocket](https://img.shields.io/badge/WebSocket-STOMP-purple.svg)](https://stomp.github.io/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-orange.svg)](https://jwt.io/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**A real-time Spring Boot backend enabling live auctions, bid streaming, and JWT-secured access — built with scalable event-driven design.**

> *Bid smarter, faster, and securely — powering real-time commerce one event at a time!*

</div>

---

## 🌟 Key Features

- 🔐 **JWT Authentication**: Secure registration and login system with token-based authentication.
- ⚡ **Live Bidding via WebSocket**: Real-time updates using STOMP over WebSocket.
- 🧩 **Auction Management**: Create, manage, and close auctions dynamically.
- 💬 **Bid Broadcasting**: Automatically updates all connected clients when new bids arrive.
- 🧱 **Modular Architecture**: Clean separation between controllers, services, and repositories.
- ⚙️ **Centralized Exception Handling**: Global handler for consistent API responses.
- 🧠 **Scalable Backend Design**: Built with flexibility for microservice extension and Kafka integration.

---

## ⚙️ Exception Handling

All exceptions are managed globally via `GlobalExceptionHandler` ensuring consistent and secure error responses.

| Exception | Status Code | Description |
| :--- | :--- | :--- |
| `ResourceNotFoundException` | 404 | Entity not found (e.g., Auction or User missing) |
| `InvalidBidException` | 400 | Invalid bid (e.g., lower than current highest) |
| `JwtAuthenticationException` | 401 | Invalid or expired JWT token |
| `SystemException` | 500 | Unexpected internal server errors |

Example error response:
```json
{
  "message": "Bid amount must be higher than the current highest bid.",
  "status": 400,
  "error": "BAD_REQUEST",
  "timestamp": "2025-11-01T12:00:00Z"
}
