  # 🚗 CarHaven - Car Dealership Web Application:








[![Jenkins Build](https://img.shields.io/badge/Jenkins-Passing-brightgreen?logo=jenkins)](http://18.209.62.205:8080)
[![Selenium Tests](https://img.shields.io/badge/Tests-10%20Passed-success)](selenium-tests/)
[![React](https://img.shields.io/badge/React-18-blue?logo=react)](https://react.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue?logo=typescript)](https://www.typescriptlang.org)

A modern, responsive car dealership web application built with React, TypeScript, and Tailwind CSS. Features automated UI testing with Selenium and CI/CD pipeline with Jenkins.

---


## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Quick Start](#-quick-start)
- [Project Structure](#-project-structure)
- [Testing](#-testing)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Author](#-author)

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🏠 **Home Page** | Beautiful landing page with featured vehicles |
| 🚗 **Car Listings** | Browse all available vehicles with search & filters |
| 🔍 **Car Details** | Detailed view with specifications and images |
| 🔐 **Authentication** | Secure user login and registration |
| 👤 **User Profiles** | Manage account and preferences |
| 🛡️ **Admin Panel** | Inventory management for administrators |
| 📱 **Responsive** | Mobile-first design that works on all devices |
| 🧪 **Automated Tests** | Selenium UI tests with Jenkins CI/CD |

---

## 🛠️ Tech Stack

### Frontend
- **React 18** - UI Library
- **TypeScript** - Type Safety
- **Vite** - Build Tool
- **Tailwind CSS** - Styling
- **shadcn/ui** - UI Components

### Backend & Infrastructure
- **Supabase** - Backend as a Service
- **Docker** - Containerization
- **Jenkins** - CI/CD Pipeline
- **AWS EC2** - Cloud Hosting

### Testing
- **Selenium WebDriver** - Browser Automation
- **JUnit 5** - Test Framework
- **Java 17** - Test Language
- **Maven** - Build Tool

---

## 🚀 Quick Start

### Prerequisites

- Node.js 18+ and npm
- Docker & Docker Compose (for CI/CD)
- Java 17+ (for running Selenium tests locally)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/hamzanaeem10/auto-suite-space.git
   cd auto-suite-space
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Set up environment variables**
   ```bash
   cp .env.example .env
   # Edit .env with your Supabase credentials
   ```

4. **Start the development server**
   ```bash
   npm run dev
   ```

5. **Open in browser**
   ```
   http://localhost:5173
   ```

### Build for Production

```bash
npm run build
npm run preview
```

---

## 📁 Project Structure

```
auto-suite-space/
├── src/
│   ├── components/          # Reusable UI components
│   │   ├── Navbar.tsx
│   │   ├── NavLink.tsx
│   │   └── ui/              # shadcn/ui components
│   ├── pages/               # Application pages
│   │   ├── Index.tsx        # Home page
│   │   ├── Cars.tsx         # Car listings
│   │   ├── CarDetail.tsx    # Car details
│   │   ├── Auth.tsx         # Authentication
│   │   ├── Admin.tsx        # Admin panel
│   │   └── Profile.tsx      # User profile
│   ├── hooks/               # Custom React hooks
│   ├── integrations/        # External service integrations
│   ├── lib/                 # Utility functions
│   ├── App.tsx              # Main app component
│   └── main.tsx             # Entry point
├── selenium-tests/          # Selenium test suite
│   ├── pom.xml              # Maven configuration
│   └── src/test/java/com/autosuite/
│       ├── BaseTest.java       # Base test class
│       ├── HomePageTest.java   # Home page tests
│       ├── CarsPageTest.java   # Cars page tests
│       └── AuthPageTest.java   # Auth page tests
├── public/                  # Static assets
├── supabase/                # Supabase migrations
├── Jenkinsfile              # CI/CD pipeline
├── docker-compose.ci.yml    # Docker Compose for CI
├── package.json
├── tailwind.config.ts
├── vite.config.ts
└── REPORT.md                # Detailed project report
```

---

## 🧪 Testing

### Selenium UI Tests

The project includes **10 automated UI tests** covering:

| Test Suite | Tests | Description |
|------------|-------|-------------|
| `HomePageTest` | 4 | Home page loading, title, navbar, content |
| `CarsPageTest` | 3 | Cars page navigation and functionality |
| `AuthPageTest` | 3 | Authentication form elements |

### Running Tests Locally

```bash
# Navigate to test directory
cd selenium-tests

# Run tests with Maven
mvn test

# Run with custom base URL
mvn test -DbaseUrl=http://localhost:5173
```

### Running Tests in Docker

```bash
docker run --rm --network host \
    -e BASE_URL="http://localhost:8081" \
    -v "$PWD/selenium-tests:/workspace" \
    markhobson/maven-chrome:latest \
    /bin/bash -lc 'cd /workspace && mvn test'
```

### Test Results

```
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

---

## 🔄 CI/CD Pipeline

### Pipeline Overview

```
┌──────────────────────────────────────────────────────────────┐
│                    Jenkins Pipeline                          │
├──────────────────────────────────────────────────────────────┤
│  📥 Checkout → 🐳 Docker → 🔧 Build → 🧪 Test → 📧 Notify    │
└──────────────────────────────────────────────────────────────┘
```

### Pipeline Stages

| Stage | Description |
|-------|-------------|
| **Checkout** | Pull latest code from GitHub |
| **Docker Info** | Verify Docker installation |
| **Stop Containers** | Clean up previous deployment |
| **Prepare .env** | Create environment configuration |
| **Build & Deploy** | Build and start Docker containers |
| **Smoke Test** | Verify application is running |
| **UI Tests** | Run Selenium tests in headless Chrome |
| **Post Actions** | Publish results and send email notifications |

### Automatic Triggers

- ✅ Triggered automatically on every push to `main` branch
- ✅ GitHub webhook integration
- ✅ Email notifications with test results

### Email Notifications

The pipeline sends HTML-formatted emails with:
- Build status (SUCCESS/FAILURE)
- Test results summary (Total/Passed/Failed)
- Pipeline stages checklist
- Link to Jenkins build
- Attached test logs

---

## 📡 Routes

| Route | Description | Auth Required |
|-------|-------------|---------------|
| `/` | Home page | No |
| `/cars` | Browse all cars | No |
| `/cars/:id` | View car details | No |
| `/auth` | Login/Register | No |
| `/profile` | User profile | Yes |
| `/admin` | Admin dashboard | Yes (Admin) |

---

## 🔧 Environment Variables

| Variable | Description |
|----------|-------------|
| `VITE_SUPABASE_URL` | Supabase project URL |
| `VITE_SUPABASE_PUBLISHABLE_KEY` | Supabase anon key |
| `VITE_SUPABASE_PROJECT_ID` | Supabase project ID |

---


---

## 🙏 Acknowledgments

- [React](https://react.dev) - UI Library
- [Vite](https://vitejs.dev) - Build Tool
- [Tailwind CSS](https://tailwindcss.com) - CSS Framework
- [shadcn/ui](https://ui.shadcn.com) - UI Components
- [Supabase](https://supabase.com) - Backend as a Service
- [Selenium](https://www.selenium.dev) - Browser Automation
- [Jenkins](https://www.jenkins.io) - CI/CD

---





