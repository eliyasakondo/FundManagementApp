# Detailed Engineering Rules, Coding Standards & Git Guidelines
## Fund Management App (Android / Jetpack Compose)

---

## 1. Architectural Guardrails

1. **Zero Business Logic in UI Composables:** Composable functions must strictly contain layout and rendering code. Composable functions MUST only receive `UiState` objects and emit event callbacks (e.g., `onLoginClick: (String, String) -> Unit`).
2. **Domain Isolation:** Classes in `domain/` must be pure Kotlin classes with zero imports from `android.*` or `androidx.*` packages.
3. **Repository Abstraction:** ViewModels MUST interact only with Domain Use Cases or Repository interfaces (`MemberRepository`), never directly with Room DAOs or Supabase client APIs.
4. **Immutable State Flow:** All ViewModel state exposed to UI MUST use immutable `StateFlow<UiState>` via `asStateFlow()`. Direct mutation of state from UI is prohibited.

---

## 2. Kotlin Naming Conventions & Code Style

### 2.1 Classes & Interfaces
- **UpperCamelCase:** `ProcessPaymentUseCase`, `MemberRepositoryImpl`, `AuthViewModel`, `MemberScreen`.
- **Suffix Standards:**
  - ViewModels: `[Feature]ViewModel` (e.g., `AdminViewModel`)
  - Repositories: `[Entity]Repository` (Interface) and `[Entity]RepositoryImpl` (Data)
  - DAOs: `[Entity]Dao` (e.g., `MemberDao`)
  - Composables: Named as Nouns or Screen Descriptions (e.g., `MemberDashboardContent`, `PaymentReceiptCard`).

### 2.2 Functions & Variables
- **lowerCamelCase:** `calculateExtraDonation()`, `monthlyContribution`, `isLoading`.
- **Composable Functions:** Must start with UpperCamelCase (e.g., `PrimaryButton()`, `StatusBadge()`).
- **State Flow Naming:** Private mutable state prefixed with `_` (e.g., `private val _uiState = MutableStateFlow(...)`), public exposed immutable state without `_` (e.g., `val uiState: StateFlow<UiState> = _uiState.asStateFlow()`).

---

## 3. Git Workflow & Conventional Commit Standards

All commit messages MUST follow the **Conventional Commits** specification:

```
<type>(<scope>): <short summary in present tense>

[optional body explaining reason for change]
```

### 3.1 Allowed Commit Types

| Commit Type | Purpose | Example |
| :--- | :--- | :--- |
| `feat` | Adding a new feature or Use Case | `feat(domain): add ProcessPaymentUseCase for surplus donation split` |
| `fix` | Bug fix in code or calculation | `fix(ui): resolve number formatting bug in Bangla locale` |
| `refactor` | Code restructuring without feature/bug change | `refactor(data): extract Supabase DTO mappers into separate file` |
| `docs` | Documentation update in `docs/` | `docs(design): update component state matrix and color tokens` |
| `style` | Formatting, whitespace, or lint fix | `style(ui): fix trailing commas in MainActivity.kt` |
| `test` | Adding or updating unit/UI tests | `test(usecase): add unit tests for MultiMonthPaymentUseCase` |
| `chore` | Dependency updates or Gradle build config | `chore(gradle): add Room DB and Supabase dependencies` |

---

## 4. Code Review & Pull Request Checklist

Before submitting a Pull Request (PR) to `main` or `production`, developers/agents MUST verify:

- [ ] All unit tests pass cleanly (`gradlew testDebugUnitTest`).
- [ ] Code compiles with zero errors and zero new lint warnings.
- [ ] All UI strings and numbers dynamically support both Bangla (`বাংলা`) and English.
- [ ] No hardcoded API keys, secrets, or database credentials exist in code.
- [ ] All new Domain Use Cases have accompanying unit test coverage.
- [ ] Commit messages strictly follow Conventional Commit format.

---

## 5. Tool Safety & Data Integrity Rules

1. **NO Shell File Edits:** NEVER use `sed`, `awk`, `rm`, `echo >`, or shell redirection commands to modify project source code. Always use built-in IDE file modification tools (`write_file`, `replace_file_content`, `multi_replace_file_content`).
2. **Surgical Modifications:** Make surgical edits to specific lines/blocks without wiping out or corrupting surrounding existing code.
3. **IDE Buffer Safety:** Always respect IDE memory buffers to prevent desynchronization between disk and IDE workspace.
