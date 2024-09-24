package games.root_final.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import games.root_final.RootGameState;
import games.root_final.RootParameters;
import games.root_final.components.Item;

import java.util.Objects;

public class ExhaustHammerForCrafting extends AbstractAction{
    public final int playerID;
    public final RootParameters.ClearingTypes activate;
    public final RootParameters.ClearingTypes actual;

    public ExhaustHammerForCrafting(int playerID, RootParameters.ClearingTypes activate, RootParameters.ClearingTypes actual){
        this.playerID = playerID;
        this.activate = activate;
        this.actual = actual;
    }
    @Override
    public boolean execute(AbstractGameState gs) {
        RootGameState state = (RootGameState) gs;
        if (gs.getCurrentPlayer() == playerID){

            for (Item item: state.getSachel()){
                if (item.itemType == Item.ItemType.hammer && !item.damaged && item.refreshed){
                    item.refreshed = false;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public AbstractAction copy() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){return true;}
        if (obj instanceof ExhaustHammerForCrafting cca){
            return playerID == cca.playerID && activate == cca.activate && actual == cca.actual;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash("ExhaustHammer", playerID, activate, actual);
    }

    @Override
    public String getString(AbstractGameState gameState) {
        RootGameState gs = (RootGameState) gameState;
        return gs.getPlayerFaction(playerID).toString() + " exhausts hammer " + activate.toString() + " to satisfy " + actual.toString() + "requirement";
    }
}
