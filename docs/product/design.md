# Comprehensive Design System & UI/UX State Specification
## Fund Management App (Android / Jetpack Compose)

---

## 1. Design System Foundations

### 1.1 Visual Identity & Philosophy
- **Modern:** Built on Google's Material 3 (Material You) adaptive specifications with smooth dynamic elevation and state transitions.
- **Minimal:** High visual hierarchy for financial data with purposeful whitespace, eliminating unnecessary decorative elements.
- **Professional:** Trustworthy financial presentation using Emerald Green branding, bilingual font rendering, and color-coded status badges.

---

## 2. Design Tokens & Foundations

### 2.1 Color Tokens (Light & Dark Themes)

| Token Name | Light Theme | Dark Theme | Usage |
| :--- | :--- | :--- | :--- |
| `colorBrandPrimary` | `#1B5E20` | `#81C784` | Primary actions, main buttons, active indicators |
| `colorBrandHeader` | `#0F3D2E` | `#121212` | Top app bars, hero headers, primary navigation |
| `colorAccentGold` | `#C9A227` | `#FBC02D` | Financial callouts, contribution badges, highlights |
| `colorBackground` | `#F5F7F5` | `#121212` | App background behind cards and scrollables |
| `colorSurface` | `#FFFFFF` | `#1E1E1E` | Card containers, dialogs, bottom sheets |
| `colorSurfaceVariant` | `#E8F5E9` | `#252525` | Input field fills, secondary card backgrounds |
| `colorTextPrimary` | `#1A1C1E` | `#E1E2E1` | Main titles, financial amounts, primary text |
| `colorTextSecondary` | `#5F6368` | `#A0A0A0` | Subtitles, labels, timestamps, muted text |
| `colorBorder` | `#E2E8F0` | `#2C2C2C` | Outlines, card dividers, field borders |

### 2.2 Semantic Status Colors

| Status Token | Hex Code | Background Fill (12% Alpha) | Context |
| :--- | :--- | :--- | :--- |
| `statusSuccess` | `#2E7D32` | `#E8F5E9` | `Paid`, Approved, Positive balance |
| `statusWarning` | `#F9A825` | `#FFFDE7` | `Partially Paid`, Pending approval |
| `statusError` | `#C62828` | `#FFEBEE` | `Unpaid`, Overdue, Rejected |
| `statusInfo` | `#1565C0` | `#E3F2FD` | `Paid in Advance`, Notes, System logs |

### 2.3 Spacing Scale (4dp Grid)
- **2xs (2dp):** Minimal gap (badge padding, icon-text gap)
- **xs (4dp):** Tight internal padding (chips, small badges)
- **sm (8dp):** Standard gap between elements inside a component
- **md (12dp):** Compact card padding, form field vertical gap
- **lg (16dp):** Standard container padding, screen margins
- **xl (24dp):** Section dividers, major header padding
- **2xl (32dp):** Dialog margins, hero banner padding
- **3xl (48dp):** Minimum touch target height/width

### 2.4 Typography Tokens

| Style Token | Font Family | Weight | Size | Line Height | Usage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `DisplayLarge` | Inter / Noto Sans Bengali | Bold | 24sp | 32sp | Dashboard total fund balance |
| `TitleLarge` | Inter / Noto Sans Bengali | SemiBold | 20sp | 26sp | Screen titles, Section headers |
| `TitleMedium` | Inter / Noto Sans Bengali | Medium | 16sp | 22sp | Member names, Card titles |
| `BodyLarge` | Inter / Noto Sans Bengali | Regular | 16sp | 24sp | Main body text, Form inputs |
| `BodyMedium` | Inter / Noto Sans Bengali | Regular | 14sp | 20sp | Subtitles, Transaction details |
| `LabelSmall` | Inter / Noto Sans Bengali | SemiBold | 11sp | 16sp | Status badges, Timestamps |

---

## 3. Component Interactive State Matrix

All interactive elements (Buttons, Cards, Inputs, List Items) MUST define and visually handle 5 distinct states:

