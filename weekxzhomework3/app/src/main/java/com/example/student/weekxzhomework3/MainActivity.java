package com.example.student.weekxzhomework3;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler handler;
    private int editValue;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.input_time);
        goToThread();
    }

    private void goToThread() {

        final Button button = findViewById(R.id.go_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,
                            getString(R.string.incorrect_argument), Toast.LENGTH_SHORT).show();
                    return;
                }
                editValue = Integer.parseInt(editText.getText().toString());
                new SleepThread().start();
            }
        });
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onPostResume() {
        final ProgressBar progressBar = findViewById(R.id.progress);
        final TextView textView = findViewById(R.id.message_time);
        super.onPostResume();
        handler = new Handler(){
            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0){
                    if (msg.arg1 == 0){
                        progressBar.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(getString(R.string.wait) + String.valueOf(editValue) + getString(R.string.seconds));
                    }
                    if (msg.arg1 == (editValue - 1)){
                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
    }

    private class SleepThread extends Thread{
        @Override
        public void run() {
            for (int i = 0; i < editValue; i++) {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(editValue * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what = 0;
                message.arg1 = i;
                handler.sendMessage(message);
            }
        }
    }
}
