package com.isn.platformer.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.isn.platformer.Platformer;
import com.isn.platformer.Screens.PlayScreen;

public class Chell extends Sprite{
	//Les differents états du personnage
	public enum State { FALLING, JUMPING, STANDING, RUNNING, DEAD };
    public State currentState;
    public State previousState;

    public World world;
    public Body body;

    private TextureRegion stand;
    private Animation<TextureRegion> run;
    private TextureRegion jump;
    private TextureRegion dead;

    private float stateTimer;
    private boolean lookingRight;
    private boolean chellIsDead;
    private PlayScreen screen;
    
    public boolean orange;
    public boolean green;
    public boolean goal;
    
    private boolean armed;

    private Array<Laser> lasers;
    
    public Chell(PlayScreen screen){
        this.world = screen.getWorld();
        this.screen = screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        lookingRight = true;
        
        //On charge les textures du personnage
        jump = new TextureRegion(new Texture("sprites\\jump.png"));
        stand = new TextureRegion(new Texture("sprites\\stand.png"));
        dead = new TextureRegion(new Texture("sprites\\dead.png"));
        
        Array<TextureRegion> frames = new Array<TextureRegion>();
        //On charge chaque frame de l'animation
        for(int i = 1; i < 4; i++) {
        	frames.add(new TextureRegion(new Texture("sprites\\run_" + i + ".png")));
        }
            
        run = new Animation<TextureRegion>(0.1f, frames);

        frames.clear();
        //On définit le personnage
        defineChell();

        //On affiche la texture initiale
        setBounds(0, 0, 16 / Platformer.SCALE, 16 / Platformer.SCALE);
        setRegion(stand);

        lasers = new Array<Laser>();
     }

