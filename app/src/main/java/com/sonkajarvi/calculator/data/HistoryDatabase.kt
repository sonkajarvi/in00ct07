package com.sonkajarvi.calculator.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Entry::class], version = 3)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun getDAO(): EntryDAO
}
