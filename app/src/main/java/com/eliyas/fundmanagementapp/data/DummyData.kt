package com.eliyas.fundmanagementapp.data

import com.eliyas.fundmanagementapp.model.*

object DummyData {

    private val banglaFirstNames = listOf(
        "মো: রফিকুল", "মো: আনোয়ার", "ফাতেমা", "শাহীন", "মোহাম্মদ", "তানভীর", "ডা: মনির", "ড. আব্দুল",
        "শারমীন", "কামরুল", "মাসুদ", "নুসরাত", "সৈয়দা", "মাহমুদুল", "ফারহানা", "কাজী", "গোলাম", "রোকেয়া",
        "তারিকুল", "হাবিবুর", "জসিম", "মোর্শেদ", "নাসরিন", "সেলিম", "সুলতানা", "আকরাম", "বাবুল", "দিলরুবা",
        "নামুল", "ফেরদৌস", "আরিফুল", "সাহাবউদ্দিন", "তাহমিনা", "ইসমাইল", "কহিনুর", "জাহিদুল", "রেজাউল"
    )

    private val banglaLastNames = listOf(
        "ইসলাম", "হোসেন", "আহমেদ", "রহমান", "বেগম", "চৌধুরী", "আক্তার", "খান", "মিয়া", "আলম", "আলী",
        "সরকার", "উদ্দিন", "হক", "সিকদার", "হাসান", "তালুকদার", "রায়", "মাহমুদ", "পারভীন", "রেজা"
    )

    private val englishFirstNames = listOf(
        "Md. Rafiqul", "Md. Anowar", "Fatema", "Shahin", "Mohammad", "Tanvir", "Dr. Monir", "Prof. Abdul",
        "Sharmin", "Kamrul", "Masud", "Nusrat", "Syeda", "Mahmudul", "Farhana", "Kazi", "Golam", "Rokeya",
        "Tariqul", "Habibur", "Jasim", "Morshed", "Nasreen", "Selim", "Sultana", "Akram", "Babul", "Dilruba",
        "Enamul", "Ferdous", "Ariful", "Sahabuddin", "Tahmina", "Ismail", "Kohinoor", "Jahidul", "Rezaul"
    )

    private val englishLastNames = listOf(
        "Islam", "Hossain", "Ahmed", "Rahman", "Begum", "Chowdhury", "Akter", "Khan", "Miah", "Alam", "Ali",
        "Sarkar", "Uddin", "Haque", "Sikder", "Hasan", "Talukder", "Roy", "Mahmud", "Parveen", "Reza"
    )

    private val professions = listOf(
        "Teacher", "Engineer", "Doctor", "Business", "Banker", "Govt Service", "Advocate", "Retired Official", "Entrepreneur"
    )

    private val designations = listOf(
        "General Member", "Senior Member", "EC Member", "Vice President", "Joint Secretary", "Finance Director", "Executive Member"
    )

