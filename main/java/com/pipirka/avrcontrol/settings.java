package com.pipirka.avrcontrol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    Button editIP;
    Button editPort;
    Intent intent;
    TextView IPview;
    TextView PortView;
    LinearLayout LinIP;
    LinearLayout LinPort;
    Switch reDesign;
    int reDesignFlag;

    String IPvar;
    String PortVar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ipAsk.answer = "no_changes564";
        portAsk.answer = "no_changes228";

        reDesign = (Switch) findViewById(R.id.reDesign);

        editIP = (Button) findViewById(R.id.editIP);
        editPort = (Button) findViewById(R.id.editPort);
        IPview = (TextView) findViewById(R.id.IPview);
        PortView = (TextView) findViewById(R.id.PortView);

        LinIP = (LinearLayout) findViewById(R.id.LinIP);
        LinPort = (LinearLayout) findViewById(R.id.LinPort);

        reDesign = (Switch) findViewById(R.id.reDesign);
        reDesignFlag = Integer.parseInt(loadText("reDesign"));

        if (reDesignFlag == 1) reDesign.setChecked(true);
        else reDesign.setChecked(false);


        IPvar = loadText("IPvar");
        IPview.setText(IPvar);

        PortVar = loadText("PortVar");
        PortView.setText(PortVar);

        View.OnClickListener shit = new View.OnClickListener() {
            @Override
            public void onClick(View object) {
                Integer s = object.getId();
                switch (s){
                    case R.id.editIP:
                        new ipAsk().show(getSupportFragmentManager(),"говно");
                        break;
                    case R.id.editPort:
                        new portAsk().show(getSupportFragmentManager(),"говно");
                        break;
                    case R.id.LinIP:
                        new ipAsk().show(getSupportFragmentManager(),"говно");
                        break;
                    case R.id.LinPort:
                        new portAsk().show(getSupportFragmentManager(),"говно");
                        break;
                    case R.id.reDesign:
                        if (reDesignFlag == 1) saveText("reDesign","0");
                        else saveText("reDesign","1");;
                }
            }
        };

        editIP.setOnClickListener(shit);
        editPort.setOnClickListener(shit);
        LinIP.setOnClickListener(shit);
        LinPort.setOnClickListener(shit);
        reDesign.setOnClickListener(shit);
    }

    @Override
    protected void onDestroy() {
        if (ipAsk.answer != "no_changes564") saveText("IPvar",ipAsk.answer);
        if (portAsk.answer != "no_changes228") saveText("PortVar",portAsk.answer);
        if (portAsk.answer != "no_changes228") saveText("PortVar",portAsk.answer);
        super.onDestroy();
    }

    public void saveText(String name,String value) {
        SharedPreferences sPref = getSharedPreferences("AVRcontrol",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(name,value);
        ed.commit();
        Toast.makeText(this, "Требуется перезапуск", Toast.LENGTH_SHORT).show();
    }

    public String loadText(String name) {
        SharedPreferences sPref = getSharedPreferences("AVRcontrol",MODE_PRIVATE);
        String savedText = sPref.getString(name, "");
        return savedText;

    }

}

