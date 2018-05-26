package com.isn.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isn.platformer.Platformer;

public class LevelScreen implements Screen {
	private Viewport viewport;
    private Stage stage;
    private Platformer game;
    private int level;
    private float timer;

    public LevelScreen(Platformer game, int level){
        this.game = game;
        this.level = level;
        viewport = new FitViewport(Platformer.SCREEN_WIDTH, Platformer.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Platformer) game).batch);
        timer = 0;
        
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        
        String string = null;
        
        //On affiche le niveau ou "Fin", si on est à la fin du jeu
        if(level > 6)
        	string = "Fin !";
        else
        	string = "Niveau " + level;
        
        Label label = new Label(string, font);

        table.add(label).expandX();

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    	timer += delta;
    	
    	//L'ecran n'affiche le message que pendant une seconde, après le niveau commence
    	if(timer > 1f) {
    		if(level > 6) {
    			System.exit(0);
                dispose();
    		} else {
    			game.setScreen(new PlayScreen((Platformer) game, level));
                dispose();
    		}
        }
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.draw();
        
    }
    
    @Override
    public void resize(int width, int height) {
    	
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    	stage.dispose();
    }
}
