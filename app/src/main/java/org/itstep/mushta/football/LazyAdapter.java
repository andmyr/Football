package org.itstep.mushta.football;

/**
 * Created by And on 06.08.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter
{
    private Activity activity;
    private Team[] data;
    private static LayoutInflater inflater = null;


    public LazyAdapter(Activity a, Team[] d)
    {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount()
    {
        return data.length;
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public static class ViewHolder
    {
        public TextView textCounter;
        public TextView textName;
        public TextView textTotalGames;
        public TextView textWin;
        public TextView textDraw;
        public TextView textLoss;
        public TextView textGoals;
        public TextView textTotal;
        public ImageView image;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        ViewHolder holder;
        if (convertView == null)
        {
            vi = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.textCounter = (TextView) vi.findViewById(R.id.textViewCounter);
            holder.textName = (TextView) vi.findViewById(R.id.textViewName);
            holder.textTotalGames = (TextView) vi.findViewById(R.id.textViewTotalGames);
            holder.textWin = (TextView) vi.findViewById(R.id.textViewWin);
            holder.textDraw = (TextView) vi.findViewById(R.id.textViewDraw);
            holder.textLoss = (TextView) vi.findViewById(R.id.textViewLoss);
            holder.textGoals = (TextView) vi.findViewById(R.id.textViewGoals);
            holder.textTotal = (TextView) vi.findViewById(R.id.textViewTotal);

            holder.image = (ImageView) vi.findViewById(R.id.imageViewIcon);
            vi.setTag(holder);
        } else
        {
            holder = (ViewHolder) vi.getTag();
        }

        holder.textCounter.setText((position + 1) + ".");
        //MainActivity.intCounter++;
        holder.textName.setText(data[position].getName() + "");
        holder.textTotalGames.setText(data[position].getTotalGames() + "");
        holder.textWin.setText(data[position].getWin() + "");
        holder.textDraw.setText(data[position].getDraw() + "");
        holder.textLoss.setText(data[position].getLoss() + "");
        holder.textGoals.setText(data[position].getGoalsOut() + " - " + data[position].getGoalsIn() + "");
        holder.textTotal.setText(data[position].getTotal() + "");

        holder.image.setImageBitmap(data[position].getBitmap());
        return vi;
    }
}