package vaccopsjava;

public class Doctor {

    public String name;
    public int popularity;

    public Doctor(String name, int popularity) {
        this.name = name;
        this.popularity = popularity;
    }

    public String getName() {
        return name;
    }

    public int getPopularity() {
        return popularity;
    }
}
