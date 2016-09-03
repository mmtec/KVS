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

public class KlasseE extends Activity {

	public static int KEY = 42598;
	private Button e;
	private EditText reihen, spalten, name, fach, kuerzel;
	private TextView richtig;
	private SQLiteDatabase db;

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
		name.requestFocus();

		e.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int r = 0;
				int s = 0;

				try {
					r = Integer.parseInt(reihen.getText().toString());
					s = Integer.parseInt(spalten.getText().toString());
					String n = name.getText().toString();
					String f = fach.getText().toString();
					String k = kuerzel.getText().toString();
					if (r <= 4 && s <= 8) {
						
						ContentValues klasseValues = new ContentValues();
						klasseValues.put("klassename", n);
						klasseValues.put("reihen", r);
						klasseValues.put("spalten", s);
						int aik = (int)insert("Klasse", null, klasseValues);
						
						ContentValues fachValues = new ContentValues();
						fachValues.put("fachname", f);
						fachValues.put("kuerzel", k);
						int aif = (int)insert("Fach", null, fachValues);
						
						ContentValues kfValues = new ContentValues();
						kfValues.put("fachid", aif + "");
						kfValues.put("klasseid", aik + "");
						insert("KF", null, kfValues);
						
						setResult(RESULT_OK);
						
						finish();
						return;
						
					} else {
						richtig.setText("In der Beta-Phase dieser App m�ssen Sie weniger als 4 Reihen und weniger als 8 Spalten einf�gen.");
						richtig.setTextColor(Color.RED);
						richtig.setVisibility(TextView.VISIBLE);
					}
				} catch (NumberFormatException ex) {

				}
				richtig.setTextColor(Color.RED);
				richtig.setVisibility(TextView.VISIBLE);

			}
		});

	}
	
	public long insert(String table, String nullColumnHack, ContentValues values){
		return db.insert(table, nullColumnHack, values);
	}
}
