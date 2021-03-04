package evaluation;

import core.AbstractGameState;
import core.Game;
import core.actions.AbstractAction;
import core.interfaces.IGameAttribute;
import core.interfaces.IGameListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static core.CoreConstants.GameEvents;
import static java.util.stream.Collectors.joining;

public class GameLogger implements IGameListener {

    public GameLogger(List<GameEvents> eventFilter, String logFile, boolean append) {
        this.logFile = logFile;
        this.append = append;
        this.filter = new ArrayList<>(eventFilter);
    }

    private List<IGameAttribute> gameAttributes = new ArrayList<>();
    private boolean append;
    private String logFile;
    private List<GameEvents> filter;
    private FileWriter writer;

    public void addAttribute(IGameAttribute attribute) {
        gameAttributes.add(attribute);
    }

    public void addAttributes(List<IGameAttribute> attributes) {
        gameAttributes.addAll(attributes);
    }

    public void clearAttributes() {
        gameAttributes = new ArrayList<>();
    }

    @Override
    public void onGameEvent(GameEvents type, Game game) {
        if (filter.contains(type)) {
            String allData = gameAttributes.stream()
                    .map(att -> att.getAsString(game.getGameState(), null))
                    .collect(joining("\t"));
            ;
            writeData(allData + "\n");
        }
    }

    @Override
    public void onEvent(GameEvents event, AbstractGameState state, AbstractAction action) {
        if (filter.contains(event)) {
            String allData = gameAttributes.stream()
                    .map(att -> att.getAsString(state, action))
                    .collect(joining("\t"));
            ;
            writeData(allData + "\n");
        }
    }

    @Override
    public Map<String, Object> getAllData() {
        return null;
    }

    @Override
    public void clear() {
        // nothing
    }

    private void writeData(String data) {
        // first we open the file, and th
        try {
            if (writer == null) {
                boolean fileExists = new File(logFile).exists();
                writer = new FileWriter(logFile, append);
                if (!append || !fileExists) {
                    writer.write(getHeader());
                }
            }
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Problem with file " + logFile + " : " + e.getMessage());
        }
    }

    private String getHeader() {
        String headerData = gameAttributes.stream()
                .map(IGameAttribute::name)
                .collect(joining("\t"));
        return headerData + "\n";
    }

    public void close() {
        try {
            if (writer != null) {
                writer.flush();
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssertionError("Problem closing file " + logFile + " : " + e.getMessage());
        }
    }
}
