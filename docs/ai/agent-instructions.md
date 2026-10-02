# AI Agent Instructions

## 1. Read Before Coding

Before implementing or changing any feature, read these files in order:

1. `docs/product/prd.md`
2. `docs/features/fund-workflows.md`
3. `docs/product/design.md`
4. `docs/decisions/decision.md`
5. `docs/architecture/system-overview.md`
6. `docs/engineering/task.md`
7. `docs/engineering/test_plan.md`

Business rules in these documents take priority over assumptions made by the coding agent.

## 2. Do Not Invent Product Rules

Do not silently invent or change:

- roles or permissions
- contribution rules
- donation allocation rules
- payment status behavior
- visibility rules
- member self-service abilities
- financial calculation logic

If a rule is unclear, flag it as `NEEDS PRODUCT DECISION` rather than guessing.

## 3. Technology Decisions Are Separate

The product documentation intentionally does not prescribe a specific Android architecture or backend stack.

The agent may recommend technical choices only after inspecting the existing project. Technical decisions should be recorded separately and must not rewrite confirmed business rules.

Examples of technical topics to decide later:

- native Android architecture
- state management pattern
- API architecture
- backend framework
- authentication implementation
- database engine
- caching/local storage
- push notifications
- dependency injection
- networking layer
- deployment architecture

## 4. Main Product Principle

The initial system is admin-controlled.

Members mainly view information. They do not:

- create their own accounts
- submit payment proof
- mark themselves paid
- edit contribution amounts
- approve financial records
- modify organization income/expense

Admin creates member accounts and gives initial credentials. Members may later change their password.

## 5. Financial Integrity Rules

Every payment must preserve an auditable allocation.

A received amount may be allocated into:

- monthly contribution
- multiple months of contribution
- partial contribution
- advance contribution
- extra donation

Never combine contribution and donation into one ambiguous value.

Never modify past financial records simply because a member's monthly contribution rate changes later.

## 6. Safe Editing Behavior

Financial records should not be destructively overwritten without trace.

If the implementation supports editing an approved payment, preserve enough information to understand:

- previous value
- new value
- who changed it
- when it changed

At minimum, the product should distinguish active, adjusted, and cancelled records.

## 7. Bilingual Requirement

All user-facing labels, buttons, validation messages, statuses, notifications, and menu items must support Bangla and English.

Do not hardcode user-facing strings inside feature logic where they cannot be translated.

## 8. UI Requirement

Keep the app simple enough for non-technical members.

- clear financial cards
- large readable values
- straightforward navigation
- explicit statuses
- no hidden critical actions
- confirmation before destructive actions
- clear empty/loading/error states

## 9. Implementation Order

Follow `docs/engineering/task.md` unless there is a technical dependency that requires a different order.

## 10. Completion Rule

A feature is not complete until:

- its business rule is implemented
- permission behavior is correct
- validation is present
- bilingual labels exist
- relevant calculations are tested
- error and empty states are handled
- acceptance criteria in the PRD and test plan pass
