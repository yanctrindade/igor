package br.com.yimobile.igor.screens.auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.yimobile.igor.R;
import br.com.yimobile.igor.screens.container.ContainerActivity;
import database.User;

public class LoginActivity extends AppCompatActivity
        implements LoginFragment.OnLoginInteractionListener, RegisterFragment.OnRegisterInteractionListener {

    private static final int RC_SIGN_IN = 0;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;

    Fragment fragmentLogin = new LoginFragment();
    Fragment fragmentRegister = new RegisterFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addFragment(fragmentLogin);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_face);
        loginButton.setReadPermissions("email", "public_profile", "user_birthday");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                Log.d("LOGIN", "facebook:onSuccess:" + loginResult);
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                handleFacebookAccessToken(loginResult.getAccessToken(), object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("CANCEL", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("ERROR", "facebook:onError", error);
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        // connection failed, should be handled
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Set the dimensions of the sign-in button.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

    }

    public void onClickFragmentRegister(View view) {
        replaceFragment(fragmentRegister);
    }

    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.layout_fragments, fragment);
        //transaction.addToBackStack(null);

        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.layout_fragments, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser); //comentar aqui caso nao queira conectar direto ao abrir
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        } else{
            // Pass the activity navDrawer back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookAccessToken(AccessToken token, JSONObject object) {
        Log.d("HANDLE_FACE", "handleFacebookAccessToken:" + token);

        try {
            final User user = new User(object.getString("email"), object.getString("name"),
                    object.getString("birthday"), object.getString("gender"));
            if(!user.getNascimento().isEmpty()){
                Calendar aux = stringToDate(user.getNascimento(), "MM/dd/yyyy");
                user.setNascimento(dateToString(aux, "dd/MM/yyyy"));
            }
            AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            testTask(task, "signInWithCredential", user);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loginUser(String email, String senha){
        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        testTask(task, "signInWithEmail", null);
                    }
                });
    }

    private void registerUser(String email, String senha, String nome, String data, String sexo){
        final User user = new User(email, nome, data, sexo);
        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        testTask(task, "createUserWithEmail", user);
                    }
                });
    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GOOGLE", "firebaseAuthWithGoogle:" + acct.getId());

        final User user = new User(acct.getEmail(), acct.getDisplayName(), "", "");
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        testTask(task, "signInWithCredential", user);
                    }
                });
    }

    private void testTask(Task<AuthResult> task, String logTask, User user){
        if (task.isSuccessful()) {
            // Sign in success, update UI with the signed-in user's information
            Log.d("SUCCESS", logTask + ":success");
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if(user != null) writeNewUser(FirebaseAuth.getInstance().getCurrentUser().getUid(), user);
            updateUI(firebaseUser);
        } else {
            // If sign in fails, display a message to the user.
            Log.w("FAILURE", logTask + ":failure", task.getException());
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    private void writeNewUser(final String userId, final User user) {
        Query dataUser = mDatabase.child("users").orderByKey().equalTo(userId);
        dataUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() < 1){
                    mDatabase.child("users").child(userId).setValue(user);
                    Log.d("NEWUSER", "true");
                } else Log.d("NEWUSER", "false");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("ERROR", "onCancelled", databaseError.toException());
            }
        });
    }

    private void updateUI(FirebaseUser firebaseUser){
        if(firebaseUser != null){
            startActivity(new Intent(LoginActivity.this, ContainerActivity.class));
            finish();
        }
    }

    @Override
    public void onLoginInteraction(String email, String senha) {
        loginUser(email, senha);
    }

    @Override
    public void onRegisterInteraction(String email, String senha, String nome, String data, String sexo) {
        registerUser(email, senha, nome, data, sexo);
    }

    public static Calendar stringToDate(String d, String f){
        if(d == null || f == null || f.isEmpty()) return null;
        Calendar cal = Calendar.getInstance();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(f, Locale.ENGLISH);
            cal.setTime(formatter.parse(d));
        } catch (ParseException e) {
            cal = null;
        }
        return cal;
    }

    public static String dateToString(Calendar d, String f){
        if(d == null || f == null || f.isEmpty()) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(f, Locale.ENGLISH);
        return formatter.format(d.getTime());
    }

    // Codigo para gerar chave para o facebook, caso necessario
    /*private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("jk", "Exception(NameNotFoundException) : " + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
        }
    }*/
}
