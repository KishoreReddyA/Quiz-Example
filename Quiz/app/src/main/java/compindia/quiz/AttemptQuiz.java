package compindia.quiz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by compindi on 25-10-2017.
 */

public class AttemptQuiz extends AppCompatActivity {
    TextView txtQuestion;
    RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    Databse databse;
    int questionNumber = 1;
    Button btnNextQuestion;
    RadioGroup rgOptions;
    int Marks = 0;
    int count = 0;
    LinearLayout loutAttemptQuiz;
    int i = 0;
    int tableCount;
    String answer = "";
    CountDownTimer countDownTimer;
    Button btnTimer;
    TextView txtAttempt;
ProgressBar pbar;
    int questionsRemain=5;
    int progress=1;
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt);
        databse = new Databse(this);
        init();
        txtAttempt.setText(questionsRemain+" Questions Remaining");
        getData(questionNumber);
       // timer();
        btnNextQuestion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
               // countDownTimer.onFinish();
                if (rgOptions.getCheckedRadioButtonId() != -1) {
                    String useranswer = getRadioButtonValue();
                    answer = getData(questionNumber);
                    pbar.setProgress(progress++);
                    txtAttempt.setText(--questionsRemain+" Questions Remaining");
                    if (useranswer.equals(answer)) {
                        ++Marks;
                    }
                    if (count == 4) {
                        popupResult();
                    }
                    ++count;
                    getData(++questionNumber);


                } else {
                    if (count == 4) {
                        popupResult();
                    } else {

                        Snackbar snackbar = Snackbar.make(loutAttemptQuiz, "Are you sure want to go for next question", Snackbar.LENGTH_LONG);
                        snackbar.setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getData(++questionNumber);
                                ++count;
                                pbar.setProgress(progress++);
                                txtAttempt.setText(--questionsRemain+" Questions Remaining");
                            }
                        });
                        snackbar.show();
                        snackbar.setActionTextColor(getResources().getColor(R.color.green));
                    }
                }
            }
        });
    }

    String getData(int quenum) {
        Cursor cursor = databse.getQuestions(quenum);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            String question = cursor.getString(cursor.getColumnIndex(Databse.QUESTION));
            String option1 = cursor.getString(cursor.getColumnIndex(Databse.OPTION1));
            String option2 = cursor.getString(cursor.getColumnIndex(Databse.OPTION2));
            String option3 = cursor.getString(cursor.getColumnIndex(Databse.OPTION3));
            String option4 = cursor.getString(cursor.getColumnIndex(Databse.OPTION4));
            String answer = cursor.getString(cursor.getColumnIndex(Databse.ANSWER));
            txtQuestion.setText("Question "+questionNumber+" : "+question);
            rbOption1.setText(option1);
            rbOption2.setText(option2);
            rbOption3.setText(option3);
            rbOption4.setText(option4);
            return answer;
        }
        return "";
    }

    void init() {
        txtQuestion = (TextView) findViewById(R.id.txt_question);
        rbOption1 = (RadioButton) findViewById(R.id.rb_option1);
        rbOption2 = (RadioButton) findViewById(R.id.rb_option2);
        rbOption3 = (RadioButton) findViewById(R.id.rb_option3);
        rbOption4 = (RadioButton) findViewById(R.id.rb_option4);
        btnNextQuestion = (Button) findViewById(R.id.btn_next_question);
        rgOptions = (RadioGroup) findViewById(R.id.rg__options);
      //  btnTimer = (Button) findViewById(R.id.btn_timer);
        loutAttemptQuiz = (LinearLayout) findViewById(R.id.lout_attempt_quiz);
        pbar= (ProgressBar) findViewById(R.id.pbar);
        txtAttempt= (TextView) findViewById(R.id.txt_attempt);
        //  getAnsersFromDatabase();
    }

    void popupResult() {
        LayoutInflater inflater = (LayoutInflater) AttemptQuiz.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.popup_result, null);
        PopupWindow popupWindow = new PopupWindow(popup, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(loutAttemptQuiz, Gravity.CENTER, 0, 0);
        loutAttemptQuiz.setVisibility(View.INVISIBLE);
        ImageView imgCongrates= (ImageView)popup.findViewById(R.id.img_congrates);
        ImageView imgFail= (ImageView)popup.findViewById(R.id.img_fail);
        TextView txtResult = (TextView) popup.findViewById(R.id.txt_result);

        if (Marks<=2)
{
   imgCongrates.setVisibility(View.GONE);
    imgFail.setVisibility(View.VISIBLE);
    txtResult.setText("You got " + Marks + " out of 5");
    txtResult.setTextColor(getResources().getColor(R.color.red));
}
if (Marks>=3)
{
    imgCongrates.setVisibility(View.VISIBLE);
    imgFail.setVisibility(View.GONE);
    txtResult.setText("You got " + Marks + " out of 5");
    txtResult.setTextColor(getResources().getColor(R.color.green));


}
        Button btnClose = (Button) popup.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttemptQuiz.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnVisit = (Button) popup.findViewById(R.id.btn_take_quiz);
        btnVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttemptQuiz.this, AttemptQuiz.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar.make(loutAttemptQuiz, "Are you sure want to quit Quiz",
                Snackbar.LENGTH_LONG).setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AttemptQuiz.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
    }

    void timer() {
            countDownTimer = new CountDownTimer(30000, 1000) {
                int time = 10;

                @Override
                public void onTick(long l) {
                    if (time == 0) {
                        onFinish();
                    }
                    btnTimer.setText("00 : 0" + time--);
                }

                @Override
                public void onFinish()
                {
                    if (count==4)
                    {
                        popupResult();
                    }
                    else {
                        timer();
                        getData(++questionNumber);
                        time = 10;
                    }
                }
            }.start();
    }

    String getRadioButtonValue() {
        int id = rgOptions.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(id);
        String rbValue = radioButton.getText().toString();
        rgOptions.clearCheck();
        return rbValue;
    }
//    void  getAnsersFromDatabase()
//    {
//        Cursor cursor=databse.getAnswers();
//        if (cursor!=null&&cursor.getCount()>0)
//        {
//            cursor.moveToFirst();
//            for (int i = 0; i<cursor.getCount(); i++)
//           {
//               answers[i]=cursor.getString(i);
//               Toast.makeText(AttemptQuiz.this,answers[i],Toast.LENGTH_SHORT).show();
//
//           }
//        }
//    }
//    void countResult(String value)
//    {
//
//    }

}
