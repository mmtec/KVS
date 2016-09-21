package com.kvs;

/**
 * Created by thawa on 21.09.2016.
 */
public class Leistungsnachweis {

    private int nid, hatid, note, tag, monat, jahr;
    private String art;

    public Leistungsnachweis(int nid, int hatid, String art, int note, int tag, int monat, int jahr) {
        this.nid = nid;
        this.hatid = hatid;
        this.art = art;
        this.note = note;
        this.tag = tag;
        this.monat = monat;
        this.jahr = jahr;
    }

    public String toString() {
        return art + ": " + note + " am " + tag + "." + monat + "." + jahr;
    }

}
