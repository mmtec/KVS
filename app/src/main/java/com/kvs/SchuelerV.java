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

/**
 * Activity, die die Schülerakte zeigt.
 */

public class SchuelerV extends Activity {

    public static int KEY = 2984756;
    private Schueler schueler;
    private TextView t, dn;
    private Button neu;
    private ListView lnliste;
    private SQLiteDatabase db;
    private int hatid;
    private boolean gefaehrdet;
    private ArrayAdapter<Leistungsnachweis> a;

    /**
     * Wird beim Start der Activity aufgerufen.
     * Instanziiert alle GUI-Objekte.
     * Zur genaueren Beschreibung des Codes die Kommentare im Code ansehen!
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schueler_v);
        schueler = getIntent().getParcelableExtra("schueler");
        t = (TextView)findViewById(R.id.schuelerName);
        dn = (TextView)findViewById(R.id.dnNote);
        neu = (Button)findViewById(R.id.neuerLn);

        lnliste = (ListView)findViewById(R.id.lnList);
        a = new ArrayAdapter<Leistungsnachweis>(this, android.R.layout.simple_list_item_1);
        lnliste.setAdapter(a);
        lnliste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { // Leistungsnachweisliste - Ausführung von "lnClick()" bei Antippen auf einen LN
                lnClick(adapterView, view, i, l);
            }
        });

        db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);
        int sid = schueler.getSid();

        Cursor c = db.rawQuery("SELECT hid FROM Hat WHERE Hat.schuelerid= "+ sid, null);
        if(c.moveToFirst()) {
            hatid = c.getInt(0); // Zuordnung des Fremdschlüssels
        }
        c.close();

        String vorname = schueler.getVorname();
        String nachname = schueler.getNachname();

        t.setText(nachname + ", " + vorname); // Beschriften

        neu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // Startet beim Tippen auf den Button einen Dialog zur Erstellung eines neuen LNs
                LnEDialog dialog = new LnEDialog();
                dialog.show(getFragmentManager(), "LnEDialog");
            }
        });
        dnBerechnen();
        listeFertigen();
    }

    /**
     * Fügt den neuen LN der Datenbank hinzu.
     * @param np NumberPicker, der die Note enthält
     * @param spinner Spinner, der die Art des LNs enthält
     * @param dp DatePicker, der Tag, Monat und Jahr des LNs enthält
     */

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

    /**
     * Berechnet den Durchschnitt. Setzt in der DB gefaehrdet auf 1 (true), wenn der DN >= 4.5 ist
     */

    public void dnBerechnen() {
        Cursor c = db.rawQuery("SELECT note, art FROM Note WHERE hatid = "+ hatid, null);
        float ges = 0;
        float doppelt = 0;

        if(c.moveToFirst()) { // Berechnen der Anzahl der Noten und des Gesamtwerts
            do {
                String art = c.getString(c.getColumnIndex("art"));
                if(art.equals("Schulaufgabe")) {
                    doppelt += 1;
                    ges += c.getFloat(c.getColumnIndex("note"));
                }
                ges += c.getFloat(c.getColumnIndex("note"));
            } while(c.moveToNext());
        }
        if(c.getCount() != 0) {
            float durchschnitt = ((ges / (c.getCount() + doppelt))*100.0F) / 100.0F; // Berechnen des Durchschnitts, 2 Nachkommastellen

            dn.setText(durchschnitt + "");

            if(durchschnitt >= 4.5) { // Setzen auf Rot, wenn nötig
                ContentValues gValue = new ContentValues();
                gValue.put("gefaehrdet", 1);
                db.update("Schueler", gValue, "sid="+ schueler.getSid(), null);
            } else {
                ContentValues gValue = new ContentValues();
                gValue.put("gefaehrdet", 0);
                db.update("Schueler", gValue, "sid="+ schueler.getSid(), null);
            }

        } else {
            dn.setText("0");
        }
        c.close();
    }

    /**
     * Theoretisch ein Umweg, praktisch eine Hilfe zur Übersicht.
     * @param table Tabelle, in die eingefügt werden soll
     * @param nullColumnHack Immer null - wird nur bei sehr spezifischen Fällen benötigt
     * @param values Die einzufügenden Werte
     * @return Der Auto-Increment-Primärschlüssel des neuen Datensatzes
     */

    public long insert(String table, String nullColumnHack, ContentValues values){
        return db.insert(table, nullColumnHack, values);
    }

    /**
     * Fertigt die Liste der LN an und berechnet anschließend den Durchschnitt neu.
     */

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
        dnBerechnen();
        c.close();
    }

    /**
     * Startet einen Dialog (Löschen/Bearbeiten) für den LN.
     * @param adapterView Der Adapter des ListViews
     * @param view Das angeklickte View
     * @param i Die Position des angetippten Items
     * @param l Die Reihen-ID des angetippten Items
     */

    public void lnClick(AdapterView<?> adapterView, View view, int i, long l) {
        Leistungsnachweis ln = (Leistungsnachweis) lnliste.getItemAtPosition(i);

        Bundle bundle = new Bundle();
        bundle.putParcelable("Leistungsnachweis", ln);

        LnDialog dialog = new LnDialog();
        dialog.setArguments(bundle);
        dialog.show(getFragmentManager(), "LnDialog");
    }

    /**
     * Auslagerung des Löschens von LN.
     * @param ln Der zu löschende LN
     */

    public void deleteLn(Leistungsnachweis ln) {
        db.delete("Note", "nid="+ln.getNid(), null);
        listeFertigen();
    }

    /**
     * Geht sicher, dass beim selbstständigen Zurückgehen das Ergebnis auch auf RESULT_OK gesetzt wird.
     */

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}