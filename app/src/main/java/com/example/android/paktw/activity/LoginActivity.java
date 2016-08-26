package com.example.android.paktw.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.paktw.R;
import com.example.android.paktw.adapter.DatabaseHelper;
import com.example.android.paktw.adapter.SessionManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hafizh on 18/12/2015.
 */
public class LoginActivity extends Activity {
    EditText editTextUserName,editTextPassword;
    Button btnLogin;
    Button btnRegister;

    // Session Manager Class
    SessionManager session;

    DatabaseHelper db;
    //    LoginDataBaseAdapter loginDataBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // get Instance  of Database Adapter
        db = new DatabaseHelper(this);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        // Get Refferences of Views
        editTextUserName=(EditText)findViewById(R.id.username);
        editTextPassword=(EditText)findViewById(R.id.password);
//        TextView test = (TextView)findViewById(R.id.message);
//        test.setText(session.getUsernameSession());
        btnLogin =(Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName = editTextUserName.getText().toString();
                String password = md5(editTextPassword.getText().toString());

                // fetch the Password form database for respective user name
                String storedPassword = db.getPassUser(userName);

                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword)) {

                    //store username to session
                    session.createLoginSession(userName);
                    Toast.makeText(LoginActivity.this, "Login Berhasil " +session.getUsernameSession(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(userName.equals("")||password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Semua field harus diisi", Toast.LENGTH_LONG).show();
                    return;
                }
                else {
                    Toast.makeText(LoginActivity.this, "Username atau password tidak sama", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister =(Button)findViewById(R.id.btnLinkToRegisterScreen);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

//        loginDataBaseAdapter.close();
    }

    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
