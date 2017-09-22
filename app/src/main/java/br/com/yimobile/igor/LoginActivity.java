package br.com.yimobile.igor;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;

    private TextView registerButton;
    private ImageButton enterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerButton =  (TextView) findViewById(R.id.criarconta);
        registerButton.setOnClickListener(registerClickListener);


        enterButton = (ImageButton) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(enterButtonClickListener);

    }

    View.OnClickListener registerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

//            builder = new AlertDialog.Builder(LoginActivity.this);
//
//            layoutInflater = getLayoutInflater();
//            builder.setView(layoutInflater.inflate(R.layout.dialog_signup, null));
//
//            builder.create();
//            builder.show();

        }
    };
    View.OnClickListener enterButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    };
}
