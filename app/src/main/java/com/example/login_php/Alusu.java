package com.example.login_php;

public class Alusu {
    String nombr;
    String matricul;
    Double loguitu;
    Double latitu;
    String clav;

    public Alusu(String nombr, String matricul, Double loguitu, Double latitu, String clav) {
        this.nombr = nombr;
        this.matricul = matricul;
        this.loguitu = loguitu;
        this.latitu = latitu;
        this.clav = clav;
    }

    public String getNombr() {
        return nombr;
    }

    public void setNombr(String nombr) {
        this.nombr = nombr;
    }

    public String getMatricul() {
        return matricul;
    }

    public void setMatricul(String matricul) {
        this.matricul = matricul;
    }

    public Double getLoguitu() {
        return loguitu;
    }

    public void setLoguitu(Double loguitu) {
        this.loguitu = loguitu;
    }

    public Double getLatitu() {
        return latitu;
    }

    public void setLatitu(Double latitu) {
        this.latitu = latitu;
    }

    public String getClav() {
        return clav;
    }

    public void setClav(String clav) {
        this.clav = clav;
    }
}
