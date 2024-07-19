package com.example.and103_lab1_ducvvph21672;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginWithPhone extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    private String mVerificationId;
    private EditText phoneEditText, otpEditText;
    private Button sendOtpButton, verifyOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_with_phone);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        phoneEditText = findViewById(R.id.phoneEditText);
        otpEditText = findViewById(R.id.otpEditText);
        sendOtpButton = findViewById(R.id.sendOtpButton);
        verifyOtpButton = findViewById(R.id.verifyOtpButton);

        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Auto-retrieval or instant verification
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginWithPhone.this, "Verification Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, token);
                mVerificationId = verificationId;
                Toast.makeText(LoginWithPhone.this, "OTP sent.", Toast.LENGTH_SHORT).show();
            }
        };

        sendOtpButton.setOnClickListener(v -> {
            String phoneNumber = phoneEditText.getText().toString().trim();
            if (!phoneNumber.isEmpty()) {
                getOTP(phoneNumber);
            } else {
                Toast.makeText(LoginWithPhone.this, "Please enter a valid phone number.", Toast.LENGTH_SHORT).show();
            }
        });

        verifyOtpButton.setOnClickListener(v -> {
            String otp = otpEditText.getText().toString().trim();
            if (!otp.isEmpty() && mVerificationId != null) {
                verifyOTP(mVerificationId, otp);
            } else {
                Toast.makeText(LoginWithPhone.this, "Please enter the OTP.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOTP(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOTP(String verificationId, String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginWithPhone.this, "Authentication Successful.", Toast.LENGTH_SHORT).show();
                        // Navigate to main activity
                         Intent intent = new Intent(LoginWithPhone.this, MainActivity.class);
                         startActivity(intent);
                         finish();
                    } else {
                        Toast.makeText(LoginWithPhone.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
