package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.stepModel.SuccessTransaction;
import com.example.drmarker.stepModel.UserTransaction;
import com.example.drmarker.userModel.User;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class EditNameActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_newName;
    public static String uid;
    private RealmConfiguration userDB;
    private User loginUser;
    private Realm mRealm;
    RealmAsyncTask realmAsyncTask;
    private static final int NAME_NO_CHANGE = 5;
    private static final int REUSED_PASSWORD = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editname);
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
                et_newName=findViewById(R.id.et_edit_name);
                String newName=et_newName.getText().toString();

                int validateResult = nameValidate(loginUser.getName(),newName,mRealm);

                if (validateResult==NAME_NO_CHANGE){
                    Toast.makeText(this, "Enter a different name from before", Toast.LENGTH_SHORT).show();
                    et_newName.setText("");
                }else if (validateResult==RegisterActivity.USERNAME_EXIST){
                    Toast.makeText(this, "Username exist, select another one", Toast.LENGTH_SHORT).show();
                    et_newName.setText("");
                }else if(validateResult==RegisterActivity.USERNAME_INVALID){
                    Toast.makeText(this, "User name length should be 3 to 12", Toast.LENGTH_SHORT).show();
                    et_newName.setText("");
                }else {


                    //Initialize an intent to the receive class
                    Intent intent_editName=new Intent(EditNameActivity.this, MeActivity.class);
                    intent_editName.putExtra("uid",getIntent().getStringExtra("uid"));
                    //Get the input from text view

                    //ut.execute(mRealm);

                    realmAsyncTask = mRealm.executeTransactionAsync(
                            new UserTransaction(newName, loginUser.getPassword(), loginUser.getUid()),
                            new SuccessTransaction(realmAsyncTask),
                            new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    error.printStackTrace();
                                    Log.d("realm", "insert error");
                                }
                            }
                    );
                    Toast.makeText(this, "Username changed to "+newName, Toast.LENGTH_LONG).show();
                    mRealm.close();
                    startActivity(intent_editName);
                    finish();
                }

                break;

            case R.id.bt_back:
                Intent intent_back=new Intent(EditNameActivity.this, MeActivity.class);
                intent_back.putExtra("uid",getIntent().getStringExtra("uid"));
                startActivity(intent_back);
                finish();
        }
    }

    private int nameValidate(String previousName,String name,Realm DB){
        if (previousName.equals(name)){
            return NAME_NO_CHANGE;
        }
        User existName = DB.where(User.class).equalTo("name",name).findFirst();
        if (existName!=null){
            return RegisterActivity.USERNAME_EXIST;
        }
        if(name.length() < 3 || name.length() > 12){
            return RegisterActivity.USERNAME_INVALID;
        }
        return RegisterActivity.VALID_USER;
    }
}
