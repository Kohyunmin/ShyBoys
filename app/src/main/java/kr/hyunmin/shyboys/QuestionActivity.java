package kr.hyunmin.shyboys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import kr.hyunmin.shyboys.kr.hyunmin.object.DTO;

public class QuestionActivity extends Actionbar {
    public static String[] content_Question;
    EditText question_content;
    Button insert_q_button;
    ListView question_listView;
    ArrayAdapter adapter_question;
    String subject;
    String roomcode;
    String name;
    Vector<String> question;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
           question = new Vector();
           Intent intent = getIntent();
           subject = intent.getExtras().getString("subject");
           roomcode = intent.getExtras().getString("roomcode");
           name = intent.getExtras().getString("name");
           setActionbar(subject);

        insert_q_button = (Button) findViewById(R.id.insert_q_button);
        if(MainActivity.isHost == 1)
        insert_q_button.setVisibility(View.INVISIBLE);
        insert_q_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuestion_writePopup();

            }
        });
           question_listView = (ListView) findViewById(R.id.question_listView);
           String[] result_array = (String[]) intent.getSerializableExtra("result");


           for(int i=0;i<result_array.length;i++){
               if(result_array[i]!=null){
                       question.add((String)result_array[i]);
               }
           }
           adapter_question = new ArrayAdapter(this,R.layout.list_font,question);
           question_listView.setAdapter(adapter_question);
           adapter_question.notifyDataSetChanged();



    }

    public void showQuestion_writePopup(){
        Context mContext = getApplicationContext();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        //R.layout.init는 xml 파일명이고  R.id.popup은 보여줄 레이아웃 아이디
        View layout = inflater.inflate(R.layout.questionpopup, (ViewGroup) findViewById(R.id.question_popup));
        final android.app.AlertDialog.Builder aDialog = new android.app.AlertDialog.Builder(QuestionActivity.this);

        aDialog.setTitle("Write Question"); //타이틀바 제목
        aDialog.setView(layout); //inti.xml 파일을 뷰로 셋팅
        aDialog.setCancelable(true);

        question_content = (EditText) layout.findViewById(R.id.Question_edittext);
        //그냥 닫기버튼을 위한 부분
        aDialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String content = question_content.getText().toString();
                DAO dao = new DAO(QuestionActivity.this);
                DTO dto = new DTO();

                Date d = new Date();
                SimpleDateFormat now_date = new SimpleDateFormat("yyyy-MM-dd");
                dto.set_content(content +"\n"+now_date.format(d)+"\t"+name);
                dto.set_room_code(roomcode);
                dto.set_QorA("Q");
                dto.set_date(now_date+"");
                dao.insert_QuestionAndAnswer(dto);
                question.add(content +"\n"+now_date.format(d)+"\t"+name);
                adapter_question.notifyDataSetChanged();
            }
        });
        //팝업창 생성
        android.app.AlertDialog ad = aDialog.create();
        ad.show();//보여줌!

    }

}
