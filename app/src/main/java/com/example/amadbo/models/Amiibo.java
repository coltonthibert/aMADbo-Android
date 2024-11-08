package com.example.amadbo.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents an Amiibo object with various properties.
 */
public class Amiibo implements Parcelable {
    private int id;
    private String amiiboSeries;
    private String character;
    private String gameSeries;
    private String head;
    private String image;
    private String name;
    private String releaseNA; // Only NA release date
    private String tail;
    private String type;


    //Required empty constructor
    public Amiibo(){}

    /**
     * Constructs a new Amiibo object with specified properties.
     *
     * @param amiiboSeries The series of the Amiibo.
     * @param character    The character represented by the Amiibo.
     * @param gameSeries   The game series the Amiibo belongs to.
     * @param head         The head of the Amiibo.
     * @param image        The image representing the Amiibo.
     * @param name         The name of the Amiibo.
     * @param releaseNA    The release date of the Amiibo in North America.
     * @param tail         The tail of the Amiibo.
     * @param type         The type of the Amiibo.
     */
    public Amiibo(String amiiboSeries, String character, String gameSeries, String head,
                  String image, String name, String releaseNA, String tail, String type) {
        this.amiiboSeries = amiiboSeries;
        this.character = character;
        this.gameSeries = gameSeries;
        this.head = head;
        this.image = image;
        this.name = name;
        this.releaseNA = releaseNA;
        this.tail = tail;
        this.type = type;
    }

    /**
     * Constructs a new Amiibo object by reading from the database.
     *
     * @param id           The ID of the Amiibo.
     * @param amiiboSeries The series of the Amiibo.
     * @param character    The character represented by the Amiibo.
     * @param gameSeries   The game series the Amiibo belongs to.
     * @param head         The head of the Amiibo.
     * @param image        The image representing the Amiibo.
     * @param name         The name of the Amiibo.
     * @param releaseNA    The release date of the Amiibo in North America.
     * @param tail         The tail of the Amiibo.
     * @param type         The type of the Amiibo.
     * @param lastUpdated  The last updated timestamp of the Amiibo.
     */
    public Amiibo(int id, String amiiboSeries, String character, String gameSeries, String head,
                  String image, String name, String releaseNA, String tail, String type, long lastUpdated) {
        this.id = id;
        this.amiiboSeries = amiiboSeries;
        this.character = character;
        this.gameSeries = gameSeries;
        this.head = head;
        this.image = image;
        this.name = name;
        this.releaseNA = releaseNA;
        this.tail = tail;
        this.type = type;
    }

    // Getters and setters
    public String getAmiiboSeries() {
        return amiiboSeries;
    }

    public void setAmiiboSeries(String amiiboSeries) {
        this.amiiboSeries = amiiboSeries;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getGameSeries() {
        return gameSeries;
    }

    public void setGameSeries(String gameSeries) {
        this.gameSeries = gameSeries;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseNA() {
        return releaseNA;
    }

    public void setReleaseNA(String releaseNA) {
        this.releaseNA = releaseNA;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Amiibo{" +
                "id=" + id +
                ", amiiboSeries='" + amiiboSeries + '\'' +
                ", character='" + character + '\'' +
                ", gameSeries='" + gameSeries + '\'' +
                ", head='" + head + '\'' +
                ", image='" + image + '\'' +
                ", name='" + name + '\'' +
                ", releaseNA='" + releaseNA + '\'' +
                ", tail='" + tail + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    //Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.amiiboSeries);
        dest.writeString(this.character);
        dest.writeString(this.gameSeries);
        dest.writeString(this.head);
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.releaseNA);
        dest.writeString(this.tail);
        dest.writeString(this.type);
    }

    public void readFromParcel(Parcel source) {
        this.amiiboSeries = source.readString();
        this.character = source.readString();
        this.gameSeries = source.readString();
        this.head = source.readString();
        this.image = source.readString();
        this.name = source.readString();
        this.releaseNA = source.readString();
        this.tail = source.readString();
        this.type = source.readString();
    }

    protected Amiibo(Parcel in) {
        this.amiiboSeries = in.readString();
        this.character = in.readString();
        this.gameSeries = in.readString();
        this.head = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.releaseNA = in.readString();
        this.tail = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<Amiibo> CREATOR = new Parcelable.Creator<Amiibo>() {
        @Override
        public Amiibo createFromParcel(Parcel source) {
            return new Amiibo(source);
        }

        @Override
        public Amiibo[] newArray(int size) {
            return new Amiibo[size];
        }
    };
}
