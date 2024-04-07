package com.example.steganograpg_txt_to_img.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.steganograpg_txt_to_img.Utils.Constants;
import com.example.steganograpg_txt_to_img.R;
import com.example.steganograpg_txt_to_img.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if (user == null) {
            login();
        } else {
            Log.d("user display name",user.getDisplayName());
            Log.d("user uid",user.getUid());
            User.init(user.getDisplayName(), user.getUid());
            try {
                loadUser();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadUser() throws InterruptedException {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_USERS);
        Thread.sleep(1000);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isNewUser = true;
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (user.getUid().equals(snap.getKey())) {
                        User.getInstance().setUser(snap.getValue(User.class));
                        isNewUser = false;
                    }
                }
                if (isNewUser) {
                    createUser();
                }
                loadMainActivity();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
    }

    private void login() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        // Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void createUser() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference(Constants.DB_USERS);
        databaseReference.child(user.getUid()).setValue(User.getInstance());
    }


    private void loadMainActivity() {
        System.out.println(User.getInstance());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
