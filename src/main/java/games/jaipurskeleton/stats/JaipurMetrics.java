package games.jaipurskeleton.stats;

import core.interfaces.IGameEvent;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Collections;
import games.jaipurskeleton.JaipurGameState;
import games.jaipurskeleton.components.JaipurCard;
import games.jaipurskeleton.actions.TakeCards;
import core.actions.AbstractAction;

public class JaipurMetrics implements IMetricsCollection {
    public static class RoundScoreDifference extends AbstractMetric {
        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            JaipurGameState gs = (JaipurGameState) e.state;
            double scoreDiff = 0;
            for (int i = 0; i < gs.getNPlayers() - 1; i++) {
                scoreDiff += Math.abs(gs.getPlayerScores().get(i).getValue()
                        - gs.getPlayerScores().get(i + 1).getValue());
            }
            scoreDiff /= (gs.getNPlayers() - 1);

            records.put("ScoreDiff", scoreDiff);

            return true;
        }
        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.ROUND_OVER);
        }
        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            Map<String, Class<?>> columns = new HashMap<>();
            columns.put("ScoreDiff", Double.class);
            return columns;
        }
    }
    public static class PurchaseFromMarket extends AbstractMetric {
        JaipurCard.GoodType[]goodTypes;
        public PurchaseFromMarket() {
            super();
            goodTypes = JaipurCard.GoodType.values();
        }
        public PurchaseFromMarket(String[] args) {
            super(args);
            goodTypes = new JaipurCard.GoodType[args.length];
            for (int i = 0; i < args.length; i++) {
                goodTypes[i] = JaipurCard.GoodType.valueOf(args[i]);
            }
        }
        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            AbstractAction action = e.action;
            if (action instanceof TakeCards tc) {

                for (JaipurCard.GoodType type : goodTypes) {
                    if (tc.howManyPerTypeTakeFromMarket.containsKey(type)) {
                        records.put("Purchase-" + type.name(), tc.howManyPerTypeTakeFromMarket.get(type));
                    }
                }
                return true;
            }
            return false;
        }
        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.ACTION_CHOSEN);
        }
        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            Map<String, Class<?>> columns = new HashMap<>();
            for (JaipurCard.GoodType type : goodTypes) {
                columns.put("Purchase-" + type.name(), Integer.class);
            }
            columns.put("Purchase", String.class);
            return columns;
        }
    }
    public static class PurchaseFromMarketTot extends AbstractMetric {
        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            AbstractAction action = e.action;
            if (action instanceof TakeCards tc) {
                int totalPurchases = 0;
                for (JaipurCard.GoodType type : JaipurCard.GoodType.values()) {
                    if (tc.howManyPerTypeTakeFromMarket.containsKey(type)) {
                        totalPurchases += tc.howManyPerTypeTakeFromMarket.get(type);
                    }
                }
                records.put("PurchaseFromMarketTot", totalPurchases);
                return true;
            }
            return false;
        }
        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.ACTION_CHOSEN);
        }
        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            Map<String, Class<?>> columns = new HashMap<>();
            columns.put("PurchaseFromMarketTot", Integer.class);
            return columns;
        }
    }
    public static class CamelCollection extends AbstractMetric {
        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            AbstractAction action = e.action;
            if (action instanceof TakeCards tc) {
                int camelsTaken = tc.howManyPerTypeTakeFromMarket.getOrDefault(JaipurCard.GoodType.Camel, 0);
                if (camelsTaken > 0) {
                    records.put("CamelsCollected", camelsTaken);
                    return true;
                }
            }
            return false;
        }
        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(Event.GameEvent.ACTION_CHOSEN);
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            Map<String, Class<?>> columns = new HashMap<>();
            columns.put("CamelsCollected", Integer.class);
            return columns;
        }
    }
}
