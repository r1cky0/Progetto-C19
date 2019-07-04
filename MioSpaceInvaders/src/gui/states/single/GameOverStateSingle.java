package gui.states.single;

import gui.states.BasicInvaderState;
import gui.states.IDStates;
import logic.environment.manager.file.ReadXmlFile;
import logic.environment.manager.game.SinglePlayer;
import logic.environment.manager.menu.Menu;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.MouseOverArea;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

public class GameOverStateSingle extends BasicInvaderState implements ComponentListener {

    private GameContainer gameContainer;
    private StateBasedGame stateBasedGame;
    private SinglePlayer singlePlayer;

    private Image gameOver;
    private Image newGame;
    private Image homeImage;
    private MouseOverArea homeButton;
    private MouseOverArea newGameButton;

    private UnicodeFont uniFontScore;
    private String score;

    private Menu menu;

    public GameOverStateSingle(Menu menu) {
        this.menu = menu;
    }

    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        this.gameContainer = gameContainer;
        this.stateBasedGame = stateBasedGame;

        newGame = new Image(ReadXmlFile.read("buttonNewGame")).getScaledCopy(30*gameContainer.getWidth()/100,
                10*gameContainer.getHeight()/100);
        newGameButton = new MouseOverArea(gameContainer, newGame,(gameContainer.getWidth() - newGame.getWidth())/2,
                80*gameContainer.getHeight()/100,30*gameContainer.getWidth()/100,10*gameContainer.getHeight()/100,
                this);

        homeImage = new Image(ReadXmlFile.read("buttonHome")).getScaledCopy(6*gameContainer.getWidth()/100,
                6*gameContainer.getWidth()/100);
        homeButton = new MouseOverArea(gameContainer, homeImage,5*gameContainer.getWidth()/100,
                7*gameContainer.getHeight()/100,6*gameContainer.getWidth()/100,6*gameContainer.getHeight()/100,
                this);

        gameOver = new Image(ReadXmlFile.read("gameoverBackground"));

        uniFontScore = build(9*gameContainer.getWidth()/100f);
    }

    public void enter(GameContainer gameContainer, StateBasedGame stateBasedGame){
        singlePlayer = menu.getSinglePlayer();
    }

    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics graphics) {
        score = "SCORE: " + singlePlayer.getPlayer().getSpaceShip().getCurrentScore();

        gameOver.draw((gameContainer.getWidth() - gameOver.getWidth())/2f,
                (gameContainer.getHeight() - gameOver.getHeight())/2f);

        newGameButton.render(gameContainer, graphics);
        homeButton.render(gameContainer,graphics);

        uniFontScore.drawString((this.gameContainer.getWidth() - uniFontScore.getWidth(score))/2f,
                7* this.gameContainer.getHeight()/100f, score);
    }

    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int i) {
    }

    /**
     * Funzione che setta i gestori degli eventi di click sui bottoni
     * @param source Il tasto di cui dobbiamo settare il comportamento
     */
    @Override
    public void componentActivated(AbstractComponent source) {
        if (source == newGameButton) {
            try {
                menu.restartGame();
                stateBasedGame.getState(IDStates.SINGLEPLAYER_STATE).init(gameContainer, stateBasedGame);
            } catch (SlickException e) {
                e.printStackTrace();
            }
            stateBasedGame.enterState(IDStates.SINGLEPLAYER_STATE, new FadeOutTransition(), new FadeInTransition());
        }
        if (source == homeButton) {
            stateBasedGame.enterState(IDStates.MENU_STATE, new FadeOutTransition(), new FadeInTransition());
        }
    }

    @Override
    public int getID() {
        return IDStates.GAMEOVERSINGLE_STATE;
    }
}