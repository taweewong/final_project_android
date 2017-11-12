package kmitl.taweewong.teamtaskboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.Profile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kmitl.taweewong.teamtaskboard.services.LoginService;
import kmitl.taweewong.teamtaskboard.utilities.ProgressSpinner;

public class LoginActivity extends AppCompatActivity implements LoginService.OnLoginFacebookCompleteListener,
    LoginService.OnVerifyFacebookAuthenticationListener {
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

        loginService.verifyFacebookAuthentication(this);
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
    public void onAuthenticated() {
        Toast.makeText(this, "Authenticated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLoginFacebookSuccess(Profile facebookProfile) {
        Toast.makeText(this, "login: " + facebookProfile.getFirstName(), Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
    }

    @Override
    public void onLoginFacebookFailed() {
        Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
    }

    @Override
    public void onLoginFacebookCancelled() {
        Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
    }
}
