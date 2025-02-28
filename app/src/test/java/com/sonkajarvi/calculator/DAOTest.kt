package com.sonkajarvi.calculator

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sonkajarvi.calculator.data.Entry
import com.sonkajarvi.calculator.data.EntryDAO
import com.sonkajarvi.calculator.data.HistoryDatabase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DAOTest {
    private lateinit var dao: EntryDAO
    private lateinit var database: HistoryDatabase

    private var one = Entry(1, "1+2")
    private var two = Entry(1, "3*4")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, HistoryDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = database.dao()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database.close()
    }

    private suspend fun addOneItemToDb() {
        dao.insert(one)
    }

    private suspend fun addTwoItemsToDb() {
        dao.insert(one)
        dao.insert(two)
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()

        val allItems = dao.getAll().first()
        assertEquals(allItems[0], one)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()

        val allItems = dao.getAll().first()
        assertEquals(allItems[0], one)
        assertEquals(allItems[1], two)
    }
}
