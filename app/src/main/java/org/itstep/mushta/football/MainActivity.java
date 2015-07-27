package org.itstep.mushta.football;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
{
    final int CAMERA_CAPTURE = 1;
    final int PIC_CROP = 2;
    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DBHelper dbhelper;
    private Uri picUri;
    private EditText editTextTeam1Goals;
    private EditText editTextTeam2Goals;
    private ArrayList<String> teamList = new ArrayList<>();
    private String team1;
    private String team2;
    public Spinner spinner;
    public Spinner spinner2;
    private SQLiteDatabase db;


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
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
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
                        teamList.add(str);
                    }
                    Log.d("TAG", str);

                } while (c.moveToNext());
            }
            c.close();
        }
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        // адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamList);
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
                team1 = teamList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                team1 = teamList.get(0);
            }
        });

        // адаптер
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teamList);
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
                team2 = teamList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                team2 = teamList.get(0);
            }
        });
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
            Log.e(this.getClass().getName(), "Error. Fragment is not created");
        }
    }

    @Override
    public void setTitle(CharSequence title)
    {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

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
        boolean dBEmpty = true;
        if (c.getCount() < 1)
        {
            dBEmpty = true;
        } else
        {
            dBEmpty = false;
        }
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
            long rowID = db.insert("teams", null, contentValues);
            Log.d("TAG", "Вставили " + rowID);
            editTextTeamName.setText("");
        } else
        {
            Log.d("TAG", "Команда уже есть в списке.");
            Toast.makeText(this, "Команда уже есть в списке.", Toast.LENGTH_LONG).show();
        }
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
            String errorMessage = "Ваше устройство не поддерживает съемку";
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
                // Получим Uri снимка
                picUri = data.getData();
                // кадрируем его
                performCrop();
            }
            // Вернулись из операции кадрирования
            else if (requestCode == PIC_CROP)
            {
                Bundle extras = data.getExtras();
                // Получим кадрированное изображение
                Bitmap thePic = extras.getParcelable("data");
                // передаём его в ImageView
                ImageView picView = (ImageView) findViewById(R.id.imageView);
                picView.setImageBitmap(thePic);
            }
        }
    }

    private void performCrop()
    {
        try
        {
            // Намерение для кадрирования. Не все устройства поддерживают его
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe)
        {
            String errorMessage = "Извините, но ваше устройство не поддерживает кадрирование";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    //Добавление картинки в базу
   /* public void insertImg(int id , Bitmap img ) {


        byte[] data = getBitmapAsByteArray(img); // this is a function

        insertStatement_logo.bindLong(1, id);
        insertStatement_logo.bindBlob(2, data);

        insertStatement_logo.executeInsert();
        insertStatement_logo.clearBindings() ;

    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


    //Получение картинки из базы
    public Bitmap getImage(int i){

        String qu = "select img  from table where feedid=" + i ;
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()){
            byte[] imgByte = cur.getBlob(0);
            cur.close();
            return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }

        return null ;
    }*/


    public void onSaveGameClick(View view)
    {
        editTextTeam1Goals = (EditText) findViewById(R.id.editTextTeam1Goals);
        editTextTeam2Goals = (EditText) findViewById(R.id.editTextTeam2Goals);

        /*//проверка на пустоту
        if (editTextTeam1.length() < 1)
        {
            Toast.makeText(this, "Введите название первой команды", Toast.LENGTH_SHORT);
            return;
        }
        if (editTextTeam2.length() < 1)
        {
            Toast.makeText(this, "Введите название второй команды", Toast.LENGTH_SHORT);
            return;
        }
        if (editTextTeam1Goals.length() < 1)
        {
            Toast.makeText(this, "Введите число голов первой команды", Toast.LENGTH_SHORT);
            return;
        }
        if (editTextTeam2Goals.length() < 1)
        {
            Toast.makeText(this, "Введите число голов второй команды", Toast.LENGTH_SHORT);
            return;
        }*/
        int team1Goals = 0;
        int team2Goals = 0;
        try
        {
            team1Goals = Integer.valueOf(editTextTeam1Goals.getText().toString());
            team2Goals = Integer.valueOf(editTextTeam2Goals.getText().toString());
        } catch (Exception e)
        {
            Toast.makeText(this, "Неверный формат счета игры", Toast.LENGTH_SHORT);
        }

        db = dbhelper.getWritableDatabase();
        if (team1.equals(team2))
        {
            Toast.makeText(this, "Выберете разные команды!", Toast.LENGTH_LONG);
            return;
        }

        //Проверка на наличие такой же игры
        Cursor c = db.rawQuery("SELECT first_team, second_team FROM Games WHERE ((first_team = '" + team1 + "' AND second_team = '" + team2 + "')" +
                " OR (first_team = '" + team2 + "' AND second_team = '" + team1 + "'))", null);
        if (c.getCount() < 1)
        {
            Toast.makeText(this, "Нет такой игры, добавляем!.", Toast.LENGTH_SHORT);
            Log.d("TAG", "Нет такой игры, добавляем в базу");
            String insertQuery = "INSERT INTO games (first_team, second_team, first_team_score, second_team_score)" +
                    " VALUES (" + team1 + ", " + team2 + ", " + team1Goals + ", " + team2Goals + ")";
            db.execSQL(insertQuery);

            editTextTeam1Goals.setText("");
            editTextTeam2Goals.setText("");
        } else
        {
            Toast.makeText(this, "Есть такая игра, обновляем!.", Toast.LENGTH_SHORT);
            Log.d("TAG", "Есть такая игра, запрос о обновлении");
            overrideGame();
        }

        //TODO Пересчет очков после добавления игры
    }

    public void overrideGame()
    {
        FragmentManager manager = getSupportFragmentManager();
        MyDialogFragment myDialogFragment = new MyDialogFragment();
        myDialogFragment.show(manager, "dialog");
    }

}
