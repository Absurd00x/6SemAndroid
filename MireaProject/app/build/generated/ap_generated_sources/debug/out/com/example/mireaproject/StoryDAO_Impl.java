package com.example.mireaproject;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StoryDAO_Impl implements StoryDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Story> __insertionAdapterOfStory;

  private final DateTypeConverter __dateTypeConverter = new DateTypeConverter();

  private final EntityDeletionOrUpdateAdapter<Story> __deletionAdapterOfStory;

  public StoryDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStory = new EntityInsertionAdapter<Story>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Story` (`id`,`date`,`content`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Story value) {
        stmt.bindLong(1, value.id);
        final long _tmp;
        _tmp = __dateTypeConverter.convertDateToLong(value.date);
        stmt.bindLong(2, _tmp);
        if (value.content == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.content);
        }
      }
    };
    this.__deletionAdapterOfStory = new EntityDeletionOrUpdateAdapter<Story>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Story` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Story value) {
        stmt.bindLong(1, value.id);
      }
    };
  }

  @Override
  public void insert(final Story story) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfStory.insert(story);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Story story) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStory.handle(story);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Story> getStories() {
    final String _sql = "SELECT * FROM story";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
      final List<Story> _result = new ArrayList<Story>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Story _item;
        _item = new Story();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        final long _tmp;
        _tmp = _cursor.getLong(_cursorIndexOfDate);
        _item.date = __dateTypeConverter.convertLongToDate(_tmp);
        if (_cursor.isNull(_cursorIndexOfContent)) {
          _item.content = null;
        } else {
          _item.content = _cursor.getString(_cursorIndexOfContent);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
