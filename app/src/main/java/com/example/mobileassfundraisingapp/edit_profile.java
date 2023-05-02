package com.example.mobileassfundraisingapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_profile extends Fragment {
    EditText editName, editEmail, cfmPassword, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        reference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);

        editPassword = view.findViewById(R.id.editPassword);
        cfmPassword = view.findViewById(R.id.confirmPasswordEdit);

        saveButton = view.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(view1 -> {
            // Check if password confirmation matches
            String password = editPassword.getText().toString();
            String confirmPassword = cfmPassword.getText().toString();
            if (!password.equals(confirmPassword)) {
                cfmPassword.setError("Password does not match");
                return; // password doesn't match, stay on the page
            }

            boolean isNameChanged = isNameChanged();
            boolean isPasswordChanged = isPasswordChanged();
            boolean isEmailChanged = isEmailChanged();
            if (isNameChanged || isPasswordChanged || isEmailChanged) {
                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();

                // Navigate back to the profile fragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                profile profileFragment = new profile();
                fragmentTransaction.replace(R.id.fragment_container, profileFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(getActivity(), "No Changes Found", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showData();
    }

    private boolean isNameChanged() {
        if (!nameUser.equals(editName.getText().toString())){
            reference.child(usernameUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        } else {
            return false;
        }
    }



    private boolean isEmailChanged() {
        if (!emailUser.equals(editEmail.getText().toString())){
            if(isValidEmail(editEmail.getText().toString())){
                reference.child(usernameUser).child("email").setValue(editEmail.getText().toString());
                emailUser = editEmail.getText().toString();
                return true;
            }else{
                editEmail.setError("Invalid email format");
            }
        }
        return false;
    }

    private boolean isPasswordChanged() {
        if (!passwordUser.equals(editPassword.getText().toString())){
            String password = editPassword.getText().toString();
            String confirmPassword = cfmPassword.getText().toString();
            if(password.equals(confirmPassword) && isValidPassword(password)){
                reference.child(usernameUser).child("password").setValue(password);
                passwordUser = password;
                return true;
            }else{
                if(!password.equals(confirmPassword)){
                    cfmPassword.setError("Password does not match");
                    return false; // password doesn't match, stay on the page
                }
                if(!isValidPassword(password)){
                    editPassword.setError("Password must be at least 6 characters long");
                }
            }
        }
        return false;
    }


    public void showData(){
        Intent intent = getActivity().getIntent();
        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");
        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editPassword.setText(passwordUser);
    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isValidPassword(CharSequence target){
        if(target == null){
            return false;
        }else{
            return target.length() >= 6;
        }
    }
}

