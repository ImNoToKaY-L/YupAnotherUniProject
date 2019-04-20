package com.example.drmarker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.realm.*;

import com.example.drmarker.R;
import com.example.drmarker.RealmModule.UserModule;
import com.example.drmarker.stepModel.StepModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.annotations.RealmModule;

/**
 * Created by littlecurl 2018/6/24
 */
public class loginActivity extends AppCompatActivity {

    private RealmConfiguration userDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        userDB = new RealmConfiguration.Builder()
                .name("user_db").schemaVersion(2).modules(new UserModule())
                .build();


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
            case R.id.StepTest:
                startActivity(new Intent(this, StepActivity.class));
                finish();
                break;
            //TODO 返回箭头功能
            case R.id.tv_loginactivity_register:
                //TODO 注册界面跳转
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            //case R.id.tv_loginactivity_forget:   //忘记密码
            //TODO 忘记密码操作界面跳转
            //    startActivity(new Intent(this, FindPasswordActivity.class));
            //    break;
            //case R.id.tv_loginactivity_check:    //短信验证码登录
            // TODO 短信验证码登录界面跳转

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
                            break;
                        }else{
                            match = false;
                        }
                    }
                    if (match) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        intent.putExtra("uid",user.getUid());
                        startActivity(intent);
                        finish();//销毁此Activity
                    }else {
                        Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
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

