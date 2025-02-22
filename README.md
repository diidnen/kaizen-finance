# Full Stack Project Example - Vue3 + Spring Boot (Session-Based Authentication)

This is a full-stack project where the front end is built with **Vue 3** and **Vite** for development and bundling, while the back end uses **Spring Boot** to provide **CRUD** services and **Session** authentication. The project implements functionality for user purchases, modifications, deletions, and user login authentication.

## Technology Stack

### Frontend
- **Vue 3**: A modern front-end framework that uses a reactive design, making it easier to develop dynamic single-page applications (SPA).
- **Vite**: A fast build tool that is based on native ES modules.
- **Element Plus**: A UI component library based on Vue 3 for building modern user interfaces.

### Backend
- **Spring Boot**: Used for quickly building RESTful API services.
- **JPA (Java Persistence API)**: Used to interact with database entities.
- **Spring Data JPA**: Simplifies the configuration and use of JPA.
- **Lombok**: A Java library used to generate common methods (such as getters, setters, `toString()`, etc.) via annotations.
- **Jakarta**: Used to provide support for RESTful API endpoints and handling API requests.
- **Spring Security**: Provides user login authentication (based on Session management).

### Build Tools
- **Maven**: Used for managing dependencies and building the backend project.

## Project Features

- **CRUD Services**: Implements functionality for buying, modifying, and deleting items.
- **User Login and Authentication**: Uses **Session** for user login authentication. All operations require authentication.
- **RESTful API**: The backend exposes RESTful API endpoints that the frontend communicates with for data exchange.

## Project Structure

🤭 Please refer to the actual project directory for detailed structure.

## Great for Beginners

This project is designed to help beginners understand the flow of a full-stack application. By working on this project, you will gain a good understanding of how frontend and backend interact, how user authentication works, and how to manage data with CRUD operations.

## How It Works

- The frontend (Vue3 + Element Plus) communicates with the backend (Spring Boot) via RESTful API requests.
- The backend uses **Session-based authentication**. After a user logs in, their session is saved, and all subsequent requests require the session to be valid for the user to access protected resources.
- The backend handles CRUD operations for items (buy, modify, delete), while also ensuring that only authenticated users can perform these actions.

## Development Setup

### 1. Clone the Repository

```bash
git@github.com:diidnen/kaizen-finance.git

