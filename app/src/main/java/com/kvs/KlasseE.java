package com.kvs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity für die Erstellung von Klassen.
 * @author Michael Maior
 * @version 0.2
 */

public class KlasseE extends Activity {

	public static final int KEY = 42598;
	private Button e;
	private EditText reihen, spalten, name, fach, kuerzel;
	private TextView richtig;
	private SQLiteDatabase db;

	/**
	 * Wird beim Start der Activity aufgerufen.
	 * Instanziiert alle GUI-Objekte.
	 * Fügt Klasse-, Fach- und KF-Values beim Tippen auf den Button der DB hinzu, wenn weniger as 4 Reihen und 8 Spalten ausgewählt sind (dient nur der Übersichtlichkeit in diesem Stadium).
     */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_klasse_e);
		db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);
		richtig = (TextView) findViewById(R.id.textView5);
		richtig.setVisibility(TextView.INVISIBLE);
		reihen = (EditText) findViewById(R.id.editText1);
		spalten = (EditText) findViewById(R.id.editText2);
		fach = (EditText) findViewById(R.id.editText3);
		name = (EditText) findViewById(R.id.editText4);
		kuerzel = (EditText) findViewById(R.id.editText5);
		e = (Button) findViewById(R.id.e);
		name.requestFocus(); // Das EditText "Name" wird zuerst ausgewählt

		e.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int r, s;

				try {
					r = Integer.parseInt(reihen.getText().toString());
					s = Integer.parseInt(spalten.getText().toString());
					String n = name.getText().toString();
					String f = fach.getText().toString();
					String k = kuerzel.getText().toString();
					if (r <= 4 && s <= 8) { // Sind es weniger als 4 Reihen und 8 Spalten?
						
						ContentValues klasseValues = new ContentValues();
						klasseValues.put("klassename", n);
						klasseValues.put("reihen", r);
						klasseValues.put("spalten", s);
						int aik = (int)insert("Klasse", null, klasseValues); // Fügt die Klasse ein, aik ist der auto incremented Primärschlüssel der neu hinzugefügten Klasse
						
						ContentValues fachValues = new ContentValues();
						fachValues.put("fachname", f);
						fachValues.put("kuerzel", k);
						int aif = (int)insert("Fach", null, fachValues); // Fügt das Fach ein, aif ist der auto incremented Primärschhlüssel des neu hinzugefügten Fachs
						
						ContentValues kfValues = new ContentValues();
						kfValues.put("fachid", aif + "");
						kfValues.put("klasseid", aik + "");
						insert("KF", null, kfValues); // Fügt in KF die neuen Primärschlüssel ein
						
						setResult(RESULT_OK);
						
						finish();
						return;
						
					} else { // Falls mehr als 4 Reihen und 8 Spalten
						richtig.setText("In der Beta-Phase dieser App müssen Sie weniger als 4 Reihen und weniger als 8 Spalten einfügen.");
						richtig.setTextColor(Color.RED);
						richtig.setVisibility(TextView.VISIBLE);
					}
				} catch (NumberFormatException ex) {

				}

			}
		});

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
}
