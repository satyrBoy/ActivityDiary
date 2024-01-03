/*
 * ActivityDiary
 *
 * Copyright (C) 2023 Raphael Mack http://www.raphael-mack.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package de.rampro.activitydiary.ui.generic;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

import de.rampro.activitydiary.R;

public class TimerActivity extends BaseActivity {

    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private NumberPicker secondPicker;
    private Button startButton;
    private Button stopButton;

    private Button continueButton;
    private Button resetButton;

    private long totalMilliseconds;

    private boolean isPaused = false;

    private long remainingTime = 0;

    private long elapsedTime = 0;
    private TextView timer;

    private CountDownTimer countDownTimer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_timer, null, false);
        setContent(contentView);
        TextView timerText = (TextView)findViewById(R.id.timerTextView);

        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        secondPicker = findViewById(R.id.secondPicker);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        continueButton = findViewById(R.id.continueButton);
        resetButton = findViewById(R.id.resetButton);
        timer = findViewById(R.id.timer);

        // 设置滚动选择器的范围
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(59);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    return;
                }
                startTimer();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueTimer();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(false);
    }

    private void startTimer() {
        // 获取滚动选择器的值
        int hours = hourPicker.getValue();
        int minutes = minutePicker.getValue();
        int seconds = secondPicker.getValue();

        if (remainingTime > 0) totalMilliseconds = remainingTime;
        else totalMilliseconds = (hours * 60 * 60 + minutes * 60 + seconds) * 1000;

        countDownTimer = new CountDownTimer(totalMilliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime = millisUntilFinished;
                if (isPaused) {

                    return;
                }
                // 更新倒计时文本
                long hours = millisUntilFinished / (60 * 60 * 1000);
                long minutes = (millisUntilFinished % (60 * 60 * 1000)) / (60 * 1000);
                long seconds = (millisUntilFinished % (60 * 1000)) / 1000;

                String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                timer.setText(timeLeft);
            }
            @Override
            public void onFinish() {
                // 倒计时完成时执行的操作
                timer.setText("00:00:00");
            }
        };

        countDownTimer.start();

        }


    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isPaused = true;
        }
    }

    private void continueTimer() {
        isPaused = false;
        remainingTime = elapsedTime;
        startTimer();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        isPaused = false;
        remainingTime = 0;
        elapsedTime = 0;
        hourPicker.setValue(0);
        minutePicker.setValue(0);
        secondPicker.setValue(0);
        timer.setText("00:00:00");
    }

}
