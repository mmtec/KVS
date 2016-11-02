package com.kvs;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class KlasseV extends Activity implements OnClickListener {

	private LinearLayout lv;
	private SQLiteDatabase db;
	private int kid, fid, spalten, reihen, kfid;

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

		schuelerZuGrid();
	}

	public void onSchuelerNeu(SchuelerView s) {
		Intent intent = new Intent(getApplicationContext(), SchuelerE.class);
		intent.putExtra("kfid", kfid);
		intent.putExtra("posx", s.getPosx());
		intent.putExtra("posy", s.getPosy());
		startActivityForResult(intent, SchuelerE.KEY);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SchuelerE.KEY && resultCode == Activity.RESULT_OK) {
			schuelerZuGrid();
		} else if (requestCode == SchuelerV.KEY && resultCode == Activity.RESULT_OK) {
            schuelerZuGrid();
        }
	}

	public void schuelerZuGrid() {

		lv.removeAllViews(); // Vertikales LinearLayout
		lv.setWeightSum(reihen);

		for(int i = 0; i < reihen; i++) {

			LinearLayout lh = new LinearLayout(this);
			lh.setOrientation(LinearLayout.HORIZONTAL);
			lh.setWeightSum(spalten); // DESIGN
			lv.addView(lh);

			for(int j = 0; j < spalten; j++) {

				Cursor c = db.rawQuery("SELECT vorname, nachname, posx, posy, sid, gefaehrdet FROM Schueler, Hat "
						+ "WHERE Hat.schuelerid = Schueler.sid AND Hat.kfid =" + kfid + " AND posx="+j+" AND posy="+i, null);

				if(c.getCount() > 0) {

					c.moveToFirst();
					String vorname = c.getString(c.getColumnIndex("vorname"));
					String nachname = c.getString(c.getColumnIndex("nachname"));
					int posx = c.getInt(c.getColumnIndex("posx"));
					int posy = c.getInt(c.getColumnIndex("posy"));
                    int sid = c.getInt(c.getColumnIndex("sid"));

					Schueler schueler = new Schueler(vorname, nachname, posx, posy, sid);

					SchuelerView t = new SchuelerView(this);
					t.setSchueler(schueler);

					t.setText(vorname + "\n" + nachname);
					t.setGravity(Gravity.CENTER); // DESIGN
					if(c.getInt(c.getColumnIndex("gefaehrdet")) == 1) {
						t.setTextColor(Color.RED);
					} else {
						t.setTextColor(Color.BLACK);
					}
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); // DESIGN

					t.setOnClickListener(this);
					t.setTextSize(16);

					lh.addView(t, params);
					c.close();
				} else {
					SchuelerView t = new SchuelerView(this);

					t.setPosx(j);
					t.setPosy(i);

					t.setText("/");
					t.setGravity(Gravity.CENTER); // DESIGN
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1); // DESIGN
					t.setOnClickListener(this);
					t.setTextSize(16);

					lh.addView(t, params);
				}
			}
		}
	}

	@Override
	public void onClick(View view) {
		SchuelerView s = (SchuelerView)view;
		s.setPressed(true);
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