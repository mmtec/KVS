package com.kvs;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class SchuelerV extends Activity {

    public static int KEY = 2984756;
    private Schueler schueler;
    private TextView t;
    private Button neu;
    private ListView lnliste;
    private SQLiteDatabase db;
    private int hatid;
    private ArrayAdapter<Leistungsnachweis> a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schueler_v);
        schueler = getIntent().getParcelableExtra("schueler");
        t = (TextView)findViewById(R.id.schuelerName);
        neu = (Button)findViewById(R.id.neuerLn);

        lnliste = (ListView)findViewById(R.id.lnList);
        a = new ArrayAdapter<Leistungsnachweis>(this, android.R.layout.simple_list_item_1);
        lnliste.setAdapter(a);
        lnliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lnClick(adapterView, view, i, l);
            }
        });

        db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);
        int sid = schueler.getSid();

        Cursor c = db.rawQuery("SELECT hid FROM Hat WHERE Hat.schuelerid= "+ sid, null);
        if(c.moveToFirst()) {
            hatid = c.getInt(0);
        }
        c.close();

        String vorname = schueler.getVorname();
        String nachname = schueler.getNachname();

        t.setText(nachname + ", " + vorname);

        neu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LnEDialog dialog = new LnEDialog();
                dialog.show(getFragmentManager(), "LnEDialog");
            }
        });
        listeFertigen();
    }

    public void lnHinzufuegen(NumberPicker np, Spinner spinner, DatePicker dp) {
        ContentValues noteValues = new ContentValues();
        noteValues.put("hatid", hatid);
        noteValues.put("art", spinner.getSelectedItem().toString());
        noteValues.put("note", np.getValue());
        noteValues.put("tag", dp.getDayOfMonth());
        noteValues.put("monat", dp.getMonth()+1);
        noteValues.put("jahr", dp.getYear());
        insert("Note", null, noteValues);

        listeFertigen();
    }

    public long insert(String table, String nullColumnHack, ContentValues values){
        return db.insert(table, nullColumnHack, values);
    }

    public void listeFertigen() {
        a.clear();
        Cursor c = db.rawQuery("SELECT nid, art, note, tag, monat, jahr FROM Note WHERE hatid="+hatid, null);
        if(c.moveToFirst()){
            do {
                int nid = c.getInt(c.getColumnIndex("nid"));
                String art = c.getString(c.getColumnIndex("art"));
                int note = c.getInt(c.getColumnIndex("note"));
                int tag = c.getInt(c.getColumnIndex("tag"));
                int monat = c.getInt(c.getColumnIndex("monat"));
                int jahr = c.getInt(c.getColumnIndex("jahr"));

                Leistungsnachweis ln = new Leistungsnachweis(nid, hatid, art, note, tag, monat, jahr);
                a.add(ln);
            } while(c.moveToNext());
        }
        c.close();
    }

    public void lnClick(AdapterView<?> adapterView, View view, int i, long l) {
        Leistungsnachweis ln = (Leistungsnachweis) lnliste.getItemAtPosition(i);

        Bundle bundle = new Bundle();
        bundle.putParcelable("Leistungsnachweis", ln);

        LnDialog dialog = new LnDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "LnDialog");
    }

    public void deleteLn(Leistungsnachweis ln) {
        db.delete("Note", "nid="+ln.getNid(), null);
        listeFertigen();
    }

}