    public void update(float dt){
    	//On affiche le sprite dans la position du corps
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2 + 3 / (2 * Platformer.SCALE));
        //On affiche l'image appropriée
        setRegion(getFrame(dt));    
        //On déssine les lasers
        for(Laser  ball : lasers) {
            ball.update(dt);
            if(ball.isDestroyed())
            	lasers.removeValue(ball, true);
        }
        //Quand le personnage tombe en dehors du niveau il meure
        if(body.getPosition().y < -0.1) {
        	die();
        }

    }

    public TextureRegion getFrame(float dt){
        //On cherche l'état présent du personnage
        currentState = getState();

        TextureRegion region;

        //La texture dépend de cet état
        switch(currentState){
            case DEAD:
                region = dead;
                break;
            case JUMPING:
                region = jump;
                break;
            case RUNNING:
                region = run.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = stand;
                break;
        }

        //On tourne l'image vers le coté que le personnage regarde
        if((!lookingRight) && !region.isFlipX()) {
            region.flip(true, false);
        } else if((lookingRight) && region.isFlipX()) {
            region.flip(true, false);
        }

        //Le timer augmente si l'état présent est le même que le precedent
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        
        previousState = currentState;
        //On envoie la texture
        return region;

    }

    public State getState(){
        
        if(chellIsDead)
            return State.DEAD;
        //Si le personnage monte il est entrain de sauter
        else if((body.getLinearVelocity().y > 0 && currentState == State.JUMPING) || (body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        //S'il descend il est entrain de tomber
        else if(body.getLinearVelocity().y < 0)
            return State.FALLING;
        //S'il a une vitesse dans l'axe x c'est qu'il est entrain de courir
        else if(body.getLinearVelocity().x != 0)
            return State.RUNNING;
        //Sinon le personnage est en arrêt
        else
            return State.STANDING;
    }

    public void die() {

        if (!isDead()) {
        	//Quand le personnage meure on arrête la musique et on joue un son
        	Platformer.manager.get("audio/music/portal_radio.ogg", Music.class).stop();
        	if(Platformer.sound)
        		Platformer.manager.get("audio/sounds/death.wav", Sound.class).play();
            chellIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = Platformer.NOTHING_BIT;

            for (Fixture fixture : body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            //Quand le personnage meure il saute dans l'air
            body.setLinearVelocity(new Vector2(0, 4f));
        }
    }

    public boolean isDead(){
        return chellIsDead;
    }

    public float getStateTimer(){
        return stateTimer;
    }

    public void jump(){
        if ( currentState != State.JUMPING ) {
        	//On ne peut sauter qu'une fois
            body.applyLinearImpulse(new Vector2(0, 4f), body.getWorldCenter(), true);
            currentState = State.JUMPING;
        }
    }

    public void defineChell(){
    	
        BodyDef bdef = new BodyDef();
        //On positionne le personnage dans le monde
        bdef.position.set(32 / Platformer.SCALE, 32 / Platformer.SCALE);
        bdef.type = BodyDef.BodyType.DynamicBody;
        //On crée un nouveau corps avec la définition
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //Le personnage a la forme d'un cercle
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Platformer.SCALE);
        fdef.filter.categoryBits =  Platformer.CHELL_BIT; //Il est un personnage
        fdef.filter.maskBits = Platformer.GROUND_BIT | //Il peut entrer en collision avec d'autres corps
        					   Platformer.ENEMY_BIT |
        		               Platformer.RED_GEL_BIT|
        		               Platformer.ORANGE_GEL_BIT|
        		               Platformer.BLUE_GEL_BIT|
        		               Platformer.POWER_BIT|
        		               Platformer.OBJECT_BIT|
        		               Platformer.GOAL_BIT|
        		               Platformer.PURPLE_GEL_BIT|
        		               Platformer.GREEN_GEL_BIT|
        		               Platformer.GUN_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
        
        //On crée sa main droite pour qu'il puisse escalader le gel vert
        PolygonShape rightHand = new PolygonShape();
        Vector2[] rightHandVertices = new Vector2[5];
        rightHandVertices[0] = new Vector2(6, 2).scl(1 / Platformer.SCALE);
        rightHandVertices[1] = new Vector2(6, -2).scl(1 / Platformer.SCALE);
        rightHandVertices[2] = new Vector2(4, -4).scl(1 / Platformer.SCALE);
        rightHandVertices[3] = new Vector2(1, -4).scl(1 / Platformer.SCALE);
        rightHandVertices[4] = new Vector2(1, 0).scl(1 / Platformer.SCALE);
        rightHand.set(rightHandVertices);

        fdef.filter.categoryBits = Platformer.CHELL_HANDS_BIT; 
        fdef.shape = rightHand;
        fdef.isSensor = true;

        body.createFixture(fdef).setUserData(this);
    }

    public void fire(){
    	//On joue le son du laser
    	if(Platformer.sound)
    		Platformer.manager.get("audio/sounds/laser.wav", Sound.class).play();
    	//On crée un nouveau laser devant le personnage
        lasers.add(new Laser(screen, body.getPosition().x, body.getPosition().y, lookingRight ? true : false));
    }
    
    public void arm(){
    	if(Platformer.sound)
    		Platformer.manager.get("audio/sounds/gun.wav", Sound.class).play();
    	//Quand le personnage attrape une arme on change les textures
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for(int i = 1; i < 4; i++)
        	frames.add(new TextureRegion(new Texture("sprites\\gun_run_" + i + ".png")));
        run = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        jump = new TextureRegion(new Texture("sprites\\gun_jump.png"));
        stand = new TextureRegion(new Texture("sprites\\gun_stand.png"));
        dead = new TextureRegion(new Texture("sprites\\gun_dead.png"));
        
        armed = true;
        
        screen.getHud().showSpace(true);
    }
    
    public boolean isArmed() {
    	return armed;
    }
    
    public void overOrange(boolean b){
    	orange = b;
    }
    
    public void touchGreen(boolean b){
    	green = b;
    }

    public void draw(Batch batch){
        super.draw(batch);
        for(Laser ball : lasers)
        	try {
        		ball.draw(batch);
        	} catch(java.lang.NullPointerException e) {}
    }
    
    public void lookRight(boolean b) {
    	lookingRight = b;
    }
    
    public void reachedGoal() {
    	goal = true;
    }
}
