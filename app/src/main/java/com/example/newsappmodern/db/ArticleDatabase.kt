package com.example.newsappmodern.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.newsappmodern.models.Article


@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase(){

    abstract fun getArticleDao(): ArticleDao

    companion object{
        @Volatile
        private var instance:ArticleDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context):ArticleDatabase{
            return instance?: synchronized(LOCK){
                instance?: createDatabase(context).also { instance = it }

            }
        }

        private fun createDatabase(context: Context):ArticleDatabase{
            val db = Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "news_db.db"
            ).build()

            return db
        }
    }
}