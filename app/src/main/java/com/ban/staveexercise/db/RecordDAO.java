package com.ban.staveexercise.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.ban.staveexercise.base.ComBase;

public class RecordDAO extends BaseDAO<Record> {
	private String[] allValueName = { "time", "corrent", "incorrent" };
	private int number = 30;// 每页30条记录
	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat format = new SimpleDateFormat(ComBase.DATE_FORMAT);

	public RecordDAO(Context context) {
		super(context, "record");
	}

	/**
	 * 到期时间升序按页查询全部user信息
	 * 
	 * @param page
	 *            从1开始的页数
	 * @return
	 */
	public List<Record> findAll(int page) {
		List<Record> records = new ArrayList<Record>();
		Cursor cursor = database.query(tableName, null, null, null, null, null,
				"deadline asc limit " + (page - 1) * number + "," + page
						* number);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				records.add(getFromCursor(cursor));
			}
		}
		cursor.close();
		return records;
	}

	public boolean insertNew(Record record) {
		String[] value = { format.format(record.getTime()),
				String.valueOf(record.getCorrect()),
				String.valueOf(record.getIncorrect()) };
		return insertNew(allValueName, value);

	}

	@Override
	public Record getFromCursor(Cursor cursor) {
		Record record = new Record();

		String time = cursor.getString(cursor.getColumnIndex(allValueName[0]));
		try {
			record.setTime(format.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		record.setCorrect(cursor.getShort(cursor
				.getColumnIndex(allValueName[1])));
		record.setIncorrect(cursor.getShort(cursor
				.getColumnIndex(allValueName[2])));

		System.out.println(record);

		return record;
	}
}
