package kmitl.taweewong.teamtaskboard.services;

import android.app.Activity;
import android.support.annotation.NonNull;

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
        void onLoginFacebookSuccess(User currentUser);
        void onLoginFacebookFailed(String message);
        void onLoginFacebookCancelled();
        void onUnauthorized();
    }

    private final String CHILD_USERS = "users";

    private Activity activity;
    private OnLoginFacebookCompleteListener loginFacebookListener;
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

    public void verifyFacebookAuthentication(OnLoginFacebookCompleteListener listener) {
        if (accessToken != null) {
            this.loginFacebookListener = listener;
            handleFacebookAccessToken(accessToken);
        } else {
            listener.onUnauthorized();
        }
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Profile facebookProfile = Profile.getCurrentProfile();
                            findFacebookUser(user, facebookProfile);
                        } else {
                            loginFacebookListener.onLoginFacebookFailed(getAuthenticationTaskErrorMessage(task));
                        }
                    }
                });
    }

    private void findFacebookUser(final FirebaseUser user, final Profile profile) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(CHILD_USERS).hasChild(user.getUid())) {
                    User existUser = dataSnapshot.child(CHILD_USERS).child(user.getUid()).getValue(User.class);
                    loginFacebookListener.onLoginFacebookSuccess(existUser);
                } else {
                    User newUser = createNewUser(user, profile);
                    myRef.child(CHILD_USERS).child(user.getUid()).setValue(newUser);
                    loginFacebookListener.onLoginFacebookSuccess(newUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loginFacebookListener.onLoginFacebookCancelled();
            }
        });
    }

    private User createNewUser(FirebaseUser user, Profile profile) {
        final User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(profile.getFirstName());
        newUser.setLastName(profile.getLastName());
        newUser.setUserId(user.getUid());

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
