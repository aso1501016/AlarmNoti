package jp.ac.asojuku.st.alarmnoti;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private InputMethodManager mInputMethodManager;
    private RelativeLayout mLayout;
    private int notificationId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText edText = (EditText) findViewById(R.id.editText);
        mLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mInputMethodManager
                = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        Button btnSet = (Button)findViewById(R.id.set);
        btnSet.setOnClickListener(this);
        Button btnCancel = (Button)findViewById(R.id.cancel);
        btnCancel.setOnClickListener(this);
        //トーストを使って表示させる
        /*ここ→*/Toast.makeText(MainActivity.this, "課題内容です", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mInputMethodManager.hideSoftInputFromWindow(mLayout.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        mLayout.requestFocus();
        return false;
    }

    @Override
    public void onClick(View v) {

        EditText edText = (EditText) findViewById(R.id.editText);
        // AlarmReceiver を呼び出すインテント
        Intent bootIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        // 追加データとして、Notificationの識別子を渡す
        bootIntent.putExtra("notificationId", notificationId);
        // 追加データとして、やることを渡す
        bootIntent.putExtra("todo", edText.getText());

        PendingIntent alarmIntent
                = PendingIntent.getBroadcast(MainActivity.this, 0, bootIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        TimePicker tPicker = (TimePicker)findViewById(R.id.timePicker);

        switch (v.getId()){
            case R.id.set:
                int hour = tPicker.getCurrentHour();
                int minute = tPicker.getCurrentMinute();

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY, hour);
                startTime.set(Calendar.MINUTE, minute);
                startTime.set(Calendar.SECOND, 0);
                long alarmStartTime = startTime.getTimeInMillis();

                alarm.set(
                        AlarmManager.RTC_WAKEUP,
                        alarmStartTime,
                        alarmIntent
                );
                Toast.makeText(MainActivity.this, "通知をセットしました！", Toast.LENGTH_SHORT).show();
                notificationId++;

                break;
            case R.id.cancel:
                alarm.cancel(alarmIntent);
                Toast.makeText(MainActivity.this, "通知をキャンセルしました！", Toast.LENGTH_SHORT).show();

        }
    }
}
