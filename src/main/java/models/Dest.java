package models;

public class Dest {
    private String firstName;
    private String smallName;
    private int age;

    public Dest(String firstName, String smallName, int age) {
        this.firstName = firstName;
        this.smallName = smallName;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSmallName() {
        return smallName;
    }

    public void setSmallName(String smallName) {
        this.smallName = smallName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
