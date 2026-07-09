# 🏨 Hotel Management System

[![CI](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/ci.yml)
[![CD](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/cd.yml/badge.svg)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/cd.yml)
[![CodeQL](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/codeql.yml/badge.svg)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/codeql.yml)
[![PR Validation](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/pr-validation.yml/badge.svg)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/pr-validation.yml)
[![Release](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/release.yml/badge.svg)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/release.yml)
[![Security Scan](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/security-scan.yml/badge.svg)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/actions/workflows/security-scan.yml)
[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Oracle](https://img.shields.io/badge/Oracle-19c-red.svg)](https://www.oracle.com/database/)
[![License](https://img.shields.io/github/license/soumyapriyagoswami/hotel-management-system-backend)](LICENSE)
[![GitHub Stars](https://img.shields.io/github/stars/soumyapriyagoswami/hotel-management-system-backend)](https://github.com/soumyapriyagoswami/hotel-management-system-backend/stargazers)

> A comprehensive Hotel Management System REST API built with **Spring Boot**, **Oracle Database**, and **Spring Security (JWT)**.

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#️-technology-stack)
- [Architecture](#️-architecture)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#️-configuration)
- [Database Setup](#️-database-setup)
- [Running the Application](#️-running-the-application)
- [API Documentation](#-api-documentation)
- [Security](#-security)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

---

## ✨ Features

### Core Features
- ✅ **Room Management** — CRUD operations for hotel rooms
- ✅ **Guest Management** — Manage guest information and history
- ✅ **Booking System** — Create, confirm, cancel, check-in, check-out
- ✅ **Payment Processing** — Secure payment handling
- ✅ **User Authentication** — JWT-based authentication
- ✅ **Role-Based Access Control** — Admin, Manager, Receptionist roles
- ✅ **Real-time Availability** — Check room availability
- ✅ **Dashboard Analytics** — Statistics and reports

### Security Features
- 🔐 JWT Authentication
- 🔐 Role-based Authorization (`ADMIN`, `MANAGER`, `RECEPTIONIST`)
- 🔐 Password Encryption (BCrypt)
- 🔐 Token Refresh Mechanism
- 🔐 Secure API Endpoints

### Technical Features
- 🚀 RESTful API Design
- 🚀 Swagger / OpenAPI Documentation
- 🚀 CI/CD Pipeline with GitHub Actions
- 🚀 Oracle Database Integration
- 🚀 Comprehensive Exception Handling
- 🚀 Input Validation
- 🚀 Unit & Integration Tests

---

## 🛠️ Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming Language |
| Spring Boot | 3.1.5 | Application Framework |
| Spring Security | 3.1.5 | Authentication & Authorization |
| Spring Data JPA | 3.1.5 | ORM & Database Operations |
| Oracle Database | 19c+ | Production Database |
| JWT | 0.11.5 | Token-based Authentication |
| Lombok | 1.18.30 | Code Generation |
| Maven | 3.9.6 | Build Tool |
| JUnit | 5.10.0 | Testing Framework |
| SpringDoc OpenAPI | 2.2.0 | API Documentation |
| GitHub Actions | — | CI/CD Pipeline |

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                          CLIENT LAYER                            │
│                    (Web / Mobile / Postman)                      │
└────────────────────┬──────────────────────────────────────────-─┘
                      │
┌────────────────────▼─────────────────────────────────────────---─┐
│                     PRESENTATION LAYER                            │
│                      (REST Controllers)                           │
│  RoomController | GuestController | BookingController             │
│  PaymentController | AuthController | DashboardController         │
└────────────────────┬─────────────────────────────────────────---─┘
                      │
┌────────────────────▼─────────────────────────────────────────---─┐
│                       BUSINESS LAYER                               │
│                          (Services)                                │
│  RoomService | GuestService | BookingService                       │
│  PaymentService | AuthService | CustomUserDetailsService           │
└────────────────────┬──────────────────────────────────────────---─┘
                      │
┌────────────────────▼──────────────────────────────────────────---─┐
│                     DATA ACCESS LAYER                               │
│                       (Repositories)                                │
│  RoomRepository | GuestRepository | BookingRepository               │
│  PaymentRepository | UserRepository                                 │
└────────────────────┬───────────────────────────────────────────---─┘
                      │
┌────────────────────▼───────────────────────────────────────────---─┐
│                       DATABASE LAYER                                 │
│                       (Oracle Database)                              │
│  ROOMS | GUESTS | BOOKINGS | PAYMENTS | USERS | USER_ROLES           │
└─────────────────────────────────────────────────────────────────---─┘
```

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- ✅ **Java JDK 17** or higher
- ✅ **Maven 3.6+** (or use the included Maven Wrapper)
- ✅ **Oracle Database 19c+** (or 11g+)
- ✅ **Git** (for version control)
- ✅ **VS Code** or **IntelliJ IDEA** (recommended)
- ✅ **SQL Developer** or **SQL\*Plus** (for database management)

---

## 💻 Installation

### 1. Clone the Repository

```bash
# Clone the project
git clone https://github.com/soumyapriyagoswami/hotel-management-system-backend.git

# Navigate to project directory
cd hotel-management-system-backend
```

### 2. Build the Project

```bash
# Clean and compile
mvn clean compile

# Build without tests
mvn clean package -DskipTests

# Full build with tests
mvn clean install
```

### 3. Install VS Code Extensions (Recommended)

**Core Extensions**
- Java Extension Pack
- Spring Boot Extension Pack
- Lombok Annotations Support
- Oracle SQL Developer for VS Code

**Additional Extensions**
- REST Client
- GitLens
- GitHub Actions
- Thunder Client (API Testing)

---

## ⚙️ Configuration

### `application.properties`

Update `src/main/resources/application.properties` with your Oracle database configuration:

```properties
# Oracle Database Configuration
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XE
spring.datasource.username=system
spring.datasource.password=your_password_here

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.OracleDialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=your-256-bit-secret-key-for-jwt-authentication
jwt.expiration=86400000
jwt.refresh-expiration=604800000
jwt.issuer=hotel-management-system

# Server Configuration
server.port=8080

# Logging
logging.level.com.hotel.hotel_management_system=DEBUG
logging.level.org.springframework.security=DEBUG
```

### Environment Variables (Optional)

```bash
# Database
export DATABASE_URL="jdbc:oracle:thin:@localhost:1521/XE"
export DATABASE_USERNAME="system"
export DATABASE_PASSWORD="your_password"

# JWT
export JWT_SECRET="your-secret-key"

# Server
export SERVER_PORT=8080
```

---

## 🗄️ Database Setup

### 1. Create Database Schema

```bash
# Using SQL*Plus
sqlplus system/your_password@localhost:1521/XE @scripts/database/setup-database.sql

# Using SQL Developer
# Open and execute scripts/database/setup-database.sql
```

### 2. Verify Database Tables

```sql
-- Check tables
SELECT table_name FROM user_tables;

-- Check sequences
SELECT sequence_name FROM user_sequences;

-- Check sample data
SELECT * FROM ROOMS;
SELECT * FROM GUESTS;
SELECT * FROM USERS;
```

---

## ▶️ Running the Application

**Option 1 — Using Maven**
```bash
# Run the application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Option 2 — Using Java JAR**
```bash
# Build JAR
mvn clean package -DskipTests

# Run JAR
java -jar target/hotel-management-system-0.0.1-SNAPSHOT.jar
```

**Option 3 — Using VS Code**
1. Open `HotelManagementSystemApplication.java`
2. Click the **Run** button above the main method
3. Or press `Ctrl+F5`

**Option 4 — Using Spring Boot Dashboard**
1. Click the Spring Boot Dashboard icon in the Activity Bar
2. Find `hotel-management-system-backend`
3. Click **Run** (▶)

---

## 📚 API Documentation

**Base URL**
```
http://localhost:8080
```

**Swagger UI (Interactive Documentation)**
```
http://localhost:8080/swagger-ui.html
```

**OpenAPI JSON**
```
http://localhost:8080/v3/api-docs
```

### API Endpoints Summary

#### 🔐 Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| POST | `/api/auth/login` | Login user | Public |
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/refresh` | Refresh token | Public |
| POST | `/api/auth/change-password` | Change password | Authenticated |
| POST | `/api/auth/reset-password` | Reset password | Public |

#### 🏠 Rooms

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/rooms` | Get all rooms | ADMIN, MANAGER, RECEPTIONIST |
| GET | `/api/rooms/{id}` | Get room by ID | ADMIN, MANAGER, RECEPTIONIST |
| GET | `/api/rooms/available` | Get available rooms | Public |
| POST | `/api/rooms` | Create room | ADMIN, MANAGER |
| PUT | `/api/rooms/{id}` | Update room | ADMIN, MANAGER |
| DELETE | `/api/rooms/{id}` | Delete room | ADMIN |

#### 👤 Guests

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/guests` | Get all guests | ADMIN, MANAGER, RECEPTIONIST |
| GET | `/api/guests/{id}` | Get guest by ID | ADMIN, MANAGER, RECEPTIONIST |
| POST | `/api/guests` | Create guest | ADMIN, MANAGER, RECEPTIONIST |
| PUT | `/api/guests/{id}` | Update guest | ADMIN, MANAGER, RECEPTIONIST |
| DELETE | `/api/guests/{id}` | Delete guest | ADMIN |

#### 📅 Bookings

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/bookings` | Get all bookings | ADMIN, MANAGER, RECEPTIONIST |
| POST | `/api/bookings` | Create booking | ADMIN, MANAGER, RECEPTIONIST |
| PATCH | `/api/bookings/{id}/confirm` | Confirm booking | ADMIN, MANAGER, RECEPTIONIST |
| PATCH | `/api/bookings/{id}/checkin` | Check-in | ADMIN, MANAGER, RECEPTIONIST |
| PATCH | `/api/bookings/{id}/checkout` | Check-out | ADMIN, MANAGER, RECEPTIONIST |
| DELETE | `/api/bookings/{id}` | Cancel booking | ADMIN, MANAGER, RECEPTIONIST |

#### 💳 Payments

| Method | Endpoint | Description | Access |
|---|---|---|---|
| GET | `/api/payments` | Get all payments | ADMIN, MANAGER, RECEPTIONIST |
| POST | `/api/payments` | Process payment | ADMIN, MANAGER, RECEPTIONIST |
| GET | `/api/payments/revenue/total` | Get total revenue | ADMIN, MANAGER |

---

## 🔐 Security

### Authentication Flow

```
1. User Login
   └── POST /api/auth/login
       ├── Validate credentials
       └── Generate JWT token

2. Token Usage
   └── Include in requests
       └── Header: Authorization: Bearer <token>

3. Token Validation
   └── JWT Filter intercepts
       ├── Validate token
       ├── Extract user details
       └── Set authentication context

4. Authorization
   └── Check user roles
       ├── ADMIN: Full access
       ├── MANAGER: Management access
       └── RECEPTIONIST: Basic access
```

### Default Users

| Username | Password | Roles |
|---|---|---|
| admin | Admin123! | ADMIN |
| manager | Manager123! | MANAGER |
| receptionist | Reception123! | RECEPTIONIST |

> ⚠️ **Note:** Change these default credentials before deploying to production.

---

## 🔄 CI/CD Pipeline

The project includes six GitHub Actions workflows under `.github/workflows/`:

| Workflow | File | Trigger | Purpose |
|---|---|---|---|
| **Continuous Integration** | `ci.yml` | Push to `main`, `develop`, `feature/*` | Build, compile, and run unit/integration tests |
| **Continuous Deployment** | `cd.yml` | Push to `main` (automated) or manual dispatch | Build deployment package, deploy, and run health checks |
| **CodeQL Analysis** | `codeql.yml` | Push, PR, and weekly schedule | Static code analysis for security vulnerabilities and code quality |
| **PR Validation** | `pr-validation.yml` | Pull request opened/updated | Lint, build, and test checks before a PR can be merged |
| **Release** | `release.yml` | Tag push / manual dispatch | Builds and publishes versioned release artifacts |
| **Security Scan** | `security-scan.yml` | Weekly schedule | OWASP dependency check and vulnerability report |

### 1. Continuous Integration (CI)
- **Trigger:** Push to `main`, `develop`, `feature/*`
- **Jobs:** Build → Test → Upload Artifact

### 2. Continuous Deployment (CD)
- **Trigger:** Push to `main` (automated) or manual dispatch
- **Jobs:** Build deployment package → Deploy → Health check

### 3. CodeQL Analysis
- **Trigger:** Push, pull request, and weekly schedule
- **Jobs:** Static analysis → Security alerts → Code scanning report

### 4. PR Validation
- **Trigger:** Pull request opened, synchronized, or reopened
- **Jobs:** Lint → Build → Test → Status checks

### 5. Release
- **Trigger:** New version tag pushed, or manual dispatch
- **Jobs:** Build release artifact → Generate changelog → Publish GitHub Release

### 6. Security Scan
- **Trigger:** Weekly schedule
- **Jobs:** OWASP Dependency Check → Code Quality Scan → Vulnerability Report

### Viewing Pipeline Status

```bash
# View workflow runs
gh run list

# View specific workflow
gh run view <run-id>

# Watch workflow progress
gh run watch <run-id>
```

---

## 🧪 Testing

### Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=RoomControllerTest

# Run tests with coverage report
mvn test jacoco:report
```

### Integration Tests

```bash
# Run integration tests
mvn verify

# Run tests with Spring Boot
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### Test Coverage Report

```bash
# Generate coverage report
mvn jacoco:report

# Open report
open target/site/jacoco/index.html
```

---

## 🚀 Deployment

### Production Deployment

**1. Build the application**
```bash
mvn clean package -Pproduction -DskipTests
```

**2. Prepare deployment package**
```bash
mkdir -p deploy
cp target/*.jar deploy/app.jar
cp application.properties deploy/
cp scripts/deployment/deploy.sh deploy/
```

**3. Execute deployment script**
```bash
chmod +x deploy/deploy.sh
./deploy/deploy.sh production
```

### Manual Deployment Steps

```bash
# 1. Stop current application
ps aux | grep app.jar | grep -v grep | awk '{print $2}' | xargs kill

# 2. Backup current version
cp app.jar backups/app.jar.$(date +%Y%m%d)

# 3. Deploy new version
cp target/*.jar app.jar

# 4. Start application
nohup java -jar app.jar --spring.profiles.active=prod > app.log 2>&1 &

# 5. Check health
curl http://localhost:8080/actuator/health
```

---

## 📁 Project Structure

```
hotel-management-system-backend/
├── .github/                          # GitHub Actions workflows
│   └── workflows/
│       ├── cd.yml                    # Continuous Deployment
│       ├── ci.yml                    # Continuous Integration
│       ├── codeql.yml                # CodeQL security analysis
│       ├── pr-validation.yml         # Pull request checks
│       ├── release.yml               # Release automation
│       └── security-scan.yml         # Dependency & vulnerability scanning
├── scripts/                          # Scripts for deployment and DB
│   ├── database/
│   │   └── setup-database.sql        # Database setup script
│   └── deployment/
│       └── deploy.sh                 # Deployment script
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/hotel/hotel_management_system/
│   │   │       ├── config/           # Configuration classes
│   │   │       ├── controller/       # REST Controllers
│   │   │       ├── model/            # Entity classes
│   │   │       ├── repository/       # Repository interfaces
│   │   │       ├── service/          # Service layer
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── exception/        # Exception handling
│   │   │       ├── security/         # Security components
│   │   │       └── HotelManagementSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties       # Main configuration
│   │       └── application-dev.properties   # Development profile
│   └── test/                                # Test classes
├── target/                                  # Build output
├── .gitignore
├── HELP.md
├── mvnw                                     # Maven wrapper
├── mvnw.cmd
├── pom.xml                                  # Maven dependencies
└── README.md
```

---

## 🤝 Contributing

### How to Contribute

1. Fork the repository
2. Create a feature branch
   ```bash
   git checkout -b feature/amazing-feature
   ```
3. Make your changes and commit
   ```bash
   git add .
   git commit -m "Add amazing feature"
   ```
4. Push to the branch
   ```bash
   git push origin feature/amazing-feature
   ```
5. Open a Pull Request

### Pull Request Guidelines

- ✅ Follow the existing code style
- ✅ Add tests for new features
- ✅ Update documentation
- ✅ Keep commits focused and atomic
- ✅ Ensure all tests pass

### Code Review Checklist

- [ ] Code follows project conventions
- [ ] Unit tests added/updated
- [ ] Integration tests pass
- [ ] No security vulnerabilities
- [ ] Documentation updated

---

## 📝 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 📧 Contact

- **Project Maintainer:** Your Name
- **Email:** your.email@example.com
- **GitHub:** [@soumyapriyagoswami](https://github.com/soumyapriyagoswami)

---

## 🙏 Acknowledgments

- Spring Boot Team for the amazing framework
- Oracle for the database
- All contributors who help improve this project

---

## 📊 Project Status

- ✅ Core Features Implemented
- ✅ Security Implemented
- ✅ CI/CD Pipeline Configured
- ✅ Documentation Complete
- 🚀 Ready for Production

---

## 🎯 Quick Start Commands

```bash
# Clone and build
git clone https://github.com/soumyapriyagoswami/hotel-management-system-backend.git
cd hotel-management-system-backend
mvn clean install

# Run application
mvn spring-boot:run

# Access Swagger UI
http://localhost:8080/swagger-ui.html

# Login with default admin
Username: admin
Password: Admin123!

# Test API
curl -X GET http://localhost:8080/api/rooms \
  -H "Authorization: Bearer <your-jwt-token>"
```

---

## 🔗 Useful Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT Documentation](https://jwt.io/introduction)
- [Oracle Database Documentation](https://docs.oracle.com/en/database/)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

⭐ **If you find this project useful, please give it a star on GitHub!**

*Last Updated: July 2026*