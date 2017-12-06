package com.example.mladen.masterradandroid.model;


import android.os.Parcel;
import android.os.Parcelable;


public class SchoolModel implements Parcelable {
    public String id;
    public String naziv;
    public String adresa;
    public String pbroj;
    public String mesto;
    public String opstina;
    public String okrug;
    public String suprava;
    public String www;
    public String tel;
    public String fax;
    public String vrsta;
    public String odeljenja;
    public String gps;

    public SchoolModel() {
        id = "";
        naziv = "";
        adresa = "";
        pbroj = "";
        mesto = "";
        opstina = "";
        okrug = "";
        suprava = "";
        www = "";
        tel = "";
        fax = "";
        vrsta = "";
        odeljenja = "";
        gps = "";
    }

    public SchoolModel (Parcel in) {
        id = in.readString();
        naziv = in.readString();
        adresa = in.readString();
        pbroj = in.readString();
        mesto = in.readString();
        opstina = in.readString();
        okrug = in.readString();
        suprava = in.readString();
        www = in.readString();
        tel = in.readString();
        fax = in.readString();
        vrsta = in.readString();
        odeljenja = in.readString();
        gps = in.readString();
    }

    public static final Creator<SchoolModel> CREATOR = new Creator<SchoolModel>() {
        @Override
        public SchoolModel createFromParcel(Parcel in) {
            return new SchoolModel(in);
        }

        @Override
        public SchoolModel[] newArray(int size) {
            return new SchoolModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getPbroj() {
        return pbroj;
    }

    public void setPbroj(String pbroj) {
        this.pbroj = pbroj;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getOpstina() {
        return opstina;
    }

    public void setOpstina(String opstina) {
        this.opstina = opstina;
    }

    public String getOkrug() {
        return okrug;
    }

    public void setOkrug(String okrug) {
        this.okrug = okrug;
    }

    public String getSuprava() {
        return suprava;
    }

    public void setSuprava(String suprava) {
        this.suprava = suprava;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getVrsta() {
        return vrsta;
    }

    public void setVrsta(String vrsta) {
        this.vrsta = vrsta;
    }

    public String getOdeljenja() {
        return odeljenja;
    }

    public void setOdeljenja(String odeljenja) {
        this.odeljenja = odeljenja;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(naziv);
        dest.writeString(adresa);
        dest.writeString(pbroj);
        dest.writeString(mesto);
        dest.writeString(opstina);
        dest.writeString(okrug);
        dest.writeString(suprava);
        dest.writeString(www);
        dest.writeString(tel);
        dest.writeString(fax);
        dest.writeString(vrsta);
        dest.writeString(odeljenja);
        dest.writeString(gps);

    }
}
