package com.flavourheight.apple.skyrestaurantapp;

public interface FacebookListener {

    void onFbSignInFail(String errorMessage);

    void onFbSignInSuccess(String authToken, String userId);

    void onFBSignOut();

}
