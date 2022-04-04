package com.example.loveforjava;

import java.util.Map;

/**
 * For returning values to other classes from the database
 */
public interface ResponseCallback {
    void onResponse(Map<String, Object> response);
}
