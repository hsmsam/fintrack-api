# FinTrack API

A personal finance tracking REST API built with Java and Spring Boot. FinTrack lets users log income, expenses, and monthly budgets per account — with automatic calculations for running balance, savings rate, overspent budgets, and a full monthly financial summary.

## Features

- Manage accounts, income, expenses, and budgets with full CRUD operations
- All financial data is scoped per account — every calculation filters by `accountId`, not just the whole database
- Automatic monthly financial summary — total income, total spending, monthly budget, savings, savings rate, running balance, largest expense, and top spending category
- Overspent budget detection — compares actual spending per category against each budget target for a given month
- Running balance calculated from full account history, independent of month
- Category and Frequency enforced via enums — Housing, Bills, Transport, Food, Healthcare, Shopping, Leisure, Subscriptions, Savings, Debt, Other
- Month-aware calculations use `YearMonth`, so figures are correctly scoped to a specific year and month rather than colliding across years

## Tech Stack

- **Java 21**
- **Spring Boot** (Web, Data JPA)
- **PostgreSQL**
- **Docker Compose**
- **Maven**
- **JUnit 5 + Mockito** (unit tests)

## Getting Started

### Prerequisites

- Java 21+
- Docker and Docker Compose
- Maven

### Database Setup

Start the PostgreSQL database using Docker Compose:
```
docker-compose up -d
```

### Running the Application

```
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

> Create an account first via `POST /api/v1/account` — every other resource requires a valid `accountId`.

---

## API Endpoints

All endpoints are under `/api/v1`. `{yearMonth}` is in `YYYY-MM` format, e.g. `2026-03`.

### Account

| Method | Endpoint | Description |
|---|---|---|
| GET | `/account` | Get all accounts |
| GET | `/account/{id}` | Get account by ID |
| POST | `/account` | Add a new account |
| PUT | `/account/{id}` | Update account details |
| DELETE | `/account/{id}` | Remove an account |

### Income

| Method | Endpoint | Description |
|---|---|---|
| GET | `/income` | Get all income records |
| GET | `/income/{id}` | Get income by ID |
| GET | `/income/total-income/{accountId}/{yearMonth}` | Get total income for a given month |
| POST | `/income` | Add a new income record |
| PUT | `/income/{id}` | Update income details |
| DELETE | `/income/{id}` | Remove an income record |

### Expense

| Method | Endpoint | Description |
|---|---|---|
| GET | `/expense` | Get all expenses |
| GET | `/expense/{id}` | Get expense by ID |
| GET | `/expense/total-spent/{accountId}/{yearMonth}` | Get total spent for a given month |
| GET | `/expense/largest-expense/{accountId}/{yearMonth}` | Get the largest single expense for a given month |
| GET | `/expense/largest-spending-category/{accountId}/{yearMonth}` | Get the category with the highest spend for a given month |
| POST | `/expense` | Add a new expense |
| PUT | `/expense/{id}` | Update expense details |
| DELETE | `/expense/{id}` | Remove an expense |

### Budget

| Method | Endpoint | Description |
|---|---|---|
| GET | `/budget` | Get all budgets |
| GET | `/budget/{id}` | Get budget by ID |
| GET | `/budget/{accountId}/{yearMonth}` | Get all budgets for a given month |
| GET | `/budget/monthly-budget/{accountId}/{yearMonth}` | Get total budgeted amount for a given month |
| GET | `/budget/overspent-budgets/{accountId}/{yearMonth}` | Get budgets where spending exceeded the target |
| POST | `/budget` | Add a new budget |
| PUT | `/budget/{id}` | Update budget details |
| DELETE | `/budget/{id}` | Remove a budget |

### Financial Summary

| Method | Endpoint | Description |
|---|---|---|
| GET | `/financial-summary/monthly-savings/{accountId}/{yearMonth}` | Get income minus spending for a given month |
| GET | `/financial-summary/running-balance/{accountId}` | Get all-time income minus all-time spending |
| GET | `/financial-summary/savings-rate/{accountId}/{yearMonth}` | Get savings as a percentage of income for a given month |
| GET | `/financial-summary/summary/{accountId}/{yearMonth}` | Get the full financial summary for a given month |

---

## Example Requests

### Add an Account

```
POST /api/v1/account
Content-Type: application/json

