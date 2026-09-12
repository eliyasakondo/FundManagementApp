package com.eliyas.fundmanagementapp.localization

import com.eliyas.fundmanagementapp.model.Language

object Strings {
    fun formatDigits(numberStr: String, language: Language): String {
        if (language == Language.EN) return numberStr
        val bnDigits = mapOf(
            '0' to '০', '1' to '১', '2' to '২', '3' to '৩', '4' to '৪',
            '5' to '৫', '6' to '৬', '7' to '৭', '8' to '৮', '9' to '৯'
        )
        return numberStr.map { bnDigits[it] ?: it }.joinToString("")
    }

    fun formatCurrency(amount: Double, language: Language): String {
        val formatted = String.format("%,.0f", amount)
        val digits = formatDigits(formatted, language)
        return if (language == Language.BN) "৳ $digits" else "৳ $digits"
    }

    fun getMonthName(monthKey: String, language: Language): String {
        val parts = monthKey.split("-")
        if (parts.size < 2) return monthKey
        val year = formatDigits(parts[0], language)
        val monthNum = parts[1].toIntOrNull() ?: 1
        val enMonths = arrayOf(
            "", "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val bnMonths = arrayOf(
            "", "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
            "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
        )
        val mName = if (language == Language.BN) bnMonths[monthNum] else enMonths[monthNum]
        return "$mName $year"
    }

    // Comprehensive Dictionary
    val appName = mapOf(Language.EN to "Organization Fund Manager", Language.BN to "তহবিল ব্যবস্থাপনা অ্যাপ")
    val appTagline = mapOf(Language.EN to "200+ Member Contribution & Donation Portal", Language.BN to "২০০+ সদস্যের চাঁদা ও অনুদান ব্যবস্থাপনা সিস্টেম")

    // Navigation Labels
    val navHome = mapOf(Language.EN to "Home", Language.BN to "হোম")
    val navHistory = mapOf(Language.EN to "History", Language.BN to "ইতিহাস")
    val navMembers = mapOf(Language.EN to "Members", Language.BN to "সদস্য")
    val navMessages = mapOf(Language.EN to "Messages", Language.BN to "বার্তা")
    val navProfile = mapOf(Language.EN to "Profile", Language.BN to "প্রোফাইল")

    // Admin Navigation
    val navDashboard = mapOf(Language.EN to "Dashboard", Language.BN to "ড্যাশবোর্ড")
    val navContributions = mapOf(Language.EN to "Contributions", Language.BN to "চাঁদাসমূহ")
    val navPayments = mapOf(Language.EN to "Payments", Language.BN to "পরিশোধসমূহ")
    val navDonations = mapOf(Language.EN to "Donations", Language.BN to "অনুদানসমূহ")
    val navIncome = mapOf(Language.EN to "Income", Language.BN to "আয়সমূহ")
    val navExpenses = mapOf(Language.EN to "Expenses", Language.BN to "ব্যয়সমূহ")
    val navReports = mapOf(Language.EN to "Reports", Language.BN to "রিপোর্টসমূহ")
    val navManagers = mapOf(Language.EN to "Managers", Language.BN to "ম্যানেজারবৃন্দ")
    val navSettings = mapOf(Language.EN to "Settings", Language.BN to "সেটিংস")

    // Buttons & Actions
    val btnLogin = mapOf(Language.EN to "Log In", Language.BN to "লগইন করুন")
    val btnLogout = mapOf(Language.EN to "Log Out", Language.BN to "লগআউট")
    val btnSave = mapOf(Language.EN to "Save Changes", Language.BN to "সংরক্ষণ করুন")
    val btnCancel = mapOf(Language.EN to "Cancel", Language.BN to "বাতিল")
    val btnAddMember = mapOf(Language.EN to "Add Member", Language.BN to "সদস্য যোগ করুন")
    val btnRecordPayment = mapOf(Language.EN to "Record Payment", Language.BN to "টাকা জমা রেকর্ড করুন")
    val btnAddDonation = mapOf(Language.EN to "Add Donation", Language.BN to "অনুদান যোগ করুন")
    val btnAddExpense = mapOf(Language.EN to "Add Expense", Language.BN to "খরচ যোগ করুন")
    val btnAddIncome = mapOf(Language.EN to "Add Income", Language.BN to "আয় যোগ করুন")
    val btnSendMessage = mapOf(Language.EN to "Send Message", Language.BN to "বার্তা পাঠান")
    val btnApprove = mapOf(Language.EN to "Approve", Language.BN to "অনুমোদন করুন")
    val btnFilter = mapOf(Language.EN to "Filter", Language.BN to "ফিল্টার")
    val btnClear = mapOf(Language.EN to "Clear", Language.BN to "ক্লিয়ার")
    val btnExportPdf = mapOf(Language.EN to "Export PDF", Language.BN to "পিডিএফ ডাউনলোড")
    val btnPrint = mapOf(Language.EN to "Print", Language.BN to "প্রিন্ট করুন")
    val btnChangePassword = mapOf(Language.EN to "Change Password", Language.BN to "পাসওয়ার্ড পরিবর্তন")
    val btnSwitchLanguage = mapOf(Language.EN to "বাংলায় পরিবর্তন করুন", Language.BN to "Switch to English")

