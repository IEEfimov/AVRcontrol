package com.pipirka.avrcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.net.InetAddress;

public class btnConf extends DialogFragment implements
        DialogInterface.OnClickListener {

    private View form=null;
    private int who;
    private RadioButton rad1;
    private RadioButton rad2;
    private EditText edit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        form= getActivity().getLayoutInflater()
                .inflate(R.layout.activity_btn_conf, null);

        rad1 = (RadioButton) form.findViewById(R.id.btnConf_radio1);
        rad2 = (RadioButton) form.findViewById(R.id.btnConf_radio2);
        edit = (EditText) form.findViewById(R.id.btnConf_edit);

        rad2.setEnabled(false);
        edit.setText(loadText(variables.btnConfWho));


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        return(builder.setTitle("Настройка кнопки").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }

    public void onClick(DialogInterface dialog, int which) {
        saveText(variables.btnConfWho,edit.getText().toString());

    }
    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }

    public void saveText(String name,String value) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("AVRcontrol", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(name,value);
        ed.commit();
    }

    public String loadText(String name) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("AVRcontrol", Context.MODE_PRIVATE);
        String savedText = preferences.getString(name, "");
        return savedText;

    }
}
