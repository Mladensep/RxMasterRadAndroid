package com.example.mladen.masterradandroid.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mladen.masterradandroid.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Context activity;
    private String email;
    private String token;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        activity = this;

        initialize();

        loginWithFb();
    }

    @OnClick(R.id.back_icon)
    public void back() {
        finish();
    }

    @OnClick(R.id.vithutLogin)
    public void vithoutLogin() {
        Intent intent = new Intent(activity, HomeTabsActivity.class);
        startActivity(intent);
    }

    private void loginWithFb() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                getUserDetailsFromFB(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                textView.setText("cancel");
            }

            @Override
            public void onError(FacebookException error) {
                textView.setText("eROR");
            }
        });
    }

    public void getUserDetailsFromFB(AccessToken accessToken) {

        GraphRequest req=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                //Toast.makeText(getApplicationContext(),"graph request completed",Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"Успешно пријављивање",Toast.LENGTH_SHORT).show();
                try{
                    email =  object.getString("email");
                    token = String.valueOf(accessToken);
                    name = object.getString("name");

                    textView.setText( email);
                    textView2.setText(token);
                    textView3.setText(name);

                    SharedPreferences sharedPref = activity.getSharedPreferences("facebook", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("emailData", email);
                    editor.putString("tokenData", token);
                    editor.putString("nameData", name);

                    editor.commit();


                    Intent intent = new Intent(activity, HomeTabsActivity.class);
                    startActivity(intent);

                    //String birthday = object.getString("birthday");
                    //String gender = object.getString("gender");
                    //String name = object.getString("name");
                    //String id = object.getString("id");
                    //String photourl =object.getJSONObject("picture").getJSONObject("data").getString("url");

                }catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"Неуспешно пријављивање"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
        req.setParameters(parameters);
        req.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initialize() {
        callbackManager = CallbackManager.Factory.create();

        textView = (TextView) findViewById(R.id.textMail);
        textView2 = (TextView) findViewById(R.id.textToken);
        textView3 = (TextView) findViewById(R.id.textName);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile");
        loginButton.setReadPermissions("email");
    }
}
