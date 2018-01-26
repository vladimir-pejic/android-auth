package de.kompitenz.uaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import de.kompitenz.uaa.entities.AccessToken;
import de.kompitenz.uaa.entities.ApiError;
import de.kompitenz.uaa.network.ApiService;
import de.kompitenz.uaa.network.RetrofitBuilder;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.til_email)
    TextInputLayout tilEmail;
    @BindView(R.id.til_password)
    TextInputLayout tilPassword;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.form_container)
    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    ApiService service;
    TokenManager tokenManager;
    AwesomeValidation validator;
    Call<AccessToken> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        service = RetrofitBuilder.createService(ApiService.class);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        validator = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        setupRules();

        if(tokenManager.getToken().getAccessToken() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    private void showForm(){
        TransitionManager.beginDelayedTransition(container);
        formContainer.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
    }


    @OnClick(R.id.btn_login)
    void login() {

        String email = tilEmail.getEditText().getText().toString();
        String password = tilPassword.getEditText().getText().toString();

        tilEmail.setError(null);
        tilPassword.setError(null);

        validator.clear();

        if (validator.validate()) {
            showLoading();
            call = service.login(email, password);
            call.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                    Log.w(TAG, "onResponse: " + response);

                    if (response.isSuccessful()) {
                        tokenManager.saveToken(response.body());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        if (response.code() == 422) {
                            handleErrors(response.errorBody());
                        }
                        if (response.code() == 401) {
                            ApiError apiError = Utils.converErrors(response.errorBody());
                            Toast.makeText(LoginActivity.this, apiError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        showForm();
                    }

                }

                @Override
                public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {
                    Log.w(TAG, "onFailure: " + t.getMessage());
                    showForm();
                }
            });

        }

    }


    private void handleErrors(ResponseBody response) {

        ApiError apiError = Utils.converErrors(response);

        for (Map.Entry<String, List<String>> error : apiError.getErrors().entrySet()) {
            if (error.getKey().equals("username")) {
                tilEmail.setError(error.getValue().get(0));
            }
            if (error.getKey().equals("password")) {
                tilPassword.setError(error.getValue().get(0));
            }
        }

    }

    public void setupRules() {

        validator.addValidation(this, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.err_email);
        validator.addValidation(this, R.id.til_password, RegexTemplate.NOT_EMPTY, R.string.err_password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //facebookManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (call != null) {
            call.cancel();
            call = null;
        }
        //facebookManager.onDestroy();
    }
}