```
┌──────────────┐     Hover/Focus     ┌──────────────┐
│   Default    │ ──────────────────► │ Focused /    │
│   (Resting)  │                     │ Hovered      │
└──────┬───────┘                     └──────┬───────┘
       │                                    │
       │ Touch Down                         │ Press
       ▼                                    ▼
┌──────────────┐                     ┌──────────────┐
│   Pressed    │ ◄────────────────── │   Active /   │
│   (Ripple)   │                     │ Selected     │
└──────────────┘                     └──────────────┘
```

---

## 4. Component Detailed Specifications & States

### 4.1 Primary Action Buttons

- **Default State:**
  - Container Fill: Solid `#1B5E20`
  - Text Color: `#FFFFFF` (SemiBold 14sp)
  - Height: 48dp (Large touch target)
  - Corner Radius: 12dp
  - Elevation: 0dp (Flat Material 3 style)
- **Hover / Focus State (Mouse / Pointer / Keyboard Tab Navigation):**
  - Container Fill: `#1B5E20` with 8% White Overlay (`#327237`)
  - Border: 2dp `#C9A227` (Gold Focus ring for accessibility focus indicator)
  - Cursor: Pointer
- **Pressed State (Touch Down / Click):**
  - Container Fill: `#1B5E20` with 12% Black Ripple Overlay (`#144618`)
  - Scale: Slightly scales down to 0.98x for tactile feedback
- **Disabled State:**
  - Container Fill: 12% `#1A1C1E` (Off-gray `#E0E0E0`)
  - Text Color: 38% `#1A1C1E` (Muted gray `#9E9E9E`)
  - Interactivity: Click disabled, pointer-events none

### 4.2 Secondary / Outlined Buttons

- **Default State:**
  - Container Fill: Transparent
  - Border: 1.5dp `#1B5E20`
  - Text Color: `#1B5E20`
  - Corner Radius: 12dp, Height: 48dp
- **Hover / Focus State:**
  - Container Fill: 8% Alpha `#1B5E20` fill (`#E8F5E9`)
  - Border: 2dp `#1B5E20`
- **Pressed State:**
  - Container Fill: 12% Alpha `#1B5E20` with ripple effect
- **Disabled State:**
  - Border: 1.5dp 12% `#1A1C1E`
  - Text Color: 38% `#1A1C1E`

### 4.3 Destructive Action Buttons (Delete / Lock Account)

- **Default State:** Container Fill `#C62828`, Text `#FFFFFF`, 12dp radius, 48dp height
- **Hover / Focus State:** Fill `#B71C1C` with Gold Focus Ring
- **Pressed State:** Fill `#8E0000` with dark ripple
- **Disabled State:** Fill `#E0E0E0`, Text `#9E9E9E`

### 4.4 Text Input & Form Fields

- **Default State:**
  - Container Fill: `#E8F5E9` (10% Alpha Green) or `#F8FAFC`
  - Border: 1dp `#E2E8F0`
  - Label Color: `#5F6368`
  - Text Color: `#1A1C1E`
- **Focused State (Active Cursor):**
  - Border: 2dp `#1B5E20` (Primary Green stroke)
  - Label Color: `#1B5E20` (Shrinks to top floating position)
  - Cursor Color: `#1B5E20`
- **Hover State (Pointer over field):**
  - Border: 1.5dp `#5F6368`
- **Error State (Invalid Validation):**
  - Border: 2dp `#C62828` (Red stroke)
  - Label & Text Color: `#C62828`
  - Helper Text: Shows error message in `#C62828` below field
- **Disabled State:**
  - Container Fill: `#F0F0F0`, Border: 1dp `#E0E0E0`, Text: `#9E9E9E`

### 4.5 Financial Cards & Transaction Items

- **Default State:**
  - Surface Fill: `#FFFFFF`
  - Border Radius: 16dp (Summary Cards) / 12dp (Transaction Items)
  - Border: 1dp `#E2E8F0`
  - Elevation: 1dp tonal elevation
  - Padding: 16dp internal padding
- **Hover / Pointer Hover State:**
  - Elevation: Increases to 4dp with soft drop shadow
  - Surface Fill: `#FAFDFB` (Subtle green tint)
- **Pressed / Clicked State:**
  - Bounded Ripple effect inside card surface
  - Scale: Soft 0.99x scale animation
