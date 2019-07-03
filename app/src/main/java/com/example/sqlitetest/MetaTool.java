package com.example.sqlitetest;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MetaTool {

	public static String[][] CursorToArray(Cursor cursor)  {
		if (cursor == null)
			return null;
		String[] col = cursor.getColumnNames();
		String[][] data = new String[cursor.getCount()][col.length];
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			for (int j = 0; j < col.length; j++) {
				try {
					data[i][j] = (cursor.getString(j) == null ? "" : cursor.getString(j));
				} catch (Exception e) {
					if (e.getMessage().contains("BLOB"))
						data[i][j] = "[BLOB]";
					else {
						throw e;
					}
				}
			}
		}
		return data;
	}

	public static List<HashMap<String, String>> CursorToList(Cursor cursor) {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		if (cursor == null)
			return data;
		String[] col = cursor.getColumnNames();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToPosition(i);
			HashMap<String, String> map = new HashMap<String, String>();
			for (int j = 0; j < col.length; j++)
				try {
					map.put(col[j].toLowerCase(), cursor.getString(j) == null ? "" : cursor.getString(j));
				} catch (Exception localException) {
				}
			data.add(map);
		}
		return data;
	}

	public static JSONArray cursorToJson(Cursor c) throws JSONException {
		JSONArray jArr = new JSONArray();
		if (c != null) {
			String[] colNames = c.getColumnNames();
			for (int i = 0; i < c.getCount(); i++) {
				c.moveToPosition(i);
				JSONObject jObj = new JSONObject();
				for (int j = 0; j < colNames.length; j++) {
					jObj.put(colNames[j],
							c.getString(j) == null || c.getString(j).equals("null") ? "" : c.getString(j));
				}
				jArr.put(jObj);
			}
		}

		return jArr;
	}
}
