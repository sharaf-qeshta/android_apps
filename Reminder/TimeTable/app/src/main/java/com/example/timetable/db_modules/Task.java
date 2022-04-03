package com.example.timetable.db_modules;

import android.annotation.SuppressLint;


import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Task
{
    private String title;
    private String start;
    private boolean finished;

    @SuppressLint("SimpleDateFormat")
    public static final  DateFormat DF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final String TABLE_NAME = "task";
    public static final String TITLE_COL = "title";
    public static final String START_COL = "start";
    public static final String FINISHED_COL = "finished";

    public static final String CREATE_TABLE =
            "create table "+ TABLE_NAME + "( "
          + TITLE_COL + " VARCHAR(260),"
          + START_COL + " datetime default current_timestamp,"
          + FINISHED_COL + " INTEGER DEFAULT 0,"
          + "primary key(" + TITLE_COL+ ", " + START_COL + "))";

    public Task(String title, Date start, boolean finished)
    {

        this.title = title;
        this.start = DF.format(start);
        this.finished = finished;
    }

    public Task(String title,
                String start,
                boolean finished)
    {
        this.title = title;
        this.start = start;
        this.finished = finished;
    }

    public Task(){}


    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getStart()
    {
        return start;
    }

    public void setStart(String start)
    {
        this.start = start;
    }


    public boolean isFinished()
    {
        return finished;
    }

    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }


    public static long compareDate(String d1, String d2)
    {
        try {
            Date date1 = DF.parse(d1);
            Date date2 = DF.parse(d2);
            return date1.getTime() - date2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return Long.MIN_VALUE;
        }
    }

}
