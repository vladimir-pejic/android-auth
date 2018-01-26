package de.kompitenz.uaa;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import de.kompitenz.uaa.entities.Contract;
import de.kompitenz.uaa.entities.Logs;
import de.kompitenz.uaa.entities.LogsResponse;
import de.kompitenz.uaa.network.ApiService;
import de.kompitenz.uaa.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogsActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ConstraintLayout container;
    @BindView(R.id.loader)
    ProgressBar loader;

    TokenManager tokenManager;
    ApiService service;
    Call<LogsResponse> call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(LogsActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.logs();
        call.enqueue(new Callback<LogsResponse>() {

            @Override
            public void onResponse(Call<LogsResponse> call, Response<LogsResponse> response) {
                if(response.isSuccessful()) {

                    TableLayout contracts_table = (TableLayout)findViewById(R.id.logs_table);
                    contracts_table.setStretchAllColumns(true);
                    contracts_table.bringToFront();

                    List<Logs> LogsData = response.body().getData();
                    if (LogsData != null) {
                        for (int i = 0; i < LogsData.size(); i++) {
                            TableRow tr1 =  new TableRow(LogsActivity.this);
                            TableRow tr2 =  new TableRow(LogsActivity.this);
                            TableRow tr3 =  new TableRow(LogsActivity.this);
                            Button c1 = new Button(LogsActivity.this);
                            TextView c2 = new TextView(LogsActivity.this);
                            TextView c3 = new TextView(LogsActivity.this);

                            // create row data
                            c1.setText(LogsData.get(i).getLogContract());
                            c1.setTextColor(0xFF993300);
                            c1.setTransitionName(LogsData.get(i).getLogContract());
                            c1.setGravity(Gravity.CENTER);
                            c1.setOnClickListener(contractButton);

                            c2.setText(LogsData.get(i).getAction());
                            c2.setGravity(Gravity.LEFT);

                            c3.setText("BY: " + LogsData.get(i).getUserLogged());
                            c3.setGravity(Gravity.LEFT);
                            c3.setTextColor(0xFF303F9F);

                            tr1.addView(c1);
                            tr2.addView(c2);
                            tr3.addView(c3);
                            contracts_table.addView(tr1);
                            contracts_table.addView(tr2);
                            contracts_table.addView(tr3);
                        }
                    }

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(LogsActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LogsResponse> call, Throwable t) {

            }
        });

    }

    View.OnClickListener contractButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showLoading();
            String contract_no = view.getTransitionName();
            Log.d("BUTTON CLICKED", contract_no);

            startActivity(new Intent(LogsActivity.this, ContractShowActivity.class).putExtra("contract_no", contract_no));
            finish();
        }
    };


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

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        //formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }


    void show_dashboard() {
        showLoading();
        startActivity(new Intent(LogsActivity.this, MainActivity.class));
        finish();
    }

    void show_contracts() {
        showLoading();
        startActivity(new Intent(LogsActivity.this, ContractsActivity.class));
        finish();
    }

    void show_logs() {

    }

    void logout() {
        showLoading();
        tokenManager.deleteToken();
        startActivity(new Intent(LogsActivity.this, LoginActivity.class));
        finish();
    }
}
