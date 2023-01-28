package com.example.esp32controller;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ControllerActivity extends AppCompatActivity {
    public static String address="";
    public static String ip="";
    public static int port=0;
    public static String message="";

    Button b1_on, b1_off, b2_on, b2_off, fan_on, fan_off, h_on, h_off;
    Switch auto_temp;
    TextView tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        if (getIntent().hasExtra("address")){
            address = getIntent().getStringExtra("address");
            String list[] = address.split(":");
            ip = list[0];
            port = Integer.valueOf(list[1]);
        }

        tv_address = findViewById(R.id.address_id);

        b1_on = findViewById(R.id.b1_on_id);
        b1_off = findViewById(R.id.b1_off_id);
        b2_on = findViewById(R.id.b2_on_id);
        b2_off = findViewById(R.id.b2_off_id);
        fan_on = findViewById(R.id.fan_on_id);
        fan_off = findViewById(R.id.fan_off_id);
        h_on = findViewById(R.id.h_on_id);
        h_off = findViewById(R.id.h_off_id);
        auto_temp = findViewById(R.id.auto_temp_id);

        tv_address.setText(address);

        b1_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "0";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        b1_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "1";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        b2_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "2";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        b2_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "3";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        fan_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "4";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        fan_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "5";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        h_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "6";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        h_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = "7";
                SockedTransfer socket = new SockedTransfer();
                socket.execute();
            }
        });

        auto_temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isOn) {
                if (isOn) {
                    // The toggle is disabled
                    fan_on.setEnabled(false);
                    fan_off.setEnabled(false);
                    h_on.setEnabled(false);
                    h_off.setEnabled(false);
                    fan_on.setBackgroundColor(Color.GRAY);
                    fan_off.setBackgroundColor(Color.GRAY);
                    h_on.setBackgroundColor(Color.GRAY);
                    h_off.setBackgroundColor(Color.GRAY);
                } else {
                    // The toggle is enabled
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


    }

    public void exit()
    {
        System.exit(0);
    }

    public class SockedTransfer extends AsyncTask<Void,Void,Void>
    {

        Socket socket;

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                InetAddress inet = InetAddress.getByName(ip);
                socket = new java.net.Socket(inet, port);
                DataOutputStream stream =  new DataOutputStream(socket.getOutputStream());
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

}