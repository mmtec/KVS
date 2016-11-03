package com.kvs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Die MainActivity. Beinhaltet Liste der Klassen, einen Knopf zum Hinzufügen der Klassen und einen Knopf zum Reset der Datenbank.
 * @author Michael Maior
 * @version 0.2
 */

public class Start extends Activity {

	private ListView liste;
	private ArrayAdapter<Klasse> a;
	private Button plus, d;
	private SQLiteDatabase db;
	private Intent intent;

	/**
	 * Wird beim Start der Activity aufgerufen.
	 * Instanziiert alle GUI-Objekte und den ArrayAdapter.
	 * Setzt ActionListener für Buttons und ListView-Elemente.
     */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		dbErstellen();
		a = new ArrayAdapter<Klasse>(this, android.R.layout.simple_list_item_1);
		liste = (ListView) findViewById(R.id.listView1);
		liste.setAdapter(a);
		plus = (Button) findViewById(R.id.plus);
		d = (Button) findViewById(R.id.delete);
		intent = new Intent(getApplicationContext(), KlasseE.class);
		
		plus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(intent, KlasseE.KEY);
			}
		});
		
		liste.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Klasse klasse = (Klasse)liste.getAdapter().getItem(position);

				String klassename = klasse.getKlasseame();
				String fachname = klasse.getFachname();
				int kid = klasse.getKid();
				int fid = klasse.getFid();

				Intent intent = new Intent(getApplicationContext(), KlasseV.class);

				intent.putExtra("kid", kid);
				intent.putExtra("fid", fid);
				intent.putExtra("fachname", fachname);
				intent.putExtra("klassename", klassename);
				startActivity(intent);
			}
		});
		
		d.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DBDialog dialog = new DBDialog();
				dialog.show(getFragmentManager(), "DBDialog");
			}
		});
		
		listeFertigen();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	/**
	 * Derzeit noch ein Stub. Hierüber können auch MenuItems angesprochen werden.
     */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Updatet die Liste, nachdem eine neue Klasse hinzugefügt wurde.
     */

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == KlasseE.KEY && resultCode == Activity.RESULT_OK) {
			listeFertigen();
		}
	}

	/**
	 * Erstellt die Datenbank und ihre Tabellen.
	 */

	public void dbErstellen() {
		db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);
		
		db.execSQL("CREATE TABLE IF NOT EXISTS Klasse"
				+ "(kid INTEGER PRIMARY KEY ASC, klassename VARCHAR(10), reihen INTEGER(2), spalten INTEGER(2));");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS Schueler(sid INTEGER PRIMARY KEY ASC,"
				+ " nachname VARCHAR(100), vorname VARCHAR(100), posx INTEGER(2),"
				+ " posy INTEGER(2), gefaehrdet INTEGER(1));");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS Fach(fid INTEGER PRIMARY KEY ASC,"
				+ " fachname VARCHAR(30), kuerzel VARCHAR(5));");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS Hat(hid INTEGER PRIMARY KEY ASC,"
				+ " kfid INTEGER(5), schuelerid INTEGER(5));");

		db.execSQL("CREATE TABLE IF NOT EXISTS KF(kfid INTEGER PRIMARY KEY ASC,"
				+ " klasseid INTEGER(5), fachid INTEGER (5));");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS Note(nid INTEGER PRIMARY KEY ASC,"
				+ " hatid INTEGER(5), art VARCHAR(100), note INTEGER(2), tag INTEGER(2), monat INTEGER(2), jahr INTEGER(4))");
	}

	/**
	 * Sucht sich alle Klassen, die es gibt heraus und fügt diese der Liste (bzw. dem ArrayAdapter) hinzu.
	 */

	public void listeFertigen() {
		a.clear();
		Cursor c = db.rawQuery("SELECT KF.klasseid, KF.fachid, klassename, fachname FROM Klasse, KF, Fach WHERE KF.klasseid = Klasse.kid AND KF.fachid = Fach.fid", null);
		if(c.moveToFirst()) {
			do {
				String klassename = c.getString(c.getColumnIndex("klassename"));
				int kid = c.getInt(c.getColumnIndex("KF.klasseid"));

				String fachname = c.getString(c.getColumnIndex("fachname"));
				int fid = c.getInt(c.getColumnIndex("KF.fachid"));

				Klasse klasse = new Klasse(kid, klassename, fid, fachname);
				a.add(klasse);				
			} while(c.moveToNext());
		}
		c.close();
	}

	/**
	 * Löscht die Datenbank, erstellt die Tabellen neu und updatet die Liste.
	 */

	public void dbLoeschen() {
		this.deleteDatabase("KVS");
		dbErstellen();
		listeFertigen();
	}

}
