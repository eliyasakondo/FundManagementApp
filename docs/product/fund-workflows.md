# Financial Calculation Engine & Workflows Specification

---

## 1. Custom Variable Monthly Contribution Engine
- **Per-Member Assignment:** Every member $i$ has an assigned custom monthly contribution rate $R_i$ set by Admin (e.g. ৳500, ৳1,000, ৳2,000, ৳3,000).
- **Rate Change Effective Date:** Admin can change rate $R_i \rightarrow R_i'$ starting from effective month $M_k$. Historical payment records $M < M_k$ preserve original due amount $R_i$.

---

## 2. Extra Donation Allocation Formula & Ledger Entry
When a total payment $P$ is received for a given target month $M$ with due rate $R_i$:
$$\text{Contribution Portion } C = \min(P, R_i)$$
$$\text{Extra Donation Portion } D = \max(0, P - R_i)$$

### Ledger Record Splitting:
- Record $C$ as `Monthly Contribution` credited to member's contribution history for month $M$.
- Record $D$ as `Extra Donation` credited to member's donation history and organization's extra donation fund.
- Both entries store timestamp, payment method, recorded_by user ID, approved_by user ID, and receipt transaction ID.

---

## 3. Multi-Month & Advance Payment Algorithm
When payment $P$ covers multiple selected months $\{M_1, M_2, \dots, M_n\}$:
1. Total Required $R_{\text{total}} = \sum_{j=1}^n R_{i, M_j}$.
2. If $P \ge R_{\text{total}}$, mark all months $M_1 \dots M_n$ as `Paid`.
3. Excess surplus $S = P - R_{\text{total}}$ is credited to `Extra Donation` or recorded as `Advance Payment` for future months.

---

## 4. Partial Payment Accumulator Engine
If payment $P < R_{i, M}$ for month $M$:
- Month status set to `Partially Paid`.
- Remaining Due $U_M = R_{i, M} - P$.
- Subsequent payment $P_2$ against month $M$ accumulates: $P_{\text{total}} = P + P_2$.
- When $P_{\text{total}} \ge R_{i, M}$, status automatically transitions to `Paid`.

---

## 5. Payment Status Lifecycle & State Machine
`Unpaid` $\xrightarrow{\text{Payment } < R}$ `Partially Paid` $\xrightarrow{\text{Accumulates } \ge R}$ `Paid`
- `Paid in Advance`: Payment received for future $M > M_{\text{current}}$.
- `Overdue`: $M < M_{\text{current}}$ and status is `Unpaid` or `Partially Paid`.
- `Waived`: Admin officially waives monthly due $R_{i, M} = 0$.
- `Adjusted`: Amount credited via adjustment ledger.
- `Cancelled`: Transaction voided with audit log.
