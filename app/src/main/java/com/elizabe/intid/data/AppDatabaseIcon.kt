package com.elizabe.intid.data

import android.content.*
import androidx.room.*
import com.elizabe.intid.*


@Database(entities = [IconWithData :: class], version = 1)
abstract class AppDatabaseIcon : RoomDatabase() {
    abstract fun iconDao():IconDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabaseIcon? = null

        fun getDatabase(context: Context): AppDatabaseIcon {

            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabaseIcon::class.java,
                    "app_database_icon"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}