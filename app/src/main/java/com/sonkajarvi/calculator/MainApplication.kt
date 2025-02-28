package com.sonkajarvi.calculator

import android.app.Application
import androidx.room.Room
import com.sonkajarvi.calculator.data.Entry
import com.sonkajarvi.calculator.data.HistoryDatabase

class MainApplication : Application() {
    companion object {
        lateinit var database: HistoryDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            HistoryDatabase::class.java,
            "history_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
