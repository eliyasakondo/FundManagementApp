# Detailed Engineering Rules & AI Guardrails

---

## 1. Architectural Guardrails
1. **Zero Logic in UI:** Composable functions must strictly contain layout and rendering code. All state operations belong in ViewModels via `UiState`.
2. **Domain Isolation:** Use Cases in `domain/usecase/` must be pure Kotlin classes with no imports from `android.*` or `androidx.*`.
3. **Repository Pattern:** ViewModel must communicate with Domain Repository interfaces, never directly with DAOs or Supabase client.

---

## 2. Localization & Formatting Rules
1. **Dynamic Locale Switcher:** Never hardcode English strings or numbers in UI code. All strings must use `Strings.kt` / string resources.
2. **Dynamic Number Formatter:** All financial amounts must pass through `CurrencyFormatter.format(amount, locale)`.

---

## 3. Tool & File Safety
1. **NO Shell File Edits:** Do not use `sed`, `awk`, `rm`, or shell redirection commands. Use built-in IDE file modification tools (`write_file`, `replace_file_content`).
2. **Surgical Changes:** Modify only necessary code blocks without touching unrelated files.
