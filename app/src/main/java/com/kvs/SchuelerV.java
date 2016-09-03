package com.kvs;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SchuelerV extends Activity {

    public static int KEY = 2984756;
    private Schueler schueler;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schueler_v);
        schueler = getIntent().getParcelableExtra("schueler");
        t = (TextView)findViewById(R.id.schuelerName);

        String vorname = schueler.getVorname();
        String nachname = schueler.getNachname();

        t.setText(nachname + ", " + vorname);
    }

}
