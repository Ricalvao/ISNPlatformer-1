package com.isn.platformer.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.isn.platformer.Platformer;
import com.isn.platformer.Screens.PlayScreen;
import com.isn.platformer.Sprites.Cube;
import com.isn.platformer.Sprites.Enemy;
import com.isn.platformer.Sprites.Gun;
import com.isn.platformer.TileObjects.BlueGel;
import com.isn.platformer.TileObjects.Path;
import com.isn.platformer.TileObjects.RedGel;

public class WorldCreator {
	private Array<Enemy> enemies;
	public Array<Cube> cubes;
	public Array<Path> paths;
	public Array<BlueGel> blueGels;
	public Gun gun;
	
	public WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        
        //Variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //Créer le sol et les murs à partir de la couche du niveau
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Platformer.SCALE, (rect.getY() + rect.getHeight() / 2) / Platformer.SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Platformer.SCALE, rect.getHeight() / 2 / Platformer.SCALE);
            fdef.shape = shape;
            body.createFixture(fdef);
        }
        
        //Créer les objets turn
        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Platformer.SCALE, (rect.getY() + rect.getHeight() / 2) / Platformer.SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Platformer.SCALE, rect.getHeight() / 2 / Platformer.SCALE);
            fdef.shape = shape;
            fdef.filter.categoryBits = Platformer.TURN_BIT;
            body.createFixture(fdef);
        }
        
        //Créer les gels rouges
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            new RedGel(screen, object);
        }
        
        //Créer les gels oranges
        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Platformer.SCALE, (rect.getY() + rect.getHeight() / 2) / Platformer.SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Platformer.SCALE, rect.getHeight() / 2 / Platformer.SCALE);
            fdef.shape = shape;
            //Le gel orange glisse
            fdef.friction = 0.08f;
            fdef.filter.categoryBits = Platformer.ORANGE_GEL_BIT;
            body.createFixture(fdef);
        }
        
        //Créer les gels bleus
        blueGels = new Array<BlueGel>();
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)){
        	blueGels.add(new BlueGel(screen, object));
        }
        
        //Créer les ponts
        paths = new Array<Path>();
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
        	paths.add(new Path(screen, object));
        }
        
        //Créer les enemies
        enemies = new Array<Enemy>();
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            enemies.add(new Enemy(screen, rect.getX() / Platformer.SCALE, rect.getY() / Platformer.SCALE));
        }
        
      //Créer les cubes
        cubes = new Array<Cube>();
        for(MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            cubes.add(new Cube(screen, rect.getX() / Platformer.SCALE, rect.getY() / Platformer.SCALE, false));
        }
        
      //Créer les goal
        for(MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Platformer.SCALE, (rect.getY() + rect.getHeight() / 2) / Platformer.SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Platformer.SCALE, rect.getHeight() / 2 / Platformer.SCALE);
            fdef.shape = shape;
            fdef.filter.categoryBits = Platformer.GOAL_BIT;
            body.createFixture(fdef);
        }
        
      //Créer les gels violets
        for(MapObject object : map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Platformer.SCALE, (rect.getY() + rect.getHeight() / 2) / Platformer.SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Platformer.SCALE, rect.getHeight() / 2 / Platformer.SCALE);
            fdef.shape = shape;
            fdef.filter.categoryBits = Platformer.PURPLE_GEL_BIT;
            body.createFixture(fdef);
        }
        
      //Créer les gels verts
        for(MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Platformer.SCALE, (rect.getY() + rect.getHeight() / 2) / Platformer.SCALE);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / Platformer.SCALE, rect.getHeight() / 2 / Platformer.SCALE);
            fdef.shape = shape;
            //On marche plus lentement sur le gel vert
            fdef.friction = 1f;
            fdef.filter.categoryBits = Platformer.GREEN_GEL_BIT;
            body.createFixture(fdef);
        }
        
        //Créer les armes
        for(MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)){
        	Rectangle rect = ((RectangleMapObject) object).getRectangle();
            gun = new Gun(screen, rect.getX() / Platformer.SCALE, rect.getY() / Platformer.SCALE);
        }
	}
	
	public Array<Enemy> getEnemies() {
        return enemies;
    }
	
	public Array<Cube> getCubes() {
        return cubes;
    }
	
	public Array<Path> getPaths() {
        return paths;
    }
	
	public Array<BlueGel> getBlueGels() {
        return blueGels;
    }
}
