package org.itstep.mushta.football;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    final int CAMERA_CAPTURE = 1;
    final int NEW_SIZE = 80; // Резмер картинки-эмблемы команды (NEW_SIZE X NEW_SIZE)

    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DBHelper dbhelper;
    private EditText editTextTeam1Goals;
    private EditText editTextTeam2Goals;
    private ArrayList<String> strTeamList = new ArrayList<>(); //список для хранения имен команд
    private String strTeam1;
    private String strTeam2;
    private SQLiteDatabase db;
    private ArrayList<Team> teamList = new ArrayList<>();
    private Bitmap thumbnailBitmap;

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image)
    {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mScreenTitles = getResources().getStringArray(R.array.screen_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_list_item, mScreenTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        )
        {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView)
            {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Initialize the first fragment when the application first loads.
        if (savedInstanceState == null)
        {
            selectItem(0);
        }
        dbhelper = new DBHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId())
        {
            case R.id.action_search:
                // Show toast about click.
                Toast.makeText(this, R.string.action_search, Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void okClicked()
    {
        editTextTeam1Goals.setText("");
        editTextTeam2Goals.setText("");
    }

    public void refreshTeamList(View view)
    {
        strTeamList.clear();
        dbhelper = new DBHelper(this);
        db = dbhelper.getWritableDatabase();

        //Создадим список команд
        String query = "SELECT name FROM teams";
        Cursor c = db.rawQuery(query, null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                String str;
                do
                {
                    str = "";
                    for (String cn : c.getColumnNames())
                    {
                        str = str.concat(c.getString(c.getColumnIndex(cn)));
                        strTeamList.add(str);
                    }
                    //Log.d("TAG", str);

                } while (c.moveToNext());
            }
            c.close();
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strTeamList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setPrompt("Title");
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                strTeam1 = strTeamList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                strTeam1 = strTeamList.get(0);
            }
        });

        // адаптер
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strTeamList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(adapter2);
        spinner2.setPrompt("Title");
        spinner2.setSelection(0);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                strTeam2 = strTeamList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                strTeam2 = strTeamList.get(0);
            }
        });
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position)
    {
        // Update the main content by replacing fragments
        Fragment fragment = null;
        switch (position)
        {
            case 0:
                fragment = new AddCommandFragment();
                break;
            case 1:
                fragment = new AddGameFragment();
                break;
            case 2:
                fragment = new ShowTableFragment();
                break;
            default:
                break;
        }

        // Insert the fragment by replacing any existing fragment
        if (fragment != null)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment).commit();
            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mScreenTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else
        {
            // Error
            //Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void onSaveClick(View view)
    {
        EditText editTextTeamName = (EditText) findViewById(R.id.editTextTeamName);
        db = dbhelper.getWritableDatabase();
        //Проверка на пустоту базы
        Cursor c = db.rawQuery("SELECT * FROM Teams", null);
        boolean dBEmpty;
        dBEmpty = (c.getCount() < 1);
        //Проверка на повторяемости имени команды
        boolean nameExistsInDB = false;
        if (!dBEmpty)
        {
            c = db.rawQuery("SELECT Name FROM Teams WHERE Name = '" + editTextTeamName.getText().toString() + "'", null);
            if (c.moveToNext())
            {
                nameExistsInDB = true;
            }
        } else
        {
            nameExistsInDB = false;
        }
        if (!nameExistsInDB)
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", editTextTeamName.getText().toString());
            contentValues.put("total_games", 0);
            contentValues.put("win", 0);
            contentValues.put("draw", 0);
            contentValues.put("loss", 0);
            contentValues.put("goals_out", 0);
            contentValues.put("goals_in", 0);
            contentValues.put("total", 0);

            if (thumbnailBitmap != null)
            {
                contentValues.put("picture", getBytes(thumbnailBitmap));
            } else
            {
                //Вставим стандартную картинку
                Bitmap tmpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
                tmpBitmap = getResizedBitmap(tmpBitmap, NEW_SIZE, NEW_SIZE);
                contentValues.put("picture", getBytes(tmpBitmap));
            }

            long rowID = db.insert("teams", null, contentValues);
            //Log.d("TAG", "Вставили " + rowID);
            editTextTeamName.setText("");
            ImageView picView = (ImageView) findViewById(R.id.picture);
            //Уберем картинку с экрана
            picView.setImageResource(0);
            thumbnailBitmap = null;
        } else
        {
            //Log.d("TAG", "Команда уже есть в списке.");
            Toast.makeText(this, getResources().getString(R.string.teamExists), Toast.LENGTH_LONG).show();
        }
        c.close();
        db.close();
    }

    public void onAddLogoClick(View view)
    {
        try
        {
            // Намерение для запуска камеры
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        } catch (ActivityNotFoundException e)
        {
            // Выводим сообщение об ошибке
            String errorMessage = getResources().getString(R.string.notSupportPhoto);
            Toast toast = Toast
                    .makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            // Вернулись от приложения Камера
            if (requestCode == CAMERA_CAPTURE)
            {
                ImageView picView = (ImageView) findViewById(R.id.picture);
                thumbnailBitmap = (Bitmap) data.getExtras().get("data");
                thumbnailBitmap = getResizedBitmap(thumbnailBitmap, NEW_SIZE, NEW_SIZE);
                picView.setImageBitmap(thumbnailBitmap);
                if (thumbnailBitmap != null)
                {
                    thumbnailBitmap.recycle();
                    thumbnailBitmap = null;
                }
            }
        }
    }

    /**
     * Изменение размера картинки
     *
     * @param bm        - картинка
     * @param newWidth  - новая ширина
     * @param newHeight - новая высота
     * @return - картинка с измененным размером
     */
    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

    }

    public void onSaveGameClick(View view)
    {
        editTextTeam1Goals = (EditText) findViewById(R.id.editTextTeam1Goals);
        editTextTeam2Goals = (EditText) findViewById(R.id.editTextTeam2Goals);
        int team1Goals;
        int team2Goals;
        try
        {
            team1Goals = Integer.valueOf(editTextTeam1Goals.getText().toString());
            team2Goals = Integer.valueOf(editTextTeam2Goals.getText().toString());
        } catch (Exception e)
        {
            Toast.makeText(this, getResources().getString(R.string.wrongGameCount), Toast.LENGTH_SHORT).show();
            return;
        }

        db = dbhelper.getWritableDatabase();
        if (strTeam1.equals(strTeam2))
        {
            Toast.makeText(this, getResources().getString(R.string.selectDifferentTeams), Toast.LENGTH_LONG).show();
            return;
        }

        //Проверка на наличие такой же игры
        Cursor c = db.rawQuery("SELECT first_team, second_team FROM Games WHERE ((first_team = '" + strTeam1 + "" +
                "' AND second_team = '" + strTeam2 + "')" +
                " OR (first_team = '" + strTeam2 + "' AND second_team = '" + strTeam1 + "'))", null);
        if (c.getCount() < 1)
        {
            Toast.makeText(this, getResources().getString(R.string.noSuchGame), Toast.LENGTH_SHORT).show();
            //Log.d("TAG", "Нет такой игры, добавляем в базу");
            String insertQuery = "INSERT INTO games (first_team, second_team, first_team_score, second_team_score)" +
                    " VALUES ('" + strTeam1 + "', '" + strTeam2 + "', " + team1Goals + ", " + team2Goals + ")";
            db.execSQL(insertQuery);

            editTextTeam1Goals.setText("");
            editTextTeam2Goals.setText("");
        } else
        {
            Toast.makeText(this, getResources().getString(R.string.updateGame), Toast.LENGTH_SHORT).show();
            //Log.d("TAG", "Есть такая игра, запрос о обновлении");
            overrideGame();
        }

        c.close();
        //Пересчет очков после добавления игры
        Team team1 = getTeamFromDB(strTeam1);
        Team team2 = getTeamFromDB(strTeam2);

        team1.addGame(team1Goals, team2Goals);
        team2.addGame(team2Goals, team1Goals);

        updateTeamScore(team1);
        updateTeamScore(team2);
    }

    /**
     * Обновление команды в базе
     *
     * @param team - объект типа Team
     */

    private void updateTeamScore(Team team)
    {
        String query = "UPDATE teams" +
                " SET total_games = " + team.getTotalGames() +
                ", win = " + team.getWin() +
                ", draw = " + team.getDraw() +
                ", loss = " + team.getLoss() +
                ", goals_out = " + team.getGoalsOut() +
                " ,goals_in = " + team.getGoalsIn() +
                " ,total = " + team.getTotal() +
                " WHERE Name = '" + team.getName() + "'";

        db = dbhelper.getWritableDatabase();
        db.execSQL(query);
    }

    /**
     * Загрузка по имени из базу и создание объекта типа Team
     *
     * @param strTeam - имя команды
     * @return объект типа Team
     */

    private Team getTeamFromDB(String strTeam)
    {
        Team team = null;
        String query = "SELECT * FROM teams WHERE Name='" + strTeam + "'";
        db = dbhelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst())
        {
            int nameColIndex = c.getColumnIndex("Name");
            int totalGamesColIndex = c.getColumnIndex("total_games");
            int winColIndex = c.getColumnIndex("win");
            int drawColIndex = c.getColumnIndex("draw");
            int lossColIndex = c.getColumnIndex("loss");
            int goalsOutColIndex = c.getColumnIndex("goals_out");
            int goalsInColIndex = c.getColumnIndex("goals_in");
            int totalIndex = c.getColumnIndex("total");

            String name = c.getString(nameColIndex);
            int totalGames = c.getInt(totalGamesColIndex);
            int win = c.getInt(winColIndex);
            int draw = c.getInt(drawColIndex);
            int loss = c.getInt(lossColIndex);
            int goalsOut = c.getInt(goalsOutColIndex);
            int goalsIn = c.getInt(goalsInColIndex);
            int total = c.getInt(totalIndex);

            team = new Team(name, totalGames, win, draw, loss, goalsOut, goalsIn, total);
        }
        c.close();
        return team;
    }

    public void overrideGame()
    {
        FragmentManager manager = getSupportFragmentManager();
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(manager, "dialog");
    }

    public void onLoadRatingClick(View view)
    {
        teamList.clear();
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        String query = "SELECT * FROM teams ORDER BY total DESC, goals_out - goals_in DESC";
        db = dbhelper.getWritableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst())
        {
            do
            {
                int nameColIndex = c.getColumnIndex("Name");
                int pictureIndex = c.getColumnIndex("picture");
                int totalGamesColIndex = c.getColumnIndex("total_games");
                int winColIndex = c.getColumnIndex("win");
                int drawColIndex = c.getColumnIndex("draw");
                int lossColIndex = c.getColumnIndex("loss");
                int goalsOutColIndex = c.getColumnIndex("goals_out");
                int goalsInColIndex = c.getColumnIndex("goals_in");
                int totalIndex = c.getColumnIndex("total");

                String name = c.getString(nameColIndex);

                byte[] imgByte = c.getBlob(pictureIndex);
                Bitmap pictureTmp = picture;
                if (imgByte != null)
                {
                    pictureTmp = getImage(imgByte);
                }
                int totalGames = c.getInt(totalGamesColIndex);
                int win = c.getInt(winColIndex);
                int draw = c.getInt(drawColIndex);
                int loss = c.getInt(lossColIndex);
                int goalsOut = c.getInt(goalsOutColIndex);
                int goalsIn = c.getInt(goalsInColIndex);
                int total = c.getInt(totalIndex);

                teamList.add(new Team(name, pictureTmp, totalGames, win, draw, loss, goalsOut, goalsIn, total));
            }
            while (c.moveToNext());
        }
        c.close();

        Team[] arrTeams = teamList.toArray(new Team[teamList.size()]);
        ListView list = (ListView) findViewById(R.id.listView);
        LazyAdapter adapter = new LazyAdapter(this, arrTeams);
        list.setAdapter(adapter);

    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectItem(position);
        }
    }
}
