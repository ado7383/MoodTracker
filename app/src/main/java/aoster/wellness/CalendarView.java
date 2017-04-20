package aoster.wellness;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Alyssa Oster
 */

public class CalendarView extends LinearLayout {

    //Internal components for the layout
    private static final int TOTAL_DAYS = 42;
    private static final CharSequence[] RATING = {"1","2","3","4","5","6","7","8","9","10"};

    private RelativeLayout header;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private TextView month;
    private LinearLayout week;
    private GridView calendar;
    private Calendar currentDate = Calendar.getInstance();

    private int placeholder;

    /**
     * Constructor for Calendar class
     * @param context
     */
    public CalendarView(Context context) {
        super(context);
        init(context);
    }

    /**
     * Initialize the calendar and set all the variables to their component in the XML file
     *
     * @param context
     */
    public void init(Context context){

        LayoutInflater inflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.calendar_layout, this);

        header = (RelativeLayout) findViewById(R.id.rl_date_bar);
        previousButton = (ImageButton) findViewById(R.id.prev_button);
        nextButton = (ImageButton) findViewById(R.id.next_button);
        month = (TextView) findViewById(R.id.tv_month);
        week = (LinearLayout) findViewById(R.id.days_of_the_week);
        calendar = (GridView) findViewById(R.id.gv_calendar);
        currentDate = Calendar.getInstance();

        assignOnClickListeners();
        fillCalendar();
    }

    /**
     * Fill in the calendar with the proper month and days of the week
     * Sunday starts the week at cell 0, Saturday ends the week at cell 6
     * Populate an Arraylist with the dates of the current month
     */
    public void fillCalendar(){
        ArrayList<Date> cells = new ArrayList<>();
        Calendar c = (Calendar) currentDate.clone();

        c.set(Calendar.DAY_OF_MONTH,1);
        int startWeekday = c.get(Calendar.DAY_OF_WEEK) - 1;

        //Change the starting day to the start of the calendar. Will subtract how many days of the
        //week come before the start
        c.add(Calendar.DAY_OF_MONTH, -startWeekday);

        while(cells.size() < TOTAL_DAYS){
            cells.add(c.getTime());
            c.add(c.DAY_OF_MONTH, 1);
        }

        calendar.setAdapter(new CalendarAdapter(getContext(), cells));

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM YYYY");
        month.setText(month_date.format(currentDate.getTime()));

    }

    /**
     * Initialize the onClickListeners for the various buttons on calendar
     * Previous button will move to the last month's calendar
     * Next button will move to the next month's calendar
     */
    public void assignOnClickListeners(){

        previousButton.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, -1);
                fillCalendar();
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate.add(Calendar.MONTH, 1);
                fillCalendar();
            }
        });

        calendar.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> view, View cell, int position, long id) {

                String day = String.valueOf(((TextView)cell).getText());
                String monthString = month.getText().toString();
                String yearString = monthString.substring(monthString.length()-4);
                monthString = monthString.substring(0, monthString.length()-5);
                String totalDate = day + "-" + monthString + "-" + yearString;

                promptInput(totalDate, cell);

                return true;
            }
        });
    }

    /**
     * Setup the popup menu for users to store their data
     */
    public void promptInput(final String dateFormat, final View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Rate mood");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Store the users input in moods with the key being the date set as string dd-mm-YYYY
                SharedPreferences.Editor editor = MainActivity.moods.edit();
                editor.putInt(dateFormat, placeholder);
                editor.commit();

                int data = MainActivity.moods.getInt(dateFormat, 0);

                ((TextView)view).setTextColor(Color.rgb(187, 142, 206));

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //Ugly but functions -- TODO change this
        builder.setSingleChoiceItems(RATING, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                placeholder = which+1;
            }
        });


        AlertDialog d = builder.create();
        d.show();
    }
}
