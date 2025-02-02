package com.example.revarioapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "UserDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, fullname TEXT, username TEXT UNIQUE, password TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun isUserRegistered(): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM users"
        val cursor = db.rawQuery(query, null)

        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()

        return count > 0
    }



    fun insertUser(fullname: String, username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("fullname", fullname)
        values.put("username", username)
        values.put("password", password)

        val result = db.insert("users", null, values)
        return result != -1L // Jika -1, berarti gagal
    }


    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM users WHERE username = ? AND password = ?"
        val cursor: Cursor = db.rawQuery(query, arrayOf(username, password))

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
}
