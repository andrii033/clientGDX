package com.ta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

public class UserService {
    private String token;

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

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                if (statusCode == 200) {
                    Gdx.app.log("UserService", "User created successfully");
                } else {
                    Gdx.app.log("UserService", "Failed to create user: " + statusCode);
                    Gdx.app.log("UserService", "Response: " + httpResponse.getResultAsString());
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
    }

    public void signIn(User user) {
        Gdx.app.log("UserService", "Signing in");

        Json json = new Json();
        json.setOutputType(JsonWriter.OutputType.json); // Ensure JSON format with double quotes
        String userJson = json.toJson(user);

        // Log the JSON payload
        Gdx.app.log("UserService", "JSON Payload: " + userJson);

        HttpRequest httpRequest = new HttpRequest(HttpMethods.POST);
        httpRequest.setUrl("http://localhost:8080/auth/sign-in");
        httpRequest.setHeader("Content-Type", "application/json");
        httpRequest.setContent(userJson);

        Gdx.net.sendHttpRequest(httpRequest, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode = httpResponse.getStatus().getStatusCode();
                if (statusCode == 200) {
                    JsonValue jsonValue = new Json().fromJson(JsonValue.class, httpResponse.getResultAsString());
                    token = jsonValue.getString("token");
                    Gdx.app.log("UserService", "Signed in successfully, token: " + token);
                    // Navigate to main screen
                    ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
                } else {
                    Gdx.app.log("UserService", "Failed to sign in: " + statusCode);
                    Gdx.app.log("UserService", "Response: " + httpResponse.getResultAsString());
                    // Update the message label
                    //Gdx.app.postRunnable(() -> messageLabel.setText("Login failed: " + statusCode));
                }
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.log("UserService", "Sign in failed", t);
                //Gdx.app.postRunnable(() -> messageLabel.setText("Login failed"));
            }

            @Override
            public void cancelled() {
                Gdx.app.log("UserService", "Sign in cancelled");
                //Gdx.app.postRunnable(() -> messageLabel.setText("Login cancelled"));
            }
        });
    }
}
