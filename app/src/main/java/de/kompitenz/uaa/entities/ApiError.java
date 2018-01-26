package de.kompitenz.uaa.entities;

import com.squareup.moshi.Json;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 03-Oct-17.
 */
public class ApiError {

    String message;
    Map<String, List<String>> errors;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
