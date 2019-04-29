package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.stepModel.SuccessTransaction;
import com.example.drmarker.stepModel.UserTransaction;
import com.example.drmarker.userModel.User;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EditPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_newPassword,et_oldPassword,et_confirmPassword,et_security_answer;
    public static String uid;
    private RealmConfiguration userDB;
    private User loginUser;
    private Realm mRealm;
    private static final int REUSED_PASSWORD = 3;
    RealmAsyncTask realmAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpassword);
        getInfo();
        //Get the intent from the input activity
        Intent fromAbove = getIntent();
        uid= fromAbove.getStringExtra("uid");
        userDB = new RealmConfiguration.Builder()
                .name("user_db").schemaVersion(2).modules(new UserModule()).deleteRealmIfMigrationNeeded()
                .build();
        mRealm=Realm.getInstance(userDB);
        RealmResults<User> userlist = mRealm.where(User.class).equalTo("uid", uid).findAll();
        loginUser = userlist.get(0);
        TextView tv_security_question = findViewById(R.id.tv_security_question);
        tv_security_question.setText(loginUser.getSecurity_question());
    }

    private void getInfo(){
        //Initialize the button
        Button button_edit = findViewById(R.id.bt_edit);
        Button button_back = findViewById(R.id.bt_back);
        button_edit.setOnClickListener(this);
        button_back.setOnClickListener(this);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_edit:

                et_newPassword=findViewById(R.id.et_edit_password);
                et_oldPassword=findViewById(R.id.et_old_password);
                et_confirmPassword=findViewById(R.id.et_confirm_password);
                et_security_answer = findViewById(R.id.et_security_answer);
                String oldPassword=et_oldPassword.getText().toString();
                String newPassword=et_newPassword.getText().toString();
                String confirmPassword=et_confirmPassword.getText().toString();
                String securityAnswer = et_security_answer.getText().toString().trim();
                if(!oldPassword.equals(loginUser.getPassword())){
                    Toast.makeText(this, "Wrong original password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!confirmPassword.equals(newPassword)){
                    Toast.makeText(this, "Two passwords are inconsistent", Toast.LENGTH_SHORT).show();
                    et_confirmPassword.setText("");
                    return;
                }

                if (!securityAnswer.equals(loginUser.getSecurity_answer())){
                    Toast.makeText(this, "Wrong security answer", Toast.LENGTH_SHORT).show();
                    et_security_answer.setText("");
                    return;
                }


                int validateResult = passwordValidate(loginUser.getPassword(),newPassword);
                if (validateResult==RegisterActivity.INVALID_PASSWORD){
                    Toast.makeText(this, "Password invalid", Toast.LENGTH_SHORT).show();
                    et_newPassword.setText("");
                    et_confirmPassword.setText("");
                }
                else if (validateResult==REUSED_PASSWORD){
                    Toast.makeText(this, "Password is the same as previous one", Toast.LENGTH_SHORT).show();
                    et_newPassword.setText("");
                    et_confirmPassword.setText("");
                }
                else {

                    //Initialize an intent to the receive class
                    Intent intent_editPassword=new Intent(EditPasswordActivity.this, MeActivity.class);
                    intent_editPassword.putExtra("uid",getIntent().getStringExtra("uid"));
                    //Get the input from text view


                    realmAsyncTask = mRealm.executeTransactionAsync(
                            new UserTransaction(loginUser.getName(), newPassword, loginUser.getUid()),
                            new SuccessTransaction(realmAsyncTask),
                            new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    error.printStackTrace();
                                    Log.d("realm", "insert error");
                                }
                            }
                    );
                    mRealm.close();
                    Toast.makeText(this, "Password successfully changed", Toast.LENGTH_LONG).show();
                    startActivity(intent_editPassword);
                    finish();
                }
                break;

            case R.id.bt_back:
                Intent intent_back=new Intent(EditPasswordActivity.this, MeActivity.class);
                intent_back.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent_back);
                finish();
        }
    }

    private int passwordValidate(String previousPw,String password){
        if (password.length()<=5||password.length()>=16){
            return RegisterActivity.INVALID_PASSWORD;
        }
        if (previousPw.equals(password)){
            return REUSED_PASSWORD;
        }
        return RegisterActivity.VALID_USER;
    }



}
