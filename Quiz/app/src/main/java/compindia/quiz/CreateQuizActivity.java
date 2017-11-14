package compindia.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by compindi on 24-10-2017.
 */

public class CreateQuizActivity extends AppCompatActivity {
    TextView txtQuestionsNumber;
    int qustionNumber = 1;
    Button btnOptions;
    LinearLayout loutOptions;
    int optionsCount = 1;
    Button btnSubmit;
    TextView txtOptionNumber;
    int optionsNumber = 1;
    TextInputLayout loutQuestion;
    EditText edtQuestion;
    int count = 0;
    Databse databse;
    PopupWindow popupWindow;
    LinearLayout loutMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);
        btnOptions = (Button) findViewById(R.id.btn_options);
        btnOptions.setText("Option " + optionsCount++);
        loutMain = (LinearLayout) findViewById(R.id.lout_main);
        loutMain.setVisibility(View.VISIBLE);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        loutQuestion = (TextInputLayout) findViewById(R.id.lout_quetion);
        edtQuestion = (EditText) findViewById(R.id.edt_question);
        btnSubmit.setVisibility(View.INVISIBLE);
        txtQuestionsNumber = (TextView) findViewById(R.id.txt_questions);
        txtQuestionsNumber.setText("Question "+qustionNumber + "/5");
        loutOptions = (LinearLayout) findViewById(R.id.lout_options);
        databse = new Databse(this);
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = edtQuestion.getText().toString();
                if (question.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(loutMain, "Before adding Option create question first", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {
                    LayoutInflater inflater = (LayoutInflater) CreateQuizActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                    View optionsView = inflater.inflate(R.layout.options, null);
                    EditText edtOption = (EditText) optionsView.findViewById(R.id.edt_option);
                    edtOption.setId(100 + count);
                    if (count==4)
                    {
                        edtOption.setImeOptions(EditorInfo.IME_ACTION_DONE);
                    }
                    edtOption.requestFocus();
                    count++;
                    TextInputLayout loutOption = (TextInputLayout) optionsView.findViewById(R.id.lout_option);
                    loutOptions.addView(optionsView);
                    txtOptionNumber = (TextView) optionsView.findViewById(R.id.txt_number);
                    if (optionsCount <= 6) {
                        if (optionsCount == 6) {
                            loutOption.setHint("Enter Answer");
                            txtOptionNumber.setText("A.");
                        }
                        if (optionsCount <= 5) {
                            btnOptions.setText("Option " + optionsCount++);
                            txtOptionNumber.setText(optionsNumber++ + ".");
                        } else {
                            buttonVisible();
                        }
                    }
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String question = edtQuestion.getText().toString();
                String[] optionValue = {"", "", "", "", ""};
                for (int i = 0; i <5; i++) {
                    EditText edtoptionValue = (EditText) findViewById(100 + i);
                    optionValue[i] = edtoptionValue.getText().toString();
                }
                if (question.isEmpty())
                {
                    Snackbar snackbar = Snackbar.make(loutMain, "Question is Required", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                if (optionValue[0].isEmpty()||optionValue[1].isEmpty()||optionValue[2].isEmpty()||optionValue[3].isEmpty())
                {
                    Snackbar snackbar = Snackbar.make(loutMain, "Please fill all Options", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else
                    if (optionValue[4].isEmpty())
                    {
                        Snackbar snackbar = Snackbar.make(loutMain, "Answer is Required", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                    else
                        if(optionValue[4].equalsIgnoreCase(optionValue[0])||
                                optionValue[4].equalsIgnoreCase(optionValue[1])||
                                optionValue[4].equalsIgnoreCase(optionValue[2])||
                                optionValue[4].equalsIgnoreCase(optionValue[3])) {

                            if (qustionNumber <= 5) {
                                boolean boolInsert = databse.createQuestion(qustionNumber, question,
                                        optionValue[0], optionValue[1], optionValue[2], optionValue[3], optionValue[4]);
                                if (boolInsert) {
                                    Snackbar snackbar = Snackbar.make(loutMain,qustionNumber+ " Question Created", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    qustionNumber++;
                                    txtQuestionsNumber.setText("Question " + qustionNumber + "/5");
                                    loutOptions.removeAllViews();
                                    btnSubmit.setVisibility(View.GONE);
                                    btnOptions.setVisibility(View.VISIBLE);
                                    btnOptions.setText("Option 1");
                                    optionsCount = 2;
                                    optionsNumber = 1;
                                    count = 0;
                                    edtQuestion.setText("");
                                    if (qustionNumber > 5) {
                                        popup();
                                    }
                                } else {
                                    Toast.makeText(CreateQuizActivity.this, "Question Inserted Failed", Toast.LENGTH_SHORT).show();

                                }
                            }

                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(loutMain, "Answer is not Matching with above Options", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Snackbar snackbar=Snackbar.make(loutMain,"Are you sure want to cancel Creating Quiz",
                Snackbar.LENGTH_LONG).setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (qustionNumber<5) {
                    databse.deleteQuiz();
                }
                Intent intent=new Intent(CreateQuizActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        snackbar.show();
        snackbar.setActionTextColor(getResources().getColor(R.color.red));
    }

    void buttonVisible() {
        btnOptions.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    void popup() {
        LayoutInflater inflater = (LayoutInflater) CreateQuizActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.popup_que_created, null);
        popupWindow = new PopupWindow(popup, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupWindow.showAtLocation(loutMain, Gravity.CENTER, 0, 0);
        loutMain.setVisibility(View.INVISIBLE);
        Button btnYes = (Button) popup.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CreateQuizActivity.this,AttemptQuiz.class);
                startActivity(intent);
                finish();
            }
        });
        Button btnLater = (Button) popup.findViewById(R.id.btn_later);
        btnLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateQuizActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
