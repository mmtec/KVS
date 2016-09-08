package com.kvs;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SchuelerE extends Activity {

	public static int KEY = 298537;
	private EditText vn, nn, x, y;
	private Button e;
	private SQLiteDatabase db;
	private int kfid, posx, posy;
	
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
				if(posx != -1 && posy != -1) {
					neuerSchueler(vn.getText().toString(), nn.getText().toString(), posx, posy);
					setResult(RESULT_OK);
					finish();
				} else {
					Toast.makeText(getApplicationContext(), "Ungültig!", Toast.LENGTH_LONG);
				}
			}
		});

		return;
	}
	
	public void neuerSchueler(String vorname, String nachname, int posx, int posy) {
		
		ContentValues schuelerValues = new ContentValues();
		schuelerValues.put("vorname", vorname);
		schuelerValues.put("nachname", nachname);
		schuelerValues.put("posx", posx);
		schuelerValues.put("posy", posy);
		
		int ais = (int)insert("Schueler", null, schuelerValues);

		ContentValues hatValues = new ContentValues();
		hatValues.put("kfid", kfid);
		hatValues.put("schuelerid", ais);
		insert("Hat", null, hatValues);

	}
	
	public long insert(String table, String nullColumnHack, ContentValues values){
		return db.insert(table, nullColumnHack, values);
	}
}
