package games.descent2e;

import core.CoreConstants;
import core.interfaces.IGameEvent;
import evaluation.listeners.MetricsGameListener;
import evaluation.metrics.AbstractMetric;
import evaluation.metrics.Event;
import evaluation.metrics.IMetricsCollection;
import games.descent2e.components.Figure;
import games.descent2e.components.Monster;
import utilities.Pair;
import utilities.Vector2D;

import java.util.*;

import static evaluation.metrics.Event.GameEvent.*;

public class DescentMetrics implements IMetricsCollection {

    public static class Heroes extends AbstractMetric
    {
        public Heroes() {
            super();
        }

        public Heroes(Event.GameEvent... args) {
            super(args);
        }

        public Heroes(Set<IGameEvent> events) {
            super(events);
        }

        public Heroes(String[] args) {
            super(args);
        }

        public Heroes(String[] args, Event.GameEvent... events) {
            super(args, events);
        }

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            DescentGameState dgs = (DescentGameState) e.state;
            for (int i = 0; i < dgs.getHeroes().size(); i++) {
                records.put("Hero " + (i + 1), dgs.getHeroes().get(i).getName().replace("Hero: ", ""));
            }
            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return new HashSet<IGameEvent>() {{
                add(ABOUT_TO_START);
            }};
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            HashMap<String, Class<?>> retVal = new HashMap<>();
            for (int i = 0; i < nPlayersPerGame-1; i++) {
                retVal.put("Hero " + (i + 1), String.class);
            }
            return retVal;
        }
    }

    public static class Kills extends AbstractMetric
    {
        public Kills() {
            super();
        }

        public Kills(Event.GameEvent... args) {
            super(args);
        }

        public Kills(Set<IGameEvent> events) {
            super(events);
        }

        public Kills(String[] args) {
            super(args);
        }

        public Kills(String[] args, Event.GameEvent... events) {
            super(args, events);
        }

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            DescentGameState dgs = (DescentGameState) e.state;

            List<Pair<String, String>> kills = dgs.getDefeatedFigures();

            for (Pair<String, String> kill : kills) {
                String[] a = kill.a.split(";");
                String[] b = kill.b.split(";");

                String target = a[0];
                String killer = b[0];

                int index1 = Integer.parseInt(a[1]);
                int index2 = Integer.parseInt(b[1]);

                // Occurs if the Master is defeated, thus Minion 1 is considered 0
                if (index1 == 0) index1++;

                if (target.contains("Hero:"))
                {
                    records.put("Hero " + index1, target.replace("Hero: ", "") + " defeated by " + killer);
                }
                else
                {
                    if (target.contains("Goblin"))
                    {
                        if (target.contains("master"))
                        {
                            records.put("Goblin Master", target + " defeated by " + killer.replace("Hero: ", ""));
                        }
                        else
                        {
                            records.put("Goblin Minion " + index1, target + " defeated by " + killer.replace("Hero: ", ""));
                        }
                    }
                    else
                    {
                        if (target.contains("Barghest"))
                        {
                            if (target.contains("master"))
                            {
                                records.put("Barghest Master", target + " defeated by " + killer.replace("Hero: ", ""));
                            }
                            else
                            {
                                records.put("Barghest Minion " + index1, target + " defeated by " + killer.replace("Hero: ", ""));
                            }
                        }
                    }
                }
            }

            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return new HashSet<IGameEvent>() {{
                add(Event.GameEvent.ROUND_OVER);
                add(GAME_OVER);
            }};
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            HashMap<String, Class<?>> retVal = new HashMap<>();
            retVal.put("Hero 1", String.class);
            retVal.put("Hero 2", String.class);
            retVal.put("Hero 3", String.class);
            retVal.put("Hero 4", String.class);
            retVal.put("Goblin Master", String.class);
            retVal.put("Goblin Minion 1", String.class);
            retVal.put("Goblin Minion 2", String.class);
            retVal.put("Goblin Minion 3", String.class);
            retVal.put("Goblin Minion 4", String.class);
            retVal.put("Barghest Master", String.class);
            retVal.put("Barghest Minion 1", String.class);
            retVal.put("Barghest Minion 2", String.class);
            retVal.put("Barghest Minion 3", String.class);
            return retVal;
        }
    }

    public static class Positions extends AbstractMetric {

        public Positions() {
            super();
        }

        public Positions(Event.GameEvent... args) {
            super(args);
        }

        public Positions(Set<IGameEvent> events) {
            super(events);
        }

        public Positions(String[] args) {
            super(args);
        }

        public Positions(String[] args, Event.GameEvent... events) {
            super(args, events);
        }

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            DescentGameState dgs = (DescentGameState) e.state;
            for (int i = 0; i < dgs.getHeroes().size(); i++) {
                Vector2D pos = dgs.getHeroes().get(i).getPosition();
                records.put("Hero " + (i + 1), pos.toString());
            }

            List<Monster> goblins = dgs.getMonsters().get(0);
            List<Monster> barghests = dgs.getMonsters().get(1);

            int index = 0;
            records.put("Goblin Master", "Dead");

            for (Monster goblin: goblins)
            {
                Vector2D pos = goblin.getPosition();
                if (goblin.getName().contains("master"))
                {
                    records.put("Goblin Master", pos.toString());
                }
                else
                {
                    records.put("Goblin Minion " + (index + 1), pos.toString());
                    index++;
                }
            }

            for (int i = index; i < dgs.getOriginalMonsters().get(0).size() - 1; i++)
            {
                records.put("Goblin Minion " + (i + 1), "Dead");
            }

            index = 0;
            records.put("Barghest Master", "Dead");

            for (Monster barghest: barghests)
            {
                Vector2D pos = barghest.getPosition();
                if (barghest.getName().contains("master"))
                {
                    records.put("Barghest Master", pos.toString());
                }
                else
                {
                    records.put("Barghest Minion " + (index + 1), pos.toString());
                    index++;
                }
            }

            for (int i = index; i < dgs.getOriginalMonsters().get(1).size() - 1; i++)
            {
                records.put("Barghest Minion " + (i + 1), "Dead");
            }

            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return new HashSet<IGameEvent>() {{
                add(ABOUT_TO_START);
                add(Event.GameEvent.ROUND_OVER);
                add(GAME_OVER);
            }};
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            HashMap<String, Class<?>> retVal = new HashMap<>();
            retVal.put("Hero 1", String.class);
            retVal.put("Hero 2", String.class);
            retVal.put("Hero 3", String.class);
            retVal.put("Hero 4", String.class);
            retVal.put("Goblin Master", String.class);
            retVal.put("Goblin Minion 1", String.class);
            retVal.put("Goblin Minion 2", String.class);
            retVal.put("Goblin Minion 3", String.class);
            retVal.put("Goblin Minion 4", String.class);
            retVal.put("Barghest Master", String.class);
            retVal.put("Barghest Minion 1", String.class);
            retVal.put("Barghest Minion 2", String.class);
            retVal.put("Barghest Minion 3", String.class);
            return retVal;
        }
    }

    public static class HealthPoints extends AbstractMetric {

        public HealthPoints() {
            super();
        }

        public HealthPoints(Event.GameEvent... args) {
            super(args);
        }

        public HealthPoints(Set<IGameEvent> events) {
            super(events);
        }

        public HealthPoints(String[] args) {
            super(args);
        }

        public HealthPoints(String[] args, Event.GameEvent... events) {
            super(args, events);
        }

        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            DescentGameState dgs = (DescentGameState) e.state;
            for (int i = 0; i < dgs.getHeroes().size(); i++) {
                records.put("Hero " + (i + 1) + " HP", Integer.toString(dgs.getHeroes().get(i).getAttributeValue(Figure.Attribute.Health)));
            }
            records.put("Overlord Fatigue", Integer.toString(dgs.getOverlord().getAttributeValue(Figure.Attribute.Fatigue)));

            List<Monster> goblins = dgs.getMonsters().get(0);
            List<Monster> barghests = dgs.getMonsters().get(1);

            int index = 0;
            records.put("Goblin Master HP", "Dead");

            for (Monster goblin: goblins)
            {
                if (goblin.getName().contains("master"))
                {
                    records.put("Goblin Master HP", Integer.toString(goblin.getAttributeValue(Figure.Attribute.Health)));
                }
                else
                {
                    records.put("Goblin Minion " + (index + 1) + " HP", Integer.toString(goblin.getAttributeValue(Figure.Attribute.Health)));
                    index++;
                }
            }

            for (int i = index; i < dgs.getOriginalMonsters().get(0).size() - 1; i++)
            {
                records.put("Goblin Minion " + (i + 1) + " HP", "Dead");
            }

            index = 0;
            records.put("Barghest Master HP", "Dead");

            for (Monster barghest: barghests)
            {
                if (barghest.getName().contains("master"))
                {
                    records.put("Barghest Master HP", Integer.toString(barghest.getAttributeValue(Figure.Attribute.Health)));
                }
                else
                {
                    records.put("Barghest Minion " + (index + 1) + " HP", Integer.toString(barghest.getAttributeValue(Figure.Attribute.Health)));
                    index++;
                }
            }

            for (int i = index; i < dgs.getOriginalMonsters().get(1).size() - 1; i++)
            {
                records.put("Barghest Minion " + (i + 1) + " HP", "Dead");
            }
            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return new HashSet<IGameEvent>() {{
                add(Event.GameEvent.ROUND_OVER);
                add(GAME_OVER);
            }};
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            HashMap<String, Class<?>> retVal = new HashMap<>();
            retVal.put("Hero 1 HP", String.class);
            retVal.put("Hero 2 HP", String.class);
            retVal.put("Hero 3 HP", String.class);
            retVal.put("Hero 4 HP", String.class);
            retVal.put("Overlord Fatigue", String.class);
            retVal.put("Goblin Master HP", String.class);
            retVal.put("Goblin Minion 1 HP", String.class);
            retVal.put("Goblin Minion 2 HP", String.class);
            retVal.put("Goblin Minion 3 HP", String.class);
            retVal.put("Goblin Minion 4 HP", String.class);
            retVal.put("Barghest Master HP", String.class);
            retVal.put("Barghest Minion 1 HP", String.class);
            retVal.put("Barghest Minion 2 HP", String.class);
            retVal.put("Barghest Minion 3 HP", String.class);
            return retVal;
        }
    }

        public static class WinnerType extends AbstractMetric {

        public WinnerType() {
            super();
        }

        public WinnerType(Event.GameEvent... args) {
            super(args);
        }
        public WinnerType(Set<IGameEvent> events) {
            super(events);
        }

        public WinnerType(String[] args) {
            super(args);
        }

        public WinnerType(String[] args, Event.GameEvent... events) {
            super(args, events);
        }


        @Override
        protected boolean _run(MetricsGameListener listener, Event e, Map<String, Object> records) {
            if (e.state.getPlayerResults()[0] == CoreConstants.GameResult.WIN_GAME)
                records.put("WinnerType", "overlord");
            else if (e.state.getPlayerResults()[0] == CoreConstants.GameResult.TIMEOUT)
                records.put("WinnerType", "timeout");
            else
                records.put("WinnerType", "hero");
            return true;
        }

        @Override
        public Set<IGameEvent> getDefaultEventTypes() {
            return Collections.singleton(GAME_OVER);
        }

        @Override
        public Map<String, Class<?>> getColumns(int nPlayersPerGame, Set<String> playerNames) {
            HashMap<String, Class<?>> retVal = new HashMap<>();
            retVal.put("WinnerType", String.class);
            return retVal;
        }
    }

}
