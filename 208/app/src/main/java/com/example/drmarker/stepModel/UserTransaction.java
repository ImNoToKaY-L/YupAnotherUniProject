package com.example.drmarker.stepModel;

import android.util.Log;

import com.example.drmarker.User;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by gojuukaze on 16/8/19.
 * Email: i@ikaze.uu.me
 */
public class UserTransaction implements Realm.Transaction {

    private String name;
    private String password;
    private String uid;

    public UserTransaction(String Name, String password, String uid) {
        this.name = name;
        this.password = password;
        this.uid = uid;
    }

    @Override
    public void execute(Realm realm) {
        Log.d("realm", "now insert [" + name + " ," + password + uid+"]");

        User user = realm.where(User.class).equalTo("uid",uid).findFirst();
        RealmResults<User> people = realm.where(User.class).findAll();
        for (User s:people){
            Log.d("DEBUG", s.getUid()+" "+s.getName()+" "+s.getPassword());
        }

        if (user == null)
            user = realm.createObject(User.class);
        user.setName(name);
        user.setPassword(password);
        user.setUid(uid);
    }
}
