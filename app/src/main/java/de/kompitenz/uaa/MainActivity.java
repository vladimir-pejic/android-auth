package de.kompitenz.uaa;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.haha.perflib.Main;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TokenManager tokenManager;

    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.loader)
    ProgressBar loader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        loader.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.btn_dashboard:
                show_dashboard();
                return true;
            case R.id.btn_contracts:
                show_contracts();
                return true;
            case R.id.btn_logs:
                show_logs();
                return true;
            case R.id.btn_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void show_dashboard() {

    }

    void show_contracts() {
        showLoading();
        startActivity(new Intent(MainActivity.this, ContractsActivity.class));
        finish();
    }

    void show_logs() {
        showLoading();
        startActivity(new Intent(MainActivity.this, LogsActivity.class));
        finish();
    }

    void logout() {
        showLoading();
        tokenManager.deleteToken();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }





}
