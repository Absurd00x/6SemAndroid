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
public final class ContactDAO_Impl implements ContactDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Contact> __insertionAdapterOfContact;

  private final DateTypeConverter __dateTypeConverter = new DateTypeConverter();

  private final EntityDeletionOrUpdateAdapter<Contact> __deletionAdapterOfContact;

  private final EntityDeletionOrUpdateAdapter<Contact> __updateAdapterOfContact;

  public ContactDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfContact = new EntityInsertionAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `contact` (`firstName`,`lastName`,`phoneNumber`,`dateCreated`) VALUES (?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        if (value.firstName == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.firstName);
        }
        if (value.lastName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.lastName);
        }
        if (value.getPhoneNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPhoneNumber());
        }
        final long _tmp;
        _tmp = __dateTypeConverter.convertDateToLong(value.dateCreated);
        stmt.bindLong(4, _tmp);
      }
    };
    this.__deletionAdapterOfContact = new EntityDeletionOrUpdateAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `contact` WHERE `phoneNumber` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        if (value.getPhoneNumber() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getPhoneNumber());
        }
      }
    };
    this.__updateAdapterOfContact = new EntityDeletionOrUpdateAdapter<Contact>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `contact` SET `firstName` = ?,`lastName` = ?,`phoneNumber` = ?,`dateCreated` = ? WHERE `phoneNumber` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Contact value) {
        if (value.firstName == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.firstName);
        }
        if (value.lastName == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.lastName);
        }
        if (value.getPhoneNumber() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getPhoneNumber());
        }
        final long _tmp;
        _tmp = __dateTypeConverter.convertDateToLong(value.dateCreated);
        stmt.bindLong(4, _tmp);
        if (value.getPhoneNumber() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getPhoneNumber());
        }
      }
    };
  }

  @Override
  public void insert(final Contact... contacts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfContact.insert(contacts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Contact contact) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfContact.handle(contact);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Contact... contacts) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfContact.handleMultiple(contacts);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Contact> getContacts() {
    final String _sql = "SELECT * FROM contact";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
      final int _cursorIndexOfDateCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "dateCreated");
      final List<Contact> _result = new ArrayList<Contact>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Contact _item;
        _item = new Contact();
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _item.firstName = null;
        } else {
          _item.firstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _item.lastName = null;
        } else {
          _item.lastName = _cursor.getString(_cursorIndexOfLastName);
        }
        final String _tmpPhoneNumber;
        if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
          _tmpPhoneNumber = null;
        } else {
          _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
        }
        _item.setPhoneNumber(_tmpPhoneNumber);
        final long _tmp;
        _tmp = _cursor.getLong(_cursorIndexOfDateCreated);
        _item.dateCreated = __dateTypeConverter.convertLongToDate(_tmp);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Contact getContactWithId(final String number) {
    final String _sql = "SELECT * FROM contact WHERE phoneNumber = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (number == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, number);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfFirstName = CursorUtil.getColumnIndexOrThrow(_cursor, "firstName");
      final int _cursorIndexOfLastName = CursorUtil.getColumnIndexOrThrow(_cursor, "lastName");
      final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
      final int _cursorIndexOfDateCreated = CursorUtil.getColumnIndexOrThrow(_cursor, "dateCreated");
      final Contact _result;
      if(_cursor.moveToFirst()) {
        _result = new Contact();
        if (_cursor.isNull(_cursorIndexOfFirstName)) {
          _result.firstName = null;
        } else {
          _result.firstName = _cursor.getString(_cursorIndexOfFirstName);
        }
        if (_cursor.isNull(_cursorIndexOfLastName)) {
          _result.lastName = null;
        } else {
          _result.lastName = _cursor.getString(_cursorIndexOfLastName);
        }
        final String _tmpPhoneNumber;
        if (_cursor.isNull(_cursorIndexOfPhoneNumber)) {
          _tmpPhoneNumber = null;
        } else {
          _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
        }
        _result.setPhoneNumber(_tmpPhoneNumber);
        final long _tmp;
        _tmp = _cursor.getLong(_cursorIndexOfDateCreated);
        _result.dateCreated = __dateTypeConverter.convertLongToDate(_tmp);
      } else {
        _result = null;
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
