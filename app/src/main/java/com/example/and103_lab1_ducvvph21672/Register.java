package com.example.and103_lab1_ducvvph21672;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Áp dụng WindowInsets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Liên kết các trường nhập liệu và nút đăng ký
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);

        // Thiết lập sự kiện cho nút đăng ký
        registerButton.setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Gọi hàm tạo tài khoản mới
            registerNewUser(email, password);
        });
    }

    private void registerNewUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Lấy thông tin tài khoản mới vừa đăng ký
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
