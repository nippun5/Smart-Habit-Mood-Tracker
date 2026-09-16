package com.habitmood.tracker.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class HabitDao_Impl implements HabitDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Habit> __insertionAdapterOfHabit;

  private final EntityInsertionAdapter<HabitCompletion> __insertionAdapterOfHabitCompletion;

  private final EntityDeletionOrUpdateAdapter<Habit> __deletionAdapterOfHabit;

  private final EntityDeletionOrUpdateAdapter<Habit> __updateAdapterOfHabit;

  private final SharedSQLiteStatement __preparedStmtOfArchiveHabit;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCompletion;

  public HabitDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfHabit = new EntityInsertionAdapter<Habit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `habits` (`id`,`name`,`emoji`,`colorHex`,`targetDaysPerWeek`,`createdAtEpochDay`,`isArchived`,`sortOrder`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Habit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getEmoji());
        statement.bindString(4, entity.getColorHex());
        statement.bindLong(5, entity.getTargetDaysPerWeek());
        statement.bindLong(6, entity.getCreatedAtEpochDay());
        final int _tmp = entity.isArchived() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getSortOrder());
      }
    };
    this.__insertionAdapterOfHabitCompletion = new EntityInsertionAdapter<HabitCompletion>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `habit_completions` (`id`,`habitId`,`dateEpochDay`,`completed`,`completedAtMillis`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final HabitCompletion entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getHabitId());
        statement.bindLong(3, entity.getDateEpochDay());
        final int _tmp = entity.getCompleted() ? 1 : 0;
        statement.bindLong(4, _tmp);
        statement.bindLong(5, entity.getCompletedAtMillis());
      }
    };
    this.__deletionAdapterOfHabit = new EntityDeletionOrUpdateAdapter<Habit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `habits` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Habit entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfHabit = new EntityDeletionOrUpdateAdapter<Habit>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `habits` SET `id` = ?,`name` = ?,`emoji` = ?,`colorHex` = ?,`targetDaysPerWeek` = ?,`createdAtEpochDay` = ?,`isArchived` = ?,`sortOrder` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Habit entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getEmoji());
        statement.bindString(4, entity.getColorHex());
        statement.bindLong(5, entity.getTargetDaysPerWeek());
        statement.bindLong(6, entity.getCreatedAtEpochDay());
        final int _tmp = entity.isArchived() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getSortOrder());
        statement.bindLong(9, entity.getId());
      }
    };
    this.__preparedStmtOfArchiveHabit = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE habits SET isArchived = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCompletion = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM habit_completions WHERE habitId = ? AND dateEpochDay = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertHabit(final Habit habit, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfHabit.insertAndReturnId(habit);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertCompletion(final HabitCompletion completion,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfHabitCompletion.insert(completion);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteHabit(final Habit habit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfHabit.handle(habit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateHabit(final Habit habit, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfHabit.handle(habit);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object archiveHabit(final long habitId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfArchiveHabit.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, habitId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfArchiveHabit.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCompletion(final long habitId, final long day,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCompletion.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, habitId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, day);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteCompletion.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Habit>> getActiveHabits() {
    final String _sql = "SELECT * FROM habits WHERE isArchived = 0 ORDER BY sortOrder ASC, createdAtEpochDay ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habits"}, new Callable<List<Habit>>() {
      @Override
      @NonNull
      public List<Habit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfTargetDaysPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "targetDaysPerWeek");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<Habit> _result = new ArrayList<Habit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Habit _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final int _tmpTargetDaysPerWeek;
            _tmpTargetDaysPerWeek = _cursor.getInt(_cursorIndexOfTargetDaysPerWeek);
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpIsArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpTargetDaysPerWeek,_tmpCreatedAtEpochDay,_tmpIsArchived,_tmpSortOrder);
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
  public Flow<List<Habit>> getAllHabits() {
    final String _sql = "SELECT * FROM habits ORDER BY sortOrder ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habits"}, new Callable<List<Habit>>() {
      @Override
      @NonNull
      public List<Habit> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfColorHex = CursorUtil.getColumnIndexOrThrow(_cursor, "colorHex");
          final int _cursorIndexOfTargetDaysPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "targetDaysPerWeek");
          final int _cursorIndexOfCreatedAtEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAtEpochDay");
          final int _cursorIndexOfIsArchived = CursorUtil.getColumnIndexOrThrow(_cursor, "isArchived");
          final int _cursorIndexOfSortOrder = CursorUtil.getColumnIndexOrThrow(_cursor, "sortOrder");
          final List<Habit> _result = new ArrayList<Habit>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Habit _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpColorHex;
            _tmpColorHex = _cursor.getString(_cursorIndexOfColorHex);
            final int _tmpTargetDaysPerWeek;
            _tmpTargetDaysPerWeek = _cursor.getInt(_cursorIndexOfTargetDaysPerWeek);
            final long _tmpCreatedAtEpochDay;
            _tmpCreatedAtEpochDay = _cursor.getLong(_cursorIndexOfCreatedAtEpochDay);
            final boolean _tmpIsArchived;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsArchived);
            _tmpIsArchived = _tmp != 0;
            final int _tmpSortOrder;
            _tmpSortOrder = _cursor.getInt(_cursorIndexOfSortOrder);
            _item = new Habit(_tmpId,_tmpName,_tmpEmoji,_tmpColorHex,_tmpTargetDaysPerWeek,_tmpCreatedAtEpochDay,_tmpIsArchived,_tmpSortOrder);
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
  public Flow<List<HabitCompletion>> getCompletionsForDay(final long day) {
    final String _sql = "SELECT * FROM habit_completions WHERE dateEpochDay = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, day);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habit_completions"}, new Callable<List<HabitCompletion>>() {
      @Override
      @NonNull
      public List<HabitCompletion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final List<HabitCompletion> _result = new ArrayList<HabitCompletion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HabitCompletion _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final long _tmpCompletedAtMillis;
            _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            _item = new HabitCompletion(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpCompleted,_tmpCompletedAtMillis);
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
  public Flow<List<HabitCompletion>> getCompletionsForHabit(final long habitId) {
    final String _sql = "SELECT * FROM habit_completions WHERE habitId = ? ORDER BY dateEpochDay DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, habitId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habit_completions"}, new Callable<List<HabitCompletion>>() {
      @Override
      @NonNull
      public List<HabitCompletion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final List<HabitCompletion> _result = new ArrayList<HabitCompletion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HabitCompletion _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final long _tmpCompletedAtMillis;
            _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            _item = new HabitCompletion(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpCompleted,_tmpCompletedAtMillis);
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
  public Flow<List<HabitCompletion>> getCompletionsBetween(final long start, final long end) {
    final String _sql = "SELECT * FROM habit_completions WHERE dateEpochDay BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, start);
    _argIndex = 2;
    _statement.bindLong(_argIndex, end);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"habit_completions"}, new Callable<List<HabitCompletion>>() {
      @Override
      @NonNull
      public List<HabitCompletion> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final List<HabitCompletion> _result = new ArrayList<HabitCompletion>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final HabitCompletion _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final long _tmpCompletedAtMillis;
            _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            _item = new HabitCompletion(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpCompleted,_tmpCompletedAtMillis);
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
  public Object getCompletion(final long habitId, final long day,
      final Continuation<? super HabitCompletion> $completion) {
    final String _sql = "SELECT * FROM habit_completions WHERE habitId = ? AND dateEpochDay = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, habitId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, day);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<HabitCompletion>() {
      @Override
      @Nullable
      public HabitCompletion call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfHabitId = CursorUtil.getColumnIndexOrThrow(_cursor, "habitId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final int _cursorIndexOfCompletedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAtMillis");
          final HabitCompletion _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpHabitId;
            _tmpHabitId = _cursor.getLong(_cursorIndexOfHabitId);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final boolean _tmpCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp != 0;
            final long _tmpCompletedAtMillis;
            _tmpCompletedAtMillis = _cursor.getLong(_cursorIndexOfCompletedAtMillis);
            _result = new HabitCompletion(_tmpId,_tmpHabitId,_tmpDateEpochDay,_tmpCompleted,_tmpCompletedAtMillis);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
