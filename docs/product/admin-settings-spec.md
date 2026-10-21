# Admin System Settings & Configuration Specifications
## Fund Management App - Admin Control Module

---

## 1. Overview
The **Admin System Settings Module** serves as the central control panel for the entire application. It allows the System Administrator (Admin) to configure organization profiles, set financial policies, delegate granular role permissions to Managers, manage Member privacy rules, view security audit logs, and trigger full system backups.

---

## 2. Setting Modules Breakdown

### 2.1 Organization Profile & System Info
- **Organization Name:** Configurable in Bangla (`তহবিল ব্যবস্থাপনা সামাজিক সংগঠন`) and English.
- **Registration Number:** Official government/registry ID (`REG-2025-88901`).
- **Fiscal Year:** Active financial tracking period (`2025 - 2026`).
- **Currency:** Official currency symbol (`৳ - BDT`).
- **Official Contact:** Email and phone details for organization inquiries.

### 2.2 Financial Policy & Dues Rules
- **Default Monthly Contribution Rate:** Base rate assigned to new members (`৳1,000 / month`).
- **Extra Donation Auto-Allocation Policy:** Surplus payments above monthly dues automatically route to the Extra Donation Fund.
- **Partial Payment Accumulator Policy:** Partial payments stay flagged as `PARTIALLY_PAID` until remaining balance is satisfied.
- **Overdue Grace Period:** Default 10-day grace period before marking unpaid months as `OVERDUE`.

### 2.3 Manager Role Permissions Matrix (Isolated to Manager Users)
Admin can interactively toggle permission flags for users with the `UserRole.MANAGER` role:
1. `canRecordPayments`: Permission to log offline contribution receipts.
2. `canApprovePayments`: Permission to approve payment entries directly without Admin queue.
3. `canAddExpense`: Permission to log organization operating expenses.
4. `canChangeContributionRate`: Permission to modify a member's monthly contribution rate.
5. `canSendNotifications`: Permission to issue broadcast announcements to members.

### 2.4 Member Role Privacy & Visibility Controls (Isolated to Member Users)
Admin can interactively configure visibility filters for users with the `UserRole.MEMBER` role:
1. `showMemberName`: Display full member names in public payment logs.
2. `showAmount`: Display payment and donation transaction amounts to other members.
3. `showOrgBalance`: Display live bank/org balance on member home dashboard.
4. `showFullContributionHistory`: Allow members to browse multi-year contribution history.
5. `allowReportExport`: Allow members to download PDF/Excel annual financial statements.

### 2.5 Security Audit Trail & Activity Logs
Immutable log tracking system events:
- Timestamp, Actor Name (`Admin` / `Manager`), Action Type (`MEMBER_CREATED`, `PAYMENT_RECORDED`, `PERMISSION_CHANGED`), Target Member, IP Address.

### 2.6 System Data Management & Backup
- One-click export of complete organizational ledger (PDF / Excel).
- Local Room DB cache clear & forced Supabase PostgreSQL cloud sync.
