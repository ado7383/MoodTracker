package aoster.wellness;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.text.DateFormatSymbols;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Alyssa Oster
 */

public class CalendarAdapter extends ArrayAdapter<Date>{

    public LayoutInflater inflater;

    /**
     * Constructor for CalendarAdapter. Initializes inflater and calls super
     * @param context
     * @param days - Arraylist of all the days to put into the calendar
     */
    public CalendarAdapter(Context context, ArrayList<Date> days){
        super(context, R.layout.calendar_day, days);
        try{
            inflater = LayoutInflater.from(context);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Override of getView method. Create displays for all days of the month in the calendar view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try{
            //Gather all data for the start day
            Date date = getItem(position);
            Calendar c = Calendar.getInstance();
            int day = date.getDate();
            int month = date.getMonth();
            int year = date.getYear();

            //Get today
            Date today = new Date();


            String monthString = new DateFormatSymbols().getMonths()[month];
            String totalString = String.valueOf(day) + "-" + monthString + "-" + String.valueOf(year + 1900);


            //Inflate XML if it isn't
            if(convertView == null)
                convertView = inflater.inflate(R.layout.calendar_day, parent, false);


            int mood_for_day = MainActivity.moods.getInt(totalString, 0);

            ((TextView) convertView).setTextColor(Color.BLACK);

            if(mood_for_day != 0 ){
                ((TextView)convertView).setTextColor(Color.rgb(187, 142, 206));
            }if(month != today.getMonth() || year != today.getYear()){
                ((TextView)convertView).setTextColor(Color.GRAY);
            } else if(day == today.getDate()){
                ((TextView)convertView).setTypeface(null, Typeface.BOLD);
                ((TextView)convertView).setTextColor(Color.rgb(153, 153, 255));
            }

            ((TextView)convertView).setText(String.valueOf(day));

            return convertView;

        }catch(Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