    fun generateMembers(): List<Member> {
        val members = mutableListOf<Member>()

        // Specific Admin & Manager First
        members.add(
            Member(
                id = "ADM-1001",
                username = "admin",
                nameEn = "Md. Eliyas Hossain (Admin)",
                nameBn = "মো: ইলিয়াস হোসেন (এডমিন)",
                mobile = "01711223344",
                email = "admin@org.bd",
                designation = "Central Admin",
                membershipType = MembershipType.EXECUTIVE,
                monthlyContribution = 2000.0,
                status = "Active",
                password = "admin",
                mustChangePassword = false,
                notes = "Primary System Controller"
            )
        )

        members.add(
            Member(
                id = "MGR-1002",
                username = "manager",
                nameEn = "Kazi Farhad Ahmed (Manager)",
                nameBn = "কাজী ফরহাদ আহমেদ (ম্যানেজার)",
                mobile = "01819887766",
                email = "manager@org.bd",
                designation = "Operational Manager",
                membershipType = MembershipType.EXECUTIVE,
                monthlyContribution = 1500.0,
                status = "Active",
                password = "manager",
                mustChangePassword = false,
                notes = "Accounts & Payment Operations"
            )
        )

        val amounts = listOf(500.0, 1000.0, 1500.0, 2000.0, 3000.0, 5000.0)
        val mTypes = MembershipType.values()

        // Generate 200 regular members
        for (i in 1..200) {
            val fnIndex = (i - 1) % englishFirstNames.size
            val lnIndex = (i * 3) % englishLastNames.size
            val bfnIndex = (i - 1) % banglaFirstNames.size
            val blnIndex = (i * 3) % banglaLastNames.size

            val nameEn = "${englishFirstNames[fnIndex]} ${englishLastNames[lnIndex]}"
            val nameBn = "${banglaFirstNames[bfnIndex]} ${banglaLastNames[blnIndex]}"
            val memberId = "MEM-${1000 + i}"
            val mobile = "01${(7 + (i % 3))}${String.format("%08d", 10000000 + i * 4321)}"
            val contribution = amounts[i % amounts.size]
            val mType = mTypes[i % mTypes.size]
            val isActive = if (i % 15 == 0) "Inactive" else "Active"

            members.add(
                Member(
                    id = memberId,
                    username = "user$i",
                    nameEn = nameEn,
                    nameBn = nameBn,
                    mobile = mobile,
                    email = "member$i@organization.org.bd",
                    dob = "198${(i % 10)}-0${((i % 9) + 1)}-15",
                    gender = if (i % 4 == 0) "Female" else "Male",
                    profession = professions[i % professions.size],
                    designation = designations[i % designations.size],
                    membershipType = mType,
                    joinDate = "202${(i % 4)}-01-10",
                    presentAddress = "House #${10 + i}, Road #${(i % 20) + 1}, Dhanmondi, Dhaka",
                    permanentAddress = "Village #${i % 50}, Zilla Sadar, Bangladesh",
                    emergencyContact = "01811${String.format("%06d", 100000 + i)}",
                    monthlyContribution = contribution,
                    contributionEffectiveDate = "2025-01-01",
                    status = isActive,
                    password = "user$i",
                    mustChangePassword = (i % 20 == 0),
                    notes = if (i % 10 == 0) "Regular prompt contributor" else "",
                    lastLogin = "2025-02-${(10 + (i % 12))} 02:45 PM"
                )
            )
        }

        return members
    }

    fun generateMemberPaymentHistory(members: List<Member>): Map<String, List<MonthlyRecord>> {
        val map = mutableMapOf<String, List<MonthlyRecord>>()
        val monthKeys = listOf(
            "2025-01", "2025-02", "2025-03", "2025-04",
            "2025-05", "2025-06", "2025-07", "2025-08",
            "2025-09", "2025-10", "2025-11", "2025-12",
            "2026-01", "2026-02"
        )

        for (member in members) {
            val records = mutableListOf<MonthlyRecord>()
            val req = member.monthlyContribution
            val memberHash = member.id.hashCode()

            for ((index, mKey) in monthKeys.withIndex()) {
                val isPastMonth = index <= 13 // up to Feb 2026
                val pattern = (memberHash + index) % 10

                val (paid, status, donation) = when {
                    index > 13 -> Triple(0.0, PaymentStatus.UNPAID, 0.0)
                    index < 12 -> { // Year 2025
                        if (pattern == 0) {
                            // Partial
                            Triple(req * 0.6, PaymentStatus.PARTIALLY_PAID, 0.0)
                        } else if (pattern == 1) {
                            // Extra donation
                            Triple(req, PaymentStatus.PAID, 500.0)
                        } else if (pattern == 2 && index >= 10) {
                            // Unpaid recently
                            Triple(0.0, PaymentStatus.UNPAID, 0.0)
                        } else {
                            Triple(req, PaymentStatus.PAID, 0.0)
                        }
                    }
                    index == 12 -> { // Jan 2026
                        if (pattern % 3 == 0) Triple(req, PaymentStatus.PAID, 0.0)
                        else if (pattern % 3 == 1) Triple(req, PaymentStatus.PAID_IN_ADVANCE, 200.0)
                        else Triple(req * 0.5, PaymentStatus.PARTIALLY_PAID, 0.0)
                    }
                    else -> { // Feb 2026
                        if (pattern % 4 == 0) Triple(req, PaymentStatus.PAID, 0.0)
                        else Triple(0.0, PaymentStatus.UNPAID, 0.0)
                    }
                }

                val pDate = if (status != PaymentStatus.UNPAID) "2025-0${(index % 9) + 1}-05" else null

                records.add(
                    MonthlyRecord(
                        monthKey = mKey,
                        monthNameEn = mKey,
                        monthNameBn = mKey,
                        requiredAmount = req,
                        paidAmount = paid,
                        donationAmount = donation,
                        status = status,
                        lastPaymentDate = pDate
                    )
                )
            }
            map[member.id] = records
        }
        return map
    }

