

package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.userModel.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class RegisterActivity extends AppCompatActivity{

    private String realCode;
    public static final int USERNAME_EXIST = 0;
    public static final int INVALID_PASSWORD = 1;
    public static final int VALID_USER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

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

    /**
     * 注册页面能点击的就三个地方
     * top处返回箭头、刷新验证码图片、注册按钮
     */
    @OnClick({
            R.id.iv_registeractivity_back,
//            R.id.iv_registeractivity_showCode,
            R.id.bt_registeractivity_register
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, loginActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = mEtRegisteractivityUsername.getText().toString().trim();
                String password = mEtRegisteractivityPassword1.getText().toString().trim();
                String re_entered_pw = mEtRegisteractivityPassword2.getText().toString().trim();
//                String phoneCode = mEtRegisteractivityPhonecodes.getText().toString().toLowerCase();
                //注册验证


                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password) ) {
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


                        Intent intent2 = new Intent(this, MainActivity.class);
                        intent2.putExtra("uid",user.getUid());
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "Registration succeed ", Toast.LENGTH_SHORT).show();
                    }

                    //将用户名和密码加入到数据库中


                }else {
                    Toast.makeText(this, "Compulsory blanks are not filled", Toast.LENGTH_SHORT).show();
                }
                break;
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

