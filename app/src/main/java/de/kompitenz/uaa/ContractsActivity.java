package de.kompitenz.uaa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.BinderThread;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.kompitenz.uaa.entities.Contract;
import de.kompitenz.uaa.entities.ContractsResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import de.kompitenz.uaa.network.ApiService;
import de.kompitenz.uaa.network.RetrofitBuilder;

public class ContractsActivity extends AppCompatActivity {

    private static final String TAG = "ContractsActivity";

    @BindView(R.id.container)
    ConstraintLayout container;
//    @BindView(R.id.form_container)
//    LinearLayout formContainer;
    @BindView(R.id.loader)
    ProgressBar loader;

    ApiService service;
    TokenManager tokenManager;
    Call<ContractsResponse> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contracts);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(ContractsActivity.this, LoginActivity.class));
            finish();
        }

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.contracts();
        call.enqueue(new Callback<ContractsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<ContractsResponse> call, @NonNull Response<ContractsResponse> response) {
                if(response.isSuccessful()) {
                    Log.e("#### RESPONSE", response.toString());
                    Log.e("Response", response.body().getData().get(0).getContractNo());

                    TableLayout contracts_table = (TableLayout)findViewById(R.id.contracts_table);
                    contracts_table.setStretchAllColumns(true);
                    contracts_table.bringToFront();

                    List<Contract> ContractData = response.body().getData();
                    if (ContractData != null) {
                        for (int i = 0; i < ContractData.size(); i++) {
                            TableRow tr =  new TableRow(ContractsActivity.this);
                            Button c1 = new Button(ContractsActivity.this);
                            TextView c2 = new TextView(ContractsActivity.this);
                            TextView c3 = new TextView(ContractsActivity.this);

                            // determine contract type string based on 1 or 0 from object
                            String contract_type;
                            if(ContractData.get(i).getContractTypeId().equals("1")) {
                                contract_type = "Purchase";
                            } else {
                                contract_type = "Sale";
                            }

                            // create row data
                            c1.setText(ContractData.get(i).getContractNo());
                            c1.setTextColor(0xFF2D912D);
                            c1.setTransitionName(ContractData.get(i).getContractNo());
                            c1.setOnClickListener(contractButton);
                            c2.setText(contract_type);
                            c2.setGravity(Gravity.CENTER);
                            c3.setText(ContractData.get(i).getQuantityAmount() + " Kg");
                            c3.setGravity(Gravity.RIGHT);
                            tr.addView(c1);
                            tr.addView(c2);
                            tr.addView(c3);
                            contracts_table.addView(tr);
                        }
                    }

                } else {
                    tokenManager.deleteToken();
                    startActivity(new Intent(ContractsActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ContractsResponse> call, @NonNull Throwable t) {
                Log.e("### RESPONSE ERROR ###", t.getMessage());
            }
        });

    }

    private void showLoading(){
        TransitionManager.beginDelayedTransition(container);
        //formContainer.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
    }

    View.OnClickListener contractButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showLoading();
            String contract_no = view.getTransitionName();
            Log.d("BUTTON CLICKED", contract_no);

            startActivity(new Intent(ContractsActivity.this, ContractShowActivity.class).putExtra("contract_no", contract_no));
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

    void show_dashboard() {
        showLoading();
        startActivity(new Intent(ContractsActivity.this, MainActivity.class));
        finish();
    }

    void show_logs() {
        showLoading();
        startActivity(new Intent(ContractsActivity.this, LogsActivity.class));
        finish();
    }

    void logout() {
        tokenManager.deleteToken();
        startActivity(new Intent(ContractsActivity.this, LoginActivity.class));
        finish();
    }

    void show_contracts() {

    }
}