    fun generatePaymentReceipts(members: List<Member>): List<PaymentRecord> {
        val receipts = mutableListOf<PaymentRecord>()
        val methods = listOf("bKash", "Nagad", "Bank Transfer", "Cash")

        var receiptCount = 1001
        for (i in 0..150) {
            val member = members[(i * 3) % members.size]
            val isMultiMonth = (i % 7 == 0)
            val isExtraDonation = (i % 5 == 0)
            val method = methods[i % methods.size]

            val months = if (isMultiMonth) listOf("2025-01", "2025-02", "2025-03") else listOf("2025-0${(i % 9) + 1}")
            val contrib = member.monthlyContribution * months.size
            val donation = if (isExtraDonation) 500.0 else 0.0
            val total = contrib + donation

            receipts.add(
                PaymentRecord(
                    receiptNo = "RCP-2025-$receiptCount",
                    memberId = member.id,
                    memberNameEn = member.nameEn,
                    memberNameBn = member.nameBn,
                    paymentDate = "2025-0${(i % 9) + 1}-${(i % 25) + 1}",
                    totalReceived = total,
                    contributionAmount = contrib,
                    donationAmount = donation,
                    coveredMonths = months,
                    paymentMethod = method,
                    reference = "TRX${1000000 + i * 883}",
                    recordedBy = if (i % 2 == 0) "Md. Eliyas Hossain (Admin)" else "Kazi Farhad Ahmed (Manager)",
                    approvedBy = "Central Accounts",
                    status = "Approved",
                    notes = if (isExtraDonation) "৳500 added as extra welfare donation" else "Regular payment"
                )
            )
            receiptCount++
        }
        return receipts
    }

    fun generateDonations(members: List<Member>): List<DonationRecord> {
        val list = mutableListOf<DonationRecord>()
        list.add(
            DonationRecord(
                id = "DON-5001",
                donorType = DonorType.MEMBER,
                donorNameEn = "Dr. Monir Hossain",
                donorNameBn = "ডা: মনির হোসেন",
                memberId = "MEM-1007",
                amount = 10000.0,
                date = "2025-01-15",
                paymentMethod = "Bank Transfer",
                purpose = "Annual Emergency Relief Fund",
                isPublic = true,
                showDonorName = true,
                notes = "Special contribution for organization welfare"
            )
        )
        list.add(
            DonationRecord(
                id = "DON-5002",
                donorType = DonorType.ANONYMOUS,
                donorNameEn = "Anonymous Donor",
                donorNameBn = "বেনামী দাতা",
                memberId = null,
                amount = 25000.0,
                date = "2025-02-01",
                paymentMethod = "Cash",
                purpose = "Office Equipment Support",
                isPublic = true,
                showDonorName = false,
                notes = "Donor requested privacy"
            )
        )
        list.add(
            DonationRecord(
                id = "DON-5003",
                donorType = DonorType.EXTERNAL,
                donorNameEn = "Al-haj Karim Trust",
                donorNameBn = "আল-হাজ্ব করিম ট্রাস্ট",
                memberId = null,
                amount = 50000.0,
                date = "2025-02-10",
                paymentMethod = "Cheque",
                purpose = "Annual General Meeting Fund",
                isPublic = true,
                showDonorName = true,
                notes = "External philanthropic grant"
            )
        )
        return list
    }

    fun generateIncome(): List<IncomeRecord> {
        return listOf(
            IncomeRecord("INC-101", "Monthly Member Contribution", "সদস্যদের মাসিক চাঁদা", 320000.0, "2025-01-31", "200+ Members", "Admin", "Central Accounts", "January Total Collection"),
            IncomeRecord("INC-102", "Extra Donations Collection", "অতিরিক্ত অনুদান সংগ্রহ", 85000.0, "2025-02-05", "Members & Well-wishers", "Admin", "Central Accounts", "Welfare Fund"),
            IncomeRecord("INC-103", "New Membership Fee", "নতুন সদস্যভুক্তি ফি", 15000.0, "2025-02-12", "15 New Members", "Manager", "Central Accounts", "Enrollment Fees")
        )
    }

