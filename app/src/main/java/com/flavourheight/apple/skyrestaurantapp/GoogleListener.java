package com.flavourheight.apple.skyrestaurantapp;

public interface GoogleListener {

    void onGoogleAuthSignIn(String authToken, String userId);

    void onGoogleAuthSignInFailed(String errorMessage);

    void onGoogleAuthSignOut();

}
