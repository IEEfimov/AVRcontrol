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

import java.net.InetAddress;



public class ipAsk extends DialogFragment implements
        DialogInterface.OnClickListener {
    private View form=null;

    public static String answer="no_changes564";

    EditText writeBox;
    TextView IPview;
    Button clearIP;

    boolean AlLRightFlag = true;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int a = 5;

        IPview = (TextView)getActivity().findViewById(R.id.IPview);


   //     writeBox.setText(IPview.getText().toString());

        form= getActivity().getLayoutInflater()
                .inflate(R.layout.activity_ip_ask, null);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        //clearIP.setOnClickListener(shit);

        clearIP = (Button)form.findViewById(R.id.clearIP);
        writeBox =(EditText)form.findViewById(R.id.login);
        String oldIP = IPview.getText().toString();
        writeBox.setText(oldIP);
        writeBox.requestFocus();


        View.OnClickListener shit = new View.OnClickListener() {
            @Override
            public void onClick(View object) {
                Integer s = object.getId();
                switch (s){
                    case R.id.clearIP:
                        writeBox.setText("");
                        writeBox.requestFocus();
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, 0);


                        break;
                }


            }
        };

        clearIP.setOnClickListener(shit);

        return(builder.setTitle("Введи IP адрес сервера").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());


    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        String login = writeBox.getText().toString();

        try {
            InetAddress Addr = InetAddress.getByName(login);
        }
        catch (Throwable e){
            Toast.makeText(getActivity(), "Ты дыбил??", Toast.LENGTH_SHORT).show();
            AlLRightFlag = false;
        }
        if (AlLRightFlag == true) {
            IPview.setText(login);
            answer = login;
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