package com.kvs;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Besitzt die gleiche Funktion wie ein TextView, nur mit einem Schüler als Attribut. Sollte kein Schüler vorhanden sein, besteht nur die Information über die x- und y-Koordinate.
 */

public class SchuelerView extends TextView {

    private Schueler schueler;
    private int posx, posy;

    public SchuelerView(Context context) {
        super(context);
    }

    public SchuelerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public void setSchueler(Schueler s) {
        schueler = s;
        posx = schueler.getPosx();
        posy = schueler.getPosy();
    }

    public Schueler getSchueler() {
        return schueler;
    }

}
