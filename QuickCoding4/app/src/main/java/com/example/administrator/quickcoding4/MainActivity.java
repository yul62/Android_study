package com.example.administrator.quickcoding4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textView1,textView2,textView3;
    private EditText edittxt = null;
    private Button inputBnt, endBnt;

    private LinkedList <String>strList = new LinkedList<String>();
    private LinkedList <Integer>intList = new LinkedList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 =(TextView) findViewById(R.id.textView1);
        textView2 =(TextView) findViewById(R.id.textView2);
        textView3 =(TextView) findViewById(R.id.textView3);

        edittxt =(EditText)findViewById(R.id.editText);
        inputBnt = (Button) findViewById(R.id.inputBnt);
        inputBnt.setOnClickListener(this);

        endBnt = (Button) findViewById(R.id.endBnt);
        endBnt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch ( v.getId()){
            case R.id.inputBnt:
                try {
                    intList.add(Integer.parseInt(edittxt.getText().toString()));  //먼저 스트링에서 인티저로 바꿔서 넣어보고 오류나면
                    Toast.makeText(MainActivity.this, "숫자를 입력했습니다.", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    strList.add(edittxt.getText().toString()); //스트링배열로 바로 넣으면됨
                    Toast.makeText(MainActivity.this, "문자열을 입력했습니다.", Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.endBnt:
                textView2.setText("입력된 문자열 :\n" +strList);
                textView3.setText("입력된 숫자 :\n"+intList);
                break;
        }

    }
}
