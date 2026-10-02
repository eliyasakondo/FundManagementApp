# Fund and Contribution Workflows

## 1. Purpose

This document defines the financial business rules for monthly contribution, payment allocation, donation, income, and expense.

These rules are product rules and must not be changed by implementation assumptions.

## 2. Core Concepts

### Monthly Contribution Requirement

The amount a specific member is expected to contribute for a specific month.

### Payment

Money physically received from a member and recorded by Admin/Manager.

### Allocation

How a payment is divided between one or more contribution months and donation.

### Donation

Money given beyond required contribution, or a direct donation entered independently.

### Outstanding

Required contribution amount not yet satisfied.

## 3. Monthly Requirement History

A member may have different contribution rates over time.

Example:

- Jan–Jun: ৳500/month
- Jul onward: ৳1,000/month

Changing the current rate must not retroactively change Jan–Jun requirements.

Implementation must preserve effective-period history.

## 4. Full Payment

Requirement: ৳1,000  
Received: ৳1,000

Allocation:

- Contribution: ৳1,000
- Donation: ৳0

Result:

- Month status = Paid
- Month remaining = ৳0

## 5. Extra Amount Payment

Requirement: ৳1,000  
Received: ৳1,500

Default review suggestion:

- Contribution: ৳1,000
- Remaining unallocated: ৳500

Admin may explicitly allocate remainder as donation.

Final:

- Contribution: ৳1,000
- Donation: ৳500
- Month status = Paid

Important: do not silently classify the remainder as donation without Admin confirmation.

## 6. Partial Payment

Requirement: ৳1,000  
Received: ৳600

Final:

- Contribution allocation: ৳600
- Remaining: ৳400
- Status = Partially Paid

Later payment:

Received: ৳400

Result:

- cumulative paid = ৳1,000
- remaining = ৳0
- status becomes Paid

## 7. Multiple Partial Payments

A month may have multiple payment records.

Example:

Requirement = ৳1,000

- Payment 1 = ৳300
- Payment 2 = ৳400
- Payment 3 = ৳300

The month becomes Paid only after cumulative valid contribution allocation reaches ৳1,000.

## 8. Multi-Month Payment

Monthly requirement = ৳1,000  
Received = ৳3,000

Admin selects:

- January
- February
- March

Allocation:

- January = ৳1,000
- February = ৳1,000
- March = ৳1,000

All three months become Paid.

## 9. Multi-Month Payment with Donation

Monthly requirement = ৳1,000  
Received = ৳3,500

Allocation:

- January = ৳1,000
- February = ৳1,000
- March = ৳1,000
- Donation = ৳500

The transaction total must satisfy:

`Total Received = Contribution Allocation + Donation Allocation + Other Explicit Allocation`

For initial product, avoid unexplained residual balance.

## 10. Advance Payment

Admin may allocate money to future months.

Example:

Current month = January  
Monthly amount = ৳1,000  
Member pays = ৳6,000

Admin selects Jan–Jun.

Each month receives its own contribution allocation.

Future months should be visible as `Paid in Advance` until the month becomes current/past, unless product later decides a different presentation.

## 11. Payment Less Than Selected Multi-Month Requirement

Example:

Selected Jan + Feb  
Total requirement = ৳2,000  
Received = ৳1,500

Admin must explicitly allocate, for example:

- January = ৳1,000 → Paid
- February = ৳500 → Partially Paid

Do not automatically guess month priority unless a product rule is later defined.

## 12. Waiver

If Admin marks a contribution as waived:

- the waived amount is not treated as cash income
- outstanding should reduce according to waiver amount
- record who applied waiver and note/reason

Waiver is not donation and not payment.

## 13. Adjustments

Adjustment may be used to correct a previously recorded financial allocation.

Recommended rule:

- preserve original record or change trace
- mark adjustment reason
- identify user who performed adjustment
- recalculate affected month, member totals, and organization totals

## 14. Cancelled Payment

A cancelled payment must not count toward:

- member paid contribution
- donation total
- organization income
- monthly collection

If a payment is cancelled, all linked month allocations must be recalculated.

## 15. Donation Types

### Extra Donation

Created from unallocated remainder of member payment after contribution allocation.

### Direct Member Donation

Member gives donation not tied to monthly contribution.

### Anonymous Donation

Donor identity is hidden publicly. Admin may still know the record internally.

### External Donation

Donation from non-member.

## 16. Donation Visibility

Each donation may have visibility configuration such as:

- public name + amount
- public name only
- amount only
- anonymous
- private

Admin configuration controls member-facing display.

## 17. Organization Income Rules

Contribution payment is income only when it is a valid recorded/approved payment according to final implementation workflow.

Donation is also income but should remain separately categorized.

Avoid double-counting.

Example:

Received = ৳1,500

- Contribution income = ৳1,000
- Donation income = ৳500
- Total income impact = ৳1,500

Do not additionally add the full payment amount a second time.

## 18. Organization Expense Rules

Every expense reduces available balance.

Basic balance formula:

`Current Balance = Valid Income Total - Valid Expense Total`

Cancelled or invalid entries must not affect totals.

## 19. Member Totals

### Total Contribution Paid

Sum of valid contribution allocations for the member.

### Total Donation

Sum of valid donation entries associated with the member.

### Overall Total Paid

`Contribution Paid + Member Donation`

### Total Outstanding

Sum of required amount minus valid contribution allocation minus valid waiver/adjustment for all applicable unpaid/partial months.

## 20. Monthly Organization Summary

Suggested fields:

- expected contribution
- received contribution
- outstanding contribution
- donation
- other income
- expense
- balance movement

Expected contribution should be based on applicable member monthly requirements for the selected month and active membership rules.

## 21. Account Deactivation and Contribution

Product decision for implementation:

- deactivating a member should stop generating future contribution requirements from the effective deactivation date/month
- historical obligations and payments remain visible

If a different policy is needed, update this document before coding.

## 22. Payment Notification Trigger

After a payment becomes valid/approved:

- update month status
- update member totals
- update organization totals
- generate payment/receipt reference
- create member notification

Example notification:

`Your January contribution of ৳1,000 has been recorded successfully.`

If extra donation exists:

`Your payment was recorded: ৳1,000 contribution and ৳500 donation.`

## 23. Validation Rules

- amount must be greater than 0
- allocation cannot be negative
- contribution allocation must reference a valid member/month
- donation amount cannot be negative
- total allocation must not exceed total received
- duplicate record protection should exist at UI/business level where practical
- cancelled payment must not remain counted

## 24. Example Test Matrix

| Scenario | Monthly | Received | Contribution | Donation | Expected Status |
|---|---:|---:|---:|---:|---|
| Full | 1,000 | 1,000 | 1,000 | 0 | Paid |
| Extra | 1,000 | 1,500 | 1,000 | 500 | Paid |
| Partial | 1,000 | 600 | 600 | 0 | Partially Paid |
| Two months full | 1,000 | 2,000 | 2,000 | 0 | Both Paid |
| Two months + donation | 1,000 | 2,500 | 2,000 | 500 | Both Paid |
| Future month | 1,000 | 1,000 | 1,000 | 0 | Paid in Advance |
