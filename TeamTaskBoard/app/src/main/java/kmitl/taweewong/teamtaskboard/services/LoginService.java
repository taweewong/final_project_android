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

    public interface OnLoginFacebookCompleteListener {
        void onLoginFacebookSuccess(Profile facebookProfile);
        void onLoginFacebookFailed();
        void onLoginFacebookCancelled();
    }

    public interface OnVerifyFacebookAuthenticationListener {
        void onAuthenticated();
    }

    private Activity activity;
    private OnLoginFacebookCompleteListener loginFacebookListener;
    private Profile facebookProfile;
    private AccessToken accessToken;

    public LoginService(Activity activity, CallbackManager callbackManager) {
        this.activity = activity;
        accessToken = AccessToken.getCurrentAccessToken();
        setupFacebookLoginManager(callbackManager);
    }

    private void setupFacebookLoginManager(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        facebookProfile = Profile.getCurrentProfile();
                        loginFacebookListener.onLoginFacebookSuccess(facebookProfile);
                    }

                    @Override
                    public void onCancel() {
                        loginFacebookListener.onLoginFacebookCancelled();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        loginFacebookListener.onLoginFacebookFailed();
                    }
                });
    }

    public void loginFacebook(OnLoginFacebookCompleteListener listener) {
        this.loginFacebookListener = listener;
        LoginManager.getInstance().logInWithReadPermissions(activity,
                Arrays.asList("email", "public_profile"));
    }

    public void verifyFacebookAuthentication(OnVerifyFacebookAuthenticationListener authenticationListener) {
        if (accessToken != null) {
            authenticationListener.onAuthenticated();
        }
    }
}
