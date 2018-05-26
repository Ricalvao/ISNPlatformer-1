package com.isn.platformer.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.isn.platformer.Platformer;
import com.isn.platformer.Sprites.Chell;
import com.isn.platformer.Sprites.Cube;
import com.isn.platformer.Sprites.Enemy;
import com.isn.platformer.Sprites.Gun;
import com.isn.platformer.Sprites.Laser;
import com.isn.platformer.TileObjects.RedGel;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
    	
    	//Les deux corps qui sont impliqués dans la collision
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){ //On vérifie si cDef correspond à un de ces cas
            case Platformer.ENEMY_BIT | Platformer.OBJECT_BIT:
            case Platformer.ENEMY_BIT | Platformer.POWER_BIT:
            case Platformer.ENEMY_BIT | Platformer.TURN_BIT:
            	//Changer la direction de l'enemy lorsqu'il touche un cube ou un objet turn
                if(fixA.getFilterData().categoryBits == Platformer.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Platformer.CHELL_BIT | Platformer.PURPLE_GEL_BIT:
            case Platformer.CHELL_BIT | Platformer.ENEMY_BIT:
            	//Le personnage meure quand il touche le gel violet
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT)
                    ((Chell) fixA.getUserData()).die();
                else
                    ((Chell) fixB.getUserData()).die();
                break;
            case Platformer.ENEMY_BIT | Platformer.ENEMY_BIT:
            	//Les enemies changent de direction quand ils se croisent
            	((Enemy)fixA.getUserData()).reverseVelocity(true, false);
            	((Enemy)fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Platformer.CHELL_BIT | Platformer.ORANGE_GEL_BIT:
            	//On informe le personnage qu'il se trouve sur le gel orange
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT)
                    ((Chell)fixA.getUserData()).overOrange(true);
                else
                	((Chell)fixB.getUserData()).overOrange(true);
                break;
            case Platformer.POWER_BIT | Platformer.RED_GEL_BIT:
            	//Le cube activé active le gel rouge
                if(fixA.getFilterData().categoryBits == Platformer.POWER_BIT) {
                    ((RedGel)fixB.getUserData()).turnOnOff(true);
                } else {
                	((RedGel)fixA.getUserData()).turnOnOff(true);
                }
                break;
            case Platformer.LASER_BIT | Platformer.ENEMY_BIT:
            	//Les lasers tuent les enemies
                if(fixA.getFilterData().categoryBits == Platformer.ENEMY_BIT) {
                    ((Enemy)fixA.getUserData()).hit();
                    ((Laser)fixB.getUserData()).setToDestroy();
                } else {
                	((Enemy)fixB.getUserData()).hit();
                	((Laser)fixA.getUserData()).setToDestroy();
                }
                break;
            case Platformer.LASER_BIT | Platformer.GROUND_BIT:
            case Platformer.LASER_BIT | Platformer.POWER_BIT:
            	//Les lasers se détruisent lorsqu'ils touchent un mur ou un cube
            	if(fixA.getFilterData().categoryBits == Platformer.LASER_BIT) {
                    ((Laser)fixA.getUserData()).setToDestroy();
                } else {
                	((Laser)fixB.getUserData()).setToDestroy();
                }
                break;
            case Platformer.LASER_BIT | Platformer.OBJECT_BIT:
            	//Les lasers activent les cubes
                if(fixA.getFilterData().categoryBits == Platformer.LASER_BIT) {
                    ((Laser)fixA.getUserData()).setToDestroy();
                    ((Cube)fixB.getUserData()).turnOn();
                } else {
                	((Laser)fixB.getUserData()).setToDestroy();
                    ((Cube)fixA.getUserData()).turnOn();
                }
                break;
            case Platformer.CHELL_BIT | Platformer.GOAL_BIT:
            	//Le personnage est arrivé à la fin du niveau
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT)
                    ((Chell) fixA.getUserData()).reachedGoal();
                else
                    ((Chell) fixB.getUserData()).reachedGoal();
                break;
            case Platformer.CHELL_HANDS_BIT | Platformer.GREEN_GEL_BIT:
            	//Le personnage peut escalader le gel vert
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT)
                    ((Chell) fixA.getUserData()).touchGreen(true);
                else
                    ((Chell) fixB.getUserData()).touchGreen(true);
                break;
            case Platformer.CHELL_BIT | Platformer.GUN_BIT:
            	//Le personnage peut attrapper l'arme
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT) {
                    ((Gun) fixB.getUserData()).use(((Chell) fixA.getUserData()));
                } else {
                    ((Gun) fixA.getUserData()).use(((Chell) fixB.getUserData()));
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    	//On fait la même chose pour endContact
    	Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            //Le personnage n'est plus en contacte avec les objets suivants
            case Platformer.CHELL_BIT | Platformer.ORANGE_GEL_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT)
                    ((Chell)fixA.getUserData()).overOrange(false);
                else
                	((Chell)fixB.getUserData()).overOrange(false);
                break;
            case Platformer.POWER_BIT | Platformer.RED_GEL_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.POWER_BIT) {
                    ((RedGel)fixB.getUserData()).turnOnOff(false);
                } else {
                	((RedGel)fixA.getUserData()).turnOnOff(false);
                }
                break;
            case Platformer.CHELL_HANDS_BIT | Platformer.GREEN_GEL_BIT:
                if(fixA.getFilterData().categoryBits == Platformer.CHELL_BIT)
                    ((Chell) fixA.getUserData()).touchGreen(false);
                else
                    ((Chell) fixB.getUserData()).touchGreen(false);
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
