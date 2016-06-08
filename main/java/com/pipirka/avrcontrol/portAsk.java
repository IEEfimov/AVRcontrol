package com.pipirka.avrcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class portAsk extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View form=null;

    private boolean AlLRightFlag = true;

    public static String answer="no_changes228";

    EditText writeBox;
    TextView PortView;
    Button clearPort;

    /**  View.OnClickListener shit = new View.OnClickListener() {
    @Override
    public void onClick(View object) {
    Integer s = object.getId();
    switch (s){
    case R.id.clearIP:
    writeBox.setText("");
    break;


    }


    }
    };

     */


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int a = 5;

        PortView = (TextView)getActivity().findViewById(R.id.PortView);


        //     writeBox.setText(IPview.getText().toString());

        form= getActivity().getLayoutInflater()
                .inflate(R.layout.activity_port_ask, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        //clearIP.setOnClickListener(shit);

        clearPort = (Button)form.findViewById(R.id.clearPort);
        writeBox =(EditText)form.findViewById(R.id.PortEditor);
        String oldIP = PortView.getText().toString();
        writeBox.setText(oldIP);
        writeBox.requestFocus();

        View.OnClickListener shit = new View.OnClickListener() {
            @Override
            public void onClick(View object) {
                Integer s = object.getId();
                switch (s){
                    case R.id.clearPort:
                        writeBox.setText("");
                        writeBox.requestFocus();

                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, 0);
                        break;


                }


            }
        };

        clearPort.setOnClickListener(shit);

        return(builder.setTitle("Введи порт сервера").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());


    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        String newPort = writeBox.getText().toString();

        try {
            int PortInt = Integer.parseInt(newPort);
                if (PortInt < 1 || PortInt > 65535){
                    Toast.makeText(getActivity(), "Только от 1 до 65535", Toast.LENGTH_SHORT).show();
                    AlLRightFlag = false;
                }

        }
        catch (Throwable e){
            Toast.makeText(getActivity(), "Ты дыбил?", Toast.LENGTH_SHORT).show();
            AlLRightFlag = false;
        }
        if (AlLRightFlag == true) {
            PortView.setText(newPort);
            answer = newPort;
        }

    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }
}