package com.ta.auth;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.ta.ClientGDX;
import com.ta.data.JwtAuthenticationResponse;
import com.ta.data.User;
import com.ta.screens.CharScreen;

import com.ta.screens.LoginScreen;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private String token;
    private final ClientGDX game;

    public UserService(ClientGDX game) {
        this.game = game;
    }

    public void createUser(User user) {
        Gdx.app.log("UserService", "Creating user");

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json); // Ensure JSON format with double quotes
        String userJson = json.toJson(user);

        // Log the JSON payload
        Gdx.app.log("UserService", "JSON Payload: " + userJson);

        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/auth/sign-up");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setContent(userJson);

        // Send the HTTP request asynchronously
        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                Json json = new Json();
                JwtAuthenticationResponse serverResponse = null;

                try {
                    serverResponse = json.fromJson(JwtAuthenticationResponse.class, responseString);
                } catch (Exception e) {
                    Gdx.app.log("UserService", "Failed to parse response JSON", e);
                }

                if (statusCode == 200 && serverResponse != null) {
                    Gdx.app.log("UserService", "User created successfully: " + serverResponse.getToken());
                    Gdx.app.postRunnable(() -> game.setScreen(new LoginScreen(game)));
                } else {
                    Gdx.app.log("UserService", "Failed to create user: " + statusCode);
                    Gdx.app.log("UserService", "Response: " + responseString);
                    // Handle failure, e.g., show error message to user
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "User creation failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "User creation cancelled");
            }
        });
        // The program continues executing immediately after sending the request
    }

    public void signIn(User user) {
        Gdx.app.log("UserService", "Signing in");

        // Create JSON payload
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", user.getUsername());
            requestBody.put("password", user.getPassword());
        } catch (Exception e) {
            Gdx.app.log("UserService", "Failed to create JSON payload", e);
            return;
        }

        String userJson = requestBody.toString();

        // Log the JSON payload
        Gdx.app.log("UserService", "JSON Payload: " + userJson);

        // Create HttpRequest
        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/auth/sign-in");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setContent(userJson);

        // Send the HTTP request asynchronously
        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                String responseString = httpResponse.getResultAsString();
                if (statusCode == 200) {
                    try {
                        // Assuming the response is a JSON object with a "token" field
                        JSONObject responseBody = new JSONObject(responseString);
                        token = responseBody.getString("token");
                        Gdx.app.log("UserService", "Signed in successfully, token: " + token);
                        // Navigate to main screen
                        Gdx.app.postRunnable(() -> game.setScreen(new CharScreen(game, token)));
                    } catch (Exception e) {
                        Gdx.app.log("UserService", "Failed to parse response JSON", e);
                    }
                } else {
                    Gdx.app.log("UserService", "Failed to sign in: " + statusCode);
                    Gdx.app.log("UserService", "Response: " + responseString);
                    // Update the message label or handle the error
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "Sign in failed", t);
                // Update the message label or handle the error
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "Sign in cancelled");
                // Update the message label or handle the error
            }
        });
    }
}
