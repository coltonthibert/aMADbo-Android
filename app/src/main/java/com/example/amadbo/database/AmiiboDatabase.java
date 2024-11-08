package com.example.amadbo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.amadbo.models.Amiibo;
import java.util.ArrayList;
import java.util.List;

/**
 * Database class for managing Amiibo data.
 * This class is a singleton and should be accessed through get().
 * The database contains two tables: amiibo and owned_amiibo.
 * The amiibo table contains information about all Amiibo figures.
 * The owned_amiibo table contains information about Amiibo figures that the user owns.
 */
public class AmiiboDatabase extends SQLiteOpenHelper {
    //Instance of the database
    private static AmiiboDatabase instance;

    // Database Info
    private static final String DATABASE_NAME = "amiiboDatabase";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_AMIIBO = "amiibo";
    private static final String TABLE_OWNED_AMIIBO = "owned_amiibo";

    // Common column names
    private static final String KEY_ID = "id";

    // Amiibo Table Columns
    private static final String KEY_AMIIBO_SERIES = "amiiboSeries";
    private static final String KEY_CHARACTER = "character";
    private static final String KEY_GAME_SERIES = "gameSeries";
    private static final String KEY_HEAD = "head";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_RELEASE_NA = "releaseNA";
    private static final String KEY_TAIL = "tail";
    private static final String KEY_TYPE = "type";

    // Create Amiibo table
    private static final String CREATE_AMIIBO_TABLE = "CREATE TABLE " + TABLE_AMIIBO +
            "(" +
            KEY_ID + " INTEGER PRIMARY KEY," +
            KEY_AMIIBO_SERIES + " TEXT," +
            KEY_CHARACTER + " TEXT," +
            KEY_GAME_SERIES + " TEXT," +
            KEY_HEAD + " TEXT," +
            KEY_IMAGE + " TEXT," +
            KEY_NAME + " TEXT," +
            KEY_RELEASE_NA + " TEXT," +
            KEY_TAIL + " TEXT," +
            KEY_TYPE + " TEXT" +
            ")";

    // Create a normalized table for owned amiibos
    // Owned Amiibo Table Columns
    private static final String KEY_OWNED_AMIIBO_ID = "ownedAmiiboId";
    private static final String KEY_OWNED_AMIIBO_AMIIBO_ID = "amiiboId";
    private static final String KEY_OWNED_AMIIBO_OWNED = "owned";

    // Create Owned Amiibo table
    private static final String CREATE_OWNED_AMIIBO_TABLE = "CREATE TABLE " + TABLE_OWNED_AMIIBO +
            "(" +
            KEY_OWNED_AMIIBO_ID + " INTEGER PRIMARY KEY," +
            KEY_OWNED_AMIIBO_AMIIBO_ID + " INTEGER," +
            KEY_OWNED_AMIIBO_OWNED + " INTEGER," +
            "FOREIGN KEY(" + KEY_OWNED_AMIIBO_AMIIBO_ID + ") REFERENCES " + TABLE_AMIIBO + "(" + KEY_ID + ")" +
            ")";

    // Constructor
    public AmiiboDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static AmiiboDatabase get(Context context) {
        if (instance == null) {
            instance = new AmiiboDatabase(context);
        }
        return instance;
    }

    /**
     * Create the database tables
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AMIIBO_TABLE);
        db.execSQL(CREATE_OWNED_AMIIBO_TABLE);
    }

    /**
     * Upgrade the database tables
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    /**
     * Set amiibo as owned (add to owned amiibos table)
     * @param amiiboId The id of the amiibo to set as owned
     */
    public void setOwnedAmiibo(int amiiboId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_OWNED_AMIIBO_AMIIBO_ID, amiiboId);
        values.put(KEY_OWNED_AMIIBO_OWNED, 1);

