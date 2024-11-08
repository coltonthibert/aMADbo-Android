package com.example.amadbo.models;

/**
 * Represents the usage details of an Amiibo, including games and their usage information.
 */
public class Usage {
    private String gameName;
    private String usage;
    private boolean write;

    public Usage() {
    }

    /**
     * Constructs a new Usage object with specified properties.
     *
     * @param gameName The name of the game.
     * @param usage    The usage information of the game.
     * @param write    Whether the usage information can be written to the database.
     */
    public Usage(String gameName, String usage, boolean write) {
        this.gameName = gameName;
        this.usage = usage;
        this.write = write;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }
}
