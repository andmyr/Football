package org.itstep.mushta.football;

import android.graphics.Bitmap;

/**
 * Created by And on 29.07.2015.
 */
public class Team
{
    String name;
    Bitmap bitmap;
    int totalGames;
    int win;
    int draw;
    int loss;
    int goalsOut;
    int goalsIn;

    public Team(String name)
    {
        this.name = name;
        this.totalGames = 0;
        this.win = 0;
        this.draw = 0;
        this.loss = 0;
        this.goalsOut = 0;
        this.goalsIn = 0;
    }

    /**
     * @param firstTeamScore  - Число забитых голов
     * @param secondTeamScore - Число пропущеных голов
     */

    public void addGame(int firstTeamScore, int secondTeamScore)
    {
        totalGames++;
        if (firstTeamScore > secondTeamScore)
        {
            win++;
        } else if (firstTeamScore < secondTeamScore)
        {
            loss++;
        } else
        {
            draw++;
        }
        goalsOut +=firstTeamScore;
        goalsIn +=secondTeamScore;
    }

    public String getName()
    {
        return name;
    }

    public int getGoalsIn()
    {
        return goalsIn;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getTotalGames()
    {
        return totalGames;
    }

    public int getWin()
    {
        return win;
    }

    public int getDraw()
    {
        return draw;
    }

    public int getLoss()
    {
        return loss;
    }

    public int getGoalsOut()
    {
        return goalsOut;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }
}
