# UI/UX & Design System Specifications
## Organization Member Contribution & Donation Management System

---

## 1. Design Philosophy & Visual Identity
The visual system is designed to convey **trust, financial clarity, accessibility, and modern simplicity**. It adheres strictly to Google's **Material 3 (Material You)** guidelines while supporting full runtime bilingual switching (Bangla & English).

---

## 2. Color Palette & Theming Tokens

### 2.1 Primary & Brand Colors
- **Primary Green:** `#1B5E20` (Represents trust, stability, organization growth)
- **Dark Green (Top Bars / Headers):** `#0F3D2E` (Deep contrast for app bars and primary navigation headers)
- **Warm Gold Accent:** `#C9A227` (Used for highlights, contribution badges, and donation callouts)
- **Soft Page Background:** `#F5F7F5` (Subtle off-white green tint reducing eye fatigue)
- **Surface White:** `#FFFFFF` (Card surfaces, dialogs, and modal sheets)

### 2.2 Semantic & Status Indicator Colors
- **Success (`Paid` / Approved):** `#2E7D32` (Green badge)
- **Warning (`Partially Paid` / Pending):** `#F9A825` (Amber badge)
- **Error / Danger (`Unpaid` / Overdue / Cancelled):** `#C62828` (Red badge)
- **Info (`Paid in Advance` / Notes):** `#1565C0` (Blue badge)
- **Muted / Neutral Text:** `#5F6368`
- **Primary Text:** `#1A1C1E`

---

## 3. Typography & Bilingual Font Rendering

### 3.1 Font Families
- **Bangla (বাংলা):** Noto Sans Bengali / Hind Siliguri (High legibility at small screen sizes with proper glyph rendering)
- **English:** Inter / Noto Sans

### 3.2 Dynamic Locale & Number Formatting Rules
- All numerical amounts, dates, and currency symbols MUST dynamically re-render based on selected locale:
  - **English:** `৳1,500.00` | `2026-09-12` | `Paid`
  - **Bangla:** `৳১,৫০০.০০` | `১২-০৯-২০২৬` | `পরিশোধিত`

---

## 4. UI Components & Accessibility Standards

### 4.1 Buttons & Interactive Elements
- **Primary Button:** Solid Primary Green `#1B5E20`, White text, 12dp rounded corners, minimum height 48dp (large touch target).
- **Secondary / Outlined Button:** Outlined Primary Green `#1B5E20`, 1.5dp stroke.
- **Destructive Action:** Solid Error Red `#C62828`.

### 4.2 Financial Summary Cards
- **Border Radius:** 16dp rounded corners.
- **Elevation:** 2dp tonal elevation with soft drop shadow.
- **Padding:** 16dp internal padding.

### 4.3 Modal Sheets & High Z-Index Overlays
- **Add / Edit Member Modal:** Must render in a full-screen overlay on mobile devices and high z-index modal on desktop/tablets above all bottom navigation bars with fixed header and sticky Save/Cancel action buttons.
