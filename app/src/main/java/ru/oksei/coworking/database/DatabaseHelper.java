package ru.oksei.coworking.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.oksei.coworking.models.Booking;
import ru.oksei.coworking.models.Category;
import ru.oksei.coworking.models.Workspace;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "coworking.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_CATEGORIES = "categories";
    public static final String TABLE_WORKSPACES = "workspaces";
    public static final String TABLE_BOOKINGS = "bookings";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT);";

        String createWorkspacesTable = "CREATE TABLE " + TABLE_WORKSPACES + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "category TEXT NOT NULL, " +
                "capacity INTEGER NOT NULL, " +
                "price_per_hour REAL NOT NULL, " +
                "description TEXT, " +
                "amenities TEXT, " +
                "is_available INTEGER DEFAULT 1);";

        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "workspace_id INTEGER NOT NULL, " +
                "booking_date TEXT NOT NULL, " +
                "start_time TEXT NOT NULL, " +
                "duration_hours INTEGER NOT NULL, " +
                "total_price REAL NOT NULL, " +
                "status TEXT NOT NULL, " +
                "qr_code TEXT NOT NULL, " +
                "created_at TEXT NOT NULL, " +
                "FOREIGN KEY (workspace_id) REFERENCES " + TABLE_WORKSPACES + "(id));";

        db.execSQL(createCategoriesTable);
        db.execSQL(createWorkspacesTable);
        db.execSQL(createBookingsTable);

        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        // Seed categories
        db.execSQL("INSERT INTO " + TABLE_CATEGORIES + " (name, description) VALUES " +
                "('Open Space', 'Открытое пространство с гибкими столами и розетками')," +
                "('Meeting Room', 'Переговорные комнаты с экранами и маркерными досками')," +
                "('Private Office', 'Изолированные кабинеты для команд с контролем доступа')," +
                "('Quiet Zone', 'Тихая зона для концентрированной индивидуальной работы');");

        // Seed workspaces
        db.execSQL("INSERT INTO " + TABLE_WORKSPACES +
                " (name, category, capacity, price_per_hour, description, amenities, is_available) VALUES " +
                "('Стол #12 (У окна)', 'Open Space', 1, 250.0, 'Комфортное рабочее место с естественным освещением и эргономичным креслом.', 'Wi-Fi 500 Мбит/с, Розетка 220V, USB Type-C, Чай/Кофе', 1)," +
                "('Стол #14 (Тихий угол)', 'Open Space', 1, 220.0, 'Место в глубине зала для сосредоточенной работы без отвлекающих факторов.', 'Wi-Fi 500 Мбит/с, 2 розетки, Настольная лампа', 1)," +
                "('Переговорная \"Альфа\"', 'Meeting Room', 6, 900.0, 'Зал для встреч и зум-коллов с акустической изоляцией и 65\" 4K ТВ.', '4K ТВ, Маркерная доска, Спикерфон Jabra, HDMI/Type-C кабели', 1)," +
                "('Переговорная \"Бета\" (Большая)', 'Meeting Room', 12, 1600.0, 'Конференц-зал с панорамным остеклением и видеоконференцсвязью.', 'Видеобар Polycom, Доска 3м, Климат-контроль, Кулер', 1)," +
                "('Офис \"Калибр\" #3', 'Private Office', 4, 1800.0, 'Отдельный запираемый мини-офис для команды стартапа или отдела.', 'Электронный замок, 4 монитора 27\", Сейф, Звукоизоляция', 1)," +
                "('Капсула FocusPod #1', 'Quiet Zone', 1, 350.0, 'Шумоизоляционная капсула для приватных звонков и deep work.', 'Полная звукоизоляция, Вентиляция, Беспроводная зарядка', 1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKSPACES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public List<Workspace> getAllWorkspaces(String categoryFilter) {
        List<Workspace> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if (categoryFilter == null || categoryFilter.equalsIgnoreCase("Все") || categoryFilter.isEmpty()) {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKSPACES + " ORDER BY id ASC", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_WORKSPACES + " WHERE category = ? ORDER BY id ASC", new String[]{categoryFilter});
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                int capacity = cursor.getInt(cursor.getColumnIndexOrThrow("capacity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price_per_hour"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String amenities = cursor.getString(cursor.getColumnIndexOrThrow("amenities"));
                boolean available = cursor.getInt(cursor.getColumnIndexOrThrow("is_available")) == 1;

                list.add(new Workspace(id, name, category, capacity, price, desc, amenities, available));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY id ASC", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                list.add(new Category(id, name, desc));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // Проверка конфликтов (Overbooking Conflict Check 409)
    public boolean hasBookingConflict(long workspaceId, String date, int startHour, int durationHours) {
        int endHour = startHour + durationHours;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT start_time, duration_hours FROM " + TABLE_BOOKINGS +
                        " WHERE workspace_id = ? AND booking_date = ? AND status = 'CONFIRMED'",
                new String[]{String.valueOf(workspaceId), date}
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String existingTimeStr = cursor.getString(0); // e.g. "10:00"
                int existingStart = Integer.parseInt(existingTimeStr.split(":")[0]);
                int existingDuration = cursor.getInt(1);
                int existingEnd = existingStart + existingDuration;

                // Пересечение интервалов: max(start1, start2) < min(end1, end2)
                if (Math.max(startHour, existingStart) < Math.min(endHour, existingEnd)) {
                    cursor.close();
                    return true; // Конфликт найден!
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        return false;
    }

    public long addBooking(long workspaceId, String date, String startTime, int durationHours, double totalPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("workspace_id", workspaceId);
        cv.put("booking_date", date);
        cv.put("start_time", startTime);
        cv.put("duration_hours", durationHours);
        cv.put("total_price", totalPrice);
        cv.put("status", "CONFIRMED");

        String createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        cv.put("created_at", createdAt);

        // QR Code content payload
        String qrPayload = "COWORK-PASS-" + workspaceId + "-" + date + "-" + startTime + "-T" + System.currentTimeMillis();
        cv.put("qr_code", qrPayload);

        return db.insert(TABLE_BOOKINGS, null, cv);
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.id, b.workspace_id, w.name, w.category, b.booking_date, b.start_time, " +
                "b.duration_hours, b.total_price, b.status, b.qr_code, b.created_at " +
                "FROM " + TABLE_BOOKINGS + " b " +
                "JOIN " + TABLE_WORKSPACES + " w ON b.workspace_id = w.id " +
                "ORDER BY b.id DESC";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                long wsId = cursor.getLong(1);
                String wsName = cursor.getString(2);
                String category = cursor.getString(3);
                String date = cursor.getString(4);
                String startTime = cursor.getString(5);
                int duration = cursor.getInt(6);
                double total = cursor.getDouble(7);
                String status = cursor.getString(8);
                String qr = cursor.getString(9);
                String createdAt = cursor.getString(10);

                list.add(new Booking(id, wsId, wsName, category, date, startTime, duration, total, status, qr, createdAt));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean cancelBooking(long bookingId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", "CANCELLED");
        int rows = db.update(TABLE_BOOKINGS, cv, "id = ?", new String[]{String.valueOf(bookingId)});
        return rows > 0;
    }
}
