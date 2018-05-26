package com.isn.platformer.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.isn.platformer.Platformer;
import com.isn.platformer.Screens.PlayScreen;

public class RedGel extends InteractiveTileObject{
	private static TiledMapTileSet tileSet;
	//Les differentes textures
    private final int FLOOR_OFF = 27;
    private final int FLOOR_ON = 51;
    private final int R_WALL_OFF = 28;
    private final int R_WALL_ON = 52;
    private final int L_WALL_OFF = 29;
    private final int L_WALL_ON = 53;
    private final int L_CORNER_OFF = 30;
    private final int L_CORNER_ON = 54;
    private final int R_CORNER_OFF = 31;
    private final int R_CORNER_ON = 55;

    public RedGel(PlayScreen screen, MapObject object){
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("Tileset");
        fixture.setUserData(this);
        setCategoryFilter(Platformer.RED_GEL_BIT);
    }

    public void turnOnOff(boolean b) {
    	//On change les textures en accord avec l'�tat courrant du gel
    	TiledMapTileLayer.Cell[] tiles = getCells();
    	if(b) {
        	for(int i = 0; i < tiles.length; i++) {
        		
        		if(tiles[i].getTile().getId() == FLOOR_OFF) {
        			tiles[i].setTile(tileSet.getTile(FLOOR_ON));
        		} else if(tiles[i].getTile().getId() == R_WALL_OFF){
        			tiles[i].setTile(tileSet.getTile(R_WALL_ON));
        		} else if(tiles[i].getTile().getId() == L_WALL_OFF){
        			tiles[i].setTile(tileSet.getTile(L_WALL_ON));
        		} else if(tiles[i].getTile().getId() == L_CORNER_OFF){
        			tiles[i].setTile(tileSet.getTile(L_CORNER_ON));
        		} else if(tiles[i].getTile().getId() == R_CORNER_OFF){
        			tiles[i].setTile(tileSet.getTile(R_CORNER_ON));
        		}
        	}
        	
        } else {
        	
        	for(int i = 0; i < tiles.length; i++) {
        		
        		if(tiles[i].getTile().getId() == FLOOR_ON) {
        			tiles[i].setTile(tileSet.getTile(FLOOR_OFF));
        		} else if(tiles[i].getTile().getId() == R_WALL_ON){
        			tiles[i].setTile(tileSet.getTile(R_WALL_OFF));
        		} else if(tiles[i].getTile().getId() == L_WALL_ON){
        			tiles[i].setTile(tileSet.getTile(L_WALL_OFF));
        		} else if(tiles[i].getTile().getId() == L_CORNER_ON){
        			tiles[i].setTile(tileSet.getTile(L_CORNER_OFF));
        		} else if(tiles[i].getTile().getId() == R_CORNER_ON){
        			tiles[i].setTile(tileSet.getTile(R_CORNER_OFF));
        		}
        	}
        }
    }
}
