# SneakerHub — Sneaker Auction Marketplace Backend

A realtime sneaker auction platform built with Spring Boot, inspired by StockX and GOAT.

## Tech Stack
- **Backend:** Spring Boot 4, Java 21
- **Database:** MySQL 8 with Hibernate/JPA
- **Security:** Spring Security + JWT Authentication
- **Realtime:** Spring WebSocket + STOMP Protocol
- **Build:** Maven

## Features
- JWT-based authentication (Register, Login, Role-based access)
- Sneaker listing management
- Auction creation with duration control
- Realtime bidding via WebSocket — live price updates across all connected clients
- Bid validation (amount, auction status, seller restrictions)
- RESTful API design

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT token |

### Auctions
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auctions/create | Create a new auction |
| GET | /api/auctions | Get all active auctions |
| GET | /api/auctions/{id} | Get auction by ID |
| POST | /api/auctions/bid | Place a bid |

### WebSocket
| Destination | Description |
|---|---|
| /app/bid | Send a bid |
| /topic/auction/{id} | Subscribe to live auction updates |

## Database Schema
- **users** — id, name, email, password, role, wallet_balance
- **sneakers** — id, title, brand, description, size, sneaker_condition, image_url, seller_id
- **auctions** — id, sneaker_id, start_price, current_price, start_time, end_time, status, seller_id, winner_id
- **bids** — id, auction_id, user_id, bid_amount, bid_time

## Setup & Run

### Prerequisites
- Java 21
- MySQL 8
- Maven

### Steps
1. Clone the repo 
git clone https://github.com/YOUR_USERNAME/sneakerhub-backend.git


2. Create MySQL database
```sql
   CREATE DATABASE sneakerhub;
```

3. Configure `application.properties`
```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/sneakerhub
   spring.datasource.username=root
   spring.datasource.password=YOUR_PASSWORD
```

4. Run
mvn spring-boot:run

## Project Structure
src/main/java/com/sneakerhub/sneakerhub/

├── config/ # WebSocket configuration

├── controller/ # REST + WebSocket controllers

├── service/ # Business logic

├── repository/ # Database layer

├── entity/ # JPA entities (DB tables)

├── dto/ # Request/Response objects

└── security/ # JWT filter, config
