package com.example.esp32controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ControllerActivity extends AppCompatActivity {
    public static String address = "";
    public static String ip = "";
    public static int port = 0;
    public static String message = "";
    public static String temp = "";
    public static Boolean is_auto_mode = false;

    private final int REQUEST_SPEECH_RECOGNIZER = 3000;
    private final int STOP_SPEECH_RECOGNIZER = -3000;
    private TextView mTextView;
    private final String mQuestion = "Which company is the largest online retailer on the planet?";
    private String mAnswer = "";

    Button b1_on, b1_off, b2_on, b2_off, fan_on, fan_off, h_on, h_off, voice_control;
    Switch auto_temp;
    TextView tv_address, tv_temp, tv_humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        if (getIntent().hasExtra("address")) {
            address = getIntent().getStringExtra("address");
            String list[] = address.split(":");
            ip = list[0];
            port = Integer.valueOf(list[1]);
        }



        tv_address = findViewById(R.id.address_id);
        tv_temp = findViewById(R.id.tv_temp_id);
        tv_humidity = findViewById(R.id.tv_humidity_id);

        b1_on = findViewById(R.id.b1_on_id);
        b1_off = findViewById(R.id.b1_off_id);
        b2_on = findViewById(R.id.b2_on_id);
        b2_off = findViewById(R.id.b2_off_id);
        fan_on = findViewById(R.id.fan_on_id);
        fan_off = findViewById(R.id.fan_off_id);
        h_on = findViewById(R.id.h_on_id);
        h_off = findViewById(R.id.h_off_id);
        auto_temp = findViewById(R.id.auto_temp_id);
        voice_control = findViewById(R.id.voice_ctrl_id);

        tv_address.setText(address);

        b1_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "1";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        b1_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "0";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        b2_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "3";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        b2_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "2";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        fan_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "5";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        fan_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "4";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        h_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "7";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        h_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "6";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        auto_temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if (isOn) {
                    // The toggle is enabled
                    is_auto_mode = true;
                    message = "a";
                    SockedTransfer socket = new SockedTransfer();
                    socket.execute();

                    fan_on.setEnabled(false);
                    fan_off.setEnabled(false);
                    h_on.setEnabled(false);
                    h_off.setEnabled(false);
                    fan_on.setBackgroundColor(Color.GRAY);
                    fan_off.setBackgroundColor(Color.GRAY);
                    h_on.setBackgroundColor(Color.GRAY);
                    h_off.setBackgroundColor(Color.GRAY);
                } else {
                    // The toggle is disabled
                    is_auto_mode = false;
                    tv_temp.setText("");
                    message = "m";
                    SockedTransfer socket = new SockedTransfer();
                    socket.execute();

                    fan_on.setEnabled(true);
                    fan_off.setEnabled(true);
                    h_on.setEnabled(true);
                    h_off.setEnabled(true);
                    fan_on.setBackgroundColor(getResources().getColor(R.color.blue_p));
                    fan_off.setBackgroundColor(getResources().getColor(R.color.red_off));
                    h_on.setBackgroundColor(getResources().getColor(R.color.blue_p));
                    h_off.setBackgroundColor(getResources().getColor(R.color.red_off));
                }
            }
        });

        voice_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSpeechRecognizer();
            }
        });
    }

    public void exit() {
        System.exit(0);
    }

    public class SockedTransfer extends AsyncTask<Void, Void, Void> {

        Socket socket;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InetAddress inet = InetAddress.getByName(ip);
                socket = new java.net.Socket(inet, port);
                DataOutputStream stream = new DataOutputStream(socket.getOutputStream());
                stream.writeBytes(message);
                stream.close();
                socket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class SocketReader extends AsyncTask<Void, Void, Void> {

        Socket socket;

        @Override
        protected Void doInBackground(Void... voids) {
            if (is_auto_mode) {
                try {
                    InetAddress inet = InetAddress.getByName(ip);
                    socket = new java.net.Socket();
                    DataInputStream inStream = new DataInputStream(socket.getInputStream());
                    temp = String.valueOf(inStream.read());
                    System.out.println("-----------------------");
                    System.out.println(temp);
                    tv_temp.setText("Temperature: " + temp + " C");
                    inStream.close();
                    socket.close();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private void startSpeechRecognizer() {
        Intent intent = new Intent
                (RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en_US");
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == RESULT_OK) {
                List<String> results = data.getStringArrayListExtra
                        (RecognizerIntent.EXTRA_RESULTS);
                mAnswer = results.get(0).toLowerCase();
                if (!auto_temp.isChecked()){
                    if (mAnswer.contains("on") && mAnswer.contains("fan")){
                        tv_humidity.setText("fan on");
                        message = "5";
                        SockedTransfer socket = new SockedTransfer();
                        socket.execute();
                    }
                    else if (mAnswer.contains("off") && mAnswer.contains("fan")){
                        tv_humidity.setText("fan off");
                        message = "4";
                        SockedTransfer socket = new SockedTransfer();
                        socket.execute();
                    }
                    else if (mAnswer.contains("on") && mAnswer.contains("heater")){
                        tv_humidity.setText("heater on");
                        message = "7";
                        SockedTransfer socket = new SockedTransfer();
                        socket.execute();
                    }
                    else if (mAnswer.contains("off") && mAnswer.contains("heater")){
                        tv_humidity.setText("heater off");
                        message = "6";
                        SockedTransfer socket = new SockedTransfer();
                        socket.execute();
                    }
                }
                if (mAnswer.contains("on") && mAnswer.contains("bulb") && (mAnswer.contains("1") || mAnswer.contains("one"))){
                    tv_humidity.setText("b1 on");
                    message = "1";
                    SockedTransfer socket = new SockedTransfer();
                    socket.execute();
                }
                else if (mAnswer.contains("off") && mAnswer.contains("bulb") && (mAnswer.contains("1") || mAnswer.contains("one"))){
                    tv_humidity.setText("b1 off");
                    message = "0";
                    SockedTransfer socket = new SockedTransfer();
                    socket.execute();
                }
                else if (mAnswer.contains("on") && mAnswer.contains("bulb") && (mAnswer.contains("2") || mAnswer.contains("two"))){
                    tv_humidity.setText("b2 on");
                    message = "3";
                    SockedTransfer socket = new SockedTransfer();
                    socket.execute();
                }
                else if (mAnswer.contains("off") && mAnswer.contains("bulb") && (mAnswer.contains("2") || mAnswer.contains("two"))){
                    tv_humidity.setText("b2 off");
                    message = "2";
                    SockedTransfer socket = new SockedTransfer();
                    socket.execute();
                }
//                tv_humidity.setText(mAnswer);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        voice_control.setChecked(false);
    }
}