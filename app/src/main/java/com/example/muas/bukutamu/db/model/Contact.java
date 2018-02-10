package com.example.muas.bukutamu.db.model;

/**
 * Visit website http://www.whats-online.info
 * **/

public class Contact {

    //private variables
    int _id;
    String nama_model;
    String alamat_model;
    String instansi_model;
    String nohp_model;
    String tujuan_model;
    String timein_model;


    byte[] _img;


    // Empty constructor
    public Contact() {

    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getnama_model() {
        return nama_model;
    }

    public void setnama_model(String nama_model) {
        this.nama_model = nama_model;
    }

    public String getalamat_model() {
        return alamat_model;
    }

    public void setalamat_model(String alamat_model) {
        this.alamat_model = alamat_model;
    }

    public String getinstansi_model() {
        return instansi_model;
    }

    public void setinstansi_model(String instansi_model) {
        this.instansi_model = instansi_model;
    }

    public String getnohp_model() {
        return nohp_model;
    }

    public void setnohp_model(String nohp_model) {
        this.nohp_model = nohp_model;
    }

    public String gettujuan_model() {
        return tujuan_model;
    }

    public void settujuan_model(String tujuan_model) {
        this.tujuan_model = tujuan_model;
    }

    public byte[] get_img() {
        return _img;
    }

    public void set_img(byte[] _img) {
        this._img = _img;
    }

    public String getTimein_model() {
        return timein_model;
    }

    public void setTimein_model(String timein_model) {
        this.timein_model = timein_model;
    }

    public Contact(int _id, String nama_model, String alamat_model, String instansi_model, String nohp_model, String tujuan_model, String timein_model, byte[] _img) {
        this._id = _id;
        this.nama_model = nama_model;
        this.alamat_model = alamat_model;
        this.instansi_model = instansi_model;
        this.nohp_model = nohp_model;
        this.tujuan_model = tujuan_model;
        this.timein_model = timein_model;
        this._img = _img;
    }

    public Contact(String nama_model, String alamat_model, String instansi_model, String nohp_model, String tujuan_model, String timein_model, byte[] _img) {

        this.nama_model = nama_model;
        this.alamat_model = alamat_model;
        this.instansi_model = instansi_model;
        this.nohp_model = nohp_model;
        this.tujuan_model = tujuan_model;
        this.timein_model = timein_model;
        this._img = _img;
    }
}