package kmitl.taweewong.teamtaskboard.services;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import kmitl.taweewong.teamtaskboard.models.User;

public class LoginService {

    public interface OnLoginFacebookCompleteListener {
        void onLoginFacebookSuccess(Profile facebookProfile);
        void onLoginFacebookFailed(String message);
        void onLoginFacebookCancelled();
    }

    public interface OnVerifyFacebookAuthenticationListener {
        void onAuthenticated();
    }

    private Activity activity;
    private OnLoginFacebookCompleteListener loginFacebookListener;
    private Profile facebookProfile;
    private AccessToken accessToken;
    private FirebaseAuth firebaseAuth;

    public LoginService(Activity activity, CallbackManager callbackManager) {
        this.activity = activity;
        accessToken = AccessToken.getCurrentAccessToken();
        firebaseAuth = FirebaseAuth.getInstance();
        setupFacebookLoginManager(callbackManager);
    }

    private void setupFacebookLoginManager(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        loginFacebookListener.onLoginFacebookCancelled();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        loginFacebookListener.onLoginFacebookFailed(exception.getMessage());
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

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            facebookProfile = Profile.getCurrentProfile();
                            findFacebookUser(user.getUid(), facebookProfile);
                        } else {
                            loginFacebookListener.onLoginFacebookFailed(getAuthenticationTaskErrorMessage(task));
                        }
                    }
                });
    }

    private void findFacebookUser(final String uid, final Profile profile) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("users").hasChild(uid)) {
                    Toast.makeText(activity, "Welcome back to TeamTaskBoard", Toast.LENGTH_SHORT).show();
                } else {
                    User newUser = createNewUser(profile);
                    myRef.child("users").child(uid).setValue(newUser);
                }

                loginFacebookListener.onLoginFacebookSuccess(facebookProfile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loginFacebookListener.onLoginFacebookCancelled();
            }
        });
    }

    private User createNewUser(Profile profile) {
        User newUser = new User();
        newUser.setFirstName(profile.getFirstName());
        newUser.setLastName(profile.getLastName());

        return newUser;
    }

    private String getAuthenticationTaskErrorMessage(Task<AuthResult> task) {
        final String[] message = new String[1];

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                message[0] = e.getMessage();
            }
        });

        return message[0];
    }
}
