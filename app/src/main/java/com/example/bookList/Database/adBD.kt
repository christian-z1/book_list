package com.example.bookList.Database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class adBD (context: Context) :SQLiteOpenHelper(context, DATABASE, null, 1)
{
    companion object
    {
        val DATABASE="capilla"
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "Create Table libros(" +
                    "idL Integer primary key,"+
                    "nombre text, "+
                    "editorial text, "+
                    "autor text)"
        )
    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {


    }
    fun consulta(select: String):Cursor? {
        try
        {
            val db = this.readableDatabase
            return db.rawQuery(select,null)
        }
        catch (ex:Exception)
        {
            return null
        }
    }
    fun Ejecuta(sentencia:String):Int {
        try
        {
            val db=this.writableDatabase// se abre base datos en base modo lectura
            db.execSQL(sentencia)
            db.close()
            return  1
        }
        catch (ex: Exception)
        {
            return 0
        }
    }
}