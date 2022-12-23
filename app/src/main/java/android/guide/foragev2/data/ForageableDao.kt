package android.guide.foragev2.data

import android.guide.foragev2.model.Forageable
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ForageableDao {
    @Query("SELECT * from forageable_database")
    fun getForageables(): Flow<List<Forageable>>

    @Query("SELECT * from forageable_database WHERE id= :id")
    fun getForageable(id: Long): Flow<Forageable>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(forageable: Forageable)

    @Update
    suspend fun update(forageable: Forageable)

    @Delete
    suspend fun delete(forageable: Forageable)


}