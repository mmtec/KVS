package com.kvs;

import android.os.Parcel;
import android.os.Parcelable;

public class Schueler implements Parcelable {
	
	private String vorname, nachname;
	private int posx, posy, sid;

    public Schueler(Parcel parcel) {
        vorname = parcel.readString();
        nachname = parcel.readString();
        posx = parcel.readInt();
        posy = parcel.readInt();
        sid = parcel.readInt();
    }

	public Schueler(String vorname, String nachname, int posx, int posy, int sid) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.posx = posx;
		this.posy = posy;
        this.sid = sid;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public int getPosx() {
		return posx;
	}

	public int getPosy() {
		return posy;
	}

    public int getSid() {
        return sid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(vorname);
        parcel.writeString(nachname);
        parcel.writeInt(posx);
        parcel.writeInt(posy);
        parcel.writeInt(sid);
    }

    public static final Parcelable.Creator<Schueler> CREATOR =
            new Parcelable.Creator<Schueler>(){

                @Override
                public Schueler createFromParcel(Parcel source) {
                    return new Schueler(source);
                }

                @Override
                public Schueler[] newArray(int size) {
                    return new Schueler[size];
                }
            };


}
