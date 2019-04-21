

package com.example.drmarker.userModel;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmModule;

/**
 * Created by littlecurl 2018/6/24
 */


public class User extends RealmObject {
    private String name;            //user name
    private String password;        //password
    @PrimaryKey
    private String uid;                //User id

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        //Generate a distinct uuid as user id
        uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
    public User (){

    }




    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }



    public String getPassword() {
        return password;
    }



    public void setPassword(String password) {
        this.password = password;
    }


    public String getUid() {
        return uid;
    }



    public void setUid(String uid) {
        this.uid = uid;
    }



    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

