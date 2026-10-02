# Product Requirements Document (PRD)

## 1. Product Name

**Organization Member Contribution Management System**

Working name only. Final brand name may be decided later.

## 2. Product Objective

Build a simple bilingual member-management and contribution-tracking application for an organization with 200+ members.

The system should let the organization centrally manage:

- member profiles
- member login accounts
- fixed monthly contribution amounts
- monthly payment status
- partial payments
- multi-month payments
- advance payments
- extra donations
- direct donations
- income
- expenses
- financial transparency
- notifications
- basic messaging

Members primarily use the application to view information.

## 3. Product Context

Payments happen outside the application. A member may send money using bKash, Nagad, bank transfer, cash, or another method and then contact the organization.

Admin or Manager records the payment in the system. After it is recorded/approved, the member sees the updated status and receives a notification.

There is no member-side payment gateway or payment-proof submission in the initial version.

## 4. Supported Languages

- Bangla
- English

Language must be changeable after login.

## 5. User Roles

### 5.1 Admin

Admin has full system access.

Admin can:

- create member accounts
- create Manager accounts
- edit all member information
- activate/deactivate member accounts
- reset member passwords
- set monthly contribution amount
- change contribution amount from an effective month
- record payments
- record partial payments
- record multiple months in one payment
- record advance payments
- split received amount between contribution and donation
- add direct donation records
- add income
- add expenses
- approve or correct payments
- cancel/adjust financial records
- send notifications
- send messages
- control member financial visibility
- view all reports

### 5.2 Manager

Manager is an operational role.

At minimum, Manager may:

- view member records
- search/filter members
- record payments
- record partial/multi-month/advance payments
- allocate donation amounts
- approve payments if permitted
- add donation entries if permitted
- add expenses if permitted
- send messages/notifications if permitted
- view reports if permitted

Admin controls Manager permissions where supported.

### 5.3 Member

Member can:

- log in using credentials created by Admin
- change password
- view own profile
- edit allowed personal fields
- view current monthly contribution amount
- view month-wise contribution status
- view own payment history
- view own total contribution
- view own total donation
- view all-member published payment history
- view organization financial summary according to visibility rules
- view expense information according to visibility rules
- receive notifications
- exchange basic messages with Admin/Manager
- change language
- log out

Member cannot:

- create own account
- create another member
- set monthly contribution amount
- submit payment as paid
- record a payment
- approve a payment
- modify donation records
- modify income/expense records

## 6. Member Account Creation

Only Admin creates member accounts.

Required account information:

- member ID
- login identifier (mobile/username/email as chosen technically)
- temporary password
- account status

Recommended behavior:

- Admin creates account.
- Member receives credentials manually.
- Member logs in.
- Member is prompted to change temporary password.

## 7. Member Profile

Suggested fields:

- profile photo
- member ID
- full name in Bangla
- full name in English
- mobile number
- alternate mobile number
- email
- date of birth
- gender
- profession
- organization designation
- membership type
- membership start date
- present address
- permanent address
- emergency contact
- current monthly contribution
- account status
- notes (Admin-only unless configured)

### Member Editable Fields

Configurable, but initial allowed fields may include:

- profile photo
- mobile number
- alternate number
- email
- address
- password

Member must not edit:

- member ID
- monthly contribution
- financial history
- account status
- role

## 8. Monthly Contribution

Each member may have a different monthly contribution amount.

Examples:

- Member A = ৳500/month
- Member B = ৳1,000/month
- Member C = ৳2,000/month

Admin sets the amount.

A contribution-rate change must have an effective month/date so historical months remain based on their original requirement.

## 9. Contribution Statuses

Supported statuses:

- Unpaid
- Partially Paid
- Paid
- Paid in Advance
- Overdue
- Waived
- Adjusted
- Cancelled

The UI must use text labels, not color alone.

## 10. Payment Recording

Admin/Manager records a payment with:

- member
- received date
- total amount received
- payment method
- optional transaction/reference ID
- selected month(s)
- contribution allocation
- donation allocation
- optional note
- recorded by
- approved by where approval is enabled

## 11. Payment Scenarios

### Single Month

Monthly requirement = ৳1,000  
Received = ৳1,000  
Result: month = Paid

### Extra Amount

Monthly requirement = ৳1,000  
Received = ৳1,500  
Contribution = ৳1,000  
Extra donation = ৳500

### Partial

Monthly requirement = ৳1,000  
Received = ৳600  
Paid = ৳600  
Remaining = ৳400  
Status = Partially Paid

### Multi-Month

Monthly requirement = ৳1,000  
Received = ৳3,000  
January = ৳1,000  
February = ৳1,000  
March = ৳1,000

### Multi-Month + Donation

Monthly requirement = ৳1,000  
Received = ৳3,500  
January = ৳1,000  
February = ৳1,000  
March = ৳1,000  
Donation = ৳500

## 12. Donation Management

Donation types may include:

- extra amount from member payment
- direct member donation
- anonymous donation
- external donor donation
- campaign/special-purpose donation

Donation fields:

- donor/member where applicable
- amount
- date
- payment method
- purpose
- visibility
- note
- recorded by

