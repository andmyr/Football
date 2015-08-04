package org.itstep.mushta.football;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by And on 17.07.2015.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "football.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //Таблица команд
        sqLiteDatabase.execSQL("CREATE TABLE [teams] (\n" +
                // "  [ID] integer primary key autoincrement, \n" +
                "  [Name] VARCHAR NOT NULL, \n" +
                "  [picture] BLOB, \n" +
                "  [total_games] INT, \n" +
                "  [win] INT, \n" +
                "  [draw] INT, \n" +
                "  [loss] INT, \n" +
                "  [goals_out] INT, \n" +
                "  [goals_in] INT, \n" +
                "  [total] INT);");

        //Таблица игр
        sqLiteDatabase.execSQL("CREATE TABLE [games] (\n" +
                "  [ID] integer primary key autoincrement, \n" +
                "  [first_team] INT NOT NULL CONSTRAINT [team1] REFERENCES [teams]([ID]), \n" +
                "  [second_team] INT NOT NULL CONSTRAINT [team2] REFERENCES [teams]([ID]), \n" +
                "  [first_team_score] INT NOT NULL, \n" +
                "  [second_team_score] INT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        /*Log.w("SQLite", "Обновляемся с версии " + i + " на версию " + i2);
        // Удаляем старую таблицу и создаём новую
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS teams");
        sqLiteDatabase.execSQL("DROP TABLE IF IT EXISTS games");
        // Создаём новую таблицу
        onCreate(sqLiteDatabase);*/
    }


}