package observis.leetgames.com.mikkeliopenspot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by idump on 22-May-16.
 */
public class ParkingRepo {
    private DBHelper dbHelper;

    public ParkingRepo(Context context) {
        dbHelper = new DBHelper(context);
    }

    public String[] id_c = {"1", "2", "3", "4", "5", "6"};
    public String[] coord_c = {"61.689739,27.274028", "61.690969,27.270927", "61.680975,27.258839", "61.691395,27.273917", "61.688228,27.277867", "61.689223,27.267973"};
    public String[] type_c = {"free", "free", "free", "free", "free", "free"};
    public String[] descr_c = {"Free parking space", "Free parking space", "Free parking space", "Free parking space", "Free parking space", "Free parking space"};


    public int insert(ParkingLots parkingLots) {

        //open Connection to db
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ParkingLots.KEY_coord, ParkingLots.coord);
        values.put(ParkingLots.KEY_type, ParkingLots.type);

        values.put(ParkingLots.KEY_descr, ParkingLots.descr);


        //inserting row
        long parking_ID = db.insert(ParkingLots.TABLE, null, values);
        db.close();
        return (int) parking_ID;
    }

    public ArrayList<HashMap<String, String>> getParkingList() {
        //open connection
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                ParkingLots.KEY_ID + "," +
                ParkingLots.KEY_coord + "," +
                ParkingLots.KEY_type + "," +
                ParkingLots.KEY_descr + " FROM " + ParkingLots.TABLE;

        ArrayList<HashMap<String, String>> parkingList = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        //check all rows and add to list

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> parking = new HashMap<String, String>();
                parking.put("id", cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_ID)));
                parking.put("coord", cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_coord)));
                parking.put("type", cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_type)));
                parking.put("descr", cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_descr)));

                parkingList.add(parking);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return parkingList;
    }

    public ParkingLots getParkingById(int Id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT " +
                ParkingLots.KEY_ID + "," +
                ParkingLots.KEY_coord + "," +
                ParkingLots.KEY_type + "," +
                ParkingLots.KEY_descr +
                " FROM " + ParkingLots.TABLE +
                " WHERE " + ParkingLots.KEY_ID + "=?";
        int iCount = 0;
        ParkingLots parking = new ParkingLots();

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(Id)});

        if (cursor.moveToFirst()) {
            do {
                ParkingLots.parking_ID = cursor.getInt(cursor.getColumnIndex(ParkingLots.KEY_ID));
                ParkingLots.coord = cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_coord));
                ParkingLots.type = cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_type));
                ParkingLots.descr = cursor.getString(cursor.getColumnIndex(ParkingLots.KEY_descr));
                String result = ParkingLots.coord + ParkingLots.type + ParkingLots.parking_ID;
                System.out.println(result);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return parking;
    }

    public void delete() {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String delete = "DROP TABLE " + ParkingLots.TABLE;
        db.execSQL(delete);

    }

    public void populate() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        try {
            for (int i = 0; i < id_c.length; i++) {

                values.put(ParkingLots.KEY_ID, id_c[i]);
                values.put(ParkingLots.KEY_coord, coord_c[i]);
                values.put(ParkingLots.KEY_type, type_c[i]);
                values.put(ParkingLots.KEY_descr, descr_c[i]);
                db.insert(ParkingLots.TABLE, null, values);
            }

            db.close();
        }catch(Exception e){
            System.out.println("error populating db");
        }
    }
}
