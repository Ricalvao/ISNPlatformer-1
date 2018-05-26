package com.isn.platformer.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.isn.platformer.Platformer;
import com.isn.platformer.Screens.PlayScreen;

public abstract class InteractiveTileObject { //Les Gels et les Ponts sont des Objets Interactifs
	protected World world;
	protected TiledMap map;
    protected Rectangle bounds;
    protected Body body;
    protected PlayScreen screen;
    protected MapObject object;
    TiledMapTileLayer layer;
    int w;
    int h;

    protected Fixture fixture;

    public  InteractiveTileObject(PlayScreen screen, MapObject object){
        this.object = object;
        this.screen = screen;
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = ((RectangleMapObject) object).getRectangle();
        layer = (TiledMapTileLayer) map.getLayers().get(1); //Couche du niveau ou se trouve les textures de l'objet
        w = (int)bounds.getWidth() /16; //Largeur de l'objet
        h= (int)bounds.getHeight() /16; //Hauteur de l'objet

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Platformer.SCALE, (bounds.getY() + bounds.getHeight() / 2) / Platformer.SCALE);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Platformer.SCALE, bounds.getHeight() / 2 / Platformer.SCALE);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);

    }

    public void setCategoryFilter(short filterBit){
    	//Foction pour changer le bit de collision
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell[] getCells(){
    	//Fonction pour trouver les textures de l'objet
        TiledMapTileLayer.Cell[] tiles;
	        
        //Si l'objet est horizontal
        if(w > 1) {
        	tiles = new TiledMapTileLayer.Cell[w];
	        
        	for(int i = 0; i < w; i++) {
        		tiles[i] = layer.getCell((int)(body.getPosition().x * Platformer.SCALE / 16 - w/2 + i), (int)(body.getPosition().y * Platformer.SCALE / 16));
        	}
        	return tiles;
        //Si l'objet est vertical
        } else { 
        	tiles = new TiledMapTileLayer.Cell[h];
	        
        	for(int i = 0; i < h; i++) {
        		tiles[i] = layer.getCell((int)(body.getPosition().x * Platformer.SCALE / 16), (int)(body.getPosition().y * Platformer.SCALE / 16 - h/2 + i));
        	}
        	return tiles;
        }
    }
    

    public TiledMapTileLayer.Cell getSource() {
    	//Fonction pour trouver le premier tile de l'objet
    	if(w > 1) {
        	return layer.getCell((int)(body.getPosition().x * Platformer.SCALE / 16 - w/2 - 1), (int)(body.getPosition().y * Platformer.SCALE / 16));
        } else {
        	return layer.getCell((int)(body.getPosition().x * Platformer.SCALE / 16), (int)(body.getPosition().y * Platformer.SCALE / 16 - h/2 - 1));        }
    }
}
