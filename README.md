# 💻 Elangovan P's Personal Portfolio & Backend API

Welcome to the source code repository of my professional portfolio website and dynamic REST API backend. This project showcases my skills, experience, projects, and creative works, built using a modern full-stack architecture.

---

## 🏗️ Architecture Overview

The portfolio is structured as a **three-tier layered application**:

1. **Presentation Tier (Frontend)**: A highly interactive, responsive, and visually polished static web interface built with vanilla HTML5, CSS3, and JavaScript, enhanced with GSAP animations for smooth dynamic transitions.
2. **Application Tier (Backend)**: A production-ready REST API built with **Spring Boot 3.3.x**, secured using **Spring Security** and **JWT (JSON Web Token)** authentication, allowing for dynamic CRUD operations on portfolio sections.
3. **Data Tier (Database)**: Driven by **H2 (In-memory)** for rapid development, and pre-configured for **PostgreSQL** in production with Hibernates' optimized queries and indexing.

---

## 🛠️ Tech Stack & Dependencies

### Frontend
*   **Structure**: Semantic HTML5
*   **Styling**: Premium Vanilla CSS3 (features dark mode, glassmorphism, responsive grid, dynamic neon glows, custom scrollbars)
*   **Animations**: GSAP (GreenSock Animation Platform) + ScrollTrigger for reveal effects
*   **Icons**: FontAwesome v6.0 (integrated dynamically in cards)

### Backend
*   **Core Framework**: Spring Boot 3.3.6 (Java 17+)
*   **Data Access**: Spring Data JPA (Hibernate)
*   **Security & Auth**: Spring Security 6.x, JWT (JJWT v0.12.6) for token-based session management
*   **API Documentation**: Springdoc OpenAPI / Swagger UI 2.6.0
*   **Validation**: Bean Validation (Jakarta Validation)
*   **Developer Productivity**: Project Lombok (reduces boilerplate)
*   **Database**: H2 (Development) / PostgreSQL 14+ (Production)
*   **DevOps**: Docker, Docker Compose (multi-stage builds)

---

## 📂 Project Structure

```
Portfolio/
├── index.html              # Frontend entry point
├── style.css               # Core design system & layout
├── script.js               # Frontend interactive logic
├── Alter_Profile_Img.jpg   # Profile banner/avatar
├── .gitignore              # Project-wide ignores
├── README.md               # You are here!
│
└── backend/                # Spring Boot application
    ├── pom.xml             # Maven dependencies & build lifecycle
    ├── Dockerfile          # Multi-stage JDK 17 Docker deployment
    ├── docker-compose.yml  # Docker environment (App + Postgres)
    └── src/
        ├── main/
        │   ├── java/com/elangovan/portfolio/
        │   │   ├── PortfolioApplication.java
        │   │   ├── config/      # Security, CORS, Swagger Config
        │   │   ├── controller/  # REST Controllers
        │   │   ├── dto/         # Request & Response Contracts
        │   │   ├── entity/      # JPA Hibernate entities
        │   │   ├── exception/   # Global Exception Handling
        │   │   ├── repository/  # Data JPA Repositories
        │   │   ├── security/    # JWT Provider, Filter, custom UserDetails
        │   │   └── util/        # DataSeeder class
        │   └── resources/
        │       ├── application.yml        # Default setup & JWT settings
        │       ├── application-dev.yml    # Dev profile (H2 settings)
        │       └── application-prod.yml   # Prod profile (PostgreSQL settings)
        └── test/                  # Controller, Service, and Integration Tests
```

---

## 🚀 Getting Started

### 1. Running the Frontend
The frontend is built with native browser technologies. Simply open `index.html` in any web browser, or use a tool like VS Code Live Server for hot-reloading.

### 2. Running the Backend
#### Prerequisites
*   **Java**: JDK 17 or higher
*   **Maven**: 3.9+

#### Start Local Dev Server:
1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
   The backend will start on `http://localhost:8080`.

#### Auto Data Seeding:
On the first startup, the database is automatically seeded via `DataSeeder.java` using my actual portfolio details:
*   **1 Admin Profile**: `admin@elangovan.dev` (Password: `Admin@2026`)
*   **6 Core Skills**: Java, Spring Boot, SQL, HTML/CSS, Git, etc.
*   **4 Projects**: AI Resume Analyzer, Personal Expense Tracker, Online Quiz, etc.
*   **3 Creative Gallery Items**

---

## 🔒 Security & JWT Flow

Write operations (`POST`, `PUT`, `DELETE`) are protected and require a valid JWT bearer token.

1.  **Request Authentication**:
    *   Send a `POST` request to `http://localhost:8080/api/auth/login` with credentials:
        ```json
        {
          "email": "admin@elangovan.dev",
          "password": "Admin@2026"
        }
        ```
    *   The server returns a JWT token:
        ```json
        {
          "success": true,
          "data": {
            "token": "eyJhbGciOiJIUzUxMiJ9...",
            "type": "Bearer",
            "email": "admin@elangovan.dev"
          }
        }
        ```
2.  **Access Protected Endpoints**:
    *   Include the token in the headers of write requests:
        `Authorization: Bearer <your_jwt_token>`

---

## 🛣️ API Endpoints

### Public Endpoints (No Token Required)
*   `GET /api/profile` - Aggregated profile payload including all sections.
*   `GET /api/projects` - Get all projects (supports display order).
*   `GET /api/projects/paged` - Paginated projects list.
*   `GET /api/projects/{id}` - Single project detail.
*   `GET /api/skills` - List skills.
*   `GET /api/experience` - List experiences.
*   `GET /api/certifications` - List certifications.
*   `GET /api/gallery` - List creative works.

### Protected Admin Endpoints (Token Required)
*   `PUT /api/profile` - Update bio, details, or social links.
*   `POST` | `PUT` | `DELETE` `/api/projects/**` - Manage projects.
*   `POST` | `PUT` | `DELETE` `/api/skills/**` - Manage skills list.
*   `POST` | `PUT` | `DELETE` `/api/experience/**` - Manage work history.
*   `POST` | `DELETE` `/api/certifications/**` - Manage certificates.
*   `POST` | `DELETE` `/api/gallery/**` - Manage creative gallery items.

---

## 🔍 Testing & API Docs

### Swagger API Documentation
Open `http://localhost:8080/swagger-ui.html` in your browser when the server is running to view, test, and interact with the endpoints.

### H2 Database Console
To browse the database tables:
*   URL: `http://localhost:8080/h2-console`
*   JDBC URL: `jdbc:h2:mem:portfoliodb`
*   Username: `sa`
*   Password: *(leave blank)*

### Running Automated Tests
Run integration and unit tests using Maven:
```bash
cd backend
mvn test
```

---

## 🐳 Docker Deployment

To spin up the PostgreSQL database and backend service together:
```bash
cd backend
docker-compose up --build
```
This builds the Spring Boot app jar via a multi-stage Dockerfile and deploys it along with PostgreSQL, linking the containers.
