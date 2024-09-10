package com.example.client.DatabaseHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.client.Model.Cliente

class DatabaseHelperCliente(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object
    {
        private const val DATABASE_NAME = "cliente.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "cliente"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NOME = "nome"
        private const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase)
    {
        val createTable = ("CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NOME TEXT, $COLUMN_EMAIL TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun adicionarCliente(cliente: Cliente)
    {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOME, cliente.nome)
            put(COLUMN_EMAIL, cliente.email)
        }

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun obterClientes(): List<Cliente>
    {
        val clientes = mutableListOf<Cliente>()
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NOME, COLUMN_EMAIL), null, null, null, null, null)

        with(cursor)
        {
            while(moveToNext())
            {
                val cliente = Cliente(
                    id = getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    nome = getString(getColumnIndexOrThrow(COLUMN_NOME)),
                    email = getString(getColumnIndexOrThrow(COLUMN_EMAIL))
                )

                clientes.add(cliente)
            }
        }

        cursor.close()
        db.close()

        return clientes
    }

    fun obterClientePorID(id: Int) : Cliente?
    {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_NAME, arrayOf(COLUMN_ID, COLUMN_NOME, COLUMN_EMAIL),
            "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null)

        var cliente: Cliente? = null

        if(cursor.moveToFirst())
        {
           cliente = Cliente(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            nome = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOME)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
           )
        }

        cursor.close()
        db.close()

        return cliente
    }

    fun atualizarClient(cliente: Cliente) : Int
    {
        val db = this.writableDatabase
        val values =  ContentValues().apply {
            put(COLUMN_NOME, cliente.nome)
            put(COLUMN_EMAIL, cliente.email)
        }

        return db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(cliente.id.toString()))
    }

    fun deletarCliente(id: Int): Int
    {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }
}