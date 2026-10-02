# Comprehensive Architecture & Database Schema Specification
## Clean Architecture, MVVM, Supabase PostgreSQL & Room Database

---

## 1. Clean Architecture Layer Mapping

```
┌─────────────────────────────────────────────────────────────────┐
│                       Presentation Layer                        │
│   Jetpack Compose UI | ViewModels | StateFlow | UiState | Event │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                          Domain Layer                           │
│   Pure Kotlin Use Cases | Domain Models | Repository Interfaces │
└────────────────────────────────┬────────────────────────────────┘
                                 │
                                 ▼
┌─────────────────────────────────────────────────────────────────┐
│                           Data Layer                            │
│  Supabase PostgreSQL API | Room DB DAOs | Repository Impl | DTOs│
└─────────────────────────────────────────────────────────────────┘
```

---

## 2. Complete Supabase PostgreSQL Relational Database Schema (12 Tables)

### 2.1 `members` Table
Stores complete profile information and authentication credentials mapping for all 200+ members.
```sql
CREATE TABLE members (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id VARCHAR(20) UNIQUE NOT NULL,
    full_name_bn TEXT NOT NULL,
    full_name_en TEXT NOT NULL,
    mobile_number VARCHAR(15) UNIQUE NOT NULL,
    alt_mobile_number VARCHAR(15),
    email VARCHAR(100),
    date_of_birth DATE,
    gender VARCHAR(10),
    profession TEXT,
    designation TEXT,
    membership_type VARCHAR(50) DEFAULT 'General',
    joining_date DATE DEFAULT CURRENT_DATE,
    present_address TEXT,
    permanent_address TEXT,
    emergency_contact VARCHAR(15),
    monthly_contribution NUMERIC(10,2) NOT NULL DEFAULT 500.00,
    contribution_effective_date DATE DEFAULT CURRENT_DATE,
    account_status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, LOCKED
    role VARCHAR(20) DEFAULT 'MEMBER',          -- ADMIN, MANAGER, MEMBER
    profile_photo_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.2 `contribution_rate_history` Table
Tracks historical changes to a member's assigned monthly contribution rate over time.
```sql
CREATE TABLE contribution_rate_history (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id UUID NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    previous_rate NUMERIC(10,2) NOT NULL,
    new_rate NUMERIC(10,2) NOT NULL,
    effective_from_date DATE NOT NULL,
    changed_by UUID NOT NULL REFERENCES members(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.3 `contributions` Table
Tracks month-by-month contribution requirements, paid amounts, and payment statuses for each member.
```sql
CREATE TABLE contributions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id UUID NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    month INT NOT NULL CHECK (month BETWEEN 1 AND 12),
    year INT NOT NULL,
    required_amount NUMERIC(10,2) NOT NULL,
    paid_amount NUMERIC(10,2) DEFAULT 0.00,
    status VARCHAR(20) DEFAULT 'UNPAID', -- UNPAID, PARTIAL, PAID, ADVANCE, OVERDUE, WAIVED, ADJUSTED
    updated_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(member_id, month, year)
);
```

### 2.4 `payments` Table (Master Receipts)
Stores master payment receipt records when money is received from a member.
```sql
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    receipt_number VARCHAR(30) UNIQUE NOT NULL,
    member_id UUID NOT NULL REFERENCES members(id),
    payment_date DATE NOT NULL,
    total_amount_received NUMERIC(10,2) NOT NULL,
    contribution_portion NUMERIC(10,2) NOT NULL,
    donation_portion NUMERIC(10,2) DEFAULT 0.00,
    payment_method VARCHAR(30) NOT NULL, -- BKASH, NAGAD, BANK_TRANSFER, CASH, OTHER
    transaction_reference VARCHAR(100),
    recorded_by UUID NOT NULL REFERENCES members(id),
    approved_by UUID REFERENCES members(id),
    approval_status VARCHAR(20) DEFAULT 'APPROVED', -- PENDING, APPROVED, REJECTED
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.5 `payment_allocations` Table
Junction table linking a payment receipt to the month(s) it covers (enabling multi-month payments).
```sql
CREATE TABLE payment_allocations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id UUID NOT NULL REFERENCES payments(id) ON DELETE CASCADE,
    contribution_id UUID NOT NULL REFERENCES contributions(id) ON DELETE CASCADE,
    allocated_amount NUMERIC(10,2) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.6 `extra_donations` Table
Stores extra donation records (surplus payments, campaign donations, anonymous donations).
```sql
CREATE TABLE extra_donations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    payment_id UUID REFERENCES payments(id) ON DELETE SET NULL,
    member_id UUID REFERENCES members(id), -- NULL for external/anonymous donors
    donor_name TEXT,                        -- For anonymous/external donors
    donation_type VARCHAR(30) DEFAULT 'SURPLUS', -- SURPLUS, GENERAL, CAMPAIGN, EXTERNAL
    amount NUMERIC(10,2) NOT NULL,
    purpose TEXT,
    donation_date DATE NOT NULL,
    is_public BOOLEAN DEFAULT TRUE,
    recorded_by UUID NOT NULL REFERENCES members(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.7 `organization_income` Table
Stores non-contribution income (event income, membership fees, grants).
```sql
CREATE TABLE organization_income (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    category VARCHAR(50) NOT NULL, -- MEMBERSHIP_FEE, EVENT_INCOME, GRANT, OTHER
    amount NUMERIC(10,2) NOT NULL,
    income_date DATE NOT NULL,
    source TEXT,
    recorded_by UUID NOT NULL REFERENCES members(id),
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.8 `organization_expenses` Table
Stores organization operating expenses with receipt attachments and approvals.
```sql
CREATE TABLE organization_expenses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title TEXT NOT NULL,
    category VARCHAR(50) NOT NULL, -- OFFICE, UTILITY, RENT, EVENT, WELFARE, TRANSPORT, OTHER
    amount NUMERIC(10,2) NOT NULL,
    expense_date DATE NOT NULL,
    vendor_receiver TEXT,
    description TEXT,
    attachment_url TEXT,
    is_public BOOLEAN DEFAULT TRUE,
    recorded_by UUID NOT NULL REFERENCES members(id),
    approved_by UUID REFERENCES members(id),
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.9 `notifications` Table
In-app notifications sent to members.
```sql
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    member_id UUID NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    title_bn TEXT NOT NULL,
    title_en TEXT NOT NULL,
    message_bn TEXT NOT NULL,
    message_en TEXT NOT NULL,
    notification_type VARCHAR(30) DEFAULT 'PAYMENT_CONFIRMATION',
    is_read BOOLEAN DEFAULT FALSE,
    related_transaction_id UUID,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.10 `messages` Table
Direct messaging between Members and Admin/Manager.
```sql
CREATE TABLE messages (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    sender_id UUID NOT NULL REFERENCES members(id),
    receiver_id UUID REFERENCES members(id), -- NULL for broadcast to all
    message_text TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.11 `audit_logs` Table
Immutable security audit ledger tracking all system changes.
```sql
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    actor_id UUID NOT NULL REFERENCES members(id),
    action_type VARCHAR(50) NOT NULL, -- MEMBER_CREATED, PAYMENT_RECORDED, RATE_CHANGED, etc.
    target_table VARCHAR(50) NOT NULL,
    details JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
);
```

### 2.12 `system_settings` Table
Admin configuration rules for privacy and manager permissions.
```sql
CREATE TABLE system_settings (
    key VARCHAR(50) PRIMARY KEY,
    value JSONB NOT NULL,
    description TEXT,
    updated_by UUID REFERENCES members(id),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);
```

---

## 3. Offline-First Room DB Caching Strategy
The Android application uses Room Database (`AppDatabase`) to mirror these tables locally:
- `MemberEntity`, `ContributionEntity`, `PaymentEntity`, `ExtraDonationEntity`, `ExpenseEntity`, `NotificationEntity`.
- Local Room DB provides instant UI rendering and offline cache, syncing seamlessly with Supabase PostgreSQL via `Repository` implementations.
