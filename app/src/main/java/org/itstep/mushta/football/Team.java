package org.itstep.mushta.football;

import android.graphics.Bitmap;

/**
 * Created by And on 29.07.2015.
 */
public class Team
{
    private String name;
    private Bitmap bitmap;
    private int totalGames;
    private int win;
    private int draw;
    private int loss;
    private int goalsOut;
    private int goalsIn;
    private int total;

    public Team(String name)
    {
        this.name = name;
        this.totalGames = 0;
        this.win = 0;
        this.draw = 0;
        this.loss = 0;
        this.goalsOut = 0;
        this.goalsIn = 0;
        this.total = 0;
    }

    /**
     * @param name       - Имя
     * @param totalGames - Сыграно игр
     * @param win        - побед
     * @param draw       - ничей
     * @param loss       - поражений
     * @param goalsOut   - забито голов
     * @param goalsIn    - пропущено голов
     * @param total      - всего очков
     */
    public Team(String name, int totalGames, int win, int draw, int loss, int goalsOut, int goalsIn, int total)
    {
        this.name = name;
        this.totalGames = totalGames;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.goalsOut = goalsOut;
        this.goalsIn = goalsIn;
        this.total = total;
    }

    public Team(String name, Bitmap bitmap, int totalGames, int win, int draw, int loss, int goalsOut, int goalsIn, int total)
    {
        this.name = name;
        this.bitmap = bitmap;
        this.totalGames = totalGames;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.goalsOut = goalsOut;
        this.goalsIn = goalsIn;
        this.total = total;
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
            total += 3;
        } else if (firstTeamScore < secondTeamScore)
        {
            loss++;
        } else
        {
            draw++;
            total += 1;
        }
        goalsOut += firstTeamScore;
        goalsIn += secondTeamScore;
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

    public int getTotal()
    {
        return total;
    }
}
