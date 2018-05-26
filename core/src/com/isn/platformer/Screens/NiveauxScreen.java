package com.isn.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isn.platformer.Platformer;

public class NiveauxScreen implements Screen {
	private Viewport viewport;
    private Stage stage;
    private Platformer game;
    
    private Image[] levels;
    private float LEVEL_WIDTH;
    private float LEVEL_HEIGHT;

    private Image boutonRetour;
    private float RETOUR_WIDTH;
    private float RETOUR_HEIGHT;
    

    public NiveauxScreen(Platformer game){
        this.game = game;
        viewport = new FitViewport(Platformer.SCREEN_WIDTH, Platformer.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Platformer) game).batch);
        
        levels = new Image[6];
        for(int i = 0; i < 6; i++)
        	levels[i] = new Image(new Texture("menu//bouton_" + (i+1) + ".png"));
        LEVEL_WIDTH = levels[0].getWidth();
        LEVEL_HEIGHT = levels[0].getHeight();
        
        boutonRetour = new Image(new Texture("menu//bouton_retour.png"));
        RETOUR_WIDTH = boutonRetour.getWidth()/2;
        RETOUR_HEIGHT = boutonRetour.getHeight()/2;
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(levels[0]).padBottom(25f).padLeft(25f).padRight(25f).width(LEVEL_WIDTH).height(LEVEL_HEIGHT);
        table.add(levels[1]).padBottom(25f).padRight(25f).width(LEVEL_WIDTH).height(LEVEL_HEIGHT);
        table.add(levels[2]).padBottom(25f).padLeft(25f).padRight(25f).width(LEVEL_WIDTH).height(LEVEL_HEIGHT);
        table.row();
        table.add(levels[3]).padBottom(25f).padLeft(25f).padRight(25f).width(LEVEL_WIDTH).height(LEVEL_HEIGHT);
        table.add(levels[4]).padBottom(25f).padRight(25f).width(LEVEL_WIDTH).height(LEVEL_HEIGHT);
        table.add(levels[5]).padBottom(25f).padLeft(25f).padRight(25f).width(LEVEL_WIDTH).height(LEVEL_HEIGHT);
        table.row();
        table.add(boutonRetour).width(RETOUR_WIDTH).height(RETOUR_HEIGHT);

        stage.addActor(table);
        
        

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    	short bouton = surQuelBouton();
    	
    	for(int i = 1; i <= 6; i++) {
    		if(Gdx.input.justTouched() && bouton == i) {
                game.setScreen(new LevelScreen((Platformer) game, i));
                Platformer.manager.get("audio/music/still_alive.ogg", Music.class).stop();
                dispose();
            }
    	}
    	if(Gdx.input.justTouched() && bouton == 7) {
            game.setScreen(new MenuScreen((Platformer) game));
            dispose();
        }
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        for(int i = 1; i <= 6; i++) {
        	if(bouton == i)
            	levels[i-1].setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_" + i + "_on.png"))));
            else
            	levels[i-1].setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_" + i + ".png"))));
        }
        
        if(bouton == 7)
        	boutonRetour.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_retour_on.png"))));
        else
        	boutonRetour.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_retour.png"))));
        
        stage.draw();
        
    }
    
    private short surQuelBouton() {
    	
    	for(int i = 0; i < 6; i++) {
    		Vector2 coords1 = new Vector2(0, 0);
        	levels[i].localToStageCoordinates(coords1);
        	levels[i].getStage().stageToScreenCoordinates(coords1);
            Vector2 coords2 = new Vector2(LEVEL_WIDTH, LEVEL_HEIGHT);
            levels[i].localToStageCoordinates(coords2);
            levels[i].getStage().stageToScreenCoordinates(coords2);
            
            if(Gdx.input.getX() > coords1.x && Gdx.input.getX() < coords2.x && Gdx.input.getY() < coords1.y && Gdx.input.getY() > coords2.y)
            	return (short)(i+1);
        }
    	
    	Vector2 coords1 = new Vector2(0, 0);
    	boutonRetour.localToStageCoordinates(coords1);
    	boutonRetour.getStage().stageToScreenCoordinates(coords1);
        Vector2 coords2 = new Vector2(RETOUR_WIDTH, RETOUR_HEIGHT);
        boutonRetour.localToStageCoordinates(coords2);
        boutonRetour.getStage().stageToScreenCoordinates(coords2);
        
        if(Gdx.input.getX() > coords1.x && Gdx.input.getX() < coords2.x && Gdx.input.getY() < coords1.y && Gdx.input.getY() > coords2.y)
        	return 7;
    	
        return 0;
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