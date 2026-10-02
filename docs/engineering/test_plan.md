# Comprehensive Automated Test Plan & Quality Assurance Specification
## Fund Management App (Android / Jetpack Compose)

---

## 1. Quality Assurance Strategy & Test Pyramid

```
                       / \
                      /   \  UI / Integration Tests (10%)
                     /  UI \ (Compose Test, End-to-End Flows)
                    /-------\
                   /         \  DAO & Integration Tests (20%)
                  / DAO/InMem \ (Room In-Memory DB, Repositories)
                 /-------------\
                /               \  Domain & ViewModel Unit Tests (70%)
               /  Unit UseCases  \ (Pure Kotlin Use Cases, Financial Engine)
              /-------------------\
```

- **Unit Tests (70%):** Pure Kotlin business logic tests for Domain Use Cases, financial calculation engine, ViewModels, and StateFlow reducers.
- **DAO & Integration Tests (20%):** Room DB DAO tests in AndroidX In-Memory database, repository sync tests.
- **Compose UI Tests (10%):** UI component rendering, language toggle switching, form input validations, and screen navigation tests.

---

## 2. Domain Layer Financial Calculation Test Matrix

Every financial Use Case in `domain/usecase/` MUST pass 100% of the following unit test scenarios before deployment:

### 2.1 `ProcessPaymentUseCaseTest` (Extra Donation Routing & Surplus Split)

| Test Case ID | Input Required Amount | Input Received Amount | Expected Monthly Portion | Expected Extra Donation | Expected Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-PAY-001` | ৳500.00 | ৳500.00 | ৳500.00 | ৳0.00 | `PAID` |
| `TC-PAY-002` | ৳500.00 | ৳800.00 | ৳500.00 | ৳300.00 (Surplus) | `PAID` |
| `TC-PAY-003` | ৳500.00 | ৳300.00 | ৳300.00 | ৳0.00 | `PARTIAL` |
| `TC-PAY-004` | ৳500.00 | ৳0.00 | ৳0.00 | ৳0.00 | `UNPAID` |

### 2.2 `PartialPaymentUseCaseTest` (Accumulator & Status Lifecycle)

| Test Case ID | Initial Month Status | Prior Paid Amount | New Payment | New Paid Total | Expected New Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-PRT-001` | `UNPAID` (Req: ৳500) | ৳0.00 | ৳200.00 | ৳200.00 | `PARTIAL` (Remaining: ৳300) |
| `TC-PRT-002` | `PARTIAL` (Req: ৳500)| ৳200.00 | ৳300.00 | ৳500.00 | `PAID` (Remaining: ৳0) |
| `TC-PRT-003` | `PARTIAL` (Req: ৳500)| ৳200.00 | ৳400.00 | ৳500.00 (Contribution) | `PAID` + ৳100 Extra Donation |

### 2.3 `MultiMonthPaymentUseCaseTest` (Lump Sum Multi-Month Distribution)

| Test Case ID | Lump Sum Input | Month 1 Req | Month 2 Req | Month 3 Req | Result Allocation |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `TC-MLT-001` | ৳1,500.00 | ৳500.00 | ৳500.00 | ৳500.00 | Month 1: `PAID`, Month 2: `PAID`, Month 3: `PAID` |
| `TC-MLT-002` | ৳1,200.00 | ৳500.00 | ৳500.00 | ৳500.00 | Month 1: `PAID`, Month 2: `PAID`, Month 3: `PARTIAL` (৳200) |
| `TC-MLT-003` | ৳2,000.00 | ৳500.00 | ৳500.00 | ৳500.00 | Months 1-3: `PAID` + ৳500 Advance/Extra Donation |

---

## 3. Instrumented Room Database DAO Testing

DAO tests run on an AndroidX `Room.inMemoryDatabaseBuilder` to verify SQL queries, cascade deletions, and unique constraints.

### 3.1 `MemberDaoTest`
- `insertAndGetMemberById()`: Verify inserting a `MemberEntity` and querying by UUID.
- `updateAccountStatus()`: Verify updating status from `ACTIVE` to `LOCKED`.
- `enforceUniqueMemberIdConstraint()`: Verify inserting duplicate `member_id` throws SQLiteConstraintException.

### 3.2 `ContributionDaoTest`
- `getContributionsByMemberAndYear()`: Verify querying 12-month contribution records for a member.
- `updatePaidAmountAndStatus()`: Verify atomic state update of paid amount and status.

---

## 4. ViewModel & StateFlow Unit Testing

ViewModels are tested using `kotlinx.coroutines.test.TestDispatcher` and `MockK` to isolate repository calls.

### 4.1 `AuthViewModelTest`
- `loginWithValidCredentials_emitsSuccessState()`: Mock successful login, verify `UiState` transitions to `Success(member)`.
- `loginWithInvalidPassword_emitsErrorState()`: Mock invalid password error, verify `UiState` emits `Error(message)`.

### 4.2 `MemberDashboardViewModelTest`
- `fetchMemberData_populatesBilingualUIState()`: Verify dashboard correctly calculates total paid, overdue balance, and notification count.

---

## 5. Compose UI & Accessibility Testing

Compose tests run using `createComposeRule()` or `createAndroidComposeRule<MainActivity>()`.

### 5.1 Test Cases
- `LoginScreen_displaysValidationErrors_onEmptySubmit()`: Test clicking "Login" button with empty fields shows validation red helper text.
- `LanguageToggle_switchesBilingualText_instantly()`: Test clicking "বাংলা / English" switch updates all screen titles, labels, and numbers (`৳1,500` $\leftrightarrow$ `৳১,৫০০`).
- `AddPaymentModal_rendersOverlayAboveBottomBar()`: Verify modal sheet displays above bottom navigation bars with 48dp touch targets.

---

## 6. Test Fixtures & Mocking Architecture

Shared test fixtures are located in `app/src/test/java/.../fixtures/`:

```kotlin
object TestFixtures {
    val sampleMember = Member(
        id = "550e8400-e29b-41d4-a716-446655440000",
        memberId = "M-101",
        fullNameBn = "মোঃ রফিকুল ইসলাম",
        fullNameEn = "Md. Rafiqul Islam",
        monthlyContribution = 500.0,
        role = Role.MEMBER
    )
}
```

---

## 7. CI/CD Code Coverage & Quality Mandate

- **Minimum Coverage Mandate:**
  - `domain/usecase/`: 100% Code Coverage
  - `data/repository/`: 85% Code Coverage
  - ViewModels: 80% Code Coverage
- **Automated Verification:** `gradlew testDebugUnitTest` and `gradlew connectedCheck` must pass clean with zero errors before merging to `main` or `production`.
