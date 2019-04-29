package com.example.drmarker.RealmModule;

import com.example.drmarker.userModel.UserInformation;

import io.realm.annotations.RealmModule;

@RealmModule(classes = { UserInformation.class })

public class UserInfoModule {
}
