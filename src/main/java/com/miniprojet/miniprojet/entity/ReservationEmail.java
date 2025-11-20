package com.miniprojet.miniprojet.entity;

public class ReservationEmail {
    private String destinataire;
    private String sujet;
    private String corps;

    public ReservationEmail(String destinataire, String sujet, String corps) {
        this.destinataire = destinataire;
        this.sujet = sujet;
        this.corps = corps;
    }

    // Getters
    public String getDestinataire() { return destinataire; }
    public String getSujet() { return sujet; }
    public String getCorps() { return corps; }
}

