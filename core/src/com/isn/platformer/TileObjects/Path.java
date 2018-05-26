package com.isn.platformer.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.isn.platformer.Platformer;
import com.isn.platformer.Screens.PlayScreen;

public class Path extends InteractiveTileObject{
	private static TiledMapTileSet tileSet;
    private int sourceOff;
    private int sourceOn;
    private int pathOff;
    private int pathOn;
    private TiledMapTileLayer.Cell[] tiles;

    public Path(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("Tileset");
        fixture.setUserData(this);
        
        //Si le pont est orienté horizontalement
        if(w > 1)
        	setCategoryFilter(Platformer.NOTHING_BIT);
        //Si'il est orienté verticalement
        else
        	setCategoryFilter(Platformer.GROUND_BIT);
        
        //On obtient les tiles qui correspondent à l'objet
        tiles = getCells();
        
        //Les textures, qui se trouvent dans le tileSet du niveau, dépendent de l'orientation de l'objet
        sourceOff = w > 1 ? 67 : 101;
        sourceOn = w > 1 ? 68 : 100;
        pathOff = w > 1 ? 43 : 34;
        pathOn = w > 1 ? 35 : 43;
    }
    
    public void update(float dt) {
    	//Si la source du pont touche un gel rouge activé le pont s'active aussi
    	//Les textures du gel rouge activé se trouvent entre le tile 69 et 73 du tileset
    	if(getSource().getTile().getId() >= 69 && getSource().getTile().getId() <= 73) { 
    		turnOnOff(true);
    	} else {
    		turnOnOff(false);
    	}
    }
    

    public void turnOnOff(boolean b) {
    	//On change les textures en accord avec l'état courrant du pont
    	if(b) {
        	for(int i = 0; i < tiles.length; i++) {
        		if(tiles[i].getTile().getId() == sourceOff) {
        			tiles[i].setTile(tileSet.getTile(sourceOn));
            	} else if(tiles[i].getTile().getId() == pathOff){
            		tiles[i].setTile(tileSet.getTile(pathOn));
            	}
        		if(w == 1)
                	setCategoryFilter(Platformer.NOTHING_BIT);
                else
                	setCategoryFilter(Platformer.GROUND_BIT);
        	}
        	
        } else {
        	
        	for(int i = 0; i < tiles.length; i++) {
        		if(tiles[i].getTile().getId() == sourceOn) {
        			tiles[i].setTile(tileSet.getTile(sourceOff));
            	} else if(tiles[i].getTile().getId() == pathOn){
            		tiles[i].setTile(tileSet.getTile(pathOff));
            	}
        		if(w > 1)
                	setCategoryFilter(Platformer.NOTHING_BIT);
                else
                	setCategoryFilter(Platformer.GROUND_BIT);
        	}
        }
    }
}
