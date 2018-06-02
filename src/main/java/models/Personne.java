package models;

public class Personne {
    private String name;
    private long dtob;

    public Personne(){}

    public Personne(String name, long dtob) {
        this.name = name;
        this.dtob = dtob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDtob() {
        return dtob;
    }

    public void setDtob(long dtob) {
        this.dtob = dtob;
    }
}
