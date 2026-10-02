# Security Specifications & Compliance

---

## 1. Authentication & Authorization
- **Authentication Engine:** Supabase Auth with JWT (JSON Web Tokens) and refresh tokens.
- **Role-Based Access Control (RBAC):**
  - **Admin:** Full CRUD access on members, contribution rates, approvals, expenses, system settings.
  - **Manager:** Operational payment entry and expense logging (requires Admin approval).
  - **Member:** Read-only access to own profile, contribution statement, and public summaries.

---

## 2. Database Security (Row Level Security - RLS)
- **`members` Table RLS:** Member can SELECT own record; Admin/Manager can SELECT/UPDATE all.
- **`contributions` Table RLS:** Member can SELECT own records and public history; Admin/Manager can INSERT/UPDATE.

---

## 3. Storage Security
- **Sensitive Tokens:** JWT access and refresh tokens stored using `EncryptedSharedPreferences` / `Encrypted DataStore`.
- **Transport Security:** Strict HTTPS/TLS 1.3 encryption on all API calls.
