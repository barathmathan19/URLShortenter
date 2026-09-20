# URL Shortener API

A RESTful backend service built with Spring Boot that converts long web addresses into compact, easily shareable links. This project was built from scratch to solidify core backend engineering principles and transition from basic application scripting to production-ready API design.

## Concepts Applied & Learned

*   **Three-Tier Architecture:** Structured the application strictly into Controller, Service, and Repository layers to separate HTTP routing, business logic, and database operations.
*   **Algorithmic Encoding:** Implemented a Base62 conversion algorithm to translate auto-incrementing database primary keys into short, alphanumeric URL strings.
*   **Data Persistence (Spring Data JPA):** Replaced volatile in-memory storage with a permanent MySQL database, utilizing Hibernate to automatically manage table schemas.
*   **Input Validation & DTOs:** Secured the API against malformed payloads by implementing Data Transfer Objects (DTOs) alongside `@Valid` and `@URL` annotations to block bad requests before they reach the business logic.
*   **Global Exception Handling:** Replaced default Spring Boot HTML error pages with standardized, professional JSON responses using `@RestControllerAdvice` and custom HTTP status codes (e.g., intercepting failed database lookups to return a clean `404 Not Found`).

## Tech Stack

*   **Language:** Java 17
*   **Framework:** Spring Boot (Web, Data JPA, Validation)
*   **Database:** MySQL 8.0
*   **Build Tool:** Maven

## Getting Started

### Prerequisites
*   Java 17 or higher
*   MySQL Server running locally on port 3306

### Installation

1. Clone the repository:
   ```bash
   git clone [https://github.com/your-username/url-shortener.git](https://github.com/barathmathan19/URLShortener.git)
