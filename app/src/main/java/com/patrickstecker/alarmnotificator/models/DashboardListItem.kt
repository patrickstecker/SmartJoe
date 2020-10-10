package com.patrickstecker.alarmnotificator.models

class DashboardListItem (
    val imgId: Int,
    val titleId: Int,
    val textId: Int,
    val type: DasboardItemType
)

enum class DasboardItemType {
    WELCOME, DHBW
}