- **Selected State (Multi-selection / Active Filter):**
  - Border: 2dp `#1B5E20`
  - Background Fill: `#E8F5E9`

### 4.6 Status Badges & Chips

- **Default State:**
  - Padding: Horizontal 10dp, Vertical 4dp (Corner radius 50% pill shape)
  - Text: LabelSmall (11sp SemiBold)
  - Fills:
    - `Paid`: Fill `#E8F5E9`, Text `#2E7D32`
    - `Partially Paid`: Fill `#FFFDE7`, Text `#F9A825`
    - `Unpaid`: Fill `#FFEBEE`, Text `#C62828`
    - `Advance`: Fill `#E3F2FD`, Text `#1565C0`
- **Hover / Focus State:**
  - Brightness increases by 5%, Subtle 1dp border added

### 4.7 Adaptive Layouts & Navigation Controls

| Component Pattern | Material 3 Implementation | Purpose & Design Rules |
| :--- | :--- | :--- |
| **Grid System** | `LazyVerticalGrid` / `GridCells.Adaptive(minSize = 160.dp)` | 2-column/3-column responsive layout for dashboard metrics, summary cards, and member lists. |
| **Side Menu (Drawer)** | `ModalNavigationDrawer` | Slide-out side menu for profile switching, language toggle, and admin management. |
| **Tab Bar (Bottom Nav)** | `NavigationBar` & `NavigationBarItem` | Fixed bottom bar with active Emerald Green pill indicators (`Dashboard`, `Statement`, `Notifications`, `Profile`). |
| **Floating Action Button** | `ExtendedFloatingActionButton` | Emerald Green (`#1B5E20`) FAB for primary action ("+ Record Payment" / "+ পেমেন্ট জমা") with scroll auto-shrink. |
| **Modal Sheet** | `ModalBottomSheet` | Drag-handle modal sheet overlay for payment entry forms, filter options, and receipt details. |
| **Three-Dot Menu** | `IconButton` with `DropdownMenu` | Contextual overflow menu on card items (Edit, Delete, Lock, Download Receipt). |
| **Rectangular Cards** | `Card` / `CardDefaults.cardColors()` | 12dp/16dp rounded rectangular cards with subtle 1dp `#E2E8F0` borders and 2dp tonal elevation. |
| **Navigation Rail (Rudder)** | `NavigationRail` & `NavigationRailItem` | Left-hand vertical navigation rail for landscape, foldable, and tablet viewports. |

---

## 5. Global UX States & Micro-interactions

### 5.1 Loading States
- **Skeleton Shimmer:** Gray gradient (`#E0E0E0` to `#F5F5F5` to `#E0E0E0`) pulsing horizontally over 1.2s ease-in-out loop on cards, tables, and lists while fetching remote data.
- **Progress Indicator:** 36dp Circular Progress Indicator in `#1B5E20` for button submission states.

### 5.2 Empty States
- **Container:** Centered layout with 32dp vertical padding
- **Elements:**
  - Contextual Icon / Illustration (48dp, `#5F6368` color)
  - Title: TitleMedium (`#1A1C1E`) — e.g. "কোনো লেনদেন পাওয়া যায়নি" / "No transactions found"
  - Subtitle: BodyMedium (`#5F6368`) — Helpful explanatory message
  - Call to Action: Primary or Secondary button to add or refresh

### 5.3 Error States & Feedback
- **Inline Error Banner:** Solid Fill `#FFEBEE`, Border `#C62828`, with Retry button.
- **Snackbar Alerts:** Dark Charcoal Fill (`#212121`), White Text, 4000ms duration with action button ("পুনরায় চেষ্টা করুন" / "Retry").

### 5.4 Bilingual & Dynamic Locale Formatting
- Every screen component MUST dynamically adapt to the selected locale:
  - **English Locale:** Numbers in ASCII (`0123456789`), Currency `৳1,500.00`, Date `2026-09-12`.
  - **Bangla Locale:** Numbers in Bengali digits (`০১২৩৪৫৬৭৮৯`), Currency `৳১,৫০০.০০`, Date `১২-০৯-২০২৬`.
