package de.kompitenz.uaa;


import java.io.IOException;
import java.lang.annotation.Annotation;

import de.kompitenz.uaa.entities.ApiError;
import de.kompitenz.uaa.network.RetrofitBuilder;
import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by User on 03-Oct-17.
 */
public class Utils {

    public static ApiError converErrors(ResponseBody response){
        Converter<ResponseBody, ApiError> converter = RetrofitBuilder.getRetrofit().responseBodyConverter(ApiError.class, new Annotation[0]);

        ApiError apiError = null;

        try {
            apiError = converter.convert(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiError;
    }
}
