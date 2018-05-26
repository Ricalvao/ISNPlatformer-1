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

public class MenuScreen implements Screen {
	private Viewport viewport;
    private Stage stage;
    private Platformer game;
    
    private Image logoISNPlatformer;
    private float LOGO_WIDTH;
    private float LOGO_HEIGHT;
    
    private Image boutonJouer;
    private float JOUER_WIDTH;
    private float JOUER_HEIGHT;
    
    private Image boutonNiveaux;
    private float NIVEAUX_WIDTH;
    private float NIVEAUX_HEIGHT;
    
    private Image boutonOptions;
    private float OPTIONS_WIDTH;
    private float OPTIONS_HEIGHT;
    
    private Image boutonQuitter;
    private float QUITTER_WIDTH;
    private float QUITTER_HEIGHT;
    
    Music music;

    public MenuScreen(Platformer game){
        this.game = game;
        viewport = new FitViewport(Platformer.SCREEN_WIDTH, Platformer.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((Platformer) game).batch);
        
        logoISNPlatformer = new Image(new Texture("menu//isnplatformer.png"));
        LOGO_WIDTH = logoISNPlatformer.getWidth() * 2/3;
        LOGO_HEIGHT = logoISNPlatformer.getHeight() * 2/3;
        
        boutonJouer =   new Image(new Texture("menu//bouton_jouer.png"));
        JOUER_WIDTH = boutonJouer.getWidth() / 2;
        JOUER_HEIGHT = boutonJouer.getHeight() / 2;
        
        boutonNiveaux =   new Image(new Texture("menu//bouton_niveaux.png"));
        NIVEAUX_WIDTH = boutonNiveaux.getWidth() / 2;
        NIVEAUX_HEIGHT = boutonNiveaux.getHeight() / 2;
        
        boutonOptions =  new Image(new Texture("menu//bouton_options.png"));
        OPTIONS_WIDTH = boutonOptions.getWidth() / 2;
        OPTIONS_HEIGHT = boutonOptions.getHeight() / 2;
        
        boutonQuitter =  new Image(new Texture("menu//bouton_quitter.png"));
        QUITTER_WIDTH = boutonQuitter.getWidth() / 2;
        QUITTER_HEIGHT = boutonQuitter.getHeight() / 2;
        
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        //boutons
        table.add(logoISNPlatformer).padBottom(20f).width(LOGO_WIDTH).height(LOGO_HEIGHT);
        table.row();
        table.add(boutonJouer).padBottom(10f).width(JOUER_WIDTH).height(JOUER_HEIGHT);
        table.row();
        table.add(boutonNiveaux).padBottom(10f).width(NIVEAUX_WIDTH).height(NIVEAUX_HEIGHT);
        table.row();
        table.add(boutonOptions).padBottom(3f).width(OPTIONS_WIDTH).height(OPTIONS_HEIGHT);
        table.row();
        table.add(boutonQuitter).width(QUITTER_WIDTH).height(QUITTER_HEIGHT);

        stage.addActor(table);
        //musique du menu
        music = Platformer.manager.get("audio/music/still_alive.ogg", Music.class);
        music.setLooping(true);
        if(Platformer.music)
        	music.setVolume(0.3f);
        else
        	music.setVolume(0);
        music.play();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
    	short bouton = surQuelBouton();
    	//ecouter les clics
        if(Gdx.input.justTouched() && bouton == 1) {
            game.setScreen(new LevelScreen((Platformer) game, 1));
            music.stop();
            stage.dispose();
        } else if(Gdx.input.justTouched() && bouton == 3) {
        	game.setScreen(new OptionsScreen((Platformer) game));
            stage.dispose();
        } else if(Gdx.input.justTouched() && bouton == 2) {
        	game.setScreen(new NiveauxScreen((Platformer) game));
            stage.dispose();
        } else if(Gdx.input.justTouched() && bouton == 4) {
        	System.exit(0);
        }
        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        //afficher les boutons
        if(bouton == 1)
        	boutonJouer.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_jouer_on.png"))));
        else
        	boutonJouer.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_jouer.png"))));
        
        if(bouton == 2)
        	boutonNiveaux.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_niveaux_on.png"))));
        else
        	boutonNiveaux.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_niveaux.png"))));
        
        if(bouton == 3)
        	boutonOptions.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_options_on.png"))));
        else
        	boutonOptions.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_options.png"))));
        
        if(bouton == 4)
        	boutonQuitter.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_quitter_on.png"))));
        else
        	boutonQuitter.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture("menu//bouton_quitter.png"))));
        
        stage.draw();
        
    }
    
    private short surQuelBouton() {
    	//la souris se trouve sur quel bouton
    	Vector2 coords1Jouer = new Vector2(0, 0);
    	boutonJouer.localToStageCoordinates(coords1Jouer);
    	boutonJouer.getStage().stageToScreenCoordinates(coords1Jouer);
        Vector2 coords2Jouer = new Vector2(JOUER_WIDTH, JOUER_HEIGHT);
        boutonJouer.localToStageCoordinates(coords2Jouer);
        boutonJouer.getStage().stageToScreenCoordinates(coords2Jouer);
        
        if(Gdx.input.getX() > coords1Jouer.x && Gdx.input.getX() < coords2Jouer.x && Gdx.input.getY() < coords1Jouer.y && Gdx.input.getY() > coords2Jouer.y)
        	return 1;

        Vector2 coords1Niveaux = new Vector2(0, 0);
    	boutonNiveaux.localToStageCoordinates(coords1Niveaux);
    	boutonNiveaux.getStage().stageToScreenCoordinates(coords1Niveaux);
        Vector2 coords2Niveaux = new Vector2(NIVEAUX_WIDTH, NIVEAUX_HEIGHT);
        boutonNiveaux.localToStageCoordinates(coords2Niveaux);
        boutonNiveaux.getStage().stageToScreenCoordinates(coords2Niveaux);
        
        if(Gdx.input.getX() > coords1Niveaux.x && Gdx.input.getX() < coords2Niveaux.x && Gdx.input.getY() < coords1Niveaux.y && Gdx.input.getY() > coords2Niveaux.y)
        	return 2;

        Vector2 coords1Options = new Vector2(0, 0);
    	boutonOptions.localToStageCoordinates(coords1Options);
    	boutonOptions.getStage().stageToScreenCoordinates(coords1Options);
        Vector2 coords2Options = new Vector2(OPTIONS_WIDTH, OPTIONS_HEIGHT);
        boutonOptions.localToStageCoordinates(coords2Options);
        boutonOptions.getStage().stageToScreenCoordinates(coords2Options);
        
        if(Gdx.input.getX() > coords1Options.x && Gdx.input.getX() < coords2Options.x && Gdx.input.getY() < coords1Options.y && Gdx.input.getY() > coords2Options.y)
        	return 3;

        Vector2 coords1Quitter = new Vector2(0, 0);
    	boutonQuitter.localToStageCoordinates(coords1Quitter);
    	boutonQuitter.getStage().stageToScreenCoordinates(coords1Quitter);
        Vector2 coords2Quitter = new Vector2(QUITTER_WIDTH, QUITTER_HEIGHT);
        boutonQuitter.localToStageCoordinates(coords2Quitter);
        boutonQuitter.getStage().stageToScreenCoordinates(coords2Quitter);
        
        if(Gdx.input.getX() > coords1Quitter.x && Gdx.input.getX() < coords2Quitter.x && Gdx.input.getY() < coords1Quitter.y && Gdx.input.getY() > coords2Quitter.y)
        	return 4;
        
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
