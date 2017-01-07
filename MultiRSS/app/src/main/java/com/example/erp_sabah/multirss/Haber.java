package com.example.erp_sabah.multirss;

public class Haber
{
    public String baslik;
    public String resimURL;
    public String link;
    public String from = "";

    public Haber(String baslik, String resimURL, String link)
    {
        this.baslik = baslik;
        this.resimURL = resimURL;
        this.link = link;
    }

    @Override
    public String toString() {
        return "Baslik = "+baslik+"\nLink : "+link+"\nResim URL : "+resimURL+"\n\n";
    }
}
