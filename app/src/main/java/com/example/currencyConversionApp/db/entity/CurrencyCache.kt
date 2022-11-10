package com.nmc.myapplication.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index("id")]
)
class CurrencyCache(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String,

    @ColumnInfo(name = "Value")
    var value: String,

    @ColumnInfo(name = "timeStamp")
    var timeStamp: String
)