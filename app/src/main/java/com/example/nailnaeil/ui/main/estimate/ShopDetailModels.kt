package com.example.nailnaeil.ui.main.estimate

data class PriceBreakdownRow(val label: String, val amount: Int)

data class PriceBreakdownSection(
    val title: String,
    val rows: List<PriceBreakdownRow>
) {
    val totalAmount: Int get() = rows.sumOf { it.amount }
}
