package me.mikolajt.jakoscpowietrza;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;


public class SensorDataManager {

    private final Activity activity;
    private final TextView value1;
    private final TextView value2;
    private final TextView value3;
    private final TextView value4;

    private final static String OK_HEX = "#12F536";
    private final static String WARNING_HEX = "#FCA600";
    private final static String BAD_HEX = "#FF0000";

    private List<SensorDataType> currentRed;

    private LineChart chart;

    private int number;

    public SensorDataManager(Activity activity){
        this.activity = activity;
        this.value1 = activity.findViewById(R.id.value1);
        this.value2 = activity.findViewById(R.id.value2);
        this.value3 = activity.findViewById(R.id.value3);
        this.value4 = activity.findViewById(R.id.value4);
        this.currentRed = new ArrayList<>();
        this.chart =  activity.findViewById(R.id.chart);
    }

    public void updateData(SensorDataType dataType, int value){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(value).append(" ").append(dataType.unit);
            TextView textView = getTextViewByDataType(dataType);
            textView.setText(sb.toString());
            setColorBasedOnSensorValue(textView, dataType, value);
            manageBigAlertBasedOnSensorValues(activity, dataType, value);
            updateGraph(dataType, value);
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }
    }

    private TextView getTextViewByDataType(SensorDataType dataType){
        switch(dataType){
            case CO2:
                return value1;
            case TEMPERATURE:
                return value2;
            case AIR_PRESSURE:
                return value3;
            case HUMIDITY:
                return value4;
            default:
                return null;
        }
    }

    private void setColorBasedOnSensorValue(TextView textView, SensorDataType dataType, int value){
        if(value <= (0.75*dataType.upperLimit)){
            textView.setTextColor(Color.parseColor(OK_HEX));
        }else if(value > (0.75*dataType.upperLimit) && value <= dataType.upperLimit){
            textView.setTextColor(Color.parseColor(WARNING_HEX));
        }else{
            textView.setTextColor(Color.parseColor(BAD_HEX));
        }
    }

    private void manageBigAlertBasedOnSensorValues(Activity activity, SensorDataType dataType, int value){
        if(value > dataType.upperLimit) {
            if(currentRed.size() == 0){
                activity.findViewById(R.id.bigalert).setVisibility(View.VISIBLE);
            }
            if(!currentRed.contains(dataType)) {
                currentRed.add(dataType);
            }
        }else{
            if(currentRed.size() > 0){
                if(currentRed.contains(dataType)){
                    currentRed.remove(dataType);
                }
                if(currentRed.size() == 0){
                    activity.findViewById(R.id.bigalert).setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private void updateGraph(SensorDataType dataType, int value){
        if(dataType == SensorDataType.CO2){
            if(this.chart.getLineData() != null) {
                LineData ld = this.chart.getLineData();
                ld.addEntry(new Entry(number, value), 0);
                chart.setData(ld);
                chart.invalidate();
                number++;
            }else{
                List<Entry> entries = new ArrayList<>();
                entries.add(new Entry(0, value));
                LineDataSet lds = new LineDataSet(entries, "Stężenie CO2");
                LineData ld = new LineData(lds);
                chart.setData(ld);
                chart.invalidate();
                number++;
            }
        }
    }
}
