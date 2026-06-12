package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "query_history")
data class QueryHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val queryText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface CountyDao {
    @Query("SELECT * FROM followed_counties")
    fun getFollowedCounties(): Flow<List<County>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun followCounty(county: County)

    @Delete
    suspend fun unfollowCounty(county: County)

    @Query("SELECT EXISTS(SELECT 1 FROM followed_counties WHERE id = :countyId LIMIT 1)")
    suspend fun isCountyFollowed(countyId: String): Boolean
}

@Dao
interface QueryHistoryDao {
    @Query("SELECT * FROM query_history ORDER BY timestamp DESC LIMIT 30")
    fun getQueryHistory(): Flow<List<QueryHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuery(query: QueryHistory)

    @Query("DELETE FROM query_history WHERE id = :id")
    suspend fun deleteQueryById(id: Int)

    @Query("DELETE FROM query_history")
    suspend fun clearHistory()
}

@Database(entities = [County::class, QueryHistory::class], version = 1, exportSchema = false)
abstract class CricketHouseDatabase : RoomDatabase() {
    abstract fun countyDao(): CountyDao
    abstract fun queryHistoryDao(): QueryHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CricketHouseDatabase? = null

        fun getDatabase(context: Context): CricketHouseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CricketHouseDatabase::class.java,
                    "cricket_house_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
