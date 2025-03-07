package com.example.revarioapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "UserDB"
        private const val DATABASE_VERSION = 2

        // Tabel users
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_FULLNAME = "fullname"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"

        // Tabel notes
        private const val TABLE_NOTES = "allnotes"
        private const val COLUMN_NOTE_ID = "id"
        private const val COLUMN_NOTE_TITLE = "title"
        private const val COLUMN_NOTE_CONTENT = "content"

        //Tabel book
        private const val TABLE_BOOKS = "book"
        private const val COLUMN_BOOK_ID = "id"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_NAMA_PANGGILAN = "nama_panggilan"
        private const val COLUMN_POTO = "poto"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ALAMAT = "alamat"
        private const val COLUMN_TGL_LAHIR = "tgl_lahir"
        private const val COLUMN_TELEPON = "telepon"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create users table
        val createUsersTable = """
        CREATE TABLE $TABLE_USERS (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $COLUMN_FULLNAME TEXT, 
            $COLUMN_USERNAME TEXT UNIQUE, 
            $COLUMN_PASSWORD TEXT
        )
    """.trimIndent()
        db.execSQL(createUsersTable)

        // Create notes table
        val createNotesTable = """
        CREATE TABLE $TABLE_NOTES (
            $COLUMN_NOTE_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $COLUMN_NOTE_TITLE TEXT NOT NULL, 
            $COLUMN_NOTE_CONTENT TEXT NOT NULL
        )
    """.trimIndent()
        db.execSQL(createNotesTable)

        // Create books table
        val createBooksTable = """
        CREATE TABLE $TABLE_BOOKS (
            $COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $COLUMN_NAMA TEXT NOT NULL, 
            $COLUMN_NAMA_PANGGILAN TEXT NOT NULL, 
            $COLUMN_POTO BLOB NOT NULL, 
            $COLUMN_EMAIL TEXT NOT NULL, 
            $COLUMN_ALAMAT TEXT NOT NULL, 
            $COLUMN_TGL_LAHIR TEXT NOT NULL, 
            $COLUMN_TELEPON TEXT NOT NULL
        )
    """.trimIndent()
        db.execSQL(createBooksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    // --- USER FUNCTIONS ---
    fun isUserRegistered(): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_USERS"
        val cursor = db.rawQuery(query, null)

        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()
        return count > 0
    }

    fun insertUser(fullname: String, username: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FULLNAME, fullname)
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result != -1L
    }

    fun checkUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))

        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    // --- NOTE FUNCTIONS ---
    fun insertNote(note: Note): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("title", note.title)
            put("content", note.content)
        }

        val result = db.insert("allnotes", null, values)

        if (result == -1L) {
            android.util.Log.e("DatabaseError", "Failed to insert note: Title=${note.title}, Content=${note.content}")
        } else {
            android.util.Log.i("DatabaseSuccess", "Note inserted successfully with ID $result")
        }

        db.close()
        return result != -1L
    }


    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_NOTES", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NOTE_ID))
                    val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_TITLE))
                    val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE_CONTENT))
                    notes.add(Note(id, title, content))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }

        return notes
    }

    fun deleteNote(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NOTES, "$COLUMN_NOTE_ID = ?", arrayOf(id.toString()))
        db.close()
        return result > 0
    }

    fun updateNote(note: Note): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOTE_TITLE, note.title)
            put(COLUMN_NOTE_CONTENT, note.content)
        }
        val result = db.update(TABLE_NOTES, values, "$COLUMN_NOTE_ID = ?", arrayOf(note.id.toString()))
        db.close()
        return result > 0
    }

    // --- BOOK FUNCTIONS ---
    fun insertBook(book: Book) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, book.nama)
            put(COLUMN_NAMA_PANGGILAN, book.nama_panggilan)
            put(COLUMN_POTO, book.poto)
            put(COLUMN_EMAIL, book.email)
            put(COLUMN_ALAMAT, book.alamat)
            put(COLUMN_TGL_LAHIR, book.tgl_lahir)
            put(COLUMN_TELEPON, book.telepon)
        }
        db.insert(TABLE_BOOKS, null, values)
        db.close()
    }

    fun getAllBooks(): List<Book> {
        val booksList = mutableListOf<Book>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_BOOKS"
        val cursor = db.rawQuery (query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
            val nama_panggilan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_PANGGILAN))
            val poto = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_POTO))
            val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
            val alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT))
            val tgl_lahir = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TGL_LAHIR))
            val telepon = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEPON))

            val book = Book(id, nama, nama_panggilan, poto, email, alamat, tgl_lahir, telepon)
            booksList.add(book)
        }
        cursor.close()
        db.close()
        return booksList
    }

    fun updateBook(book: Book): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAMA, book.nama)
            put(COLUMN_NAMA_PANGGILAN, book.nama_panggilan)
            put(COLUMN_POTO, book.poto)
            put(COLUMN_EMAIL, book.email)
            put(COLUMN_ALAMAT, book.alamat)
            put(COLUMN_TGL_LAHIR, book.tgl_lahir)
            put(COLUMN_TELEPON, book.telepon)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(book.id.toString())
        val rowsAffected = db.update(TABLE_BOOKS, values, whereClause, whereArgs)
        db.close()
        return rowsAffected > 0
    }

    fun getBookById(bookId: Int): Book {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_BOOKS WHERE $COLUMN_ID = $bookId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val nama = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA))
        val nama_panggilan = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA_PANGGILAN))
        val poto = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_POTO))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val alamat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALAMAT))
        val tgl_lahir = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TGL_LAHIR))
        val telepon = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TELEPON))
        cursor.close()
        db.close()
        return Book(id, nama, nama_panggilan, poto, email, alamat, tgl_lahir, telepon)
    }

    fun deleteBook(bookId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(bookId.toString())
        db.delete (TABLE_BOOKS, whereClause, whereArgs)
        db.close()
    }
}