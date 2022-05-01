package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.PurseAdapter;
import com.example.myapplication.bean.EpidemicInfoSearch;
import com.example.myapplication.bean.NoiseSearch;
import com.example.myapplication.bean.Option;
import com.example.myapplication.bean.Quest;
import com.example.myapplication.bean.SleepQualitySearch;
import com.example.myapplication.bean.SubQuestion;
import com.example.myapplication.bean.UserInfoSearch;
import com.example.myapplication.helper.AlertDialogCreator;
import com.example.myapplication.helper.NetWorkTask;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SubQuestionActivity extends AppCompatActivity implements View.OnClickListener{
    Button submit;
    List<SubQuestion> questionList = new ArrayList<SubQuestion>();
    List<Option> optionList = new ArrayList<Option>();
    List<View> viewList = new ArrayList<View>();
    LinearLayout container;
    Quest quest;
    int userid;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_question);
        //get questId from intent;
        quest = (Quest) getIntent().getSerializableExtra("quest");
        userid = getIntent().getIntExtra("userid", 0);

        SetUI();

        if (NetWorkTask.isNetConnection(this))
        {
            DownloadTask task = new DownloadTask() ;
            task.execute();
        }
        else
            Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();
    }

    void SetUI()
    {
        //button
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        Button quit = (Button) findViewById(R.id.quit);
        quit.setOnClickListener(this);
        TextView title = findViewById(R.id.title);
        title.setText(quest.getTitle());

        //Action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("提交数据");
    }

    void CreateQuestion()
    {
        container = findViewById(R.id.layout_container);
        int optionIndex = 0;
        for (int i=0; i<questionList.size(); ++i)
        {
            SubQuestion question = questionList.get(i);
            if(question.getType() == SubQuestion.QuestionType.Option)
            {
                //group
                RadioGroup group = new RadioGroup(getApplicationContext());
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(0,20,0,20);
                group.setLayoutParams(lp1);

                //textview
                TextView textView = new TextView(getApplicationContext());
                textView.setText(question.getDescription());
                textView.setTextSize(18);
                group.addView(textView);

                //option
                for (;optionIndex < optionList.size() &&
                        optionList.get(optionIndex).getBelongedQuestionId() == question.getQuestionId();++optionIndex)
                {
                    Option option = optionList.get(optionIndex);
                    RadioButton radioButton = new RadioButton(getApplicationContext());
                    radioButton.setText(option.getDescription());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins(10,10,0,10);
                    radioButton.setLayoutParams(lp2);
                    group.addView(radioButton);
                }
                container.addView(group);
                viewList.add(group);
            }
            else
            {
                //textview
                TextView textView = new TextView(getApplicationContext());
                textView.setText(question.getDescription());
                textView.setTextSize(18);
                container.addView(textView);

                //EditText
                EditText inputText = new EditText(getApplicationContext());
                inputText.setInputType(InputType.TYPE_CLASS_NUMBER);

                //layout
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.setMargins(0,20,0,20);
                inputText.setLayoutParams(lp1);

                container.addView(inputText);
                viewList.add(inputText);
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.submit:
                if (NetWorkTask.isNetConnection(this))
                {
                    Boolean hasUnselect = false;
                    List<String> params = new ArrayList<String>();
                    for(int i = 0; i< viewList.size(); ++i)
                    {
                        View item = viewList.get(i);
                        if (item instanceof RadioGroup)
                        {
                            RadioGroup radioGroup = (RadioGroup) item;
                            int buttonid = radioGroup.getCheckedRadioButtonId();
                            if(buttonid == -1)
                            {
                                hasUnselect = true;
                                break;
                            }
                            else
                            {
                                RadioButton button = (RadioButton) findViewById(buttonid);
                                String answer = button.getText().toString();
                                params.add(answer);
                            }
                        }
                        else
                        {
                            EditText editText = (EditText)item;
                            String ans = editText.getText().toString().trim();
                            if (ans.isEmpty())
                            {
                                hasUnselect = true;
                                break;
                            }
                            else
                            {
                                params.add(ans);
                            }
                        }
                    }

                    //According to questid insert data
                    if (hasUnselect)
                    {
                        Toast.makeText(SubQuestionActivity.this,"存在未完成的问题", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String jsonStr = "";
                        Gson gson = new Gson();
                        switch (quest.getQuestId())
                        {
                            case 1:
                            {
                                UserInfoSearch data = UserInfoSearch.BuildData(params);
                                jsonStr = gson.toJson(data);
                                break;
                            }
                            case 2:
                            {
                                EpidemicInfoSearch data = EpidemicInfoSearch.BuildData(params);
                                jsonStr = gson.toJson(data);
                                break;
                            }
                            case 3:
                            {
                                SleepQualitySearch data = SleepQualitySearch.BuildData(params);
                                jsonStr = gson.toJson(data);
                                break;
                            }
                            case 4:
                            {
                                NoiseSearch data = NoiseSearch.BuildData(params);
                                jsonStr = gson.toJson(data);
                                break;
                            }
                        }

                        InsertDataTask task = new InsertDataTask();
                        task.execute(jsonStr);
                    }
                }
                else
                    Toast.makeText(this, "网络连接错误", Toast.LENGTH_SHORT).show();
                break;
            case R.id.quit:
                finish();
                break;
            default:
                break;
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void>
    {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(SubQuestionActivity.this);
            alertDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {
            questionList = NetWorkTask.GetSubQuestion(quest.getQuestId());
            optionList = NetWorkTask.GetOption(quest.getQuestId());
            questionList.sort(new Comparator<SubQuestion>() {
                @Override
                public int compare(SubQuestion t1, SubQuestion t2) {
                    return t1.getQuestionId() - t2.getQuestionId();
                }
            });

            optionList.sort(new Comparator<Option>() {
                @Override
                public int compare(Option t1, Option t2) {
                    return t1.getBelongedQuestionId() - t2.getBelongedQuestionId();
                }
            });
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            alertDialog.dismiss();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            alertDialog.dismiss();
            CreateQuestion();
        }
    }

    class InsertDataTask extends AsyncTask<String, Integer, Void>
    {
        AlertDialog alertDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = AlertDialogCreator.MakeDialog(SubQuestionActivity.this);
            alertDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String jsonStr = strings[0];
            NetWorkTask.InsertData(userid, quest.getQuestId(), quest.getReward(), jsonStr);
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            alertDialog.dismiss();
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            alertDialog.dismiss();
            Toast.makeText(SubQuestionActivity.this, "完成任务，获得积分：" + quest.getReward(), Toast.LENGTH_SHORT).show();
            submit.setText("已提交");
            submit.setVisibility(View.INVISIBLE);
        }
    }

}