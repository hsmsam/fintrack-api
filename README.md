# FinTrack API

A personal finance tracking REST API built with Java and Spring Boot. FinTrack lets users log income, expenses, and
monthly budgets per account — with automatic calculations for running balance, savings rate, overspent budgets, and a
full monthly financial summary.

## Features

- Manage accounts, income, expenses, and budgets with full CRUD operations
- All financial data is scoped per account — every calculation filters by `accountId`, not just the whole database
- Automatic monthly financial summary — total income, total spending, monthly budget, savings, savings rate, running
  balance, largest expense, and top spending category
- Overspent budget detection — compares actual spending per category against each budget target for a given month
- Running balance calculated from full account history, independent of month
- Category and Frequency enforced via enums — Housing, Bills, Transport, Food, Healthcare, Shopping, Leisure,
  Subscriptions, Savings, Debt, Other
- Month-aware calculations use `YearMonth`, so figures are correctly scoped to a specific year and month rather than
  colliding across years
- Request DTOs for creating and updating records, so clients send only the fields they should control (for example, an
  income's account is set once at creation by `accountId` and cannot be changed by an update)
- Bean Validation on every request, with field-level error messages (negative amounts, blank names, missing dates,
  invalid emails and so on)
- Global exception handling — `404 Not Found` for missing records and `400 Bad Request` for invalid input, instead of
  generic `500` errors
- Pagination on every list endpoint
- Deleting an account also deletes its incomes, expenses, and budgets, in a single transaction

## Tech Stack

- **Java 21**
- **Spring Boot** (Web, Data JPA, Validation)
- **PostgreSQL**
- **Docker and Docker Compose** (multi-stage Dockerfile; the app and database run together)
- **Maven**
- **JUnit 5 + Mockito** (unit tests)

## Getting Started

### Prerequisites

- Java 21+
- Docker and Docker Compose
- Maven

### Option 1: Run everything with Docker

The app and the database both run in containers:

```
docker compose up --build
```

The API starts on `http://localhost:8080`. Stop any locally running copy of the app first so port 8080 is free.

### Option 2: Run the app locally

Start only the PostgreSQL database using Docker Compose:

```
docker compose up -d postgres
```

Then run the application:

```
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`.

> Create an account first via `POST /api/v1/account` — every other resource requires a valid `accountId`.

---

## API Endpoints

All endpoints are under `/api/v1`. `{yearMonth}` is in `YYYY-MM` format, e.g. `2026-03`.

List endpoints (`GET /account`, `/income`, `/expense`, `/budget`) are paginated. Use `?page=0&size=20` and optionally
`&sort=amount,desc` (results are sorted by `id` by default). The response contains the items in `content` plus paging
fields such as `totalElements` and `totalPages`.

### Account

| Method | Endpoint        | Description                                                     |
|--------|-----------------|-----------------------------------------------------------------|
| GET    | `/account`      | Get all accounts (paginated)                                    |
| GET    | `/account/{id}` | Get account by ID                                               |
| POST   | `/account`      | Add a new account                                               |
| PUT    | `/account/{id}` | Update account details                                          |
| DELETE | `/account/{id}` | Remove an account and all of its incomes, expenses, and budgets |

### Income

| Method | Endpoint                                       | Description                        |
|--------|------------------------------------------------|------------------------------------|
| GET    | `/income`                                      | Get all income records (paginated) |
| GET    | `/income/{id}`                                 | Get income by ID                   |
| GET    | `/income/total-income/{accountId}/{yearMonth}` | Get total income for a given month |
| POST   | `/income`                                      | Add a new income record            |
| PUT    | `/income/{id}`                                 | Update income details              |
| DELETE | `/income/{id}`                                 | Remove an income record            |

### Expense

| Method | Endpoint                                                     | Description                                               |
|--------|--------------------------------------------------------------|-----------------------------------------------------------|
| GET    | `/expense`                                                   | Get all expenses (paginated)                              |
| GET    | `/expense/{id}`                                              | Get expense by ID                                         |
| GET    | `/expense/total-spent/{accountId}/{yearMonth}`               | Get total spent for a given month                         |
| GET    | `/expense/largest-expense/{accountId}/{yearMonth}`           | Get the largest single expense for a given month          |
| GET    | `/expense/largest-spending-category/{accountId}/{yearMonth}` | Get the category with the highest spend for a given month |
| POST   | `/expense`                                                   | Add a new expense                                         |
| PUT    | `/expense/{id}`                                              | Update expense details                                    |
| DELETE | `/expense/{id}`                                              | Remove an expense                                         |

### Budget

| Method | Endpoint                                            | Description                                    |
|--------|-----------------------------------------------------|------------------------------------------------|
| GET    | `/budget`                                           | Get all budgets (paginated)                    |
| GET    | `/budget/{id}`                                      | Get budget by ID                               |
| GET    | `/budget/{accountId}/{yearMonth}`                   | Get all budgets for a given month              |
| GET    | `/budget/monthly-budget/{accountId}/{yearMonth}`    | Get total budgeted amount for a given month    |
| GET    | `/budget/overspent-budgets/{accountId}/{yearMonth}` | Get budgets where spending exceeded the target |
| POST   | `/budget`                                           | Add a new budget                               |
| PUT    | `/budget/{id}`                                      | Update budget details                          |
| DELETE | `/budget/{id}`                                      | Remove a budget                                |

### Financial Summary

| Method | Endpoint                                                     | Description                                             |
|--------|--------------------------------------------------------------|---------------------------------------------------------|
| GET    | `/financial-summary/monthly-savings/{accountId}/{yearMonth}` | Get income minus spending for a given month             |
| GET    | `/financial-summary/running-balance/{accountId}`             | Get all-time income minus all-time spending             |
| GET    | `/financial-summary/savings-rate/{accountId}/{yearMonth}`    | Get savings as a percentage of income for a given month |
| GET    | `/financial-summary/summary/{accountId}/{yearMonth}`         | Get the full financial summary for a given month        |

---

## Example Requests

Create requests (`POST`) for income, expenses, and budgets take a flat `accountId`. Update requests (`PUT`) do not
include `accountId`, because a record cannot be moved to a different account.

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
  "accountId": 1,
  "amount": 2000,
  "source": "Work",
  "dateReceived": "2026-03-07",
  "frequency": "MONTHLY",
  "description": "Monthly work wage"
}
```

### Update an Income Record

```
PUT /api/v1/income/1
Content-Type: application/json

