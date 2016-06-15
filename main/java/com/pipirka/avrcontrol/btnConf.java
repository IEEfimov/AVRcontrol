package com.pipirka.avrcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import java.net.InetAddress;

public class btnConf extends DialogFragment implements
        DialogInterface.OnClickListener {

    private View form=null;
    private LinearLayout linear;
    private RadioButton rad1;
    private RadioButton rad2;
    private EditText edit;
    private String currentJob;
    private String modificator;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        form= getActivity().getLayoutInflater()
                .inflate(R.layout.activity_btn_conf, null);

        linear = (LinearLayout) form.findViewById(R.id.btnConf_ll);

        rad1 = (RadioButton) form.findViewById(R.id.btnConf_radio1);
        rad2 = (RadioButton) form.findViewById(R.id.btnConf_radio2);
        edit = (EditText) form.findViewById(R.id.btnConf_edit);

        if (variables.btnConfWho == "btnSendValue") { //edit.setVisibility(View.INVISIBLE);
            linear.removeView(edit);
            rad2.setText("Число");
        }

        edit.addTextChangedListener(textWatcher);

     //   rad2.setEnabled(false);
        currentJob = (loadText(variables.btnConfWho));

        if (currentJob.charAt(0)=='s') {
            String temp = "";
            for (int i = 1; i<currentJob.length(); i++){
                temp += currentJob.charAt(i);
            }
            edit.setText(temp);
            rad1.setChecked(true);
            rad2.setChecked(false);
            modificator = "s";

        }


        if (currentJob.charAt(0)=='n') {
            String temp = "";
            for (int i = 1; i<currentJob.length(); i++){
                temp += currentJob.charAt(i);
            }
            edit.setText(temp);
            rad1.setChecked(false);
            rad2.setChecked(true);
            modificator = "n";

        }

        rad1.setOnClickListener(singleClick);
        rad2.setOnClickListener(singleClick);


        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        return(builder.setTitle("Настройка кнопки").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }

    public void onClick(DialogInterface dialog, int which) {
        modificator += edit.getText().toString();
        saveText(variables.btnConfWho,modificator);

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

    public View.OnClickListener singleClick = new View.OnClickListener() {
        @Override
        public void onClick(View object) {
            Integer s = object.getId();
            String value = null;
            switch (s){
                case R.id.btnConf_radio1:
                    rad2.setChecked(false);
                    modificator = "s";
                    break;
                case R.id.btnConf_radio2:
                    rad1.setChecked(false);
                    modificator = "n";
                    break;

            }


        }
    };

    public String loadText(String name) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("AVRcontrol", Context.MODE_PRIVATE);
        String savedText = preferences.getString(name, "");
        return savedText;

    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            isCorrect();
        }
    };

    private boolean isCorrect(){
        String str = edit.getText().toString();
        String temp = "";
        String a[] = new String[255];
        for (int i = 0; i < 255; i++) a[i] = "";
        int count = 0;
        boolean flag = true;
        for (int i = 0; i < str.length(); i++) temp += str.charAt(i);
        temp += " ";
        for (int i = 0; i < temp.length(); i++) {
            if (temp.charAt(i) != ' ') {
                a[count] += temp.charAt(i);
                flag = true;
            } else {
                if (flag) {
                    flag = false;
                    count++;
                }

            }
        }
        // разбили на числа через пробел
        boolean isAllRight = true;
        for (int i = 0; i < count; i++) {
            if (a[i].length()<=0) {
                continue;
            }
            if (a[i].length()>=2) {
                if (a[i].charAt(0) == '0' && (a[i].charAt(1) == 'b')) {
                    if (a[i].length() == 2) {
                        isAllRight = false;
                        continue;
                    }
                    for (int g = a[i].length() - 1; g >= 2; g--) {
                        if (a[i].charAt(g) != '1' && a[i].charAt(g) != '0')
                            isAllRight = false;
                        break;
                    }
                    continue;
                }
                if (a[i].charAt(0) == '0' && (a[i].charAt(1) == 'x' || a[i].charAt(1) == 'h')) {
                    if (a[i].length() == 2) {
                        isAllRight = false;
                        continue;
                    }
                    for (int g = a[i].length() - 1; g >= 2; g--) {
                        if (!Character.isDigit(a[i].charAt(g)) && (a[i].charAt(g) < 'a' || a[i].charAt(g) > 'f'))
                            isAllRight = false;
                        break;
                    }
                    continue;
                }
            }
            if (a[i].charAt(a[i].length() - 1) == 'h') {
                if (a[i].length()==1){
                    isAllRight = false;
                    continue;
                }
                for (int g = a[i].length() - 2; g >= 0; g--) {
                    if (!Character.isDigit(a[i].charAt(g)) && (a[i].charAt(g) < 'a' || a[i].charAt(g) > 'f'))
                        isAllRight = false;
                    break;
                }
                continue;
            }


            try {
                int tempInt = Integer.parseInt(a[i]);
            } catch (Throwable e) {
                isAllRight = false;
                break;
            }

        }
        if (!isAllRight) {
            edit.getText().delete(edit.getText().length() - 1, edit.getText().length() - 1);
            rad1.setChecked(true);
            rad2.setChecked(false);
            rad2.setEnabled(false);
            modificator = "s";
            return false;

        }
        else rad2.setEnabled(true);
        return true;
    }

}
