package com.example.and103_lab1_ducvvph21672;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        firebaseAuth = FirebaseAuth.getInstance();


        logoutButton = findViewById(R.id.logoutButton);


        logoutButton.setOnClickListener(view -> {
            // Đăng xuất người dùng khỏi Firebase
            firebaseAuth.signOut();


            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
