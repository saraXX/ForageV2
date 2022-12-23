package android.guide.foragev2

import android.app.Application
import android.guide.foragev2.data.ForageDatabase
import android.util.Log

class BaseApplication : Application() {

    // TODO: provide a ForageDatabase value by lazy here
    val database: ForageDatabase by lazy{
        ForageDatabase.getDatabase(this)
    }
}