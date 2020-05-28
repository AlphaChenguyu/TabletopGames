package games.explodingkittens.actions;

import core.AbstractGameState;
import core.actions.DoNothing;
import core.interfaces.IPrintable;

public class PassAction extends DoNothing implements IPrintable {

    @Override
    public String toString(){//overriding the toString() method
        return "Player passes";
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return "Player " + gameState.getCurrentPlayer() + " passes.";
    }

    @Override
    public void printToConsole(AbstractGameState gameState) {
        System.out.println(this.toString());
    }
}
