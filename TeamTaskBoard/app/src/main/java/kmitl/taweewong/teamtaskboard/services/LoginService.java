package kmitl.taweewong.teamtaskboard.services;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LoginService {

    public interface OnLoginFacebookComplete {
        void onLoginFacebookSuccess(Profile facebookProfile);
        void onLoginFacebookFailed();
        void onLoginFacebookCancelled();
    }

    private Activity activity;
    private OnLoginFacebookComplete listener;
    private Profile facebookProfile;
    private AccessToken accessToken;

    public LoginService(Activity activity, CallbackManager callbackManager, OnLoginFacebookComplete listener) {
        this.activity = activity;
        this.listener = listener;
        accessToken = AccessToken.getCurrentAccessToken();
        setupFacebookLoginManager(callbackManager);
    }

    private void setupFacebookLoginManager(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        facebookProfile = Profile.getCurrentProfile();
                        listener.onLoginFacebookSuccess(facebookProfile);
                    }

                    @Override
                    public void onCancel() {
                        listener.onLoginFacebookCancelled();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        listener.onLoginFacebookFailed();
                    }
                });
    }

    public void loginFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("email", "public_profile"));
    }

    public boolean isAlreadyLoginFacebook() {
        return accessToken != null;
    }
}
