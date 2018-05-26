package com.isn.platformer.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.isn.platformer.Platformer;
import com.isn.platformer.Sprites.Chell;

public class Hud implements Disposable{

    public Stage stage;
    private Viewport viewport;

    private boolean showSpace;

    private Label escapeLabel;
    private Label RLabel;
    private Label spaceLabel;

    public Hud(SpriteBatch sb, Chell player){
    	this.showSpace = player.isArmed();

        //Le hud a un caméra différent
        viewport = new FitViewport(Platformer.SCREEN_WIDTH, Platformer.SCREEN_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //Des labels pour afficher les controles 
        escapeLabel = new Label("ECHAP - Menu", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        RLabel = new Label("R - Recommencer", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        spaceLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(escapeLabel).expandX().padTop(5).left().padLeft(5);
        table.row();
        table.add(RLabel).expandX().padTop(5).left().padLeft(5);
        table.row();
        table.add(spaceLabel).expandX().padTop(5).left().padLeft(5);

        stage.addActor(table);

    }

    public void update(float dt){
    	//On n'affiche la barre d'espace que quand le joueur est armé
        if(showSpace)
            spaceLabel.setText("ESPACE - Tirer");
    }
    
    public void showSpace(boolean b){
        showSpace = b;
    }

    @Override
    public void dispose() { 
    	stage.dispose(); 
    }
}
