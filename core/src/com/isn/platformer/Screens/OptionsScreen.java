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

public class OptionsScreen implements Screen {
	private Viewport viewport;
    private Stage stage;
    private Platformer game;
    
    private Image boutonSon;
    private float SON_WIDTH;
    private float SON_HEIGHT;
    
    private Image boutonMusique;
    private float MUSIQUE_WIDTH;
    private float MUSIQUE_HEIGHT;

    private Image boutonRetour;
    private float RETOUR_WIDTH;
    private float RETOUR_HEIGHT;
    

    public OptionsScreen(Platformer game){
        this.game = game;
        viewport = new FitViewport(Platformer.SCREEN_WIDTH, Platformer.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Platformer) game).batch);
        
        boutonSon = new Image(new Texture("menu//bouton_son.png"));
        SON_WIDTH = boutonSon.getWidth()/2;
        SON_HEIGHT = boutonSon.getHeight()/2;
        
        boutonMusique = new Image(new Texture("menu//bouton_musique.png"));
        MUSIQUE_WIDTH = boutonMusique.getWidth()/2;
        MUSIQUE_HEIGHT = boutonMusique.getHeight()/2;
        
        boutonRetour = new Image(new Texture("menu//bouton_retour.png"));
        RETOUR_WIDTH = boutonRetour.getWidth()/2;
        RETOUR_HEIGHT = boutonRetour.getHeight()/2;
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        table.add(boutonSon).padBottom(20f).width(SON_WIDTH).height(SON_HEIGHT);
        table.row();
        table.add(boutonMusique).padBottom(10f).width(MUSIQUE_WIDTH).height(MUSIQUE_HEIGHT);
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
    	
    	if(Gdx.input.justTouched() && bouton == 1) {
    		Platformer.sound = Platformer.sound ? false : true;
        } else if(Gdx.input.justTouched() && bouton == 2) {
        	
        	if(Platformer.music)
    			Platformer.manager.get("audio/music/still_alive.ogg", Music.class).stop();
    		else
    			Platformer.manager.get("audio/music/still_alive.ogg", Music.class).play();
        	Platformer.music = Platformer.music ? false : true;
        	
        } else if(Gdx.input.justTouched() && bouton == 3) {
            game.setScreen(new MenuScreen((Platformer) game));
            dispose();
        }
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(bouton == 1) {
        	if(Platformer.sound)
        		boutonSon.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_son_on.png"))));
        	else
        		boutonSon.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_son_off_on.png"))));
        } else {
        	if(Platformer.sound)
        		boutonSon.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_son.png"))));
        	else
        		boutonSon.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_son_off.png"))));
        }
        
        if(bouton == 2) {
        	if(Platformer.music)
        		boutonMusique.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_musique_on.png"))));
        	else
        		boutonMusique.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_musique_off_on.png"))));
        } else {
        	if(Platformer.music)
        		boutonMusique.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_musique.png"))));
        	else
        		boutonMusique.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_musique_off.png"))));
        }
        
        if(bouton == 3)
        	boutonRetour.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_retour_on.png"))));
        else
        	boutonRetour.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_retour.png"))));
        
        stage.draw();
        
    }
    
    private short surQuelBouton() {
    	Vector2 coords1Son = new Vector2(0, 0);
    	boutonSon.localToStageCoordinates(coords1Son);
    	boutonSon.getStage().stageToScreenCoordinates(coords1Son);
        Vector2 coords2Son = new Vector2(SON_WIDTH, SON_HEIGHT);
        boutonSon.localToStageCoordinates(coords2Son);
        boutonSon.getStage().stageToScreenCoordinates(coords2Son);
        
        if(Gdx.input.getX() > coords1Son.x && Gdx.input.getX() < coords2Son.x && Gdx.input.getY() < coords1Son.y && Gdx.input.getY() > coords2Son.y)
        	return 1;
        
    	Vector2 coords1Musique = new Vector2(0, 0);
    	boutonMusique.localToStageCoordinates(coords1Musique);
    	boutonMusique.getStage().stageToScreenCoordinates(coords1Musique);
        Vector2 coords2Musique = new Vector2(MUSIQUE_WIDTH, MUSIQUE_HEIGHT);
        boutonMusique.localToStageCoordinates(coords2Musique);
        boutonMusique.getStage().stageToScreenCoordinates(coords2Musique);
        
        if(Gdx.input.getX() > coords1Musique.x && Gdx.input.getX() < coords2Musique.x && Gdx.input.getY() < coords1Musique.y && Gdx.input.getY() > coords2Musique.y)
        	return 2;
        
    	Vector2 coords1Retour = new Vector2(0, 0);
    	boutonRetour.localToStageCoordinates(coords1Retour);
    	boutonRetour.getStage().stageToScreenCoordinates(coords1Retour);
        Vector2 coords2Retour = new Vector2(RETOUR_WIDTH, RETOUR_HEIGHT);
        boutonRetour.localToStageCoordinates(coords2Retour);
        boutonRetour.getStage().stageToScreenCoordinates(coords2Retour);
        
        if(Gdx.input.getX() > coords1Retour.x && Gdx.input.getX() < coords2Retour.x && Gdx.input.getY() < coords1Retour.y && Gdx.input.getY() > coords2Retour.y)
        	return 3;
    	
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