    fun generateExpenses(): List<ExpenseRecord> {
        return listOf(
            ExpenseRecord("EXP-201", "Office Rent & Utility Bill", "অফিস ভাড়া ও ইউটিলিটি বিল", "Rent & Utilities", "ভাড়া ও ইউটিলিটি", 18500.0, "2025-01-05", "Building Authority", "Bank", "Monthly Rent for HQ", "Admin", "Central Accounts"),
            ExpenseRecord("EXP-202", "Annual Member Directory Printing", "বার্ষিক নির্দেশিকা মুদ্রণ", "Printing", "মুদ্রণ", 35000.0, "2025-01-20", "Dhaka Press", "Cheque", "Printed 250 copies member directory", "Manager", "Admin"),
            ExpenseRecord("EXP-203", "Emergency Welfare Support", "জরুরি জনকল্যাণ সহায়তা", "Welfare", "জনকল্যাণ", 20000.0, "2025-02-02", "Member Medical Aid", "bKash", "Financial assistance for sick member", "Admin", "Admin"),
            ExpenseRecord("EXP-204", "IT & Server Maintenance", "আইটি ও সার্ভার রক্ষণাবেক্ষণ", "IT & Tech", "আইটি ও প্রযুক্তি", 7500.0, "2025-02-14", "TechSolutions BD", "Nagad", "Hosting & Software updates", "Manager", "Admin")
        )
    }

    fun generateNotifications(): List<NotificationItem> {
        return listOf(
            NotificationItem(
                id = "NOT-1",
                memberId = null,
                titleEn = "Payment Confirmation System Updated",
                titleBn = "পেমেন্ট নিশ্চিতকরণ সিস্টেম আপডেট করা হয়েছে",
                messageEn = "Your monthly contribution payments are now recorded and approved by Admin/Manager.",
                messageBn = "আপনার মাসিক চাঁদা প্রদান এখন এডমিন/ম্যানেজার দ্বারা ডিজিটালভাবে সংরক্ষিত ও অনুমোদিত হচ্ছে।",
                timestamp = "2025-02-18 09:00 AM",
                isRead = false
            ),
            NotificationItem(
                id = "NOT-2",
                memberId = "MEM-1001",
                titleEn = "January Contribution Received",
                titleBn = "জানুয়ারি মাসের চাঁদা জমা হয়েছে",
                messageEn = "Your payment of ৳2,000 for January 2025 has been approved. Receipt: RCP-2025-1001",
                messageBn = "আপনার জানুয়ারি ২০২৫ মাসের ৳২,০০০ চাঁদা সফলভাবে জমা ও অনুমোদিত হয়েছে। রশিদ: RCP-2025-1001",
                timestamp = "2025-01-10 04:15 PM",
                isRead = true,
                relatedReceiptNo = "RCP-2025-1001"
            ),
            NotificationItem(
                id = "NOT-3",
                memberId = null,
                titleEn = "Annual General Assembly Announcement",
                titleBn = "বার্ষিক সাধারণ সভা সংক্রান্ত বিজ্ঞপ্তি",
                messageEn = "All 200+ members are requested to review their contribution records before the assembly.",
                messageBn = "সকল ২০০+ সদস্যকে সাধারণ সভার পূর্বে তাদের চাঁদার হিসাব ও বকেয়া খতিয়ে দেখার জন্য অনুরোধ করা যাচ্ছে।",
                timestamp = "2025-02-15 11:30 AM",
                isRead = false
            )
        )
    }

    fun generateMessages(): List<MessageItem> {
        return listOf(
            MessageItem("MSG-1", "MEM-1005", "Fatema Begum", UserRole.MEMBER, "ADM-1001", "Assalamu Alaikum. I sent my February contribution via bKash today.", "2025-02-19 10:15 AM", true),
            MessageItem("MSG-2", "ADM-1001", "Md. Eliyas Hossain (Admin)", UserRole.ADMIN, "MEM-1005", "Wa Alaikum Assalam. Received and recorded successfully. Jazakallah!", "2025-02-19 10:22 AM", false)
        )
    }
}
