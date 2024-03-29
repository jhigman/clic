package io.flic.lib;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by Emil on 2015-09-06.
 */
class DB extends SQLiteOpenHelper {
	private static final String TAG = "DB";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "fliclib";
	private static final String BUTTONS_TABLE_NAME = "buttons";
	private static final String BUTTONS_TABLE_CREATE =
			"CREATE TABLE " + BUTTONS_TABLE_NAME + " (mac TEXT UNIQUE);";

	public DB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(BUTTONS_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public HashMap<String, FlicButton> getButtons(FlicManager manager) {
		SQLiteDatabase db = this.getWritableDatabase();
		HashMap<String, FlicButton> map = new HashMap<>();
		Cursor query = db.query(BUTTONS_TABLE_NAME, null, null, null, null, null, null);
		query.moveToFirst();
		if (!query.isAfterLast()) {
			do {
				String mac = query.getString(query.getColumnIndex("mac"));
				FlicButton button = new FlicButton(manager, mac);
				map.put(mac, button);
			} while(query.moveToNext());
		}
		query.close();
		db.close();

		return map;
	}

	public void addButton(String mac) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mac", mac);
		db.insertWithOnConflict(BUTTONS_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_NONE);
		db.close();
	}

	public void removeButton(String mac) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(BUTTONS_TABLE_NAME, "mac = ?", new String[] { mac });
		db.close();
	}
}
