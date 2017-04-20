package aoster.wellness;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarV;
    private GraphView graphV;
    private LinearLayout layout;

    public static SharedPreferences moods;

    protected static final String MOODS_NAME = "MoodFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        moods = getApplicationContext().getSharedPreferences(MOODS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = moods.edit();
        ed.clear();
        ed.commit();
        setContentView(R.layout.activity_main);
        calendarV = new CalendarView(this);
        graphV = new GraphView(this);
        final Context c = this;

        final Button calendar = (Button)findViewById(R.id.calendar_button);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getChildAt(1).equals(graphV)){
                    layout.removeView(graphV);
                    layout.addView(calendarV);
                }
            }
        });

        Button graph = (Button)findViewById(R.id.graph_button);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layout.getChildAt(1).equals(calendarV)){
                    layout.removeView(calendarV);
                    graphV = new GraphView(c);
                    layout.addView(graphV);
                }
            }
        });

        layout = (LinearLayout) findViewById(R.id.activity_main);
        layout.addView(calendarV);
    }


}
