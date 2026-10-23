package com.eliyas.fundmanagementapp.ui.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eliyas.fundmanagementapp.localization.Strings
import com.eliyas.fundmanagementapp.model.Language
import com.eliyas.fundmanagementapp.ui.theme.*

// 1. MONTHLY COLLECTION BAR CHART (Last 6 Months, Bottom-Aligned Month Labels)
@Composable
fun BarChartCard(
    title: String,
    data: List<Pair<String, Double>>, // Month name/key to amount
    maxVal: Double = 400000.0,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val last6MonthsData = data.takeLast(6)
    val calculatedMax = maxOf(maxVal, (last6MonthsData.maxOfOrNull { it.second } ?: 1.0) * 1.15)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Last 6 Months Vertical Bar Chart Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                last6MonthsData.forEach { (label, amount) ->
                    val ratio = (amount / calculatedMax).coerceIn(0.08, 1.0).toFloat()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        // 1. Numeric Amount Label above bar
                        Text(
                            text = Strings.formatDigits(String.format("%.0fk", amount / 1000), lang),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryGreen
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // 2. Fixed-Height Bar Track Container (100dp)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(horizontal = 4.dp)
                                .background(SoftBackground, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(ratio)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(if (amount >= 300000) PrimaryGreen else WarmGold)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // 3. Month Name Label explicitly sitting at the Bottom Axis
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

// 2. DONUT BREAKDOWN CHART
@Composable
fun DonutChartCard(
    title: String,
    items: List<Triple<String, Double, Color>>,
    totalAmount: Double,
    lang: Language,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryGreen
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(110.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(100.dp)) {
                        var startAngle = -90f
                        items.forEach { (_, value, color) ->
                            val sweepAngle = if (totalAmount > 0) ((value / totalAmount) * 360f).toFloat() else 0f
                            drawArc(
                                color = color,
                                startAngle = startAngle,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = 24f, cap = StrokeCap.Round),
                                size = Size(size.width, size.height)
                            )
                            startAngle += sweepAngle
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (lang == Language.BN) "মোট" else "Total",
                            fontSize = 9.sp,
                            color = TextSecondary
                        )
                        Text(
                            text = Strings.formatCurrency(totalAmount, lang),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items.forEach { (label, amount, color) ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = label, fontSize = 11.sp, color = TextSecondary)
                                Text(
                                    text = Strings.formatCurrency(amount, lang),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. TARGET vs COLLECTED PROGRESS BAR CARD
@Composable
fun CollectionProgressBarCard(
    title: String,
    collected: Double,
    target: Double,
    lang: Language,
    modifier: Modifier = Modifier
) {
    val progress = if (target > 0) (collected / target).coerceIn(0.0, 1.0).toFloat() else 0f
    val percentage = (progress * 100).toInt()

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryGreen
                )
                Text(
                    text = "${Strings.formatDigits(percentage.toString(), lang)}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = StatusSuccess
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(BorderLight)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(RoundedCornerShape(6.dp))
                        .background(StatusSuccess)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${if (lang == Language.BN) "সংগৃহীত:" else "Collected:"} ${Strings.formatCurrency(collected, lang)}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = "${if (lang == Language.BN) "লক্ষ্যমাত্রা:" else "Target:"} ${Strings.formatCurrency(target, lang)}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}
