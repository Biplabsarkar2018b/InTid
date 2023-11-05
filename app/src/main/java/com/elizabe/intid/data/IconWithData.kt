package com.elizabe.intid.data

import androidx.room.*


@Entity(tableName = "icon_table")
data class IconWithData(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "icon_resource_id") val iconResourceId: Int?,
    @ColumnInfo(name = "app_name") val appName: String?,
    @ColumnInfo(name = "app_url") val appUrl: String?
)
