package com.kvs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity zur Erstellung eines neuen Schülers.
 * @author Michael Maior
 * @version 0.2
 */

public class SchuelerE extends Activity {

	public static int KEY = 298537;
	private EditText vn, nn, x, y;
	private Button e;
	private SQLiteDatabase db;
	private int kfid, posx, posy;

	/**
	 * Wird beim Start der Activity aufgerufen.
	 * Instanziiert alle GUI-Objekte.
	 * Beim Tippen auf den Button wird der neue Schüler erstellt.
     */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schueler_e);
		vn = (EditText)findViewById(R.id.schV);
		nn = (EditText)findViewById(R.id.schN);
		e = (Button)findViewById(R.id.schE);
		db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);
		int j = getIntent().getIntExtra("kfid", -1);
		posx = getIntent().getIntExtra("posx", -1);
		posy = getIntent().getIntExtra("posy", -1);
		if(j != -1) {
			kfid = j;
		}
		
		e.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String vorname = vn.getText().toString();
				String nachname = nn.getText().toString();
				if(posx != -1 && posy != -1) {
					neuerSchueler(vorname, nachname, posx, posy);
					setResult(RESULT_OK);
					finish();
				} else { // Dieser Fall kommt eigentlich nie vor, sollte er aber, ist er für das Debuggen geeignet
					Toast.makeText(getApplicationContext(), "Ungültig!", Toast.LENGTH_LONG);
				}
			}
		});

		return;
	}

	/**
	 * Auslagerung des Vorgangs, den Schüler hinzuzufügen.
	 * @param vorname Vorname des Schülers
	 * @param nachname Nachname des Schülers
	 * @param posx x-Koordinate des Schülers
     * @param posy y-Koordinate des Schülers
     */

	public void neuerSchueler(String vorname, String nachname, int posx, int posy) {
		
		ContentValues schuelerValues = new ContentValues();
		schuelerValues.put("vorname", vorname);
		schuelerValues.put("nachname", nachname);
		schuelerValues.put("posx", posx);
		schuelerValues.put("posy", posy);
		
		int ais = (int)insert("Schueler", null, schuelerValues); // Auto-Increment-Wert des neuen Schülers

		ContentValues hatValues = new ContentValues();
		hatValues.put("kfid", kfid);
		hatValues.put("schuelerid", ais);
		insert("Hat", null, hatValues);

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
