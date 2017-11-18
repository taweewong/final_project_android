package kmitl.taweewong.teamtaskboard.controllers.activities;

import android.content.Intent;
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

        progressSpinner.show();
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
    public void onLoginFacebookSuccess(User user) {
        Toast.makeText(this, "login: " + user.getFirstName(), Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
        startProjectActivity();
    }

    @Override
    public void onLoginFacebookFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
    }

    @Override
    public void onLoginFacebookCancelled() {
        Toast.makeText(this, "Login cancelled", Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
    }

    @Override
    public void onUnauthorized() {
        Toast.makeText(this, "You haven't login yet", Toast.LENGTH_SHORT).show();
        progressSpinner.hide();
    }

    private void startProjectActivity() {
        Intent intent = new Intent(this, ProjectActivity.class);
        startActivity(intent);
        finish();
    }
}
