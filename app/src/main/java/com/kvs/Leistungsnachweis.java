package com.kvs;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thawa on 21.09.2016.
 */
public class Leistungsnachweis implements Parcelable {

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

    public Leistungsnachweis(Parcel parcel) {
        nid = parcel.readInt();
        hatid = parcel.readInt();
        art = parcel.readString();
        note = parcel.readInt();
        tag = parcel.readInt();
        monat = parcel.readInt();
        jahr = parcel.readInt();
    }

    public int getNid() {
        return nid;
    }

    public String toString() {
        return art + ": " + note + " am " + tag + "." + monat + "." + jahr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(nid);
        parcel.writeInt(hatid);
        parcel.writeString(art);
        parcel.writeInt(note);
        parcel.writeInt(tag);
        parcel.writeInt(monat);
        parcel.writeInt(jahr);
    }

    public static final Parcelable.Creator<Leistungsnachweis> CREATOR =
            new Parcelable.Creator<Leistungsnachweis>(){

                @Override
                public Leistungsnachweis createFromParcel(Parcel source) {
                    return new Leistungsnachweis(source);
                }

                @Override
                public Leistungsnachweis[] newArray(int size) {
                    return new Leistungsnachweis[size];
                }
            };
}
