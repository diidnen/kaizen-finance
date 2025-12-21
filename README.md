# Kaizen Finance - Full Stack Application

A comprehensive finance management application built with Vue 3 frontend and Spring Boot backend.

## Technology Stack

### Frontend
- **Vue 3**: Modern reactive frontend framework
- **Vite**: Fast build tool based on ES modules
- **Element Plus**: UI component library for Vue 3
- **Vue Router**: Client-side routing
- **Axios**: HTTP client for API requests

### Backend
- **Spring Boot 3.2.1**: RESTful API services
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database interaction
- **MySQL 8.0**: Database
- **Lombok**: Code generation
- **Java 17**: Runtime environment

## Features

- User authentication and authorization
- Service pricing and quote generation
- Order management
- Contact form with email notifications
- PDF and CSV export for quotes
- Contract management
- Testimonials management

## Project Structure

```
kaizen-finance-master/
├── kaizen-finance/          # Frontend (Vue 3)
│   ├── src/
│   ├── public/
│   └── package.json
└── kaizen-finance-backend/  # Backend (Spring Boot)
    └── backend/
        └── src/
```

## Development Setup

### Frontend Setup

```bash
cd kaizen-finance
npm install
npm run dev
```

Frontend will run on http://localhost:5173

### Backend Setup

1. Install MySQL and create database:
```sql
CREATE DATABASE kaizenfinance;
```

2. Update `application.properties` with your database credentials

3. Run backend:
```bash
cd kaizen-finance-backend/backend
mvn spring-boot:run
```

Backend will run on http://localhost:8080

## Deployment

See deployment guides:
- `VERCEL_RAILWAY_TUTORIAL.md` - Complete deployment tutorial
- `DEPLOY_VERCEL_RAILWAY.md` - Quick deployment guide
- `DEPLOYMENT_GUIDE.md` - All deployment options

## License

Private project
