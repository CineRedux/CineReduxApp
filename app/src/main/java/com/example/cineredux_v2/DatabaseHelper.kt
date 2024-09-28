package com.example.cineredux_v2
//
//import android.content.ContentValues
//import android.content.Context
//import android.database.Cursor
//import android.database.sqlite.SQLiteDatabase
//import android.database.sqlite.SQLiteOpenHelper
//
//class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//
//    companion object {
//        private const val DATABASE_NAME = "CineRedux.db"
//        private const val DATABASE_VERSION = 1
//        const val TABLE_NAME = "Users"
//        const val COLUMN_ID = "id"
//        const val COLUMN_NAME = "name"
//        const val COLUMN_SURNAME = "surname"
//        const val COLUMN_USERNAME = "username"
//        const val COLUMN_EMAIL = "email"
//        const val COLUMN_PHONE = "phone"
//        const val COLUMN_PASSWORD = "password"
//
//
//    }
//
//    override fun onCreate(db: SQLiteDatabase?) {
//        val createTable = ("CREATE TABLE $TABLE_NAME (" +
//                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "$COLUMN_NAME TEXT," +
//                "$COLUMN_SURNAME TEXT," +
//                "$COLUMN_USERNAME TEXT," +
//                "$COLUMN_EMAIL TEXT," +
//                "$COLUMN_PHONE TEXT," +
//                "$COLUMN_PASSWORD TEXT)")
//        db?.execSQL(createTable)
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
//        onCreate(db)
//    }
//
//    // Function to add a new user
//    fun addUser(name: String, surname: String, username: String, email: String, phone: String, password: String): Long {
//        val db = this.writableDatabase
//        val values = ContentValues()
//
//        values.put(COLUMN_NAME, name)
//        values.put(COLUMN_SURNAME, surname)
//        values.put(COLUMN_USERNAME, username)
//        values.put(COLUMN_EMAIL, email)
//        values.put(COLUMN_PHONE, phone)
//        values.put(COLUMN_PASSWORD, password)
//
//        return db.insert(TABLE_NAME, null, values)
//    }
//
//    // Function to verify login credentials
//    fun verifyUser(username: String, password: String): Boolean {
//        val db = this.readableDatabase
//        val cursor: Cursor = db.query(
//            TABLE_NAME, arrayOf(COLUMN_USERNAME, COLUMN_PASSWORD),
//            "$COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?",
//            arrayOf(username, password), null, null, null, null
//        )
//        val userExists = cursor.moveToFirst()
//        cursor.close()
//        return userExists
//    }
//
//    // Function to check if a username or email already exists
//    fun isUserRegistered(username: String, email: String): Boolean {
//        val db = this.readableDatabase
//        val cursor: Cursor = db.query(
//            TABLE_NAME, arrayOf(COLUMN_USERNAME, COLUMN_EMAIL),
//            "$COLUMN_USERNAME=? OR $COLUMN_EMAIL=?",
//            arrayOf(username, email), null, null, null, null
//        )
//        val userExists = cursor.moveToFirst()
//        cursor.close()
//        return userExists
//    }
//
//    // Function to update user information (e.g., if the user wants to update their profile)
//    fun updateUser(id: Int, name: String, surname: String, username: String, email: String, phone: String, password: String): Int {
//        val db = this.writableDatabase
//        val



import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.cineredux_v2.models.User

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "CineRedux.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SURNAME = "surname"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_SURNAME TEXT, "
                + "$COLUMN_USERNAME TEXT, "
                + "$COLUMN_EMAIL TEXT, "
                + "$COLUMN_PHONE TEXT, "
                + "$COLUMN_PASSWORD TEXT)")
        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    // Function to add a new user
    fun addUser(
        name: String,
        surname: String,
        username: String,
        email: String,
        phone: String,
        password: String
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SURNAME, surname)
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    // Function to verify login credentials
    fun verifyUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS, arrayOf(COLUMN_USERNAME, COLUMN_PASSWORD),
            "$COLUMN_USERNAME=? AND $COLUMN_PASSWORD=?",
            arrayOf(username, password), null, null, null
        )
        val userExists = cursor.moveToFirst()
        cursor.close()
        return userExists
    }

    // Function to get a user by ID
    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val cursor: Cursor? = db.query(
            TABLE_USERS, null, "$COLUMN_ID = ?", arrayOf(userId.toString()), null, null, null
        )

        cursor?.use {
            // Log column names for debugging
            val columnCount = it.columnCount
            for (i in 0 until columnCount) {
                Log.d("DatabaseHelper", "Column $i: ${it.getColumnName(i)}")
            }

            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(COLUMN_NAME)
                val surnameIndex = it.getColumnIndex(COLUMN_SURNAME)
                val usernameIndex = it.getColumnIndex(COLUMN_USERNAME)
                val emailIndex = it.getColumnIndex(COLUMN_EMAIL)
                val phoneIndex = it.getColumnIndex(COLUMN_PHONE)
                val passwordIndex = it.getColumnIndex(COLUMN_PASSWORD)

                // Check if column indices are valid
                if (nameIndex != -1 && surnameIndex != -1 && usernameIndex != -1 && emailIndex != -1 && phoneIndex != -1 && passwordIndex != -1) {
                    val name = it.getString(nameIndex)
                    val surname = it.getString(surnameIndex)
                    val username = it.getString(usernameIndex)
                    val email = it.getString(emailIndex)
                    val phone = it.getString(phoneIndex)
                    val password = it.getString(passwordIndex)

                    return User(userId, name, surname, username, email, phone, password)
                } else {
                    Log.e("DatabaseHelper", "One or more column indices are invalid.")
                }
            }
        }

        // Make sure to close the database in a finally block or after all operations
        db.close()
        return null
    }


    // Function to update user details
    fun updateUser(id: Int, name: String, surname: String, username: String, email: String, phone: String, password: String): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SURNAME, surname)
            put(COLUMN_USERNAME, username)
            put(COLUMN_EMAIL, email)
            put(COLUMN_PHONE, phone)
            put(COLUMN_PASSWORD, password)
        }

        return db.update(TABLE_USERS, contentValues, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
    fun deleteUser(userId: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_USERS, "$COLUMN_ID = ?", arrayOf(userId.toString()))
    }
    // Function to check if a username or email already exists
    fun isUserRegistered(username: String, email: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USERS, arrayOf(COLUMN_USERNAME, COLUMN_EMAIL),
            "$COLUMN_USERNAME=? OR $COLUMN_EMAIL=?",
            arrayOf(username, email), null, null, null
        )
        val userExists = cursor.moveToFirst()
        cursor.close()
        return userExists
    }
    fun clearUserSession() {

    }
}