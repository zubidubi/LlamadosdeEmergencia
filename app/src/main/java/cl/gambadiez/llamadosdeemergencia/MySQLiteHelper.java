package cl.gambadiez.llamadosdeemergencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "LlamadosDB";

    // Llamados table name
    private static final String TABLE_LLAMADOS = "llamados";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_CLAVE = "clave";
    private static final String KEY_SECTOR = "sector";
    private static final String KEY_DIRECCION = "direccion";
    private static final String KEY_UNIDADES = "unidades";
    private static final String KEY_DATE = "date";
    private static final String KEY_ICON_RESOURCE_ID = "iconResourceID";

    private static final String[] COLUMNS = {KEY_ID, KEY_CLAVE, KEY_SECTOR, KEY_DIRECCION, KEY_UNIDADES, KEY_DATE, KEY_ICON_RESOURCE_ID};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_LLAMADOS_TABLE = "CREATE TABLE llamados ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "clave TEXT, " + "sector TEXT, " + "direccion TEXT, " + "unidades TEXT, " + "date INTEGER, " + "iconResourceID INTEGER " + ")";

        // create books table
        db.execSQL(CREATE_LLAMADOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS llamados");

        // create fresh books table
        this.onCreate(db);
    }

    public void addLLamado(Llamado llamado){
        //for logging
        Log.d("addLlamado", llamado.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CLAVE, llamado.getClave());
        values.put(KEY_SECTOR, llamado.getSector());
        values.put(KEY_DIRECCION, llamado.getDireccion());
        values.put(KEY_UNIDADES, llamado.getUnidades());
        values.put(KEY_DATE, llamado.getDate().getTime());
        values.put(KEY_ICON_RESOURCE_ID, llamado.getIconResourceID());

        // 3. insert
        db.insert(TABLE_LLAMADOS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Llamado getLlamado(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_LLAMADOS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build LLamado object
        Llamado llamado = new Llamado();
        llamado.setID(Integer.parseInt(cursor.getString(0)));
        llamado.setClave(cursor.getString(1));
        llamado.setSector(cursor.getString(2));
        llamado.setDireccion(cursor.getString(3));
        llamado.setUnidades(cursor.getString(4));
        llamado.setDate(new Date(Long.parseLong(cursor.getString(5))));
        llamado.setIconResourceID(Integer.parseInt(cursor.getString(6)));

        //log
        Log.d("getLlamado(" + id + ")", llamado.toString());

        // 5. return book
        return llamado;
    }

    public List<Llamado> getAllLlamados() {
        List<Llamado> llamados = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_LLAMADOS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
                Llamado llamado = new Llamado();
                llamado.setID(Integer.parseInt(cursor.getString(0)));
                llamado.setClave(cursor.getString(1));
                llamado.setSector(cursor.getString(2));
                llamado.setDireccion(cursor.getString(3));
                llamado.setUnidades(cursor.getString(4));
                llamado.setDate(new Date(Long.parseLong(cursor.getString(5))));
                llamado.setIconResourceID(Integer.parseInt(cursor.getString(6)));

                // Add llamado to lamadoss
                llamados.add(llamado);
            } while (cursor.moveToNext());
        }

        Log.d("getAllLlamados()", llamados.toString());

        // return llamados
        return llamados;
    }

    public int updateLlamado(Llamado llamado) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_CLAVE, llamado.getClave());
        values.put(KEY_SECTOR, llamado.getSector());
        values.put(KEY_DIRECCION, llamado.getDireccion());
        values.put(KEY_UNIDADES, llamado.getUnidades());
        values.put(KEY_DATE, llamado.getDate().getTime());
        values.put(KEY_ICON_RESOURCE_ID, llamado.getIconResourceID());

        // 3. updating row
        int i = db.update(TABLE_LLAMADOS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(llamado.getID()) }); //selection args

        // 4. close
        db.close();

        return i;

    }

    public void deleteLlamado(Llamado llamado) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_LLAMADOS, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(llamado.getID()) }); //selections args

        // 3. close
        db.close();

        //log
        Log.d("deleteLlamado", llamado.toString());

    }
}
