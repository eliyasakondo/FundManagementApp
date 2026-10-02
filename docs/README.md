# Organization Member Contribution Management System — Documentation

This folder contains the product, business-rule, UX, workflow, delivery, and testing documentation for the Organization Member Contribution Management System.

## Purpose

The system is for an organization with 200+ members. Members do not submit or approve payments. Admin/Manager records payments after receiving money outside the application (for example bKash, Nagad, bank, or cash). Members mainly log in to view their profile, monthly contribution, payment history, donation history, organization financial summary, notifications, and messages.

## Documentation Index

- `product/prd.md` — Product requirements, roles, scope, modules, permissions, and acceptance criteria.
- `product/design.md` — Screens, navigation, UI states, bilingual UX rules, and form behavior.
- `features/fund-workflows.md` — Contribution, partial payment, multi-month payment, advance, donation, income, and expense rules.
- `architecture/system-overview.md` — Technology-agnostic system modules and business data relationships.
- `decisions/decision.md` — Confirmed product and business decisions.
- `ai/agent-instructions.md` — Instructions for Gemini/Codex/other coding agents before implementing the system.
- `engineering/task.md` — Recommended implementation sequence and feature task breakdown.
- `engineering/test_plan.md` — Functional tests, calculation tests, permission tests, and acceptance tests.

## Important Rule

Technology-specific decisions are intentionally not finalized in this documentation. Android architecture, framework choices, backend framework, database engine, networking, local storage, dependency injection, push-notification implementation, and deployment architecture should be decided after reviewing the actual Android Studio project and discussing the options with the development agent.

## Product Scope Summary

### Roles

- Admin
- Manager
- Member

### Core Functions

- Member account management
- Per-member monthly contribution setup
- Single-month payment recording
- Multi-month payment recording
- Partial payment handling
- Advance payment handling
- Extra donation allocation
- Direct donation records
- Income and expense records
- Organization financial summary
- Member payment and donation history
- All-member published contribution history
- Notifications
- Basic member/admin messaging
- Bangla and English language support

## Out of Scope for Initial Version

- Member-side payment submission
- Online payment gateway
- Student, guardian, school, class, attendance, examination, or education features
- E-commerce
- Payroll
- Full accounting/ERP
- Real-time social chat
- Multi-organization SaaS unless added later