    // Financial Cards & Labels
    val lblMonthlyContribution = mapOf(Language.EN to "Monthly Contribution", Language.BN to "মাসিক চাঁদা")
    val lblCurrentMonthStatus = mapOf(Language.EN to "Current Month Status", Language.BN to "চলতি মাসের অবস্থা")
    val lblPaidAmount = mapOf(Language.EN to "Paid Amount", Language.BN to "পরিশোধিত পরিমাণ")
    val lblRemainingDue = mapOf(Language.EN to "Remaining Due", Language.BN to "বকেয়া পরিমাণ")
    val lblTotalOutstanding = mapOf(Language.EN to "Total Outstanding Due", Language.BN to "মোট বকেয়া")
    val lblTotalContributionsPaid = mapOf(Language.EN to "Total Contributions Paid", Language.BN to "মোট পরিশোধিত চাঁদা")
    val lblTotalExtraDonation = mapOf(Language.EN to "Total Extra Donation", Language.BN to "মোট অতিরিক্ত অনুদান")
    val lblOverallTotalPaid = mapOf(Language.EN to "Overall Total Paid", Language.BN to "সর্বমোট জমা")
    val lblTotalIncome = mapOf(Language.EN to "Total Income", Language.BN to "সংগঠনের মোট আয়")
    val lblTotalExpenses = mapOf(Language.EN to "Total Expenses", Language.BN to "সংগঠনের মোট ব্যয়")
    val lblAvailableBalance = mapOf(Language.EN to "Available Balance", Language.BN to "বর্তমান অবশিষ্টাংশ/ব্যালেন্স")

    // Roles
    val roleAdmin = mapOf(Language.EN to "Admin", Language.BN to "এডমিন")
    val roleManager = mapOf(Language.EN to "Manager", Language.BN to "ম্যানেজার")
    val roleMember = mapOf(Language.EN to "Member", Language.BN to "সদস্য")

    // Form Fields
    val fieldMemberId = mapOf(Language.EN to "Member ID", Language.BN to "সদস্য আইডি")
    val fieldUsername = mapOf(Language.EN to "Username / Phone / Email", Language.BN to "ইউজারনেম / মোবাইল / ইমেইল")
    val fieldPassword = mapOf(Language.EN to "Password", Language.BN to "পাসওয়ার্ড")
    val fieldFullNameEn = mapOf(Language.EN to "Full Name (English)", Language.BN to "পূর্ণ নাম (ইংরেজি)")
    val fieldFullNameBn = mapOf(Language.EN to "Full Name (Bangla)", Language.BN to "পূর্ণ নাম (বাংলা)")
    val fieldMobile = mapOf(Language.EN to "Mobile Number", Language.BN to "মোবাইল নম্বর")
    val fieldAltMobile = mapOf(Language.EN to "Alternative Mobile", Language.BN to "বিকল্প মোবাইল নম্বর")
    val fieldEmail = mapOf(Language.EN to "Email Address", Language.BN to "ইমেইল ঠিকানা")
    val fieldAddress = mapOf(Language.EN to "Present Address", Language.BN to "বর্তমান ঠিকানা")
    val fieldMembershipType = mapOf(Language.EN to "Membership Type", Language.BN to "সদস্যপদের ধরন")
    val fieldTotalReceived = mapOf(Language.EN to "Total Received Amount (৳)", Language.BN to "মোট প্রাপ্ত অর্থ (৳)")
    val fieldPaymentMethod = mapOf(Language.EN to "Payment Method", Language.BN to "পেমেন্ট মাধ্যম")
    val fieldSelectMonths = mapOf(Language.EN to "Select Month(s) Covered", Language.BN to "পরিশোধিত মাসসমূহ নির্বাচন করুন")
    val fieldContributionAllocation = mapOf(Language.EN to "Contribution Allocation (৳)", Language.BN to "চাঁদা বাবদ অংশ (৳)")
    val fieldDonationAllocation = mapOf(Language.EN to "Extra Donation Allocation (৳)", Language.BN to "অতিরিক্ত অনুদান অংশ (৳)")
    val fieldNotes = mapOf(Language.EN to "Optional Note / Reference", Language.BN to "নোট / ট্রানজেকশন রেফারেন্স")

    // Status Texts
    val statusPaid = mapOf(Language.EN to "Paid", Language.BN to "পরিশোধিত")
    val statusUnpaid = mapOf(Language.EN to "Unpaid", Language.BN to "অপরিশোধিত")
    val statusPartial = mapOf(Language.EN to "Partially Paid", Language.BN to "আংশিক পরিশোধিত")
    val statusAdvance = mapOf(Language.EN to "Paid in Advance", Language.BN to "অগ্রিম পরিশোধিত")

    // Role Switching Notice
    val demoRoleNotice = mapOf(
        Language.EN to "Demo Switcher: Quickly view app as Member, Manager, or Admin",
        Language.BN to "ডেমো সুইচ: সহজেই সদস্য, ম্যানেজার বা এডমিন ভিউ পরিবর্তন করুন"
    )
}
