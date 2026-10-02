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

---

## 6. Supabase Auto-Pause Prevention & Health Check Keep-Alive Strategy

Supabase Free Tier automatically pauses projects after 7 consecutive days of zero database activity. To ensure 100% continuous uptime and prevent auto-pausing during low-activity periods (e.g., holidays), the system implements a **Secure Automated Health Check Keep-Alive Mechanism**:

### 6.1 Architecture & Workflow

```
┌─────────────────────────┐          2x Daily HTTP GET          ┌───────────────────────────┐
│  GitHub Actions / Cron  │ ──────────────────────────────────► │ Supabase Health Endpoint  │
│  (Scheduled Keep-Alive) │   x-api-key: [SECURE_PING_KEY]    │ (/rest/v1/system_settings)│
└─────────────────────────┘                                     └─────────────┬─────────────┘
                                                                              │
                                                                              ▼
                                                                ┌───────────────────────────┐
                                                                │ PostgreSQL DB Touch Ping  │
                                                                │ (Updates last_ping_at)    │
                                                                └───────────────────────────┘
```

### 6.2 Implementation Specifications

1. **Scheduled Automated Trigger:**
   - A GitHub Actions workflow (`.github/workflows/supabase-keep-alive.yml`) runs on a Cron schedule twice daily (`0 0,12 * * *` — 12:00 AM & 12:00 PM UTC).
2. **Lightweight Health Query:**
   - The trigger issues a lightweight HTTP `GET` query to Supabase PostgREST requesting a single system setting row:
     `GET /rest/v1/system_settings?select=key&key=eq.app_version`
3. **Security Safeguards (Zero Vulnerability):**
   - **Read-Only Public Scope:** The query executes against a read-only setting key without modifying any member data or balances.
   - **Authentication Header:** Requests pass the standard `apikey: ANON_KEY` and are subject to standard Supabase Kong API rate limiting.
   - **No Data Exposure:** Returns zero PII, passwords, or transaction records (returns only string `app_version`).
   - **pg_cron Internal Backup:** Alternatively, PostgreSQL internal `pg_cron` extension executes a 12-hour `SELECT 1;` query inside PostgreSQL.

### 6.3 Outcome
Guarantees the Supabase project stays active 24/7/365 without ever going to sleep, ensuring zero latency delays for members opening the app after days of inactivity.
