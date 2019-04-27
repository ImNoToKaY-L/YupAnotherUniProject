
package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import io.realm.RealmResults;


public class loginActivity extends AppCompatActivity {

    private RealmConfiguration userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userDB = new RealmConfiguration.Builder()
                .name("user_db").schemaVersion(2).modules(new UserModule()).deleteRealmIfMigrationNeeded()
                .build();
           Intent i = new Intent(loginActivity.this,StepService.class);
           stopService(i);

    }

    @BindView(R.id.tv_loginactivity_register)
    TextView mTvLoginactivityRegister;
    @BindView(R.id.rl_loginactivity_top)
    RelativeLayout mRlLoginactivityTop;
    @BindView(R.id.et_loginactivity_username)
    EditText mEtLoginactivityUsername;
    @BindView(R.id.et_loginactivity_password)
    EditText mEtLoginactivityPassword;
    @BindView(R.id.ll_loginactivity_two)
    LinearLayout mLlLoginactivityTwo;

    @OnClick({
            R.id.tv_loginactivity_register,
            R.id.bt_loginactivity_login,
    })

    public void onClick(View view) {
        switch (view.getId()) {
            //TODO back button activity
            case R.id.tv_loginactivity_register:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.tv_loginactivity_forget:   //Forget the password
            //TODO something
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.tv_loginactivity_check:    //Login through text
            // TODO something

            case R.id.bt_loginactivity_login:
                String name = mEtLoginactivityUsername.getText().toString().trim();
                String password = mEtLoginactivityPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    //Get the list form the realm database
                    ArrayList<User> data = (ArrayList<User>) queryAllUser();
                    boolean match = false;
                    User user = null;
                    for(int i=0;i<data.size();i++) {
                        user = data.get(i);
                        if (name.equals(user.getName()) && password.equals(user.getPassword())){
                            match = true;
                            Log.d("sqDBUG", user.getSecurity_question());
                            Log.d("sqDBUG", user.getSecurity_answer());

                            break;
                        }else{
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("uid",user.getUid());
                        startActivity(intent);
                        finish();//Destroy this Activity
                    }else {
                        Toast.makeText(this, "Invalid user name or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Please enter your user name and password", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_loginactivity_guest:
                Realm mRealm=Realm.getInstance(new RealmConfiguration.Builder()
                        .name("user_db").schemaVersion(2).modules(new UserModule()).deleteRealmIfMigrationNeeded()
                        .build());
                User tempGuest= mRealm.where(User.class).equalTo("name","Guest").findFirst();
                String GuestID;
               if (tempGuest==null){
                   final User guest = new User("Guest","1"," "," ");
                   GuestID = guest.getUid();
                   mRealm.executeTransaction(new Realm.Transaction() {
                       @Override
                       public void execute(Realm realm) {
                           realm.copyToRealm(guest);
                       }
                   });
               }else GuestID = tempGuest.getUid();

                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("uid",GuestID);
                intent.putExtra("guest",true);
                startActivity(intent);
                finish();
                break;
        }
    }


    //This method is to traverse the user list
    public List<User> queryAllUser() {
        Realm  mRealm=Realm.getInstance(userDB);
        RealmResults<User> userlist = mRealm.where(User.class).findAll();
        return mRealm.copyFromRealm(userlist);
    }

}

