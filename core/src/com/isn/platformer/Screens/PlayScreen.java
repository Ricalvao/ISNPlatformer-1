package com.isn.platformer.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isn.platformer.Platformer;
import com.isn.platformer.Sprites.Chell;
import com.isn.platformer.Sprites.Cube;
import com.isn.platformer.Sprites.Enemy;
import com.isn.platformer.TileObjects.BlueGel;
import com.isn.platformer.TileObjects.Path;
import com.isn.platformer.Tools.WorldContactListener;
import com.isn.platformer.Tools.WorldCreator;

public class PlayScreen implements Screen{
	private Platformer game;
	public int level;
	private boolean restart;

    private OrthographicCamera gamecam;
    private Viewport gamePort;

    //Tiled variables
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
    public WorldCreator creator;

    //Sprites
    private Chell player;
    
    //Musique
    private Music music;

    public PlayScreen(Platformer game, int level){

    	this.game = game;
    	this.level = level;
    	restart = false;
    	
        //On crée le caméra qui va suivre le personnage
        gamecam = new OrthographicCamera();

        //Le FitViewport permet de mantenir les proportions de l'image lorsque la taille de la fenetre change
        gamePort = new FitViewport(Platformer.SCREEN_WIDTH / Platformer.SCALE, Platformer.SCREEN_HEIGHT / Platformer.SCALE, gamecam);

        //On charge le niveau
        map = new TmxMapLoader().load("levels//test" + level + ".tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Platformer.SCALE);

        //On positionne le caméra au début du niveau
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //On crée un monde Box2D pour pouvoir créer des corps dedans
        world = new World(new Vector2(0, -10), true);
        
        b2dr = new Box2DDebugRenderer();

        //On crée tous les corps nécessaires dans le monde
        creator = new WorldCreator(this);

        //On crée le personnage
        player = new Chell(this);

        //On crée le contact listener
        world.setContactListener(new WorldContactListener());
        
        //On joue la musique
        music = Platformer.manager.get("audio/music/portal_radio.ogg", Music.class);
        music.setLooping(true);
        if(Platformer.music)
        	music.setVolume(0.3f);
        else
        	music.setVolume(0);
        music.play();
    }

    public void show() {
    	//
    }

    public void handleInput(float dt){
        //Le personnage est controllé par des forces
        if(player.currentState != Chell.State.DEAD) {
            
        	if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.jump();
        	
        	if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        		if(player.isArmed()) //Si le personnae est armée il tire des lasers
        			player.fire();
        	
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            	for (BlueGel blueGel : creator.getBlueGels())
            		if(blueGel.getBounciness() != 1.2f)
                    	blueGel.setBounciness(1.2f); //On saute plus haut sur le gel bleu si on appuie sur la touche HAUT
            } else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            	for (BlueGel blueGel : creator.getBlueGels())
                    if(blueGel.getBounciness() != 0.5f)
                    	blueGel.setBounciness(0.5f); //On saute moins haut sur le gel bleu si on appuie sur la touche BAS
            } else {
            	for (BlueGel blueGel : creator.getBlueGels())
                    if(blueGel.getBounciness() != 1f)
                    	blueGel.setBounciness(1f);
            }
            
            if(player.orange) {
            	//Le personnage coure plus vite sur le gel orange
            	if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 6) {
            		player.body.applyLinearImpulse(new Vector2(0.4f, 0), player.body.getWorldCenter(), true);
        			player.lookRight(true);
            	}
            	
            	if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -6) {
            		player.body.applyLinearImpulse(new Vector2(-0.4f, 0), player.body.getWorldCenter(), true);
            		player.lookRight(false);
            	}
            	
            } else {
            	if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            		if(player.green && player.body.getLinearVelocity().y <= .5f) //Le personnage peut escalader le gel vert
                    	player.body.applyLinearImpulse(new Vector2(0, .5f), player.body.getWorldCenter(), true);
                    else if (player.body.getLinearVelocity().x <= 1.5f)
                    	player.body.applyLinearImpulse(new Vector2(0.1f, 0), player.body.getWorldCenter(), true);
            		player.lookRight(true);
            	}
            	
            	if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -1.5f) {
            		player.body.applyLinearImpulse(new Vector2(-0.1f, 0), player.body.getWorldCenter(), true);
            		player.lookRight(false);
            	}
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            	restart = true; //R pour recommencer le niveau
            }
        }
        	
    }

    public void update(float dt){
        //On gère les instructions de l'utilisateur
        handleInput(dt);

        //60 frames par seconde
        world.step(1 / 60f, 6, 2);
        
        //On met les corps à jour dans le monde
        player.update(dt);
        for(Enemy enemy : creator.getEnemies()) {
            enemy.update(dt);
        }
        for(Cube cubes : creator.getCubes()) {
        	cubes.update(dt);
        }
        for(Path paths : creator.getPaths()) {
        	paths.update(dt);
        }
        if(creator.gun !=null)
        	creator.gun.update(dt);;

        //Le caméra suit le personnage dans l'axe x
        if(player.currentState != Chell.State.DEAD) {
            gamecam.position.x = player.body.getPosition().x;
        }
        gamecam.update();
        //On affiche ce que le caméra voit
        renderer.setView(gamecam);

    }


    @Override
    public void render(float delta) {
        //On fait les mises à jour dans une autre fonction
        update(delta);

        //On nettoye l'écran et on met un fond noir
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //On affiche le niveau 
        renderer.render();

        //b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);

        //On dessine les sprites
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : creator.getEnemies())
            enemy.draw(game.batch);
        for (Cube cube : creator.getCubes())
            cube.draw(game.batch);
        if(creator.gun !=null)
        	creator.gun.draw(game.batch);
        game.batch.end();
        
        //Recommencer le niveau ou passer au niveau suivant
        if(restart()){
        	game.setScreen(new PlayScreen((Platformer) game, level));
            dispose();
        } else if(nextLevel()){
        	game.setScreen(new LevelScreen((Platformer) game, level + 1));
            music.stop();
            dispose();
        }

	}

    public boolean restart(){
    	//Si le personnage est mort depuis 2 secondes le niveau recommence
        if((player.currentState == Chell.State.DEAD && player.getStateTimer() > 2) || restart){
            return true;
        }
        return false;
    }
    
    public boolean nextLevel(){
    	//Si le personnage arrive à la fin du niveau on passe au niveau suivant
        if(player.goal){
            return true;
        }
        return false;
    }

    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}