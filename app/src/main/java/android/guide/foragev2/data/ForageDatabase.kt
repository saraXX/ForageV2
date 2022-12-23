package android.guide.foragev2.data

import android.content.Context
import android.guide.foragev2.model.Forageable
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Forageable::class], version = 1, exportSchema = false)
abstract class ForageDatabase : RoomDatabase(){
    abstract fun forageableDao(): ForageableDao
    companion object{
        @Volatile
        private var INSTANCE: ForageDatabase? = null
        fun getDatabase(context: Context): ForageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ForageDatabase::class.java,
                    "forage_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}