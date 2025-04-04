# THPT 2024 Exam Score Analysis

This project allows users to check THPT 2024 exam scores, view reports, and analyze student performance. It is built using:
- **Frontend:** Next.js (React)
- **Backend:** NestJS (Node.js)
- **Database:** PostgreSQL
- **Containerization:** Docker & Docker Compose
- **🌐Live Demo:** https://g-scores-frontend.vercel.app

## 🚀 Getting Started

### 1️⃣ Prerequisites
Ensure you have:
- **Docker** installed ([Download Docker](https://www.docker.com/get-started))
- **Git** installed ([Download Git](https://git-scm.com/downloads))

### 2️⃣ Clone the Repository
```sh
git clone https://github.com/giabao0301/gscores-backend
```
### 3️⃣ Create .env file 
For **local development**, create a `.env` file in the root directory with the following:
```bash
POSTGRES_USER=user
POSTGRES_PASSWORD=password
POSTGRES_URL=jdbc:postgresql://postgres:5432/thpt2024
POSTGRES_DB=thpt2024

PGADMIN_DEFAULT_EMAIL=admin@example.com
PGADMIN_DEFAULT_PASSWORD=admin

NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

### 4️⃣ Run docker-compose.yml
```sh
docker-compose up -d    
```

### 5️⃣ Create database
Connect to default database first:
```sh
docker exec -it postgres psql -U user -d postgres
```
Then run:
```sh
CREATE DATABASE thpt2024;
```

### 6️⃣ Restart backend container
```sh
docker restart g-scores-backend
```
### ⏳Seeding data
There are over 1 million data rows. 
Run this command and wait until the process is finished:
```sh
docker logs -f g-scores-backend
``` 


### 🛑 Stopping the Application
To stop the containers, run:
```sh
docker-compose down
```

## 🚀 Deployment
If you want to deploy the application, you can use:

-   **Vercel** (Frontend) → [https://vercel.com](https://vercel.com)
    
-   **Render** (Backend) → [https://render.com/](https://render.com/)

## 📝 Notes

-   Ensure **Docker is running** before starting.
    
-   If you face database connection issues, wait a few seconds and try again.
