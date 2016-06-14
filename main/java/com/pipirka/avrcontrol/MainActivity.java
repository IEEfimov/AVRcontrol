package com.pipirka.avrcontrol;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.print.PrintAttributes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Calendar;

import java.lang.Math;


public class MainActivity extends AppCompatActivity {

    // хуйня для скрытия экрана

    public static String ServerIp = "192.168.0.100";
    public static int ServerPort = 5168;
    public Socket socket;
    Thread myClient = null;
    Thread TCPconrol = null;
    InputStreamReader in;
    OutputStreamWriter out;
    public Handler addTextThread;

    public Handler fineHand;
    boolean connection = false;

    public static String clientHandStatus;



    LinearLayout allText;
    ScrollView allTextView;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn4;

    int reDesignFlag;

    Intent intent;

    EditText editSend;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        allText = (LinearLayout) findViewById(R.id.allText);
        allTextView = (ScrollView) findViewById(R.id.allTextView);


        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        intent = new Intent(this, settings.class);

        editSend = (EditText) findViewById(R.id.editSend);
        btnSend = (Button) findViewById(R.id.btnSend);





        ServerIp = loadText("IPvar");
        if (ServerIp == null||ServerIp=="" || ServerIp ==" "){
            ServerIp = "192.168.0.100";
            saveText("IPvar",ServerIp);
            saveText("reDeisgn","1");
        }

        String PortVar = loadText("PortVar");
        if (PortVar == null||PortVar=="" || PortVar ==" "){
            PortVar = "8888";
            saveText("PortVar",PortVar);
        }
        String temp = loadText("reDesign");
        if (temp == null || temp == "" || temp == " ")
            saveText("reDesign","1");
        reDesignFlag = Integer.parseInt(loadText("reDesign"));

        String btnTest = null;
        btnTest = loadText("btn1SendValue");
        if (btnTest == null||btnTest=="" || btnTest ==" ")  saveText("btn1SendValue","sbtn1");

        btnTest = loadText("btn2SendValue");
        if (btnTest == null||btnTest=="" || btnTest ==" ")  saveText("btn2SendValue","sbtn2");

        btnTest = loadText("btn3SendValue");
        if (btnTest == null||btnTest=="" || btnTest ==" ")  saveText("btn3SendValue","sbtn3");

        btnTest = loadText("btn4SendValue");
        if (btnTest == null||btnTest=="" || btnTest ==" ")  saveText("btn4SendValue","sbtn4");


        ServerPort = Integer.parseInt(PortVar);

        btn1.setOnClickListener(singleClick);
        btn2.setOnClickListener(singleClick);
        btn3.setOnClickListener(singleClick);
        btn4.setOnClickListener(singleClick);

        btn1.setOnLongClickListener(longClick);
        btn2.setOnLongClickListener(longClick);
        btn3.setOnLongClickListener(longClick);
        btn4.setOnLongClickListener(longClick);

        btnSend.setOnClickListener(singleClick);
     //   clearAllText();

//        TCPconrol = new Thread(new TCPcontrol());
//        TCPconrol.start();


        myClient = new Thread(new ClientThread());
        myClient.start();



