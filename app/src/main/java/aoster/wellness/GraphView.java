package aoster.wellness;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.DateFormatSymbols;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * @author Alyssa Oster
 */

public class GraphView extends LinearLayout{

    public LayoutInflater inflater;
    public LineDataSet curve;

    public GraphView(Context context) {
        super(context);
        try{
            inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.graph_layout, this);
            populateScroll();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Go through the past 30 days and get points for each day that has data on the users mood
     * Populate a scroll with a graph
     */
    public void populateScroll(){
        ArrayList<Calendar> dates = new ArrayList<>();
        ArrayList<Integer> mood = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        String dataString = getDataString(c);

        ScrollView scroll = (ScrollView) findViewById(R.id.scroll);

        for(int i = 0; i < 30; i++){
            TextView text = new TextView(getContext());
            text.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                    LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dates.add((Calendar)c.clone());
            mood.add(getData(dataString));

            c.add(Calendar.DATE, -1);
            dataString = getDataString(c);
        }
        scroll.addView(createChart(dates, mood));
        inflater.inflate(R.layout.graph_layout, this);

    }

    /**
     * Create a LineChart with the given information and set up the LineChart with a set YAxix
     * of 0 to 10
     * @param x - ArrayList of x-values to be plot
     * @param y - ArrayList of y-values to be plot
     * @return
     */
    private LineChart createChart(ArrayList<Calendar> x, ArrayList<Integer> y){
        LineChart line = new LineChart(getContext());
        line.setData(createGraphData(x, y));
        line.getAxisLeft().setAxisMinValue(0);
        line.getAxisLeft().setAxisMaxValue(10);
        line.getAxisRight().setAxisMinValue(0);
        line.getAxisRight().setAxisMaxValue(10);
        line.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        line.setDescription("");
        line.setMinimumHeight(1000);

        return line;
    }

    public LineData createGraphData(ArrayList<Calendar> xVal, ArrayList<Integer> yVal){
        ArrayList<Entry> data = new ArrayList<>();
        ArrayList<String> x = new ArrayList<>();
        for(int i = xVal.size()-1; i >= 0; i--){
             if(yVal.get(i)!=0)
                data.add(new Entry(yVal.get(i), xVal.size()-i));
            x.add(String.valueOf(xVal.get(i).get(Calendar.MONTH)+1 + "/" + xVal.get(i).get(Calendar.DAY_OF_MONTH)) );
        }
        Collections.reverse(data);
        curve = new LineDataSet(data, "Mood");
        curve.setColor(Color.RED);
        curve.setCircleColor(Color.RED);
        curve.setDrawValues(false);

        LineData lineData = new LineData(x, curve);

        return lineData;

    }

    private String getDataString(Calendar c){
        int day = c.get(c.DAY_OF_MONTH);
        int month = c.get(c.MONTH);
        int year = c.get(c.YEAR);

        String monthString = new DateFormatSymbols().getMonths()[month];
        String totalString = String.valueOf(day) + "-" + monthString + "-" + String.valueOf(year);
        return totalString;
    }

    private int getData(String s){
        return MainActivity.moods.getInt(s, 0);
    }
}
