# 🎬 MovieDatabase

A full-stack **client–server application** for discovering, rating, and discussing movies. A JavaFX desktop client talks to a Spring Boot backend over a REST API and a real-time socket connection, with all data persisted in MySQL.

> Built as a team project for the *Software Engineering Internship* (SEP), Summer Semester 2022, University of Duisburg-Essen.

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-6DB33F?logo=springboot&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-18-1f8b4c)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white)

---

## ✨ Features

- 👤 **Accounts & roles** — registration and login for both regular users and system administrators
- 🎞️ **Movie catalogue** — browse, search, add, and update movies
- ⭐ **Reviews & ratings** — write reviews, rate movies, and view aggregated movie statistics
- 💬 **Real-time chat** — live messaging between users over a socket connection
- 🗣️ **Discussion groups** — create and join groups to talk about movies
- 🧑‍🤝‍🧑 **Friends** — send and accept friend requests and manage a friends list
- 🎉 **Watch parties** — create watch parties and invite friends
- 📺 **Watchlist & history** — keep a personal watchlist and viewing history
- 🔔 **Notifications** — in-app notifications plus email notifications via SMTP
- 🚩 **Moderation** — report content, with admin tools to review reports
- 📊 **Statistics** — user and administrator statistics views
- 👍 **Recommendations** — personalised movie recommendations

---

## 🏗️ Architecture

The system is split into a **desktop client** and a **backend service** that communicate over two channels: a REST API for standard requests and a socket connection for real-time features (chat, watch parties).

```
┌────────────────────────┐     REST  (HTTP / JSON)     ┌────────────────────────┐
│   JavaFX Desktop        │ ─────────────────────────▶ │   Spring Boot Backend   │
│   Client (Frontend)     │                             │   (embedded Jetty)      │
│                         │ ◀───────────────────────── │                         │
│   FXML views + MVC      │     Sockets (real-time      │   Controller            │
│   controllers           │     chat / watch party)     │     → Service           │
│                         │                             │       → Repository      │
└────────────────────────┘                             └───────────┬────────────┘
                                                                    │ Spring Data JPA
                                                                    ▼
                                                          ┌──────────────────┐
                                                          │     MySQL DB      │
                                                          └──────────────────┘
```

- **Frontend** (`FrontendApplication/`) — a JavaFX desktop client with ~40 FXML views and matching MVC controllers. Talks to the backend via Spring's REST client + Gson, and via raw sockets for live features.
- **Backend** (`BackendApplication/`) — a Spring Boot service with a clean layered architecture (**Controller → Service → Repository → Entity**), Spring Data JPA over MySQL, Jetty as the embedded server, Spring Mail for notifications, and a socket `Server` / `ClientHandler` for real-time chat.
- **resources/** — shared FXML views and the application stylesheet.

---

## 🛠️ Tech Stack

| Layer        | Technologies                                                      |
| ------------ | ----------------------------------------------------------------- |
| **Frontend** | Java 17, JavaFX 18 (FXML), Gson                                    |
| **Backend**  | Java 17, Spring Boot 2.7, Spring Web, Spring Data JPA, Spring Mail, Jetty |
| **Database** | MySQL                                                              |
| **Build**    | Maven (wrapper included — no global install needed)               |
| **Testing**  | JUnit / Spring Boot Test                                           |

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17+**
- **MySQL** running locally
- Maven is **not** required — the project ships with the Maven wrapper (`mvnw`)

### 1. Set up the database

The backend expects a MySQL instance at `localhost:3306`. The schema (`sepdatabase`) is created automatically on first run, and tables are generated/updated by Hibernate.

### 2. Configure credentials

Set your database credentials — and, if you want email notifications, your SMTP credentials — in:

```
BackendApplication/src/main/resources/application.properties
```

> ⚠️ **Never commit real credentials.** Use placeholders here and provide real values through environment variables or a local, untracked config file. (See the security note below.)

### 3. Run the backend

```bash
cd BackendApplication
./mvnw spring-boot:run
```

### 4. Run the frontend

```bash
cd FrontendApplication
./mvnw javafx:run
# or run com.frontend.FrontendMain from your IDE
```

---

## 📁 Project Structure

```
MovieDatabase/
├── BackendApplication/          # Spring Boot service
│   └── src/main/java/com/backend/
│       ├── controller/          # REST endpoints
│       ├── service/             # Business logic (+ socket Server)
│       ├── repository/          # Spring Data JPA repositories
│       ├── model/               # JPA entities
│       └── configuration/       # Mail config, etc.
├── FrontendApplication/         # JavaFX desktop client
│   └── src/main/java/com/frontend/
│       ├── controller/          # MVC controllers for each view
│       └── model/               # Client-side models + socket Client
└── resources/
    ├── views/                   # FXML view definitions
    └── styles.css               # Application stylesheet
```

---

## 🔐 Security Note

This started as a coursework project. If you clone or fork it, make sure `application.properties` contains **only placeholders** — no real database or email passwords — and supply secrets via environment variables or a local config file that is excluded from version control.

---

## 👥 Team

Built by **Group P** for the Software Engineering Internship (SEP), Summer Semester 2022, University of Duisburg-Essen. Full project documentation is included in `SEP_Gruppe-P_Abgabe_Zyklus3_Projektmappe.docx`.

---

## 📄 License

> _Add a license of your choice (e.g. MIT), or state that the project is shared for educational and portfolio purposes._
