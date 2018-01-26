package de.kompitenz.uaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.kompitenz.uaa.entities.Contract;
import de.kompitenz.uaa.entities.ContractDetail;
import de.kompitenz.uaa.network.ApiService;
import de.kompitenz.uaa.network.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContractShowActivity extends AppCompatActivity {

    ApiService service;
    TokenManager tokenManager;
    Call<ContractDetail> call;

    @BindView(R.id.view_contract_no)
    TextView view_contract_no;
    @BindView(R.id.view_seller_name)
    TextView view_seller_name;
    @BindView(R.id.view_buyer_name)
    TextView view_buyer_name;
    @BindView(R.id.view_amount_value)
    TextView view_amount_value;
    @BindView(R.id.view_delivery_date)
    TextView view_delivery_date;
    @BindView(R.id.view_price_value)
    TextView view_price_value;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_show);

        ButterKnife.bind(this);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken() == null){
            startActivity(new Intent(ContractShowActivity.this, LoginActivity.class));
            finish();
        }

        Button back_to_contracts = (Button) findViewById(R.id.back_to_contracts);
        back_to_contracts.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContractShowActivity.this, ContractsActivity.class));
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        String contract_no = extras.getString("contract_no");
        Log.d("CARRIED OVER", contract_no);

        service = RetrofitBuilder.createServiceWithAuth(ApiService.class, tokenManager);
        call = service.contracts_show(contract_no);
        call.enqueue(new Callback<ContractDetail>() {

            @Override
            public void onResponse(Call<ContractDetail> call, Response<ContractDetail> response) {
                if(response.isSuccessful()) {
                    Log.d("### BUYER", response.body().getBuyer());
                    view_contract_no.setText(response.body().getContractNo());
                    view_seller_name.setText(response.body().getSeller());
                    view_buyer_name.setText(response.body().getBuyer());
                    view_amount_value.setText(response.body().getAmount());
                    view_delivery_date.setText(response.body().getDelivery());
                    view_price_value.setText(response.body().getPrice());
                }
            }

            @Override
            public void onFailure(Call<ContractDetail> call, Throwable t) {
                Log.d("CONTRACT DETAILS", " ...JEBI GA: " + t.getMessage());
            }
        });

    }

}
