package com.example.mobileassfundraisingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    EditText signupName, signupUsername, signupEmail, signupPassword, confirmPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI elements
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);
        confirmPassword = findViewById(R.id.confirmPassword);

        // Set up click listener for sign up button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Get input values from user
                String name = signupName.getText().toString().trim();
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String password = signupPassword.getText().toString().trim();
                String cfmPassword = confirmPassword.getText().toString().trim();


                // Check if any input fields are empty
                // Check if any input fields are empty
                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || cfmPassword.isEmpty()) {
                    // Set error messages for empty fields
                    if (name.isEmpty()) {
                        signupName.setError("Name is required");
                    } else {
                        signupName.setError(null);
                    }
                    if (email.isEmpty()) {
                        signupEmail.setError("Email is required");
                    } else {
                        signupEmail.setError(null);
                    }
                    if (username.isEmpty()) {
                        signupUsername.setError("Username is required");
                    } else {
                        signupUsername.setError(null);
                    }
                    if (password.isEmpty()) {
                        signupPassword.setError("Password is required");
                    } else {
                        signupPassword.setError(null);
                    }
                    if (cfmPassword.isEmpty()) {
                        confirmPassword.setError("Please confirm password");
                    } else {
                        confirmPassword.setError(null);
                    }
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                // Check if password and confirm password match
                if (!password.equals(cfmPassword)) {
                    signupUsername.setError(null);
                    signupEmail.setError(null);
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email is valid
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupUsername.setError(null);
                    signupEmail.setError("Please enter a valid email address");
                    return;
                }

                // Check if username is already taken
                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            signupUsername.setError("Username already taken");
                        } else {
                            signupUsername.setError(null);

                            // Check if email is already registered
                            Query query = reference.orderByChild("email").equalTo(email);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        signupEmail.setError("Email already registered");
                                    } else {
                                        signupEmail.setError(null);

                                        // Create helper class object with input values and default role "user"
                                        HelperClass helperClass = new HelperClass(name, email, username, password, "user");

                                        // Store helper class object in Firebase database under their username as key
                                        reference.child(username).setValue(helperClass);

                                        // Display success message to user and redirect to login screen
                                        Toast.makeText(SignupActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle error
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            }
        });

        // Set up click listener for redirecting to login screen
        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

