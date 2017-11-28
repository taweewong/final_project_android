package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.R;
import kmitl.taweewong.teamtaskboard.models.User;
import kmitl.taweewong.teamtaskboard.services.LoginService;
import kmitl.taweewong.teamtaskboard.utilities.ProgressSpinner;

import static kmitl.taweewong.teamtaskboard.models.User.USER_CLASS_KEY;

public class LoginActivity extends AppCompatActivity implements LoginService.OnLoginFacebookCompleteListener {
    LoginService loginService;
    CallbackManager callbackManager;
    ProgressSpinner progressSpinner;

    @BindView(R.id.facebookLoginButton) Button facebookLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        callbackManager = CallbackManager.Factory.create();
        loginService = new LoginService(this, callbackManager);
        progressSpinner = new ProgressSpinner(this);

        if (isNetworkAvailable()) {
            progressSpinner.show();
            loginService.verifyFacebookAuthentication(this);
        } else {
            Toast.makeText(this, "please connect internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.facebookLoginButton)
    public void loginWithFacebook() {
        loginService.loginFacebook(this);
        progressSpinner.show();
    }

    @Override
    public void onLoginFacebookSuccess(User user) {
        Toast.makeText(this, "login: " + user.getFirstName(), Toast.LENGTH_SHORT).show();
        progressSpinner.dismiss();
        startProjectActivity(user);
    }

    @Override
    public void onLoginFacebookFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressSpinner.dismiss();
    }

    @Override
    public void onLoginFacebookCancelled() {
        Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
        progressSpinner.dismiss();
    }

    @Override
    public void onUnauthorized() {
        Toast.makeText(this, "Please Login with your Facebook", Toast.LENGTH_SHORT).show();
        progressSpinner.dismiss();
    }

    private void startProjectActivity(User user) {
        Intent intent = new Intent(this, ProjectActivity.class);
        intent.putExtra(USER_CLASS_KEY, user);
        startActivity(intent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
