package com.example.marrezarifa.comamonitoring;

//this is very simple class and it only contains the user attributes, a constructor and the getters
// you can easily do this by right click -> generate -> constructor and getters
public class User {

    private String id_pasien, passpas;

    public User(String id_pasien, String passpas) {
        this.id_pasien = id_pasien;
        this.passpas = passpas;
    }

    public String getIdpasien() {
        return id_pasien;
    }

    public String getPasspas() {
        return passpas;
    }

}