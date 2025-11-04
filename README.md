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
```
---
## 🔗 API Details

| Detail | Value |
| :--- | :--- |
| **Base URL** | `http://localhost:8081/api` |
| **Data Format** | JSON |
| **Authentication** | Bearer Token (JWT) |
| **WebSocket Endpoint** | `/live-bids` *(STOMP protocol)* |

---

## 👥 User Management

| Method | Endpoint | Purpose | Security |
| :--- | :--- | :--- | :--- |
| **`POST`** | `/api/auth/register` | Register new user | Public |
| **`POST`** | `/api/auth/login` | Login & receive JWT token | Public |
| **`GET`** | `/api/users/{id}` | Fetch user details | Auth Required |
| **`GET`** | `/api/users` | List all users | Admin Only |
| **`DELETE`** | `/api/users/{id}` | Delete user | Admin Only |

---

## 🎯 Auction Management

| Method | Endpoint | Purpose | Security |
| :--- | :--- | :--- | :--- |
| **`POST`** | `/api/auctions` | Create new auction | Admin Only |
| **`GET`** | `/api/auctions` | List all auctions | Public |
| **`GET`** | `/api/auctions/{id}` | Get auction details | Public |
| **`PUT`** | `/api/auctions/{id}` | Update auction details | Admin Only |
| **`DELETE`** | `/api/auctions/{id}` | Delete auction | Admin Only |

---

## 💸 Bid Management

| Method | Endpoint | Purpose | Security |
| :--- | :--- | :--- | :--- |
| **`POST`** | `/api/bids` | Place a new bid *(broadcasted live via WebSocket)* | Auth Required |
| **`GET`** | `/api/bids/highest/{auctionId}` | Get current highest bid for an auction | Public |
| **`GET`** | `/api/bids/{auctionId}` | View all bids for a specific auction | Public |

---

## ⚙️ WebSocket Details

| Detail | Description |
| :--- | :--- |
| **Endpoint** | `/live-bids` |
| **Protocol** | STOMP over WebSocket |
| **Broker Prefix** | `/topic` |
| **Subscription Example** | `/topic/auction/{auctionId}` |
| **Message Type** | JSON |
| **Broadcast Behavior** | Real-time updates when new bid is placed |

---

## 📬 Sample JSON Payloads

### 🧍 User Registration
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

---

## 🛠 Project Setup & Quick Start

Get the **Live Auction Service** running on your local machine in minutes!

### 🧩 Build and Run

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/LakshyaDhouchak/live-auction-service.git
   cd live-auction-service


