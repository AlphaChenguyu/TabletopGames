package core;

import actions.Action;
import components.*;

import java.util.*;

import static pandemic.Constants.GAME_ONGOING;

/**
 * Placeholder class. Will contain all game state information.
 */
public abstract class GameState {

    protected int activePlayer;  // Player who's currently taking a turn, index from player list, N+1 is game master, -1 is game
    protected ArrayList<Integer> reactivePlayers;
    protected int nPlayers;
    public int roundStep;

    protected HashMap<Integer, Area> areas;
    protected List<Board> boards;
    protected List<Deck<Card>> decks;
    protected List<Token> tokens;
    protected List<Counter> counters;
    protected List<Dice> dice;

    /**
     * Forward model of this game. Logic and setup.
     */
    protected ForwardModel forwardModel;

    /**
     * Set of parameters for this game.
     */
    protected GameParameters gameParameters;


    protected int gameStatus = GAME_ONGOING;

    public abstract GameState copy();
    public GameState copyTo(GameState gs) {
        gs.activePlayer = activePlayer;
        gs.reactivePlayers = new ArrayList<>(reactivePlayers);

        // TODO: copy super game state objects
        return gs;
    }

    public final void init()
    {
        areas = new HashMap<>();  // Game State has areas! Initialize.
        reactivePlayers = new ArrayList<>();
    }

    public abstract void setComponents();

    public void load(String dataPath)
    {
        boards = Board.loadBoards(dataPath + "boards.json");
        decks = Deck.loadDecks(dataPath + "decks.json");
        tokens = Token.loadTokens(dataPath + "tokens.json");
        counters = Counter.loadCounters(dataPath + "counters.json");
//        dice  = Dice.loadDice(dataPath + "dice.json");  // TODO
    }


    public void next(Action action) {
        forwardModel.next(this, action);
    }


    public void addDeckToList(Deck deck){
        this.decks.add(deck);
    }

    public Board findBoard(String name) {
        for (Board c: boards) {
            if (name.equalsIgnoreCase(c.getNameID())) {
                return c;
            }
        }
        return null;
    }

    public Counter findCounter(String name) {
        for (Counter c: counters) {
            if (name.equalsIgnoreCase(c.getID())) {
                return c;
            }
        }
        return null;
    }

    public Token findToken(String name) {
        for (Token t: tokens) {
            if (name.equalsIgnoreCase(t.getNameID())) {
                return t;
            }
        }
        return null;
    }

    public Deck<Card> findDeck(String name) {
        for (Deck<Card> d: decks) {
            if (name.equalsIgnoreCase(d.getID())) {
                return d;
            }
        }
        return null;
    }

    public String tempDeck() {
        Deck temp = findDeck("tempDeck");
        if (temp == null) {
            temp = new Deck("tempDeck");
            decks.add(temp);
        } else {
            temp.clear();
        }
        return "tempDeck";
    }

    public void clearTempDeck() {
        tempDeck();
    }

    //Getters & setters
    public int getGameStatus() {  return gameStatus; }
    void setForwardModel(ForwardModel fm) { this.forwardModel = fm; }
    ForwardModel getModel() {return this.forwardModel;}
    public GameParameters getGameParameters() { return this.gameParameters; }
    void setGameParameters(GameParameters gp) { this.gameParameters = gp; }
    public void setGameOver(int status){  this.gameStatus = status; }
    public int getActingPlayer() {  // Returns player taking an action (or possibly a reaction) next
        if (reactivePlayers.size() == 0)
            return activePlayer;
        else return reactivePlayers.get(0);
    }
    public int getActivePlayer() { return activePlayer; }  // Returns the player whose turn it is, might not be active player
    public ArrayList<Integer> getReactivePlayers() { return reactivePlayers; }  // Returns players queued to react
    public void addReactivePlayer(int player) { reactivePlayers.add(player); }
    public boolean removeReactivePlayer() {
        if (reactivePlayers.size() > 0) {
            reactivePlayers.remove(0);
            return true;
        }
        return false;
    }
    public HashMap<Integer, Area> getAreas() { return areas; }
    public int getNPlayers() { return nPlayers; }
    public void setNPlayers(int nPlayers) { this.nPlayers = nPlayers; }

    /* Methods to be implemented by subclass */
    public abstract int nPossibleActions();
    public abstract List<Action> possibleActions();

}