{
  "amount": 2200,
  "source": "Work",
  "dateReceived": "2026-03-07",
  "frequency": "MONTHLY",
  "description": "Monthly work wage after pay rise"
}
```

### Add an Expense

```
POST /api/v1/expense
Content-Type: application/json

{
  "accountId": 1,
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
  "accountId": 1,
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

Sample `.http` request files for every resource are included in the `http/` directory for use with IntelliJ's HTTP
client.

---

## Validation and Error Handling

Request bodies are validated before any business logic runs. Errors are returned by a single `@RestControllerAdvice`
handler (`GlobalExceptionHandler`).

**Invalid input returns `400 Bad Request`** with a map of field name to message:

```
POST /api/v1/expense
{ "accountId": 1, "amount": -150, "category": "FOOD", "datePaid": "2026-03-10", "frequency": "MONTHLY" }

HTTP/1.1 400
{
  "amount": "Expense amount must be greater than zero"
}
```

**A missing record returns `404 Not Found`** with a message:

```
POST /api/v1/income   (with "accountId": 9999)

HTTP/1.1 404
Account with ID 9999 not found
```

The same applies to unknown IDs on `GET`, `PUT`, and `DELETE`.

Validation rules include: amounts must be present and greater than zero; text fields such as names and sources must not
be blank; dates, categories, frequencies, and months are required; and account emails must be valid.

---

## Project Structure

```
src/
├── main/java/com/project/fintrackapi/
│   ├── controller/    Account, Income, Expense, Budget, FinancialSummary controllers
│   ├── dto/           Create and update request records for each resource
│   ├── service/       Business logic and cross-entity calculations
│   ├── repository/    Spring Data JPA interfaces
│   ├── entity/        Account, Income, Expense, Budget, FinancialSummary
│   ├── exception/     ResourceNotFoundException and GlobalExceptionHandler
│   └── enums/         Category, Frequency
├── test/java/com/project/fintrackapi/service/
│   └── Unit tests for each service, mirroring the main package structure
├── http/               Sample request files per resource
└── docker-compose.yml
```

---

## Testing

Unit tests cover the service layer using JUnit 5 and Mockito, focused on methods containing real business logic —
filtering, aggregation, branching — rather than trivial CRUD passthroughs that just forward to a repository. Run with:

```
./mvnw test
```

> The application context test connects to the PostgreSQL database, so start it first with
> `docker compose up -d postgres`.

## What I Learned

- Designing a multi-entity REST API around a shared owner (`Account`), and scoping every query and calculation by
  `accountId` rather than operating globally
- Composing business logic across services — `FinancialSummaryService` depends on `IncomeService`, `ExpenseService`, and
  `BudgetService` rather than duplicating their logic
- Using Java Streams and Collectors for aggregation — grouping expenses by category, finding maximums, summing filtered
  results
- The difference between `java.time.Month` and `java.time.YearMonth`, and why filtering financial data by month name
  alone silently merges data across different years
- Writing unit tests with JUnit 5 and Mockito, including mocking one service layer against another (not just
  repositories), and being deliberate about which methods are worth testing
- Structuring a Spring Boot application into controller, service, repository, and entity packages rather than one flat
  package
- Using Mockito's `@Mock`/`@InjectMocks` to isolate a class under test from its real dependencies, and testing both
  branches of methods that return `null`/zero on empty input
- Using Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Positive`) on request objects, and why `@Positive` alone
  still accepts `null`
- Centralising error handling with `@RestControllerAdvice`, and choosing the correct status code (`400` for bad input,
  `404` for a missing resource) instead of letting unhandled exceptions become `500` errors
- Using request DTOs (as Java records) to separate the API contract from the JPA entities, so a client cannot set fields
  like `id` or move a record to a different account
- Paginating list endpoints with Spring Data's `Pageable`
- Using `@Transactional` so that deleting an account and all of its records either fully succeeds or fully fails

## Known Limitations / Future Improvements

- **No authentication or authorisation.** Every endpoint is open.
- **Responses still return entities directly.** Requests use DTOs, but responses do not, so each income, expense, and
  budget response includes its full nested account (including the email address). Response DTOs are a planned
  improvement.
- **Account emails are not enforced as unique.**
- **Deleting an account permanently removes its data.** The delete is atomic (all or nothing), but there is no soft
  delete or undo.
- **Frequency is descriptive only.** A `WEEKLY` or `ANNUALLY` income/expense record is not currently projected into a
  monthly-equivalent figure — one record represents one actual transaction on one date. Recurring-payment projection is
  a planned enhancement.
- **Aggregations are computed in application memory** rather than pushed down to the database via SQL aggregation
  queries.

## Author

Hafez Sam — [github.com/hsmsam](https://github.com/hsmsam)