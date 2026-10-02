# Security Requirements & Compliance Specification
## Fund Management App (Android / Supabase Backend)

---

> [!CRITICAL]
> **Core Non-Negotiable Security Directives:**
> 1. **Never Reinvent Authentication (Use Trusted Tools):** DO NOT build custom authentication, JWT generation, or custom password hashing logic from scratch. Always use battle-tested industry tools (Supabase Auth SDK).
> 2. **Generic Authentication Error Messages:** Never reveal whether an email/mobile exists during login errors. Always display generic error messages (e.g. "মোবাইল নম্বর অথবা পাসওয়ার্ড সঠিক নয়" / "Invalid mobile number or password") to prevent Account Enumeration attacks.
> 3. **Dual Validation (Client-Side & Server-Side):** Perform client-side validation in Compose UI for responsive user feedback, and ALWAYS re-validate and sanitize on the server-side (PostgreSQL constraints & RLS).
> 4. **Encrypted Passwords & Tokens:** Passwords MUST be hashed server-side using Bcrypt/Argon2 via Supabase Auth. Local session tokens on Android MUST be encrypted at rest via `EncryptedSharedPreferences` / `Encrypted DataStore`.
> 5. **Login Rate Limiting:** Login endpoints MUST enforce server-side rate limits (max 5 attempts per minute per IP/device) to prevent brute-force attacks.
> 6. **Never Expose API Keys:** Secret service-role keys and database connection strings MUST NEVER be shipped in Android APKs or client code.

---

## 1. Master Production Security Hardening Checklist (18 Directives)

| # | Security Requirement | Implementation Specification | Compliance Status |
| :- | :--- | :--- | :--- |
| **1** | **Use Standard Auth Tools** | Use Supabase Auth SDK; DO NOT invent custom authentication, custom token generators, or custom password hashing algorithms. | ✅ Enforced |
| **2** | **Generic Error Messages** | Display generic error messages ("Invalid credentials") for login failures to prevent Account Enumeration attacks. | ✅ Enforced |
| **3** | **Client-Side Validation** | Validate inputs (mobile length, required fields, amount bounds) in Compose forms before submission. | ✅ Enforced |
| **4** | **Server-Side Validation** | Re-validate all parameters on server-side using PostgreSQL `CHECK` constraints, foreign keys, and RLS policies. | ✅ Enforced |
| **5** | **Login Rate Limiting** | Limit login attempts to max 5 attempts/minute on Supabase Auth Gateway to prevent brute-force attacks. | ✅ Enforced |
| **6** | **Password Encryption & Hashing** | Passwords hashed server-side with Bcrypt/Argon2; local tokens encrypted via Android `EncryptedSharedPreferences`. | ✅ Enforced |
| **7** | **Hide API Keys** | Never embed secret service-role keys in client code/APK. Only public Supabase Anon Key permitted in client build. | ✅ Enforced |
| **8** | **Protect Admin Routes** | All Admin/Manager screens (Payment Entry, Rate Changes, Approvals) protected via Role-Based Navigation Guards. | ✅ Enforced |
| **9** | **Access Control (RBAC & RLS)** | User resource ownership enforced on backend via PostgreSQL Row Level Security (RLS) on all 21 tables. | ✅ Enforced |
| **10** | **Sanitize Forms** | Sanitize all text fields (Bangla and English) to strip malicious characters before processing. | ✅ Enforced |
| **11** | **XSS Protection** | HTML/Script tag escaping on all member inputs, notes, and notice board posts. | ✅ Enforced |
| **12** | **Secure API Endpoints** | Strict HTTPS / TLS 1.3 transport encryption with payload validation using Kotlin data classes. | ✅ Enforced |
| **13** | **CORS Settings** | Strict origin domain restriction configured on Supabase PostgREST API Gateway. | ✅ Enforced |
| **14** | **Security Headers** | Enforce HSTS, `X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff`, and `Content-Security-Policy`. | ✅ Enforced |
| **15** | **Debug Mode OFF in Release** | Release build configuration must have `isDebuggable = false`, `isMinifyEnabled = true` (R8/ProGuard enabled), and Timber debug logs disabled. | ✅ Enforced |
| **16** | **Update Dependencies** | Keep AndroidX, Compose, Supabase, Kotlin, and Room dependencies updated via `libs.versions.toml`. | ✅ Enforced |
| **17** | **Remove Unused Packages** | R8 code shrinking, dead-code elimination, and tree-shaking enabled in `build.gradle.kts`. | ✅ Enforced |
| **18** | **Check Exposed Files** | `.env`, `google-services.json`, keystore files, and local properties strictly excluded in `.gitignore`. | ✅ Enforced |

