package com.ta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ta.auth.UserService;
import com.ta.screens.LoginSignupScreen;

public class ClientGDX extends Game {
	private UserService userService;
	@Override
	public void create() {
		userService = new UserService(this);
		Gdx.app.log("ClientGDX", "create");
		this.setScreen(new LoginSignupScreen(this));
	}

	public UserService getUserService() {
		return userService;
	}
}
