package vaccopsjava;

public class Patient {

    public String name;
    public int height;
    public int age;
    public String town;

    public Patient(String name, int height, int age, String town) {
        this.name = name;
        this.height = height;
        this.age = age;
        this.town = town;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public String getTown() {
        return town;
    }
}