---

## 2. Authentication & Session Protection
- **Standard Auth Engine:** Use Supabase Auth for identity management, password hashing, and short-lived JWT token rotation.
- **Generic Login Feedback:** Login failure screen displays: "মোবাইল নম্বর অথবা পাসওয়ার্ড সঠিক নয়" (Invalid mobile number or password). Never reveal whether the mobile number exists in the database.
- **Private Route Protection:** All app screens (Dashboard, Contributions, Payments, Member Profiles, Admin Panel) require an authenticated session. Unauthenticated users are automatically redirected to the Login screen.
- **Biometric & PIN Lock:** Local Android Biometric Prompt (Fingerprint / Face Unlock) or App PIN for quick re-authentication.

---

## 3. Authorization & Access Control (RBAC & RLS)
- **Role-Based Access Control:**
  - **MEMBER:** Read-only access to their own financial statement, contribution history, organization summary, and notifications.
  - **MANAGER:** Can record member payments and log expenses (requires Admin approval for final posting).
  - **ADMIN:** Full administrative rights, member account creation, contribution rate changes, payment approvals, and audit log access.
- **Resource Ownership Enforcement:** Members can only view and edit their own profile information. Cross-member data modification is strictly prevented by API and database policies.

---

## 4. Input Validation (Dual-Layer: Client & Server)

> [!IMPORTANT]
> **Dual Validation Strategy:**
> - **Client-Side Validation (UX Layer):** Compose form states validate mobile number format, non-empty fields, positive numeric amounts, and display immediate helpful error text.
> - **Server-Side Validation (Security Layer):** PostgreSQL constraints (`CHECK`, `NOT NULL`, `UNIQUE`) and Supabase Edge Functions re-validate every field server-side regardless of client input.
> - **SQL Injection & XSS Prevention:** Prepared statements via Room DAOs / Supabase SDK prevent SQL injection; HTML tag escaping prevents XSS.

---

## 5. Secrets & Key Management

> [!CAUTION]
> **API Key & Secret Handling Rules:**
> - **Public Anon Key Only:** Only the public Supabase Anon Key is included in the Android application.
> - **Service-Role Key Isolation:** The Supabase Service-Role / Admin key is strictly kept on server-side functions / Edge Functions and NEVER shipped in the Android APK.
> - **Local Storage Encryption:** JWT session tokens, refresh tokens, and cached user credentials MUST be encrypted at rest using Android `EncryptedSharedPreferences` / `Encrypted DataStore` powered by Android Keystore.
> - **Version Control Protection:** `.env` files, production API secrets, and keystore passwords are strictly excluded via `.gitignore`.

---

## 6. APIs, Data Transfer, CORS & Rate Limiting

- **API Rate Limiting:**
  - Login attempts limited to maximum 5 attempts per minute per IP/Device to prevent brute-force attacks.
  - Payment and donation submission APIs limited to prevent duplicate transaction posts.
- **CORS & Transport Security:** Strict origin domain restriction configured on Supabase PostgREST API Gateway with strict HTTPS / TLS 1.3 transport encryption.
- **Security Headers:** Enforce HSTS, `X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff`, and `Content-Security-Policy`.

---

## 7. Release Build Hardening & Dependency Safety
- **Debug Mode OFF:** Production release builds MUST set `isDebuggable = false` and `isMinifyEnabled = true` with ProGuard/R8 code shrinking and obfuscation enabled.
- **Tree-Shaking:** Remove unused packages, dead code, and unused resources during release builds.
- **Dependency Audit:** Routinely audit dependencies in `libs.versions.toml` to patch vulnerabilities.
- **Exposed File Check:** Verify `.env`, `keystore.jks`, and private config files are strictly excluded via `.gitignore`.
