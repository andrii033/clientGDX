package com.ta;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class ClientGDX extends Game {
	@Override
	public void create() {
		Gdx.app.log("ClientGDX", "create");
		//this.setScreen(new GameScreen());
		//this.setScreen(new LoginScreen());
		//this.setScreen(new SignIn());
		this.setScreen(new CharScreen());
	}
}
