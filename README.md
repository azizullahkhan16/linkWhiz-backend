# LinkWhiz - URL shortening service

A robust and scalable URL shortening service built with Spring Boot, providing secure authentication, analytics tracking, and QR code generation capabilities.

## Introduction

LinkWhiz is a comprehensive URL shortening platform designed to help users create, manage, and track shortened URLs efficiently. The service offers advanced features including user authentication, OAuth2 integration, analytics tracking, QR code generation, and customizable URL expiration settings.

This project serves developers, businesses, and individuals who need a reliable URL shortening solution with enterprise-grade features like user management, detailed analytics, and secure authentication mechanisms.

## Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [API Overview](#api-overview)
- [Architecture](#architecture)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)

## Features

- **URL Shortening**: Create short, memorable URLs from long links
- **User Authentication**: Secure JWT-based authentication system
- **OAuth2 Integration**: Google and GitHub OAuth2 login support
- **QR Code Generation**: Generate QR codes for shortened URLs with customization options
- **Analytics Tracking**: Monitor click statistics and user engagement
- **Custom Aliases**: Create custom short URLs with preferred aliases
- **URL Expiration**: Set expiration dates for temporary links
- **User Management**: Complete user profile and account management
- **Plan-based System**: Subscription-based feature access
- **File Upload Support**: Handle file uploads up to 10MB
- **RESTful API**: Comprehensive REST API for all operations

## Tech Stack

### Backend Framework

- **Spring Boot 3.4.2**: Main application framework
- **Java 17**: Programming language
- **Maven**: Build tool and dependency management

### Database

- **PostgreSQL**: Primary database
- **Spring Data JPA**: Object-relational mapping
- **Hibernate**: JPA implementation

### Security

- **Spring Security**: Authentication and authorization
- **JWT (JSON Web Tokens)**: Stateless authentication
- **OAuth2 Client**: Third-party authentication (Google, GitHub)

### Additional Libraries

- **Lombok**: Reduces boilerplate code
- **ZXing**: QR code generation and processing
- **JJWT**: JWT token handling
- **Spring Validation**: Request validation

### Development Tools

- **Spring Boot DevTools**: Development convenience
- **Spring Boot Test**: Testing framework

## Project Structure

```
linkWhiz-backend/
├── src/
│   ├── main/
│   │   ├── java/com/aktic/linkWhiz_backend/
│   │   │   ├── config/                    # Configuration classes
│   │   │   │   ├── ApplicationConfig.java
│   │   │   │   ├── CorsConfig.java
│   │   │   │   ├── DataInitializerConfig.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── StorageInitializerConfig.java
│   │   │   ├── constant/                  # Application constants
│   │   │   │   └── SecurityConstants.java
│   │   │   ├── controller/                # REST API endpoints
│   │   │   │   ├── auth/                  # Authentication endpoints
│   │   │   │   ├── oauth2/                # OAuth2 authentication
│   │   │   │   ├── plan/                  # Subscription plan management
│   │   │   │   ├── redirect/              # URL redirection logic
│   │   │   │   ├── shortUrl/              # URL shortening operations
│   │   │   │   └── user/                  # User management
│   │   │   ├── exception/                 # Custom exception handlers
│   │   │   │   ├── JwtExceptionHandler.java
│   │   │   │   └── ValidationExceptionHandler.java
│   │   │   ├── filter/                    # HTTP request filters
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── RequestLoggingFilter.java
│   │   │   ├── model/                     # Data models and entities
│   │   │   │   ├── entity/                # Database entities
│   │   │   │   ├── enums/                 # Enumeration types
│   │   │   │   ├── request/               # API request models
│   │   │   │   └── response/              # API response models
│   │   │   ├── repository/                # Data access layer
│   │   │   ├── security/                  # Security implementations
│   │   │   │   └── oauth2/                # OAuth2 user services
│   │   │   ├── service/                   # Business logic layer
│   │   │   │   ├── auth/                  # Authentication services
│   │   │   │   ├── fileStorage/           # File handling services
│   │   │   │   ├── hashing/               # Password hashing
│   │   │   │   ├── jwt/                   # JWT token services
│   │   │   │   ├── oauth2/                # OAuth2 services
│   │   │   │   ├── plan/                  # Plan management
│   │   │   │   ├── qrCode/                # QR code generation
│   │   │   │   ├── redirect/              # URL redirection
│   │   │   │   ├── shortUrl/              # URL shortening logic
│   │   │   │   └── user/                  # User management
│   │   │   └── util/                      # Utility classes
│   │   └── resources/
│   │       ├── application.yml            # Main configuration
│   │       ├── application-dev.yml        # Development environment
│   │       ├── application-prod.yml       # Production environment
│   │       ├── schema.sql                 # Database schema
│   │       ├── static/                    # Static resources
│   │       └── templates/                 # Template files
│   └── test/                              # Test files
├── target/                                # Compiled output
├── uploads/                               # File upload directory
├── pom.xml                                # Maven configuration
├── mvnw                                   # Maven wrapper (Unix)
└── mvnw.cmd                               # Maven wrapper (Windows)
```

### Key Components Explained

- **Controllers**: Handle HTTP requests and define API endpoints
- **Services**: Contain business logic and orchestrate operations
- **Repositories**: Provide data access to the database
- **Models**: Define data structures for requests, responses, and database entities
- **Config**: Application configuration and security settings
- **Security**: Authentication and authorization implementations
- **Filters**: Process requests before they reach controllers
- **Exceptions**: Custom error handling and validation

## Installation

### Prerequisites

- **Java 17** or higher
- **Maven 3.6** or higher
- **PostgreSQL 12** or higher
- **Git** for version control

### Step-by-Step Setup

1. **Clone the Repository**

   ```bash
   git clone <repository-url>
   cd linkWhiz-backend
   ```

2. **Set Up PostgreSQL Database**

   ```sql
   CREATE DATABASE LinkWhiz;
   CREATE USER postgres WITH PASSWORD '123456789';
   GRANT ALL PRIVILEGES ON DATABASE LinkWhiz TO postgres;
   ```

3. **Configure Environment Variables**

   Create a `.env` file in the project root or set these environment variables:

   ```bash
   # JWT Configuration
   JWT_SECRET_KEY=your-super-secret-jwt-key-here

   # OAuth2 Configuration
   GOOGLE_CLIENT_ID=your-google-client-id
   GOOGLE_CLIENT_SECRET=your-google-client-secret
   ```

4. **Build the Project**

   ```bash
   mvn clean install
   ```

5. **Run the Application**

   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

## Configuration

### Database Configuration

The application uses PostgreSQL with the following default settings:

- **Host**: localhost
- **Port**: 5432
- **Database**: LinkWhiz
- **Username**: postgres
- **Password**: 123456789

### File Upload Configuration

- **Maximum file size**: 10MB
- **Upload directory**: `uploads/`
- **Supported formats**: Images for QR code customization

### Security Configuration

- **JWT expiration**: 24 hours
- **Refresh token expiration**: 7 days
- **OAuth2 providers**: Google, GitHub

## Usage

### Starting the Application

**Technical Steps:**

```bash
# Development mode
mvn spring-boot:run

# Production mode
mvn clean package
java -jar target/linkWhiz-backend-0.0.1-SNAPSHOT.jar
```

**Non-Technical Interpretation:**
The application starts automatically and begins listening for requests. The server will be available at `http://localhost:8080` and ready to handle URL shortening requests.

### API Endpoints

#### Authentication

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/refresh` - Refresh JWT token

#### URL Management

- `POST /api/shortUrl/shorten` - Create shortened URL
- `GET /api/shortUrl/getShortUrls` - List user's URLs
- `PATCH /api/shortUrl/updateShortUrl/{id}` - Update URL settings
- `DELETE /api/shortUrl/deleteShortUrl/{id}` - Delete URL

#### QR Code Generation

- `POST /api/shortUrl/qrCode/{urlId}` - Generate QR code
- `GET /api/shortUrl/qrCode/{urlId}` - Download QR code

#### Analytics

- `GET /api/shortUrl/getAnalytics/{urlId}` - Get URL analytics

#### User Management

- `GET /api/user/profile` - Get user profile
- `PATCH /api/user/updateProfile` - Update user information
- `PATCH /api/user/changePassword` - Change password

## API Overview

The LinkWhiz Backend provides a RESTful API with the following main categories:

### Authentication Endpoints

Handle user registration, login, and token management using JWT and OAuth2.

### URL Management Endpoints

Core functionality for creating, reading, updating, and deleting shortened URLs.

### Analytics Endpoints

Track and retrieve click statistics and user engagement metrics.

### User Management Endpoints

Manage user profiles, account settings, and preferences.

### QR Code Endpoints

Generate and customize QR codes for shortened URLs.

## Architecture

### High-Level Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client Apps   │    │   Web Browser   │    │   Mobile Apps   │
└─────────┬───────┘    └─────────┬───────┘    └─────────┬───────┘
          │                      │                      │
          └──────────────────────┼──────────────────────┘
                                 │
                    ┌─────────────▼─────────────┐
                    │   LinkWhiz Backend API    │
                    │     (Spring Boot)         │
                    └─────────────┬─────────────┘
                                  │
          ┌───────────────────────┼───────────────────────┐
          │                       │                       │
┌─────────▼─────────┐  ┌─────────▼─────────┐  ┌─────────▼─────────┐
│   PostgreSQL      │  │   File Storage    │  │   OAuth2          │
│   Database        │  │   (QR Codes)      │  │   Providers       │
└───────────────────┘  └───────────────────┘  └───────────────────┘
```

### Key Design Patterns

- **Clean Architecture**: Controllers → Services → Repositories
- **Dependency Injection**: Spring's IoC container
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Separate request/response models
- **Filter Pattern**: Request processing middleware

## Contributing

We welcome contributions from the community! Here's how you can help:

### Development Setup

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Make your changes and add tests
4. Commit your changes: `git commit -m 'Add amazing feature'`
5. Push to the branch: `git push origin feature/amazing-feature`
6. Open a Pull Request

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Write unit tests for new features
- Ensure all tests pass before submitting

### Pull Request Process

1. Update the README.md if needed
2. Add or update tests as appropriate
3. Ensure the build passes: `mvn clean test`
4. Update documentation for any new features
5. Provide clear commit messages

### Reporting Issues

When reporting bugs, please include:

- Detailed description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Environment details (OS, Java version, etc.)
- Any relevant error messages or logs

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

### Project Maintainer

- **Author**: Azizullah Khan
- **Email**: aziz.bin.aman16@gmail.com

### Support

For technical support or questions:

- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation for common solutions

### Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL communities
- All contributors and testers

---

**Note**: This is a demo project for Spring Boot showcasing URL shortening capabilities with modern web technologies. For production use, ensure proper security configurations and environment variable management.
