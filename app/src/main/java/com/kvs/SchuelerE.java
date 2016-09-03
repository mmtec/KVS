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

public class SchuelerE extends Activity {

	public static int KEY = 298537;
	private EditText vn, nn, x, y;
	private Button e;
	private SQLiteDatabase db;
	private int kfid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schueler_e);
		vn = (EditText)findViewById(R.id.schV);
		nn = (EditText)findViewById(R.id.schN);
		x = (EditText)findViewById(R.id.schX);
		y = (EditText)findViewById(R.id.schY);
		e = (Button)findViewById(R.id.schE);
		db = openOrCreateDatabase("KVS", MODE_PRIVATE, null);
		int j = getIntent().getIntExtra("kfid", -1);
		if(j != -1) {
			kfid = j;
		}
		
		e.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				neuerSchueler(vn.getText().toString(), nn.getText().toString(), Integer.parseInt(x.getText().toString()), Integer.parseInt(y.getText().toString()));
				setResult(RESULT_OK);
				finish();
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
