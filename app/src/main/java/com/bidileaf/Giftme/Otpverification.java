package com.bidileaf.Giftme;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Otpverification extends AppCompatActivity {


    private EditText otpInput;
    private Button btnVerifyOTP;
    private TextView resendOtpText;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_otpverification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ///



        otpInput = findViewById(R.id.otpInput);
        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);
        resendOtpText = findViewById(R.id.resendOtpText);
        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");
        String phoneNumber = getIntent().getStringExtra("phoneNumber");
        // Don't start sendVerificationCode(phoneNumber) again!

        if (verificationId == null) {
            Toast.makeText(this, "Error: Verification ID is missing.", Toast.LENGTH_LONG).show();
            finish(); // Close activity if no verification ID
        }

        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpInput.getText().toString().trim();
                if (TextUtils.isEmpty(code) || code.length() < 6) {
                    otpInput.setError("Enter valid OTP");
                    return;
                }
                verifyCode(code);
            }
        });

        resendOtpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode(phoneNumber);
                Toast.makeText(Otpverification.this, "OTP has been resent. Please check your messages.", Toast.LENGTH_SHORT).show();
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
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        String code = credential.getSmsCode();
                        if (code != null) {
                            otpInput.setText(code);
                            verifyCode(code);
                        }
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(Otpverification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(s, token);
                        verificationId = s; // Save verification ID globally
                        // Show a toast message
                        Toast.makeText(Otpverification.this, "OTP resent successfully!", Toast.LENGTH_SHORT).show();

                    }
                }
        );



    }

    private void verifyCode(String code) {
        if (verificationId == null) {
            Toast.makeText(this, "Verification ID is missing. Please try again.", Toast.LENGTH_SHORT).show();
            return; // Prevent crash
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }


    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();
                            String phone = user.getPhoneNumber();

                            // Save to Firebase Database
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                            UserModel userModel = new UserModel(uid, null, phone); // Email is null for phone sign-in
                            databaseReference.child(uid).setValue(userModel)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            Toast.makeText(Otpverification.this, "User saved to database", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(Otpverification.this, "Database error: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    });

                            // Proceed to HomeActivity
                            Toast.makeText(Otpverification.this, "Verification Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Otpverification.this, HomeActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(Otpverification.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}