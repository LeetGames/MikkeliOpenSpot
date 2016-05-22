package observis.leetgames.com.mikkeliopenspot;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.io.File;

/**
 * Created by idump on 21-May-16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Parking.db";
   public DBHelper(Context context ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //All necessary tables you like to create will create here

        String CREATE_TABLE_PARKING_LOTS = "CREATE TABLE " + ParkingLots.TABLE  + "("
                + ParkingLots.KEY_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ParkingLots.KEY_coord + " TEXT, "
                + ParkingLots.KEY_type + " TEXT, "
                + ParkingLots.KEY_descr + " TEXT)";
              // db.execSQL(CREATE_TABLE_PARKING_LOTS);
        new dBAsyncTask(CREATE_TABLE_PARKING_LOTS,db).execute();


    }

    private class dBAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;
        String s;
        SQLiteDatabase db;

        dBAsyncTask(String su,SQLiteDatabase dbu) {
            db = dbu;
            s = su;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            db.execSQL(s);
           return null;
        }

        @Override
        protected void onPostExecute(Void v) {

        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + ParkingLots.TABLE);

        // Create tables again
        onCreate(db);

    }
    public static void deleteAll(SQLiteDatabase db) {
        db.execSQL("DROP TABLE" + ParkingLots.TABLE);
    }

   }

