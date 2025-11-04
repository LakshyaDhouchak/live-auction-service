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

## 🛠 Project Setup & Quick Start

Get the **Live Auction Service** running on your local machine in minutes!

### 🧩 Build and Run

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/LakshyaDhouchak/live-auction-service.git
    cd live-auction-service
    ```

2.  **Build and Start:**
    ```bash
    mvn clean install
    mvn spring-boot:run  # Starts on http://localhost:8081
    ```

---

### ⚙️ Configuration Essentials

Update your **database**, **JWT**, and **WebSocket** settings in  
`src/main/resources/application.properties`.

| Area | Key Properties | Default Values (Change These!) |
| :--- | :--- | :--- |
| **Database (MySQL)** | `spring.datasource.url` | `jdbc:mysql://localhost:3306/live_auction_db` |
|  | `spring.datasource.username` | `root` |
|  | `spring.datasource.password` | `lakshya5911` |
| **Security (JWT)** | `application.security.jwt.secret-key` | `your256bitsecret` (Must be strong for production) |
|  | `application.security.jwt.expiration` | `86400000` (24 hrs) |
| **WebSocket** | `spring.websocket.endpoint` | `/live-bids` |
| **Logging** | `logging.level.com.lakshya.live_auction` | `DEBUG` (Good for development) |

---

## ⚙️ Database Entities

- ✅ **User** — Core entity representing bidders and admins (id, username, email (unique), hashedPassword, role [USER, ADMIN], createdAt).  
- ✅ **Auction** — Represents each live auction (id, title, description, startTime, endTime, basePrice, currentHighestBid, status).  
- ✅ **Bid** — Tracks each placed bid (id, amount, bidder (ManyToOne → User), auction (ManyToOne → Auction), timestamp).  
- ✅ **Role** — Defines user roles for Spring Security authorization (id, name).

**Relationships & Optimizations:**  
Each **Auction** can have multiple **Bids**, and each **Bid** is linked to a **User** and an **Auction** using `@ManyToOne` relationships.  
Indexes on `auctionId`, `bidAmount`, and `userId` ensure fast leaderboard lookups and real-time bid updates.  
Optimized using JPA + Hibernate lazy loading and cascading operations.

---

## 🚀 Roadmap

- ⚡ **Real-Time Leaderboard** — Display top bidders dynamically using WebSocket updates.  
- 💬 **Bid Notifications** — Push live updates instantly to all connected clients.  
- 📧 **Email Alerts** — Notify winners and auctioneers post-auction (Kafka or RabbitMQ integration).  
- 🧩 **Frontend Integration** — Connect React/Angular frontend for an interactive live bidding experience.  
- 🔒 **Role-Based Access Control** — Extend roles (Admin, Seller, Bidder) with permissions.  
- 💸 **Payment Gateway** — Integrate Stripe/PayPal for deposits or winning payments.  
- ☁️ **Cloud Deployment** — Dockerize and deploy on AWS ECS or Azure App Service.  
- 📊 **Analytics Dashboard** — Track auction stats, bid frequency, and overall revenue insights.  

---

## 🤝 Contributing

- 1️⃣ **Fork** the repo and create your own copy.  
- 2️⃣ **Create Branch**:  
   ```bash
   git checkout -b feature/add-leaderboard

---

# 🏆 Live Auction Event System

## 📘 Overview

The **Live Auction Event System** powers real-time bidding, enabling multiple users to compete simultaneously in active auctions.  
Each bid is instantly broadcasted via **WebSocket (STOMP)**, ensuring all participants see live updates — *no page refresh needed*.  

This project follows an **Event-Driven Architecture**, making it scalable and ready for microservice integration with modules like **notifications**, **payments**, and **analytics**.

---

## 🎯 Purpose & Goals

- ⚡ **Enable True Real-Time Bidding** — Achieve sub-second latency for bid broadcasts.  
- 🔐 **Ensure Secure User Authentication** — Use JWT for secure token validation.  
- 🧩 **Simplify Auction Management** — Admins can create, close, or extend auctions easily.  
- 📢 **Broadcast Live Updates** — Notify bidders instantly when new bids arrive.  
- 🧠 **Maintain Data Consistency** — Handle concurrent bid submissions safely and fairly.  

---

## ⚙️ How It Works

1. **Admin creates** an auction with a start and end time.  
2. **Bidders connect** to the WebSocket endpoint `/live-bids`.  
3. When a **bid is placed**:
   - It’s validated and saved in the database.  
   - Then broadcasted to `/topic/auction/{id}` subscribers.  
   - The system updates the highest bid dynamically.  
4. When the time ends, the **winner is automatically announced**.

---

### 🧠 Example STOMP Subscription

```js
stompClient.subscribe("/topic/auction/12", (message) => {
  console.log("New bid received:", JSON.parse(message.body));
});



