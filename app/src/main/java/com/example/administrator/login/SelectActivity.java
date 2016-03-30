package com.example.administrator.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-3-25.
 */
public class SelectActivity extends AppCompatActivity {

    final String[] time = new String[1];
    CalendarView cv;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selecttime);
        cv = (CalendarView) findViewById(R.id.calendarView);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                month = month +1;
                time[0] = year + "年" + month+ "月" + dayOfMonth + "日";
//                Toast.makeText(SelectActivity.this, time[0], Toast.LENGTH_SHORT).show();
                Intent intent =new Intent();
                intent.putExtra("time", time[0]);
                setResult(20, intent);//设置返回数据
                finish();//关闭activity
            }
        });
    }
}
