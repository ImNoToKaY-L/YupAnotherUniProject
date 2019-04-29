

package com.example.drmarker.userModel;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;



public class User extends RealmObject {
    private String name;            //user name
    private String password;        //password
    @PrimaryKey
    private String uid;                //User id
    private String security_question;
    private String security_answer;

    public String getSecurity_answer() {
        return security_answer;
    }

    public String getSecurity_question() {
        return security_question;
    }

    public void setSecurity_answer(String security_answer) {
        this.security_answer = security_answer;
    }

    public void setSecurity_question(String security_question) {
        this.security_question = security_question;
    }

    public User(String name, String password,String security_question,String security_answer) {
        this.name = name;
        this.password = password;
        //Generate a distinct uuid as user id
        uid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        this.security_question = security_question;
        this.security_answer = security_answer;
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

