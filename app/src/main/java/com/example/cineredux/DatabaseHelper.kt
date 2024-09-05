package com.example.cineredux

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CineRedux.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_SURNAME = "surname"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_SURNAME TEXT," +
                "$COLUMN_USERNAME TEXT," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_PHONE TEXT," +
                "$COLUMN_PASSWORD TEXT)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Function to add a new user
    fun addUser(name: String, surname: String, username: String, email: String, phone: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SURNAME, surname)
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_PASSWORD, password)

        return db.insert(TABLE_NAME, null, values)
    }

    // Function to verify login credentials
    fun verifyUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_USERNAME, COLUMN_PASSWORD),
            "$COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?",
            arrayOf(username, password), null, null, null, null
        )
        val userExists = cursor.moveToFirst()
        cursor.close()
        return userExists
    }

    // Function to check if a username or email already exists
    fun isUserRegistered(username: String, email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME, arrayOf(COLUMN_USERNAME, COLUMN_EMAIL),
            "$COLUMN_USERNAME=? OR $COLUMN_EMAIL=?",
            arrayOf(username, email), null, null, null, null
        )
        val userExists = cursor.moveToFirst()
        cursor.close()
        return userExists
    }

    // Function to update user information (e.g., if the user wants to update their profile)
    fun updateUser(id: Int, name: String, surname: String, username: String, email: String, phone: String, password: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(COLUMN_NAME, name)
        values.put(COLUMN_SURNAME, surname)
        values.put(COLUMN_USERNAME, username)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_PASSWORD, password)

        return db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    // Function to get user details by username
    fun getUserByUsername(username: String): Cursor? {
        val db = this.readableDatabase
        return db.query(
            TABLE_NAME, null,
            "$COLUMN_USERNAME=?", arrayOf(username),
            null, null, null
        )
    }
}
