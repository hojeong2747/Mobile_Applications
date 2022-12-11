package ddwu.mobile.dbtest.roomexam01;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.RxRoom;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.lang.Void;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class FoodDAO_Impl implements FoodDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Food> __insertionAdapterOfFood;

  private final EntityDeletionOrUpdateAdapter<Food> __deletionAdapterOfFood;

  private final EntityDeletionOrUpdateAdapter<Food> __updateAdapterOfFood;

  private final SharedSQLiteStatement __preparedStmtOfDataUpdate;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByFood;

  public FoodDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFood = new EntityInsertionAdapter<Food>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `food_table` (`id`,`food`,`nation`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Food value) {
        stmt.bindLong(1, value.id);
        if (value.food == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.food);
        }
        if (value.nation == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.nation);
        }
      }
    };
    this.__deletionAdapterOfFood = new EntityDeletionOrUpdateAdapter<Food>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `food_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Food value) {
        stmt.bindLong(1, value.id);
      }
    };
    this.__updateAdapterOfFood = new EntityDeletionOrUpdateAdapter<Food>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `food_table` SET `id` = ?,`food` = ?,`nation` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Food value) {
        stmt.bindLong(1, value.id);
        if (value.food == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.food);
        }
        if (value.nation == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.nation);
        }
        stmt.bindLong(4, value.id);
      }
    };
    this.__preparedStmtOfDataUpdate = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE food_table SET nation =?  WHERE food =? ";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteByFood = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from food_table where food =?";
        return _query;
      }
    };
  }

  @Override
  public Single<Long> insertFood(final Food food) {
    return Single.fromCallable(new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfFood.insertAndReturnId(food);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable deleteFood(final Food food) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFood.handle(food);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable updateFood(final Food food) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFood.handle(food);
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
        }
      }
    });
  }

  @Override
  public Completable dataUpdate(final String food, final String nation) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDataUpdate.acquire();
        int _argIndex = 1;
        if (nation == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, nation);
        }
        _argIndex = 2;
        if (food == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, food);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDataUpdate.release(_stmt);
        }
      }
    });
  }

  @Override
  public Completable deleteByFood(final String food) {
    return Completable.fromCallable(new Callable<Void>() {
      @Override
      public Void call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByFood.acquire();
        int _argIndex = 1;
        if (food == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, food);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return null;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteByFood.release(_stmt);
        }
      }
    });
  }

  @Override
  public Flowable<List<Food>> getAllFoods() {
    final String _sql = "SELECT * FROM food_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return RxRoom.createFlowable(__db, false, new String[]{"food_table"}, new Callable<List<Food>>() {
      @Override
      public List<Food> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFood = CursorUtil.getColumnIndexOrThrow(_cursor, "food");
          final int _cursorIndexOfNation = CursorUtil.getColumnIndexOrThrow(_cursor, "nation");
          final List<Food> _result = new ArrayList<Food>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Food _item;
            final String _tmpFood;
            if (_cursor.isNull(_cursorIndexOfFood)) {
              _tmpFood = null;
            } else {
              _tmpFood = _cursor.getString(_cursorIndexOfFood);
            }
            final String _tmpNation;
            if (_cursor.isNull(_cursorIndexOfNation)) {
              _tmpNation = null;
            } else {
              _tmpNation = _cursor.getString(_cursorIndexOfNation);
            }
            _item = new Food(_tmpFood,_tmpNation);
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flowable<List<Food>> getFoodByNation(final String nation) {
    final String _sql = "SELECT * FROM food_table WHERE nation = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (nation == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, nation);
    }
    return RxRoom.createFlowable(__db, false, new String[]{"food_table"}, new Callable<List<Food>>() {
      @Override
      public List<Food> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfFood = CursorUtil.getColumnIndexOrThrow(_cursor, "food");
          final int _cursorIndexOfNation = CursorUtil.getColumnIndexOrThrow(_cursor, "nation");
          final List<Food> _result = new ArrayList<Food>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Food _item;
            final String _tmpFood;
            if (_cursor.isNull(_cursorIndexOfFood)) {
              _tmpFood = null;
            } else {
              _tmpFood = _cursor.getString(_cursorIndexOfFood);
            }
            final String _tmpNation;
            if (_cursor.isNull(_cursorIndexOfNation)) {
              _tmpNation = null;
            } else {
              _tmpNation = _cursor.getString(_cursorIndexOfNation);
            }
            _item = new Food(_tmpFood,_tmpNation);
            _item.id = _cursor.getInt(_cursorIndexOfId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Food getFoodByFood(final String food) {
    final String _sql = "SELECT * FROM food_table WHERE food = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (food == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, food);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfFood = CursorUtil.getColumnIndexOrThrow(_cursor, "food");
      final int _cursorIndexOfNation = CursorUtil.getColumnIndexOrThrow(_cursor, "nation");
      final Food _result;
      if(_cursor.moveToFirst()) {
        final String _tmpFood;
        if (_cursor.isNull(_cursorIndexOfFood)) {
          _tmpFood = null;
        } else {
          _tmpFood = _cursor.getString(_cursorIndexOfFood);
        }
        final String _tmpNation;
        if (_cursor.isNull(_cursorIndexOfNation)) {
          _tmpNation = null;
        } else {
          _tmpNation = _cursor.getString(_cursorIndexOfNation);
        }
        _result = new Food(_tmpFood,_tmpNation);
        _result.id = _cursor.getInt(_cursorIndexOfId);
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
