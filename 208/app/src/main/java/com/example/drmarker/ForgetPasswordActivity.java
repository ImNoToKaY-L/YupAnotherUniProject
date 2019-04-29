package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.userModel.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private String uid;
    private User currentUser;
    private EditText et_username,et_new_password,et_confirm_password,et_security_answer;
    private TextView tv_security_question;
    private Realm users;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra("uid")){
            uid = intent.getStringExtra("uid");
            setContentView(R.layout.activity_forget);
            users = Realm.getInstance(new RealmConfiguration.Builder()
                    .name("user_db").schemaVersion(2).modules(new UserModule()).
                            deleteRealmIfMigrationNeeded().build());
            tv_security_question = findViewById(R.id.tv_security_question);
            currentUser = users.where(User.class).equalTo("uid",uid).findFirst();
            tv_security_question.setText(currentUser.getSecurity_question());

        }else {
            setContentView(R.layout.activity_forget_input_name);
            EditText et_name = findViewById(R.id.et_forget_edit_name);
            et_name.setHint("Enter your username to identify");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_edit_forget:
                et_username = findViewById(R.id.et_forget_edit_name);
                String username = et_username.getText().toString().trim();
                if (username.length()!=0){
                    Realm tempUsers = Realm.getInstance(new RealmConfiguration.Builder()
                            .name("user_db").schemaVersion(2).modules(new UserModule()).
                                    deleteRealmIfMigrationNeeded().build());
                    User tempUser = tempUsers.where(User.class).equalTo("name",username).findFirst();
                    if (tempUser!=null){
                        uid = tempUser.getUid();
                        Intent intent = new Intent(ForgetPasswordActivity.this,ForgetPasswordActivity.class);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(this, "No such user exists", Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(this, "Please input your username", Toast.LENGTH_SHORT).show();

                break;

            case R.id.bt_edit:
                et_new_password = findViewById(R.id.et_edit_password);
                et_confirm_password = findViewById(R.id.et_confirm_password);
                et_security_answer = findViewById(R.id.et_security_answer);
                String newPassword = et_new_password.getText().toString().trim();
                Log.d("forgetDebug", newPassword);
                String confirmPassword = et_confirm_password.getText().toString().trim();
                String answer = et_security_answer.getText().toString().trim();

                if (newPassword.length()==0&&answer.length()==0){
                    Toast.makeText(this, "Put the needed information to" +
                            "the corresponding places", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length()<=5||newPassword.length()>=16){
                    Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show();
                    et_new_password.setText("");
                    et_confirm_password.setText("");
                    return;
                }
                if (!newPassword.equals(confirmPassword)){
                    Toast.makeText(this, "Two passwords are inconsistent", Toast.LENGTH_SHORT).show();
                    et_confirm_password.setText("");
                    return;
                }
                if (!answer.equals(currentUser.getSecurity_answer())){
                    Toast.makeText(this, "Wrong security answer", Toast.LENGTH_SHORT).show();
                    et_security_answer.setText("");
                    return;
                }
                final String password = newPassword;


                users.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        User tempUser = realm.where(User.class).equalTo("uid",uid).findFirst();
                        tempUser.setPassword(password);
                    }
                });
                Toast.makeText(this, "Your password changed successfully", Toast.LENGTH_SHORT).show();
                Intent intent_main = new Intent(ForgetPasswordActivity.this,MainActivity.class);
                intent_main.putExtra("uid",uid);
                startActivity(intent_main);
                finish();
                break;
            case R.id.bt_back:
                Intent intent = new Intent(ForgetPasswordActivity.this,loginActivity.class);
                startActivity(intent);
                finish();
                break;



        }
    }
}
