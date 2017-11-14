package compindia.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import static android.support.design.widget.Snackbar.make;

/**
 * Created by compindi on 24-10-2017.
 */

public class HomeActivity extends AppCompatActivity {
    Databse databse;
    TextView txtCreateQuiz;
    TextView txtAttemptQuiz;
    int count;
    LinearLayout loutHome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtCreateQuiz = (TextView) findViewById(R.id.txt_create_quiz);
        txtAttemptQuiz = (TextView) findViewById(R.id.txt_attempt);
        loutHome= (LinearLayout) findViewById(R.id.lout_home);
        databse = new Databse(this);
        count = databse.getTableCount();
        txtCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (count==5)
                {
                    popup();
                }
                else
                {
                    Intent intent=new Intent(HomeActivity.this,CreateQuizActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        txtAttemptQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count==5) {
                    Intent intent = new Intent(HomeActivity.this, AttemptQuiz.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Snackbar snackbar= make(loutHome,"No Questions available",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }
    void popup() {
        LayoutInflater inflater = (LayoutInflater) HomeActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.popup_check, null);
        PopupWindow popupWindow = new PopupWindow(popup, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(loutHome, Gravity.CENTER, 0, 0);
//        loutHome.setVisibility(View.INVISIBLE);
        Button btnAttemptQuiz = (Button) popup.findViewById(R.id.btn_attempt_quiz);
        btnAttemptQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(HomeActivity.this,AttemptQuiz.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnCreateQuiz = (Button) popup.findViewById(R.id.btn_create_quiz);
        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int c=databse.deleteQuiz();
                if (c==0) {
                    Toast.makeText(HomeActivity.this,"Old Quiz Deleted",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HomeActivity.this, CreateQuizActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
