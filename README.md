# ServerListOnGo

ServerListOnGo is a Spring Boot REST API for a list and product management platform. It supports user registration and login, product listing with image upload, admin approval workflows, credit management, OTP-based password recovery, and grouped shopping-list history.

## Features

- User signup and login with BCrypt password hashing.
- OTP email flow for account verification and password reset.
- Product upload with multipart image storage in PostgreSQL.
- Product search and filtering by category, title, nickname, and keyword.
- Admin approval workflow for products and admin-role requests.
- Credit purchase and credit usage APIs with email notifications.
- User shopping-list creation, clearing, and grouping by timestamp.
- Health-check endpoint for server availability.

## Tech Stack

- Java 21
- Spring Boot 3.5.3
- Spring Web
- Spring Data JPA
- PostgreSQL
- Spring Mail
- Spring Security BCrypt
- Maven
- Lombok

## Project Structure

```text
src/main/java/com/ListOnGo/ServerListOnGo
|-- Api              # API base URL helper
|-- Controllar       # REST controllers
|-- Model            # JPA entities and DTOs
|-- Repository       # Spring Data repositories and custom queries
|-- Service          # Business logic and email services
`-- ServerListOnGoApplication.java
```

## Main API Areas

| Area | Example endpoints |
| --- | --- |
| Health | `GET /api/isServerOn` |
| Users | `POST /api/list-on-go/user/signup`, `POST /api/list-on-go/user/login` |
| OTP and password | `POST /api/list-on-go/user/send-otp`, `PUT /api/list-on-go/user/reset-pass` |
| Credits | `GET /api/list-on-go/user/get-credit`, `PUT /api/list-on-go/user/buy-credit` |
| Admin workflow | `PUT /api/list-on-go/user/request-admin`, `GET /api/list-on-go/user/pen-req`, `PUT /api/list-on-go/user/approve-admin` |
| Products | `POST /api/list-on-go/product/add-product`, `GET /api/list-on-go/product/get-all-product`, `GET /api/list-on-go/product/get-filter-product` |
| Lists | `POST /api/list-on-go/list/create-list`, `GET /api/list-on-go/list/grouped-by-time`, `DELETE /api/list-on-go/list/clear-list` |

## Getting Started

### Prerequisites

- Java 21
- Maven, or the included Maven wrapper
- PostgreSQL database
- SMTP credentials for email features

### Configure Environment

Update `src/main/resources/application.properties` with your local database and email settings.

Typical required values include:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.mail.username=your_email
spring.mail.password=your_app_password
```

Do not commit real passwords, mail app passwords, or production database credentials.

### Run Locally

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API runs on:

```text
http://localhost:8080
```

### Build

```bash
./mvnw clean package
```

On Windows:

```bash
mvnw.cmd clean package
```

The packaged JAR is generated as:

```text
target/listongo.jar
```

## Resume Bullet Points

- Built a Java 21 Spring Boot REST API for a list and product management platform with user authentication, product upload, admin approval, and credit-based workflows.
- Designed PostgreSQL-backed JPA entities and repositories for users, products, and shopping lists, including custom native queries for search, filtering, approval, and account operations.
- Implemented secure login and password reset flows using BCrypt password hashing, OTP validation, and email notifications through Spring Mail.
- Developed multipart product image upload and retrieval APIs, enabling products to store metadata and binary image data directly through backend services.
- Created admin moderation APIs for product approvals and user admin-role requests, supporting approval history, request status tracking, and notification emails.
- Added shopping-list APIs that group user list items by timestamp for organized purchase-history retrieval.

## Author

Soumen Maity
