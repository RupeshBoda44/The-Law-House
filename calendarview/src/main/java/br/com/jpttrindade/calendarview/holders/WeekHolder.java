package br.com.jpttrindade.calendarview.holders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by jpttrindade on 09/09/16.
 */
public class WeekHolder extends RecyclerView.ViewHolder {
    public TextView[] daysView = new TextView[7];

    public WeekHolder(View itemView) {
        super(itemView);

        for (int i=0; i<7; i++) {
            daysView[i] = (TextView) ((ViewGroup)itemView).getChildAt(i);
        }
    }
}
