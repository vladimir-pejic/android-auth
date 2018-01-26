package de.kompitenz.uaa.network;

import de.kompitenz.uaa.entities.AccessToken;
import de.kompitenz.uaa.entities.Contract;
import de.kompitenz.uaa.entities.ContractDetail;
import de.kompitenz.uaa.entities.ContractsResponse;
import de.kompitenz.uaa.entities.LogsResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ApiService {

    @POST("login")
    @FormUrlEncoded
    Call<AccessToken> login(@Field("email") String email, @Field("password") String password);

    @POST("refresh")
    @FormUrlEncoded
    Call<AccessToken> refresh(@Field("refresh_token") String refreshToken);

    @GET("contracts")
    Call<ContractsResponse> contracts();

    @GET("contracts/{contract_no}")
    Call<ContractDetail> contracts_show(@Path("contract_no") String contract_no);

    @GET("logs")
    Call<LogsResponse> logs();


}
