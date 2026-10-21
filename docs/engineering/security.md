# Security & Granular Role-Based Access Control (RBAC)
## Fund Management App Permission Matrix & Admin Controls

---

## 1. Role Architecture & Stack
The application supports 3 distinct user role stacks:
1. **Admin (এডমিন):** Full System Authority & Permission Delegator.
2. **Manager (ম্যানেজার):** Operational Access (Configurable by Admin).
3. **Member (সদস্য):** View-Only Access (Configurable Privacy Filters by Admin).

---

## 2. Granular Role & Permission Control Matrix

| Category | Permission Flag | Admin Default | Manager Configurable | Member Configurable |
| :--- | :--- | :---: | :---: | :---: |
| **Financial Operations** | `RECORD_PAYMENT` (পেমেন্ট এন্ট্রি) | ✅ Always | 🎛️ Toggleable (On) | ❌ Restricted |
| | `APPROVE_PAYMENT` (পেমেন্ট এপ্রুভাল) | ✅ Always | 🎛️ Toggleable (Off) | ❌ Restricted |
| | `ALLOCATE_EXTRA_DONATION` (অনুদান বরাদ্দ) | ✅ Always | 🎛️ Toggleable (On) | ❌ Restricted |
| | `RECORD_EXPENSE` (খরচ যুক্তকরণ) | ✅ Always | 🎛️ Toggleable (On) | ❌ Restricted |
| | `SET_CONTRIBUTION_RATE` (চাঁদার হার নির্ধারণ) | ✅ Always | 🎛️ Toggleable (Off) | ❌ Restricted |
| **Member Management** | `CREATE_MEMBER` (নতুন সদস্য তৈরি) | ✅ Always | 🎛️ Toggleable (Off) | ❌ Restricted |
| | `RESET_PASSWORD` (পাসওয়ার্ড রিসেট) | ✅ Always | 🎛️ Toggleable (Off) | ❌ Restricted |
| | `LOCK_ACCOUNT` (অ্যাকাউন্ট লক/আনলক) | ✅ Always | 🎛️ Toggleable (Off) | ❌ Restricted |
| **Privacy & Visibility** | `SHOW_NAMES_PUBLICLY` (নাম প্রকাশ) | ✅ Always | 🎛️ Toggleable (On) | 🎛️ Toggleable (On) |
| | `SHOW_AMOUNTS_PUBLICLY` (টাকা প্রকাশ) | ✅ Always | 🎛️ Toggleable (On) | 🎛️ Toggleable (On) |
| | `SHOW_EXPENSE_DETAILS` (খরচের তথ্য প্রকাশ) | ✅ Always | 🎛️ Toggleable (On) | 🎛️ Toggleable (On) |
| | `SHOW_ORG_BALANCE` (ফান্ড ব্যালেন্স প্রকাশ) | ✅ Always | 🎛️ Toggleable (On) | 🎛️ Toggleable (On) |
| **Communication & Reports**| `SEND_BROADCAST` (মেসেজ পাঠানো) | ✅ Always | 🎛️ Toggleable (On) | ❌ Restricted |
| | `EXPORT_PDF_EXCEL` (রিপোর্ট এক্সপোর্ট) | ✅ Always | 🎛️ Toggleable (On) | 🎛️ Toggleable (Off) |

---

## 3. Dynamic Permission Enforcer & Storage
- All permission flags are stored in the PostgreSQL `system_settings` table (JSONB format) and cached locally in Room DB.
- Admin Settings Screen (`AdminSettingsScreen.kt`) provides interactive toggle switches for Admin to grant or revoke any flag in real time.
