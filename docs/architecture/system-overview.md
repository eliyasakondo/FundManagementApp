# System Architecture & Infrastructure Specifications
## Security, Rate Limiting, API, Monitoring & Logging Architecture

---

## 1. Security & Authentication Layer
- **Backend (Supabase Auth & RLS):**
  - **Authentication:** JWT (JSON Web Tokens) with auto-refresh mechanism.
  - **Row Level Security (RLS):** Enforced directly at the PostgreSQL database level. RLS policies guarantee that Members can ONLY read their own records and public summaries, while Admins/Managers hold elevated permissions.
- **Client (Android App):**
  - Encrypted storage for session tokens using `EncryptedSharedPreferences` / `Encrypted DataStore`.
  - HTTPS/TLS 1.3 transport security for all API and WebSocket endpoints.

---

## 2. Rate Limiting & DDoS Protection
- **Backend Edge:** Supabase Cloud API Gateway / Kong Gateway enforces IP-based and user-based rate limits (e.g., max 100 API calls/minute per user, 5 login attempts/minute).
- **Client UI:** Debouncing UI events using Kotlin Coroutines `Flow.debounce(300ms)` on search fields, login forms, and payment submit buttons to prevent accidental double-submission or spam.

---

## 3. API Backend & Transport Layer
- **Auto-Generated REST & Realtime API:** Supabase PostgREST exposes type-safe REST APIs directly over the PostgreSQL schema.
- **WebSocket Subscriptions:** Realtime WebSocket channel for instant payment confirmation alerts and live organizational balance updates.
- **Client Data Repository:** Android Clean Architecture `Repository` maps network responses to immutable Domain Models.

---

## 4. Monitoring & Telemetry
- **Backend Infrastructure:** Supabase Cloud Dashboard tracks API latency, database CPU/Memory usage, active WebSocket connections, and query execution times.
- **Client App Health:** Integration with Firebase Crashlytics and Android Vitals to monitor app crashes, ANRs (Application Not Responding), and frame rendering performance.

---

## 5. Audit Logging & System Ledger
- **Immutable Database Audit Ledger (`audit_logs` Table):** Every critical operation (Member Creation, Rate Changes, Payment Recording, Approvals, Expense Logging, Password Resets) automatically writes an immutable log containing:
  - `timestamp`
  - `actor_id` (Admin / Manager User ID)
  - `action_type` (e.g., `PAYMENT_RECORDED`, `RATE_CHANGED`)
  - `old_value` / `new_value`
  - `ip_address`
- **Client Application Logging:** Timber logger for development; sanitized error telemetry sent to Sentry / Crashlytics in production (zero PII / password logging).
