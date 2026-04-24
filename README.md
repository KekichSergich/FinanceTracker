# 💰 Finance Tracker

A desktop application for personal finance management built with **Java + JavaFX**.

---

## 📋 Overview

Finance Tracker allows users to easily track income and expenses, categorize transactions, view statistics, and analyze financial behavior over time. The goal is to give users a clear picture of their finances and help them plan their budget more effectively.

---

## ✨ Features

### 💳 Transaction Management
- Add, edit, and delete transactions
- Each transaction includes:
  - Amount
  - Type (`income` / `expense`)
  - Category
  - Date
  - Note

### 📊 Statistics & Analytics
- Total income and expenses
- Current balance
- Expenses by category
- Expenses by month
- Visualized with **pie charts** and **bar charts**

### 🔍 Filtering & Search
- Filter transactions by date, category, or type
- Examples: all expenses for a month, all transactions in category "Food"

### 💾 Data Import & Export
- Save and load data from file
- Supported formats: **JSON**, **XML**

---

## 🖥️ GUI Overview

| Screen | Description |
|---|---|
| **Dashboard** | Current balance, income/expense summary, category chart |
| **Transactions** | Full transaction list with Add / Edit / Delete buttons |
| **Add Transaction** | Form with amount, type, category, date, note |
| **Statistics** | Charts by category and by month |
| **Data** | Export & import data files |

---

## 🛠️ Tech Stack

- **Language:** Java
- **UI Framework:** JavaFX
- **Data Formats:** JSON / XML
- **Concurrency:** Multi-threading (e.g. for statistics and time updates)

---

## 🚀 Getting Started

### Prerequisites
- Java 17+
- JavaFX SDK

### Run the application
```bash
git clone https://github.com/your-username/finance-tracker.git
cd finance-tracker
./gradlew run
```

This project is for educational purposes.
