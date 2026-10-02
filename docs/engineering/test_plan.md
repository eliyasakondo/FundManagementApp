# Comprehensive Test Plan

---

## 1. Unit Testing Strategy
- **Target:** 100% unit test coverage for Domain Use Cases in `domain/usecase/`.
- **Test Cases:**
  - `ProcessPaymentUseCaseTest`: Test exact split between monthly contribution and extra donation fund.
  - `PartialPaymentUseCaseTest`: Test status transition from `Unpaid` $\rightarrow$ `Partially Paid` $\rightarrow$ `Paid`.
  - `MultiMonthPaymentUseCaseTest`: Test lump sum distribution across 3 consecutive months.
- **Frameworks:** JUnit 5, MockK, Kotlin Coroutines Test (`runTest`).

---

## 2. Instrumented & DAO Testing
- **Target:** Room Database DAOs (`MemberDaoTest`, `ContributionDaoTest`).
- **Frameworks:** AndroidX Test, Room In-Memory Database.

---

## 3. Compose UI & Integration Testing
- **Target:** Member Home Dashboard rendering, language toggle switching, Login form validation.
- **Frameworks:** Compose UI Test JUnit4.
