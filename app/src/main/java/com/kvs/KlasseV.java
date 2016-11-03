package com.kvs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

/**
 * Activity, die den Sitzplan zeigt.
 * @author Michael Maior
 * @version 0.2
 */

public class KlasseV extends Activity implements OnClickListener {

	private LinearLayout lv;
	private SQLiteDatabase db;
	private int kid, fid, spalten, reihen, kfid;

	/**
	 * Wird beim Start der Activity aufgerufen.
	 * Instanziiert alle GUI-Objekte.
	 * Setzt die nötigen Primär- bzw. Fremdschlüssel und die Spalten und Reihen dieser Klasse.
	 * @param savedInstanceState
     */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_klasse_v);

		lv = (LinearLayout)findViewById(R.id.linearLayout1);

		db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);

		kid = getIntent().getIntExtra("kid", -1);
		fid = getIntent().getIntExtra("fid", -1);

		Cursor c2 = db.rawQuery("SELECT kfid FROM KF WHERE klasseid="+kid+" AND fachid="+fid, null);
		if(c2.moveToFirst()) {
			kfid = c2.getInt(0);
		}

		Cursor c = db.rawQuery("SELECT spalten, reihen FROM Klasse WHERE kid="+kid, null);
		c.moveToFirst();
		spalten = c.getInt(c.getColumnIndex("spalten"));
		reihen = c.getInt(c.getColumnIndex("reihen"));

		c.close();
		c2.close();

		sitzplanErstellen();
	}

	/**
	 * Startet die Activity zur Erstellung eines neuen Schülers.
	 * @param s SchülerView, der in diesem Fall nur die Positionen enthält.
     */

	public void onSchuelerNeu(SchuelerView s) {
		Intent intent = new Intent(getApplicationContext(), SchuelerE.class);
		intent.putExtra("kfid", kfid);
		intent.putExtra("posx", s.getPosx());
		intent.putExtra("posy", s.getPosy());
		startActivityForResult(intent, SchuelerE.KEY);
	}

	/**
	 * Aktualisiert den Sitzplan nach Rückkehr aus einer anderen Activity
	 * @param requestCode Integer, der je nach anfragender Klasse unterschiedlich ist
	 * @param resultCode Muss RESULT_OK sein
	 * @param data Optionale übergebene Daten
     */

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SchuelerE.KEY && resultCode == Activity.RESULT_OK) {
			sitzplanErstellen();
		} else if (requestCode == SchuelerV.KEY && resultCode == Activity.RESULT_OK) {
            sitzplanErstellen();
        }
	}

	/**
	 * Erstellt den Sitzplan.
	 */

	public void sitzplanErstellen() {

		lv.removeAllViews(); // Vertikales LinearLayout
		lv.setWeightSum(reihen); // Summe der Weights ist die Anzahl der Reihen

		for(int i = 0; i < reihen; i++) { // Arbeitet Reihe für Reihe ab

			LinearLayout lh = new LinearLayout(this);
			lh.setOrientation(LinearLayout.HORIZONTAL);
			lh.setWeightSum(spalten); // Summe der Weights dieser Reihe ist die Anzahl der Spalten
			lv.addView(lh);

			for(int j = 0; j < spalten; j++) { // Arbeitet anschließend Spalte für Spalte ab

				Cursor c = db.rawQuery("SELECT vorname, nachname, posx, posy, sid, gefaehrdet FROM Schueler, Hat "
						+ "WHERE Hat.schuelerid = Schueler.sid AND Hat.kfid =" + kfid + " AND posx="+j+" AND posy="+i, null);

				if(c.getCount() > 0) { // Gibt es an dieser Position überhaupt einen Schüler?

					c.moveToFirst();
					String vorname = c.getString(c.getColumnIndex("vorname"));
					String nachname = c.getString(c.getColumnIndex("nachname"));
					int posx = c.getInt(c.getColumnIndex("posx"));
					int posy = c.getInt(c.getColumnIndex("posy"));
                    int sid = c.getInt(c.getColumnIndex("sid"));

					Schueler schueler = new Schueler(vorname, nachname, posx, posy, sid); // Erstellung des Schülers

					SchuelerView t = new SchuelerView(this);
					t.setSchueler(schueler); // Hinzufügen des Schülers zum SchülerView

					t.setText(vorname + "\n" + nachname);
					t.setGravity(Gravity.CENTER);
					if(c.getInt(c.getColumnIndex("gefaehrdet")) == 1) { // Ist er gefährdet, dann erscheint der Text rot
						t.setTextColor(Color.RED);
					} else {
						t.setTextColor(Color.BLACK);
					}
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

					t.setOnClickListener(this);
					t.setTextSize(16);

					lh.addView(t, params);
					c.close();
				} else { // Wenn der Platz leer ist, erscheint nur ein "/"
					SchuelerView t = new SchuelerView(this);

					t.setPosx(j);
					t.setPosy(i);

					t.setText("/");
					t.setGravity(Gravity.CENTER);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); // DESIGN
					t.setOnClickListener(this);
					t.setTextSize(16);

					lh.addView(t, params);
				}
			}
		}
	}

	/**
	 * Erstellt entweder einen neuen Schüler oder geht zur Schülerakte über
	 * @param view Das angeklickte SchülerView
     */

	@Override
	public void onClick(View view) {
		SchuelerView s = (SchuelerView)view;
        Schueler schueler = s.getSchueler();
		if(schueler != null) {
			Intent intent = new Intent(getApplicationContext(), SchuelerV.class);
			intent.putExtra("schueler", schueler);
			startActivityForResult(intent, SchuelerV.KEY);
		} else {
			onSchuelerNeu(s);
		}
	}
}