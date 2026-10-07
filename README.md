# 🚀 Spring Boot URL Shortener API

A highly optimized, production-ready URL Shortener built with Spring Boot. This application converts long URLs into compact Base62 short links, instantly redirects users to their original destinations, tracks click analytics, and utilizes Redis caching to handle high-traffic read operations.

## ✨ Features

* **Base62 Encoding:** Efficiently compresses database IDs into short, alphanumeric strings.
* **Redis Caching (Cache-aside Pattern):** Bypasses the MySQL database for frequently accessed URLs, ensuring lightning-fast redirects and preventing database read spikes.
* **Click Analytics:** Tracks the number of times a short link is accessed using optimized JPQL `@Modifying` queries.
* **Comprehensive Testing:** Fully tested business logic and edge cases using JUnit 5 and Mockito.
* **Global Exception Handling:** Clean, structured error responses for missing or invalid URLs.

## 🛠️ Tech Stack

* **Language:** Java 17
* **Framework:** Spring Boot (Web, Data JPA, Cache)
* **Database:** MySQL
* **Caching:** Redis (via Docker)
* **Testing:** JUnit 5, Mockito
* **Build Tool:** Maven

## 📋 Prerequisites

Before running the application, ensure you have the following installed:
* [Java 17+](https://adoptium.net/)
* [Docker Desktop](https://www.docker.com/products/docker-desktop/) (for Redis)
* [MySQL Server](https://dev.mysql.com/downloads/)

## 🚀 Getting Started

### 1. Database Setup
Create a blank database in your local MySQL instance:
```sql
CREATE DATABASE url_shortener;
```
Update your `src/main/resources/application.properties` with your MySQL credentials:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### 2. Start the Redis Cache
Use Docker to spin up a lightweight Redis container in the background:
```bash
docker run --name redis-cache -p 6379:6379 -d redis:alpine
```

### 3. Run the Application
Build and run the Spring Boot application using the Maven wrapper:
```bash
./mvnw spring-boot:run
```
The server will start on `http://localhost:8080`.

## 🔌 API Usage

### 1. Create a Short URL
* **Endpoint:** `POST /api/url`
* **Content-Type:** `application/json`
* **Payload:**
  ```json
  {
      "longUrl": "[https://www.example.com/very-long-article-path](https://www.example.com/very-long-article-path)"
  }
  ```
* **Response (String):** `http://localhost:8080/b`

### 2. Redirect & Track Analytics
* **Endpoint:** `GET /{shortUrl}` (e.g., `http://localhost:8080/b`)
* **Behavior:** 
  1. Checks Redis cache for the URL.
  2. If found (Cache Hit), instantly redirects the user (HTTP 302).
  3. If not found (Cache Miss), queries MySQL, saves the result to Redis, and redirects the user.
  4. Asynchronously increments the `clickCount` statistic in the database.

## 🧪 Running Tests
This project includes a comprehensive test suite isolating the core service logic from the database layer. To execute the JUnit/Mockito tests, run:
```bash
./mvnw test
```
