package com.bidileaf.Giftme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Signup extends AppCompatActivity {

    private EditText phoneNumberInput;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private String verificationId;

    private GoogleSignInClient googleSignInClient;
    private SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ///

        phoneNumberInput = findViewById(R.id.phoneNumberInput);
        signUpButton = findViewById(R.id.btnSignUp);
        googleSignInButton = findViewById(R.id.btnGoogleSignIn);
        mAuth = FirebaseAuth.getInstance();


        // Initialize Google Sign-In without Web Client ID
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("105334471191-7iuhtaigc0ar5im7jth7ue99sinhcidn.apps.googleusercontent.com")
                .requestEmail()  // Only requesting email, NOT requesting ID token
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "+91" + phoneNumberInput.getText().toString().trim();
                if (phoneNumber.isEmpty() || phoneNumber.length() < 10) {
                    Toast.makeText(Signup.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendVerificationCode(phoneNumber);
            }
        });


        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signInWithGoogle();
            }
        });




    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException.class);
                if (account != null) {
                    Toast.makeText(Signup.this, "Signed in as: " + account.getEmail(), Toast.LENGTH_SHORT).show();

                    GoogleSignInAccount accountg = GoogleSignIn.getLastSignedInAccount(this);
                    if (accountg != null) {
                        firebaseAuthWithGoogle(accountg);
                    }

                }
            } catch (ApiException e) {
                Toast.makeText(Signup.this, "Google Sign-In Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // Get the ID Token from the signed-in account
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("FirebaseAuth", "Firebase Auth Success: " + user.getEmail());

                        Intent intent = new Intent(Signup.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("FirebaseAuth", "Firebase Auth Failed", task.getException());
                    }
                });
    }


    private void sendVerificationCode(String phoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // Auto-verification (for some devices)
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(Signup.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        Intent intent = new Intent(Signup.this, Otpverification.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("phoneNumber", phoneNumber);
                        startActivity(intent);
                    }
                }
        );
    }
}