        // Inserting Row
        db.insert(TABLE_OWNED_AMIIBO, null, values);
        db.close(); // Closing database connection
    }


    /**
     * Set amiibo as not owned (remove from owned amiibos table)
     * @param amiiboId The id of the amiibo to set as not owned
     */
    public void setNotOwnedAmiibo(int amiiboId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OWNED_AMIIBO, KEY_OWNED_AMIIBO_AMIIBO_ID + " = ?", new String[] { String.valueOf(amiiboId) });
        db.close(); // Closing database connection
    }

    /**
     * Get all owned amiibos
     * @return ArrayList of all owned amiibos
     */
    public ArrayList<Amiibo> getAllOwnedAmiibos() {
        ArrayList<Amiibo> amiibos = new ArrayList<Amiibo>();
        String selectQuery = "SELECT * FROM " + TABLE_AMIIBO + " WHERE " + KEY_ID + " IN (SELECT " + KEY_OWNED_AMIIBO_AMIIBO_ID + " FROM " + TABLE_OWNED_AMIIBO + " WHERE " + KEY_OWNED_AMIIBO_OWNED + " = 1)";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Amiibo amiibo = new Amiibo();
                amiibo.setId(Integer.parseInt(cursor.getString(0)));
                amiibo.setAmiiboSeries(cursor.getString(1));
                amiibo.setCharacter(cursor.getString(2));
                amiibo.setGameSeries(cursor.getString(3));
                amiibo.setHead(cursor.getString(4));
                amiibo.setImage(cursor.getString(5));
                amiibo.setName(cursor.getString(6));
                amiibo.setReleaseNA(cursor.getString(7));
                amiibo.setTail(cursor.getString(8));
                amiibo.setType(cursor.getString(9));
                amiibos.add(amiibo);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return amiibos;
    }

    /**
     * Get the count of owned amiibos
     * @return The count of owned amiibos
     */
    public int getOwnedAmiiboCount() {
        String countQuery = "SELECT * FROM " + TABLE_OWNED_AMIIBO + " WHERE " + KEY_OWNED_AMIIBO_OWNED + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    /**
     * Check if an amiibo is in the owned amiibos table
     * @param amiiboId The id of the amiibo to check
     * @return True if the amiibo is in the owned amiibos table, false otherwise
     */
    public boolean isInOwnedAmiibos(int amiiboId) {
        String selectQuery = "SELECT * FROM " + TABLE_OWNED_AMIIBO + " WHERE " + KEY_OWNED_AMIIBO_AMIIBO_ID + " = " + amiiboId + " AND " + KEY_OWNED_AMIIBO_OWNED + " = 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        boolean isInOwnedAmiibos = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isInOwnedAmiibos;
    }

    /**
     * Add an amiibo to the amiibo table
     * @param amiibo The amiibo to add
     */
    public void addAmiibo(Amiibo amiibo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMIIBO_SERIES, amiibo.getAmiiboSeries());
        values.put(KEY_CHARACTER, amiibo.getCharacter());
        values.put(KEY_GAME_SERIES, amiibo.getGameSeries());
        values.put(KEY_HEAD, amiibo.getHead());
        values.put(KEY_IMAGE, amiibo.getImage());
        values.put(KEY_NAME, amiibo.getName());
        values.put(KEY_RELEASE_NA, amiibo.getReleaseNA());
        values.put(KEY_TAIL, amiibo.getTail());
        values.put(KEY_TYPE, amiibo.getType());

        // Inserting Row
        db.insert(TABLE_AMIIBO, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Get an amiibo by id
     * @param id The id of the amiibo to get
     * @return The amiibo with the given id
     */
    public Amiibo getAmiibo(int id) {
        Amiibo amiibo = new Amiibo();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AMIIBO, new String[]{KEY_ID, KEY_AMIIBO_SERIES,
                        KEY_CHARACTER, KEY_GAME_SERIES, KEY_HEAD, KEY_IMAGE, KEY_NAME,
                        KEY_RELEASE_NA, KEY_TAIL, KEY_TYPE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            amiibo.setId(Integer.parseInt(cursor.getString(0)));
            amiibo.setAmiiboSeries(cursor.getString(1));
            amiibo.setCharacter(cursor.getString(2));
            amiibo.setGameSeries(cursor.getString(3));
            amiibo.setHead(cursor.getString(4));
            amiibo.setImage(cursor.getString(5));
            amiibo.setName(cursor.getString(6));
            amiibo.setReleaseNA(cursor.getString(7));
            amiibo.setTail(cursor.getString(8));
            amiibo.setType(cursor.getString(9));
        }
        // return amiibo
        return amiibo;
    }

    /**
     * Get all amiibos
     * @return A list of all amiibos
     */
    public ArrayList<Amiibo> getAllAmiibos() {
        ArrayList<Amiibo> amiiboList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_AMIIBO;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Amiibo amiibo = new Amiibo();
                amiibo.setId(Integer.parseInt(cursor.getString(0)));
                amiibo.setAmiiboSeries(cursor.getString(1));
                amiibo.setCharacter(cursor.getString(2));
                amiibo.setGameSeries(cursor.getString(3));
                amiibo.setHead(cursor.getString(4));
                amiibo.setImage(cursor.getString(5));
                amiibo.setName(cursor.getString(6));
                amiibo.setReleaseNA(cursor.getString(7));
                amiibo.setTail(cursor.getString(8));
                amiibo.setType(cursor.getString(9));
                // Adding amiibo to list
                amiiboList.add(amiibo);
            } while (cursor.moveToNext());
        }
        // return amiibo list
        return amiiboList;
    }

    /**
     * Update an amiibo in the amiibo table
     * @param amiibo The amiibo to update
     */
    public int updateAmiibo(Amiibo amiibo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AMIIBO_SERIES, amiibo.getAmiiboSeries());
        values.put(KEY_CHARACTER, amiibo.getCharacter());
        values.put(KEY_GAME_SERIES, amiibo.getGameSeries());
        values.put(KEY_HEAD, amiibo.getHead());
        values.put(KEY_IMAGE, amiibo.getImage());
        values.put(KEY_NAME, amiibo.getName());
        values.put(KEY_RELEASE_NA, amiibo.getReleaseNA());
        values.put(KEY_TAIL, amiibo.getTail());
        values.put(KEY_TYPE, amiibo.getType());

        // updating row
        return db.update(TABLE_AMIIBO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(amiibo.getId())});
    }

    /**
     * Delete an amiibo from the amiibo table
     * @param amiibo The amiibo to delete
     */
    public void deleteAmiibo(Amiibo amiibo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_AMIIBO, KEY_ID + " = ?",
                new String[]{String.valueOf(amiibo.getId())});
        db.close();
    }

    /**
     * Get the number of amiibos in the amiibo table
     * @return The number of amiibos in the amiibo table
     */
    public int getAmiiboCount() {
        String countQuery = "SELECT  * FROM " + TABLE_AMIIBO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    /**
     * Search for amiibos in the amiibo table
     * @param query The query to search for
     * @param searchType The type of search to perform (name, gameSeries, amiiboSeries)
     * @param includeType The types to include in the search (figure, card, other)
     * @return A list of amiibos that match the search query
     */
    public ArrayList<Amiibo> searchAmiibos(String query, String searchType, List<String> includeType) {

        ArrayList<Amiibo> searchedAmiibos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("AmiiboDatabase", query);
        String selectQuery = "";
        String whereClause = "";
        switch (searchType){
            case "name":
                whereClause = KEY_NAME;
                break;
            case "gameSeries":
                whereClause = KEY_GAME_SERIES;
                break;
            case "amiiboSeries":
                whereClause = KEY_AMIIBO_SERIES;
                break;
            default:
                whereClause = KEY_NAME;
        }

        String includeClause = "";
        // If no types are selected, search all types, otherwise search only selected types
        if (includeType.size() > 0) {
            includeClause = " AND " + KEY_TYPE + " IN (";
            for (int i = 0; i < includeType.size(); i++) {
                includeClause = includeClause + "'" + includeType.get(i) + "'";
                if (i < includeType.size() - 1) {
                    includeClause = includeClause + ", ";
                }
            }
            includeClause = includeClause + ")";
        }


        // Define query to search for amiibos
        selectQuery = "SELECT * FROM " + TABLE_AMIIBO + " WHERE " + whereClause + " LIKE \"%" + query + "%\"" + includeClause;
        Log.d("Search Query", selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Get column indices
        int idIndex = cursor.getColumnIndex(KEY_ID);
        int amiiboSeriesIndex = cursor.getColumnIndex(KEY_AMIIBO_SERIES);
        int characterIndex = cursor.getColumnIndex(KEY_CHARACTER);
        int gameSeriesIndex = cursor.getColumnIndex(KEY_GAME_SERIES);
        int headIndex = cursor.getColumnIndex(KEY_HEAD);
        int imageIndex = cursor.getColumnIndex(KEY_IMAGE);
        int nameIndex = cursor.getColumnIndex(KEY_NAME);
        int releaseNAIndex = cursor.getColumnIndex(KEY_RELEASE_NA);
        int tailIndex = cursor.getColumnIndex(KEY_TAIL);
        int typeIndex = cursor.getColumnIndex(KEY_TYPE);

        // Loop through cursor and add matching amiibos to the list
        if (cursor.moveToFirst()) {
            do {
                Amiibo amiibo = new Amiibo();
                amiibo.setId(cursor.getInt(idIndex));
                amiibo.setAmiiboSeries(cursor.getString(amiiboSeriesIndex));
                amiibo.setCharacter(cursor.getString(characterIndex));
                amiibo.setGameSeries(cursor.getString(gameSeriesIndex));
                amiibo.setHead(cursor.getString(headIndex));
                amiibo.setImage(cursor.getString(imageIndex));
                amiibo.setName(cursor.getString(nameIndex));
                amiibo.setReleaseNA(cursor.getString(releaseNAIndex));
                amiibo.setTail(cursor.getString(tailIndex));
                amiibo.setType(cursor.getString(typeIndex));
                searchedAmiibos.add(amiibo);
            } while (cursor.moveToNext());
        }
        // Close cursor and return searched amiibos
        cursor.close();
        return searchedAmiibos;
    }

    /**
     * Clear the amiibo database, optionally clearing owned amiibos
     * @param clearOwned
     */
    public void clearDatabase(boolean clearOwned) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_AMIIBO);

        if (clearOwned) {
            db.execSQL("DELETE FROM " + TABLE_OWNED_AMIIBO);
        }

        db.close();
    }

    /**
     * Add the Gwimbly amiibo to the database (Easter egg)
     */
    public void addGwimblyAmiibo() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMIIBO_SERIES, "Smiling Friends");
        values.put(KEY_CHARACTER, "Gwimbly");
        values.put(KEY_GAME_SERIES, "Gwimbly");
        values.put(KEY_HEAD, "01010300");
        values.put(KEY_IMAGE, "https://cdn.discordapp.com/attachments/488479298530050057/1225819093878181920/amiibo_qwimbly.png?ex=662283fe&is=66100efe&hm=6516d77ef9b523b1657aaf8e4a5bc69db5604ba53e4eeffe472fdc06961d179c&");
        values.put(KEY_NAME, "Gwimbly");
        values.put(KEY_RELEASE_NA, "2024-03-31");
        values.put(KEY_TAIL, "04140902");
        values.put(KEY_TYPE, "Figure");
        db.insert(TABLE_AMIIBO, null, values);
        db.close();
    }
}