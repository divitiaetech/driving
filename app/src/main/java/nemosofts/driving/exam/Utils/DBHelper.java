package nemosofts.driving.exam.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import nemosofts.driving.exam.item.ItemResult;


public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "exam.db";
    private final SQLiteDatabase db;

    private static final String TAG_ID = "id";
    private static final String TAG_DATE = "date";
    private static final String TAG_TIME = "time";
    private static final String TAG_RESULT = "exam_result";

    public static final String TABLE_RESULT = "result";

    private final String[] columns_result = new String[]{TAG_ID, TAG_DATE, TAG_TIME, TAG_RESULT};

    // Creating table query
    private static final String CREATE_TABLE_RESULT = "create table " + TABLE_RESULT + "(" +
            TAG_ID + " integer PRIMARY KEY AUTOINCREMENT," +
            TAG_DATE + " TEXT," +
            TAG_TIME + " TEXT," +
            TAG_RESULT + " TEXT);";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, 5);
        db = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE_RESULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    public ArrayList<ItemResult> getResult(String table) {
        ArrayList<ItemResult> arrayList = new ArrayList<>();

        Cursor cursor = db.query(table, columns_result, null, null, null, null, TAG_ID + " DESC");

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String id = cursor.getString(cursor.getColumnIndex(TAG_ID));
                String date = cursor.getString(cursor.getColumnIndex(TAG_DATE));
                String time = cursor.getString(cursor.getColumnIndex(TAG_TIME));
                String result = cursor.getString(cursor.getColumnIndex(TAG_RESULT));
                ItemResult objItem = new ItemResult(id, date, time, result);
                arrayList.add(objItem);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return arrayList;
    }

    public void addtoResult(String date, String time, String result) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_DATE, date);
        contentValues.put(TAG_TIME, time);
        contentValues.put(TAG_RESULT, result);

        db.insert(TABLE_RESULT, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}