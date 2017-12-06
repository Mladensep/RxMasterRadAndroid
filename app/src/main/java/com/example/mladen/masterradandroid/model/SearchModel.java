package com.example.mladen.masterradandroid.model;


import android.os.Parcel;
import android.os.Parcelable;

public class SearchModel implements Parcelable {

    private String naziv;
    private String mesto;
    private int samoOsnovne;
    private int samoSrednje;

    public SearchModel() {}

    protected SearchModel(Parcel in) {
        naziv = in.readString();
        mesto = in.readString();
        samoOsnovne = in.readInt();
        samoSrednje = in.readInt();
    }

    public static final Creator<SearchModel> CREATOR = new Creator<SearchModel>() {
        @Override
        public SearchModel createFromParcel(Parcel in) {
            return new SearchModel(in);
        }

        @Override
        public SearchModel[] newArray(int size) {
            return new SearchModel[size];
        }
    };

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public int getSamoOsnovne() {
        return samoOsnovne;
    }

    public void setSamoOsnovne(int samoOsnovne) {
        this.samoOsnovne = samoOsnovne;
    }

    public int getSamoSrednje() {
        return samoSrednje;
    }

    public void setSamoSrednje(int samoSrednje) {
        this.samoSrednje = samoSrednje;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(mesto);
        dest.writeInt(samoOsnovne);
        dest.writeInt(samoSrednje);
    }
}