        addLog("подключаюсь к  "+ServerIp+":"+ServerPort);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //    getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0,1,1,"Очистить лог");
        menu.add(0,2,1,"Повторить подключение");
        menu.add(0,3,1,"Информация");
        menu.add(0,4,1,"Настройки");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == 1) {
            clearAllText();
            return true;
        }
        if (id == 2){
            reconnect();
            return true;
        }
        if (id == 3 ){
            addText("Информация не работает блеать!","transmitted");
            return true;
        }

        if (id == 4 ){
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
       if (connection) {
           try {
               socket.close();
               Toast.makeText(this, "Гасим сокет...", Toast.LENGTH_SHORT).show();
           }catch (Throwable e){
               e.printStackTrace();
               Toast.makeText(this, "Дыбил, сокет не потушил", Toast.LENGTH_SHORT).show();
           }

           super.onDestroy();
       }
        super.onDestroy();

    }

    public void addText(String myText,String status) {
        if (reDesignFlag == 1) {
            TextView a = new TextView(getApplicationContext());
            LinearLayout b = new LinearLayout(getApplicationContext());
            a.setText(myText);
            b.setOrientation(LinearLayout.VERTICAL);

            switch (status){
                case "transmitted":
                    a.setTextColor(Color.rgb(0, 75, 0));
                    a.setGravity(Gravity.LEFT);
                    a.setPadding(30, 10, 30, 10);
                    a.setBackgroundColor(Color.rgb(216, 216, 216));
                    b.setOrientation(LinearLayout.VERTICAL);
                    b.setGravity(Gravity.LEFT);
                    b.setPadding(0, 5, 0, 5);
                    break;
                case "received":
                    a.setTextColor(Color.BLUE);
                    a.setGravity(Gravity.RIGHT);
                    a.setPadding(30, 10, 30, 10);
                    a.setBackgroundColor(Color.rgb(216, 216, 216));
                    b.setOrientation(LinearLayout.VERTICAL);
                    b.setGravity(Gravity.RIGHT);
                    b.setPadding(0, 5, 50, 5);
                    break;
                case "error":
                    a.setTextColor(Color.rgb(207,0,0));
                    a.setGravity(Gravity.CENTER);
                    a.setPadding(30,10,30,10);
                    a.setBackgroundColor(Color.rgb(216,216,216));
                    b.setOrientation(LinearLayout.VERTICAL);
                    b.setGravity(Gravity.CENTER);
                    b.setPadding(0,5,50,5);
                    break;
                case "fine":
                    a.setTextColor(Color.rgb(0, 175, 0));
                    a.setGravity(Gravity.CENTER);
                    a.setPadding(30, 10, 30, 10);
                    a.setBackgroundColor(Color.rgb(216, 216, 216));
                    b.setOrientation(LinearLayout.VERTICAL);
                    b.setGravity(Gravity.CENTER);
                    b.setPadding(0, 5, 50, 5);
                    break;
                default:
                    return;

            }

            b.addView(a, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            allText.addView(b);
            allTextView.fullScroll(ScrollView.FOCUS_DOWN);
        }
        else {
            TextView a = new TextView(getApplicationContext());
            a.setTextColor(Color.DKGRAY);
            switch (status) {
                case "transmitted":
                    a.setTextColor(Color.DKGRAY);
                    a.setText("Отправлено: " + myText);
                    break;
                case "received":
                    a.setTextColor(Color.BLUE);
                    a.setText("Получено: " + myText);
                    break;
                case "error":
                    a.setTextColor(Color.RED);
                    a.setText("Ошибка: " + myText);
                    break;
                case "fine":
                    a.setTextColor(Color.GREEN);
                    a.setText(myText);
                    break;
                default:
                    return;
            }
            allText.addView(a);
            allTextView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }

    public void addLog(String myText){
        TextView a = new TextView(getApplicationContext());
        a.setText(myText);
        allText.addView(a);
        allTextView.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void clearAllText(){
        allText.removeAllViews();
        TextView a = new TextView(this);
        a.setText(" Лог обмена:");
        a.setTextSize(23);
        a.setTextColor(Color.BLACK);
        allText.addView(a);
        editSend.clearFocus();
        //   getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    public void saveText(String name,String value) {
        SharedPreferences sPref = getSharedPreferences("AVRcontrol",MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(name,value);
        ed.commit();
        Toast.makeText(this, "Использованы стандартные настройки", Toast.LENGTH_SHORT).show();
    }

    public String loadText(String name) {
        SharedPreferences sPref = getSharedPreferences("AVRcontrol",MODE_PRIVATE);
        String savedText = sPref.getString(name, "");
        return savedText;

    }

    void reconnect(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public boolean sendToServer(String str){
        if (str.charAt(0)=='s'){
            String temp = "";
            for (int i=1;i<str.length();i++) temp+=str.charAt(i);
            return (sendStrToServer(temp));
        }
        if (str.charAt(0)=='n'){
            String temp = "";
            String a[] = new String[255];
            for (int i = 0;i<255;i++) a[i] = "";
            int count = 0;
            boolean flag = true;
            boolean resultFlag = true;
            for (int i=1;i<str.length();i++) temp+=str.charAt(i);
            temp += " ";
            for (int i=0;i<str.length();i++){
                if (temp.charAt(i) != ' '){
                    a[count] += temp.charAt(i);
                    flag = true;
                }else{
                    if (flag) {
                        flag = false;
                        count++;
                    }

                }
            }
                // на данном этапе имеется массив строк с числами
            for (int i=0;i<count;i++){
                int tempInt;
                if (a[i].charAt(0)=='0' && (a[i].charAt(1)=='b')){
                    tempInt=0;
                    int power = 0;
                    for (int g = a[i].length()-1; g>=2; g--){
                        if (a[i].charAt(g) == '1'){
                            int currentTemp = (int) Math.pow(2,power);
                            tempInt+=currentTemp;
                        }
                        power++;
                    }
                    if (!sendIntToServer(tempInt)) resultFlag = false;
                    continue;
                }
                if (a[i].charAt(0)=='0' && (a[i].charAt(1)=='x' || a[i].charAt(1)=='h')){
                    continue;
                }
                if (a[i].charAt(a[i].length()-1)=='h'){
                    continue;
                }

                try{
                tempInt = Integer.parseInt(a[i]);
                } catch (Throwable e){
                    Message msg = new Message();
                    msg.obj = "Некорректное число!";
                    clientHandStatus = "transmitted";
                    clientHand.sendMessage(msg);
                    resultFlag = false;
                    continue;
                }
                if (!sendIntToServer(tempInt)) resultFlag = false ;
            }

            return (resultFlag);
        }


        return false;
    }


    public boolean sendIntToServer(int num){
        if (connection == true) {
            if (num > 255) Toast.makeText(this, "Есть число > 255", Toast.LENGTH_SHORT).show();
            try {
                out.write(num);
                out.flush();
           } catch (Throwable e) {
                Message msg = new Message();
                msg.obj = "Соединение потеряно";
                clientHandStatus = "error";
                clientHand.sendMessage(msg);
                connection = false;
                return false;
            }
            return true;
        }
        else {
            Toast.makeText(this, "Нет подключения", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean sendStrToServer(String str){
        if (connection == true) {

            try {
                out.write(str);
                out.write(10);
                out.flush();
            } catch (Throwable e) {
                Message msg = new Message();
                msg.obj = "Соединение потеряно";
                clientHandStatus = "error";
                clientHand.sendMessage(msg);
                connection = false;                return false;
            }
            return true;
        }
        else {
            Toast.makeText(this, "Нет подключения", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    public View.OnClickListener singleClick = new View.OnClickListener() {
        @Override
        public void onClick(View object) {
            Integer s = object.getId();
            String value = null;
            switch (s){
                case R.id.btn1:
                    value = loadText("btn1SendValue");
                    if (sendToServer(value))addText("=== кнопка 1 ===","transmitted");
                    break;
                case R.id.btn2:
                    value = loadText("btn2SendValue");
                    if (sendToServer(value))addText("=== кнопка 2 ===","transmitted");
                    break;
                case R.id.btn3:
                    value = loadText("btn3SendValue");
                    if (sendToServer(value))addText("=== кнопка 3 ===","transmitted");
                    break;
                case R.id.btn4:
                    value = loadText("btn4SendValue");
                    if (sendToServer(value))addText("=== кнопка 4 ===","transmitted");
                    break;
                case R.id.btnSend:
                    boolean normal = true;
                    String sendNumber = editSend.getText().toString();
                    int sendValue=0;
                    try {
                        sendValue = Integer.parseInt(sendNumber);
                        if (sendValue < 0 || sendValue > 255){
                            Toast.makeText(MainActivity.this, "Пока только 1 байт", Toast.LENGTH_SHORT).show();
                            normal = false;
                        }
                    }
                    catch (Throwable e){
                        Toast.makeText(MainActivity.this, "Только целые числа!", Toast.LENGTH_SHORT).show();
                        normal = false;
                    }
                    if (normal == true) {
                        if (sendToServer("s"+sendValue))   addText(" " + sendNumber,"transmitted");
                    }

                    editSend.setText("");
                    editSend.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(btnSend.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    break;

            }


        }
    };

    public View.OnLongClickListener longClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View object) {
            Integer s = object.getId();
            switch (s){
                case R.id.btn1:
                    variables.btnConfWho = "btn1SendValue";
                    new btnConf().show(getSupportFragmentManager(),"говно");
                    break;
                case R.id.btn2:
                    variables.btnConfWho = "btn2SendValue";
                    new btnConf().show(getSupportFragmentManager(),"говно");
                    break;
                case R.id.btn3:
                    variables.btnConfWho = "btn3SendValue";
                    new btnConf().show(getSupportFragmentManager(),"говно");
                    break;
                case R.id.btn4:
                    variables.btnConfWho = "btn4SendValue";
                    new btnConf().show(getSupportFragmentManager(),"говно");
                    break;


            }

            return true;
        }
    };

    public Handler clientHand = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            addText(msg.obj.toString(),clientHandStatus);
            clientHandStatus = null;
        }
    };

    class ClientThread implements Runnable {

        @Override
        public void run() {
           try {
                SocketAddress sockAddr = new InetSocketAddress(ServerIp, ServerPort);
                socket = new Socket();
                socket.connect(sockAddr,5000);
                out = new OutputStreamWriter(socket.getOutputStream(),"ASCII");
                in = new InputStreamReader(socket.getInputStream(),"ASCII");
               Message msg = new Message();
               msg.obj = "Связь установлена :)\n Твой порт = "+socket.getLocalPort();
               clientHandStatus = "fine";
               clientHand.sendMessage(msg);
                connection = true;
                scan();
            } catch (Throwable e) {
               Message msg = new Message();
               msg.obj = "Ошибка подключения";
               clientHandStatus = "error";
               clientHand.sendMessage(msg);
                connection = false;
            }
        }
        public  void scan(){
            String tempStr=" ";
            int tempChar;
            while (connection){
                try {
                    tempChar = in.read();
                    if ((tempChar != 10) && (tempChar != 13)){
                        if (tempChar != 0)  tempStr += (char) tempChar;
                    }
                    else {
                        tempChar = in.read();


                        Message msg = new Message();
                        msg.obj = tempStr;
                        clientHandStatus = "received";
                        clientHand.sendMessage(msg);
                        tempStr = " ";
                        tempChar = 0;
                    }
                }catch (Throwable e){
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.obj = "Соединение потеряно";
                    clientHandStatus = "error";
                    clientHand.sendMessage(msg);
                    connection = false;
                }
            }
            Message msg = new Message();
            msg.obj = "Соединение потеряно";
            clientHandStatus = "error";
            clientHand.sendMessage(msg);
            connection = false;
        }

    }
}