## 13. Member Visibility

Admin controls what members can see about other members.

Possible settings:

- show name and amount
- show name but hide amount
- hide name but show amount
- show anonymous label
- show only total donation
- hide selected transactions

All-member payment history is available to members, but visible fields follow Admin configuration.

## 14. Income Management

Income types may include:

- monthly contribution
- extra donation
- direct donation
- membership fee
- event income
- other income

Income record fields:

- category
- amount
- date
- source
- related member if applicable
- note
- recorded by
- approved by if enabled

## 15. Expense Management

Expense fields:

- title
- category
- amount
- date
- vendor/receiver
- payment method
- description
- optional attachment
- member visibility setting
- recorded by
- approved by if enabled

Possible expense categories:

- office
- rent
- utility
- event
- welfare support
- transport
- food
- administration
- other

## 16. Financial Summary

Member-facing summary may show, depending on visibility settings:

- total contribution income
- total donation income
- total other income
- total income
- total expense
- current balance

Admin dashboard should additionally show:

- expected contribution this month
- collected contribution this month
- outstanding this month
- unpaid member count
- partial payment count
- advance-paid count

## 17. Notifications

Member notification triggers include:

- payment recorded
- payment approved
- contribution amount changed
- partial payment recorded
- extra donation recorded
- account password reset
- organization announcement
- new message

Example:

> Your contribution payment for January 2027 has been recorded successfully.

## 18. Messaging

Initial messaging is intentionally basic.

Support:

- Member → Admin
- Member → Manager
- Admin/Manager → Member
- Admin broadcast announcement if included

Not required:

- typing indicators
- read receipts
- media-rich chat
- calls
- group chat

## 19. Member App Navigation

Recommended:

- Home
- History
- Members
- Messages
- Profile

Notification icon in header.

## 20. Member Screens

### Login

- login identifier
- password
- language switch
- forgot password/contact organization

No public registration.

### Home Dashboard

Show:

- member name
- member ID
- membership type
- monthly contribution
- current month status
- current month paid
- current month remaining
- total outstanding
- total contribution paid
- total extra donation
- overall total paid
- organization financial summary
- latest payment
- recent notification

### My Contribution

Month-by-month:

- month
- required amount
- paid amount
- remaining amount
- donation amount
- status
- payment date

### My Payment History

- transaction/receipt number
- date
- amount
- contribution amount
- donation amount
- covered months
- method
- recorded by
- approved by if visible
- status

### All Members History

Filters:

- member
- month
- year
- status

Visible fields depend on Admin settings.

### Organization Finance

- total income
- total expense
- current balance
- contribution total
- donation total

### Expense List

Show permitted expense information.

### Notifications

- list
- unread/read state
- mark read
- open related record when supported

### Messages

- conversation list
- simple text messages

### Profile

- personal information
- monthly contribution
- account status
- edit allowed information
- change password
- language
- logout

## 21. Admin Navigation

Recommended sections:

- Dashboard
- Members
- Contributions
- Payments
- Donations
- Income
- Expenses
- Messages
- Notifications
- Reports
- Managers
- Settings

## 22. Admin Dashboard

Cards:

- total members
- active members
- inactive members
- expected this month
- collected this month
- outstanding
- total donations
- total income
- total expense
- current balance

Lists:

- recent payments
- recent donations
- recent expenses
- unpaid members
- partial payments

## 23. Member Management

Member list supports:

- search by name
- search by member ID
- search by mobile
- filter by membership type
- filter by payment status
- filter by account status

Actions:

- view
- edit
- record payment
- view history
- send message
- reset password
- activate/deactivate

## 24. Reports

Initial report set:

- monthly collection
- member-wise contribution
- unpaid members
- partial payments
- advance payments
- donation report
- income report
- expense report
- balance report
- member statement

Export support depends on selected package/scope.

## 25. Non-Functional Product Requirements

- Support at least 200+ members without UI becoming difficult to use.
- Financial totals must be deterministic and traceable.
- All forms must validate required fields.
- Financial destructive actions require confirmation.
- Financial data should be protected by role permission.
- UI must remain usable on typical Android screen sizes.
- Bangla text must render properly.
- Search/filter should not require loading all member records into one huge screen.

## 26. Out of Scope

Initial version excludes:

- online payment gateway
- member payment submission
- student/institute features
- attendance
- classes
- exams
- payroll
- inventory
- full accounting ledger
- complex ERP
- real-time social chat
- multi-tenant SaaS

## 27. Product Acceptance Criteria

The initial product is accepted when:

1. Admin can create a member and credentials.
2. Member can log in and change password.
3. Admin can set member monthly contribution.
4. Admin/Manager can record full monthly payment.
5. Admin/Manager can record partial payment.
6. Admin/Manager can record multiple months in one transaction.
7. Extra received amount can be allocated to donation.
8. Member sees updated payment status and history.
9. Member receives payment notification.
10. Admin can add income and expense.
11. Member sees allowed organization financial summary.
12. Bangla and English interfaces work for core flows.
13. Member cannot perform Admin-only financial actions.
