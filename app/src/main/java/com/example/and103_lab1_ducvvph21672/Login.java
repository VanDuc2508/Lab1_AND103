package com.example.and103_lab1_ducvvph21672;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button forgotPasswordButton;
    private TextView signup;

    private Button loginWPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Liên kết các trường nhập liệu và nút đăng nhập
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        signup = findViewById(R.id.signUp);
        loginWPhone = findViewById(R.id.loginWPhoneButton);

        loginWPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, LoginWithPhone.class);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
            }
        });
        // Thiết lập sự kiện cho nút đăng nhập
        loginButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Gọi hàm đăng nhập
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập email và mật khẩu!", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Thiết lập sự kiện cho nút quên mật khẩu
        forgotPasswordButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập email để đặt lại mật khẩu!", Toast.LENGTH_SHORT).show();
            } else {
                resetPassword(email);
            }
        });
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công, cập nhật giao diện người dùng
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            // Chuyển sang hoạt động khác sau khi đăng nhập thành công
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Email đặt lại mật khẩu đã được gửi!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w(TAG, "sendPasswordResetEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Gửi email đặt lại mật khẩu thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
