package com.ta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ta.screens.LoginSignupScreen;

public class ClientGDX extends Game {
	@Override
	public void create() {
		Gdx.app.log("ClientGDX", "create");
		this.setScreen(new LoginSignupScreen(this));
	}
}