{
  "name": "Arthur Stewart",
  "email": "arthurstewart@gmail.com"
}
```

### Add an Income Record

```
POST /api/v1/income
Content-Type: application/json

{
  "account": { "id": 1 },
  "amount": 2000,
  "source": "Work",
  "dateReceived": "2026-03-07",
  "frequency": "MONTHLY",
  "description": "Monthly work wage"
}
```

### Add an Expense

```
POST /api/v1/expense
Content-Type: application/json

{
  "account": { "id": 1 },
  "amount": 140,
  "category": "LEISURE",
  "datePaid": "2026-03-10",
  "frequency": "MONTHLY",
  "description": "Going out having fun and eating"
}
```

### Add a Budget

```
POST /api/v1/budget
Content-Type: application/json

{
  "account": { "id": 1 },
  "budgetCategory": "LEISURE",
  "budgetTotal": 200,
  "budgetMonth": "2026-03"
}
```

### Get Financial Summary

```
GET /api/v1/financial-summary/summary/1/2026-03
```

Example response:
```
{
  "budget": [ ... ],
  "monthlyIncome": 2400.00,
  "monthlySpending": 1000.00,
  "monthlyBudget": 150.00,
  "monthlySaving": 1400.00,
  "savingsRate": 58.00,
  "runningBalance": 1400.00,
  "largestExpense": 800.00,
  "largestSpendingCategory": "HOUSING"
}
```

Sample `.http` request files for every resource are included in the `http/` directory for use with IntelliJ's HTTP client.

---

## Project Structure

```
src/
├── main/java/com/project/fintrackapi/
│   ├── controller/    Account, Income, Expense, Budget, FinancialSummary controllers
│   ├── service/       Business logic and cross-entity calculations
│   ├── repository/    Spring Data JPA interfaces
│   ├── entity/        Account, Income, Expense, Budget, FinancialSummary
│   └── enums/         Category, Frequency
├── test/java/com/project/fintrackapi/service/
│   └── Unit tests for each service, mirroring the main package structure
├── http/               Sample request files per resource
└── docker-compose.yml
```

---

## Testing

Unit tests cover the service layer using JUnit 5 and Mockito, focused on methods containing real business logic — filtering, aggregation, branching — rather than trivial CRUD passthroughs that just forward to a repository. Run with:
```
./mvnw test
```

## What I Learned

- Designing a multi-entity REST API around a shared owner (`Account`), and scoping every query and calculation by `accountId` rather than operating globally
- Composing business logic across services — `FinancialSummaryService` depends on `IncomeService`, `ExpenseService`, and `BudgetService` rather than duplicating their logic
- Using Java Streams and Collectors for aggregation — grouping expenses by category, finding maximums, summing filtered results
- The difference between `java.time.Month` and `java.time.YearMonth`, and why filtering financial data by month name alone silently merges data across different years
- Writing unit tests with JUnit 5 and Mockito, including mocking one service layer against another (not just repositories), and being deliberate about which methods are worth testing
- Structuring a Spring Boot application into controller, service, repository, and entity packages rather than one flat package
- Using Mockito's `@Mock`/`@InjectMocks` to isolate a class under test from its real dependencies, and testing both branches of methods that return `null`/zero on empty input

## Known Limitations / Future Improvements

- **Frequency is descriptive only.** A `WEEKLY` or `ANNUALLY` income/expense record is not currently projected into a monthly-equivalent figure — one record represents one actual transaction on one date. Recurring-payment projection is a planned enhancement.
- **Entities are returned directly**, rather than through request/response DTOs. This risks overposting and couples the API contract to the JPA model.
- **No global exception handling.** Missing records currently return HTTP 500 rather than 404.
- **No input validation** (e.g. negative amounts, malformed emails are not currently rejected).
- **Aggregations are computed in application memory** rather than pushed down to the database via SQL aggregation queries.

## Author

Hafez Sam — [github.com/hsmsam](https://github.com/hsmsam)
