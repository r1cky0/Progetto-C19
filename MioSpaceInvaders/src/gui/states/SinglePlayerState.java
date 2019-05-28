package gui.states;

import logic.environment.Field;
import logic.environment.Menu;
import logic.environment.MovingDirections;
import logic.exception.GameOverException;
import logic.exception.NewHighscoreException;
import logic.exception.NextLevelException;
import logic.sprite.dinamic.Bullet;
import logic.sprite.dinamic.Invader;
import logic.sprite.unmovable.Brick;
import logic.sprite.unmovable.Bunker;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.ResourceLoader;

public class SinglePlayerState extends BasicGameState {

    private Menu menu;
    private Field field;
    private GameContainer gameContainer;
    private Image background;

    private java.awt.Font fontData;
    private UnicodeFont uniFontData;

    public SinglePlayerState(Menu menu){
        this.menu = menu;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        background = new Image("res/images/BackgroundSpace.png");

        try{
            fontData = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                    ResourceLoader.getResourceAsStream("res/font/invaders_font.ttf"));
            fontData = fontData.deriveFont(java.awt.Font.BOLD,3*gameContainer.getWidth()/100);

            uniFontData = new UnicodeFont(fontData);

            uniFontData.getEffects().add(new ColorEffect(java.awt.Color.white));

            uniFontData.addAsciiGlyphs();
            uniFontData.loadGlyphs();
        }catch(Exception e){
            e.printStackTrace();
        }
        field = menu.getField();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) throws SlickException {
        graphics.drawImage(background,0,0);
        uniFontData.drawString(80*gameContainer.getWidth()/100,15,"Lives: " + field.getSpaceShip().getLife(), Color.white);
        uniFontData.drawString(20,15,"Score: " + field.getSpaceShip().getCurrentScore(), Color.white);

        field.getSpaceShip().render("res/images/SpaceShip1.png");

        for(Invader invader:field.getInvaders()){
            invader.render("res/images/Alien1.png");
        }

        for(Bunker bunker: field.getBunkers()){
            for(Brick brick:bunker.getBricks()){
                switch (brick.getLife()){

                    case 4:
                        brick.render("res/images/Brick1.png");
                        break;
                    case 3:
                        brick.render("res/images/Brick2.png");
                        break;
                    case 2:
                        brick.render("res/images/Brick3.png");
                        break;
                    case 1:
                        brick.render("res/images/Brick4.png");
                        break;
                }
            }
        }

        if(field.getShipBullet() != null){
            field.getShipBullet().render("res/images/Shot.png");
        }

        for (Bullet bullet : field.getInvaderBullets()) {
            bullet.render("res/images/Shot.png");
        }

    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) throws SlickException {
        Input input = gameContainer.getInput();

        field.invaderDirection();

        if(field.isGameOver()){
            stateBasedGame.enterState(3, new FadeOutTransition(), new FadeInTransition());
        }

        if(field.isNewHighscore()){
            stateBasedGame.getState(6).init(gameContainer,stateBasedGame);
            stateBasedGame.enterState(6, new FadeOutTransition(), new FadeInTransition());
        }

        field.checkSpaceShipShotCollision();

        if(field.isNextLevel()){
            stateBasedGame.getState(2).init(gameContainer,stateBasedGame);
        }

        field.checkInvaderShotCollision();

        if (input.isKeyDown(Input.KEY_LEFT)) {
            field.shipMovement(MovingDirections.LEFT);
        }

        if (input.isKeyDown(Input.KEY_RIGHT)) {
            field.shipMovement(MovingDirections.RIGHT);
        }

        if (input.isKeyPressed(Input.KEY_SPACE)) {
            field.shipShot();
        }

        if (input.isKeyPressed(Input.KEY_0)) {
            field.invaderShot();
        }

        if (field.getShipBullet() != null) {
            field.getShipBullet().moveUp();
        }
    }

    @Override
    public int getID() {
        return 2;
    }
}
