package com.example.timetable.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.timetable.db_modules.Task

class TaskOpenHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,
    null, DATABASE_VERSION)
{
    private var db: SQLiteDatabase = this.writableDatabase

    override fun onCreate(p0: SQLiteDatabase?)
    {
        p0!!.execSQL(Task.CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int)
    {
        p0!!.execSQL("DROP TABLE IF EXISTS ${Task.TABLE_NAME}")
        onCreate(p0)
    }

    /** i put all the database operations in a different
     * thread to make the app faster */
    fun getAllData(): ArrayList<Task>
    {
        val tasks = ArrayList<Task>()

        Thread{
            val cursor = db.rawQuery("select * from ${Task.TABLE_NAME}", null)
            cursor.moveToFirst()

            while (!cursor.isAfterLast){
                val task = Task(cursor.getString(0),
                    cursor.getString(1)
                    , cursor.getInt(2) == 1)
                tasks.add(task)
                cursor.moveToNext()
            }
            cursor.close()
        }.start()
        return tasks
    }

    fun update(task: Task, oldTitle: String):Boolean
    {
        var success = false
        Thread{
            val cv = ContentValues()
            cv.put(Task.TITLE_COL, task.title)
            cv.put(Task.START_COL, task.start)
            var finished = 0
            if (task.isFinished)
                finished = 1
            cv.put(Task.FINISHED_COL, finished)
            success =  try {
                db.update(Task.TABLE_NAME, cv, "${Task.TITLE_COL} = '$oldTitle' and ${Task.START_COL} = '${task.start}'", null) > 0
            }catch (e: Exception){
                e.printStackTrace()
                false
            }
        }.start()

        return success
    }


    fun delete(task: Task): Boolean
    {
        var success = false
       Thread{
           success = db.delete(Task.TABLE_NAME,
               "${Task.TITLE_COL} = '${task.title}' and ${Task.START_COL} = '${task.start}'", null) > 0
       }.start()
       return success
    }


    fun insert(task: Task): Boolean
    {
        var success = false
        Thread{
            val cv = ContentValues()
            cv.put(Task.TITLE_COL, task.title)
            cv.put(Task.START_COL, task.start)
            cv.put(Task.FINISHED_COL, task.isFinished)
            success = db.insert(Task.TABLE_NAME, null, cv) > 0
        }.start()
        return success
    }


    companion object
    {
        private const val DATABASE_NAME = "taskdb"
        private const val DATABASE_VERSION = 3
    }


}