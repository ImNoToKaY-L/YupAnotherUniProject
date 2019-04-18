package com.example.drmarker.RealmModule;

import com.example.drmarker.User;

import io.realm.annotations.RealmModule;

@RealmModule(classes = { User.class })
public class UserModule {
}