# 💰 Finance Tracker

A desktop application for personal finance management built with **Java + JavaFX**.

---

## 📋 Overview

Finance Tracker allows users to track income and expenses, categorize transactions, view statistics, and export or import financial data. All data is stored locally in a JSON file.

---

## ✨ Features

### 💳 Transaction Management
- Add and delete transactions
- Each transaction includes:
  - Amount
  - Type (`INCOME` / `EXPENSE`)
  - Category
  - Date
  - Note

### 📊 Statistics & Analytics
- Total income, expenses, and current balance
- Expenses and income filtered by category
- Charts: pie chart by category, line chart by month

### 💾 Data Import & Export
- Export all transactions to **JSON**
- Import from JSON with two modes:
  - **Replace** — clears existing data and loads new
  - **Merge** — adds imported transactions to existing ones

### 🔧 Debug Logging
- Toggle debug logging via UI checkbox in the header
- Or enable at launch with `--debug` flag

---

## 🖥️ GUI Overview

| Screen | Description |
|---|---|
| **Dashboard** | Balance cards, add transaction form, category summary, transaction table |
| **Analytics** | Pie charts by category, line chart by month |
| **Data** | Export and import JSON files |

---

## 🛠️ Tech Stack

| | |
|---|---|
| Language | Java 25 |
| UI Framework | JavaFX 21.0.2 |
| Build Tool | Maven |
| Testing | JUnit 5 |
| Storage | JSON file (`data/transactions.json`) |
| Logging | java.util.logging |

---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- Maven 3.8+

### Run the application

```bash
git clone https://github.com/YOUR_USERNAME/FinanceTracker.git
cd FinanceTracker
mvn javafx:run
```

### Run with debug logging

```bash
mvn javafx:run -Djavafx.args="--debug"
```

### Run tests

```bash
mvn test
```

---

## 📁 Project Structure

```text
src/
├── domain/         # Transaction, Category, TransactionType, repository interface
├── application/    # TransactionService, StatisticsService, DataService
├── infrastructure/ # FileTransactionRepository (JSON)
├── presentation/   # Controllers, DTOs, JavaFX views and components
└── util/           # AppLogger
```

---

*This project is for educational purposes.*
