

package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.Step.StepService;
import com.example.drmarker.userModel.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class RegisterActivity extends AppCompatActivity{

    public static final int USERNAME_EXIST = 0;
    public static final int INVALID_PASSWORD = 1;
    public static final int VALID_USER = 2;
    private boolean isGuest;
    private Intent fromAbove;
    private Spinner questionSpinner;
    private ArrayAdapter<String> adapter_Question;
    private boolean questionSelected;
    private String question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fromAbove = getIntent();
        isGuest= fromAbove.getBooleanExtra("guest",false);
        ButterKnife.bind(this);

        questionSpinner = (Spinner) findViewById(R.id.security_spinner);
        adapter_Question = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initQuestion());
        questionSpinner.setAdapter(adapter_Question);

        questionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                question = adapter_Question.getItem(position);
                Log.d("position", position + "");
                if (question.equals(getString(R.string.question_none))) {
                    questionSelected = false;
                } else {
                    questionSelected = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                questionSelected = false;
            }
        });
    }


    private List<String> initQuestion() {
        List<String> questionList = new ArrayList<>();
        questionList.add("Please select a security question");
        questionList.add("What's your mother's name?");
        questionList.add("What's your father's name?");
        questionList.add("What's the street name of your home?");
        questionList.add("What's your girl/boyfriend's name?");
        return questionList;
    }

    @BindView(R.id.rl_registeractivity_top)
    RelativeLayout mRlRegisteractivityTop;
    @BindView(R.id.iv_registeractivity_back)
    ImageView mIvRegisteractivityBack;
    @BindView(R.id.ll_registeractivity_body)
    LinearLayout mLlRegisteractivityBody;
    @BindView(R.id.et_registeractivity_username)
    EditText mEtRegisteractivityUsername;
    @BindView(R.id.et_registeractivity_password1)
    EditText mEtRegisteractivityPassword1;
    @BindView(R.id.et_registeractivity_password2)
    EditText mEtRegisteractivityPassword2;
    //    @BindView(R.id.et_registeractivity_phoneCodes)
//    EditText mEtRegisteractivityPhonecodes;
//    @BindView(R.id.iv_registeractivity_showCode)
//    ImageView mIvRegisteractivityShowcode;
    @BindView(R.id.rl_registeractivity_bottom)
    RelativeLayout mRlRegisteractivityBottom;
    @BindView(R.id.security_answer)
    EditText securityAnswer;

    @OnClick({
            R.id.iv_registeractivity_back,
//            R.id.iv_registeractivity_showCode,
            R.id.bt_registeractivity_register
    })

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back:

                if (isGuest){
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("uid",fromAbove.getStringExtra("uid"));
                    intent.putExtra("guest",true);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent1 = new Intent(this, loginActivity.class);
                    startActivity(intent1);
                    finish();
                }


                break;

            case R.id.bt_registeractivity_register:

                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword1.getText().toString().trim();
                String re_entered_pw = mEtRegisteractivityPassword2.getText().toString().trim();
                String security_Answer = securityAnswer.getText().toString().trim();
//                String phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();



                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) && questionSelected && !TextUtils.isEmpty(security_Answer)) {
                    int validateResult = userValidate(username,password);
                    if (validateResult==USERNAME_EXIST){
                        Toast.makeText(this, "Username is duplicate", Toast.LENGTH_SHORT).show();
                        mEtRegisteractivityUsername.setText("");
                    }else if (validateResult==INVALID_PASSWORD){
                        Toast.makeText(this,"Password length should be within 5-16 characters",Toast.LENGTH_LONG).show();
                        mEtRegisteractivityPassword1.setText("");
                        mEtRegisteractivityPassword2.setText("");
                    }else if (!password.equals(re_entered_pw)){
                        Toast.makeText(this, "Passwords are not the same", Toast.LENGTH_SHORT).show();
                        mEtRegisteractivityPassword2.setText("");
                    }else if (validateResult == VALID_USER){
                        Realm mRealm=Realm.getInstance(new RealmConfiguration.Builder()
                                .name("user_db").schemaVersion(2).modules(new UserModule())
                                .build());
                        final User user = new User(username,password);

                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealm(user);
                            }
                        });

                        if (isGuest){
                            Intent i = new Intent(RegisterActivity.this, StepService.class);
                            stopService(i);
                        }

                        Intent intent2 = new Intent(this, MainActivity.class);
                        intent2.putExtra("uid",user.getUid());
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "Registration succeed ", Toast.LENGTH_SHORT).show();
                    }


                }else {
                    Toast.makeText(this, "Compulsory blanks are not filled", Toast.LENGTH_SHORT).show();
                }
                break;
                case R.id.bt_registeractivity_login:
                    Intent intent = new Intent(this,loginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isGuest){
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            intent.putExtra("uid",fromAbove.getStringExtra("uid"));
            intent.putExtra("guest",true);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(RegisterActivity.this,loginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private static int userValidate(String username, String password){
        Realm mRealm=Realm.getInstance(new RealmConfiguration.Builder()
                .name("user_db").schemaVersion(2).modules(new UserModule())
                .build());

        User existName = mRealm.where(User.class).equalTo("name",username).findFirst();
        if (existName!=null){
            return USERNAME_EXIST;
        }else if (password.length()<=5||password.length()>=16){
            return INVALID_PASSWORD;
        }
        return VALID_USER;

    }


}

