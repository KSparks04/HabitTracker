# Habitual: Full-Stack Habit Tracker & Analytics Platform (In Progress)

> ⚠️ **Project Status:** Backend API is fully fleshed out and secured. Frontend UI components and dashboard integration are currently under active development.

A robust, production-grade full-stack web application designed to help users build consistency, track daily habits, and visualize long-term streaks. Built with a modern **React** frontend, a secure **Java Spring Boot** REST API backend, and a relational **PostgreSQL** database.

![Java Spring Boot](https://img.shields.io/badge/Backend-Spring_Boot_3.x-brightgreen?style=flat-square&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?style=flat-square&logo=postgresql)
![Security](https://img.shields.io/badge/Security-Spring_Security_%2B_JWT-red?style=flat-square&logo=jsonwebtokens)
![Status](https://img.shields.io/badge/Status-Active_Development-yellow?style=flat-square)
## ✨ Current Implementation Status

### **Backend (Fully Functional & Tested)**
* **Secure Authentication:** Implements user registration and authentication using **Spring Security** and **JWT (JSON Web Tokens)** with encrypted password hashing (`BCrypt`).
* **RESTful API Architecture:** Complete CRUD endpoints for habits, custom completion logging, and preset templates.
* **Relational Database Design:** Fully structured PostgreSQL schema managing users, habits, logs, and template relationships via Spring Data JPA.

### **Frontend (Under Construction)**
* Component-driven UI architecture built with **React** and **Tailwind CSS**.
* Interactive form elements like inline expandable habit selectors (currently being integrated with backend API endpoints via Axios).
