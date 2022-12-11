import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class Competition {

    private int id;
    private String name;
    private int score;
    private Collection<Competitor> competitors;

    public Competition(String name, int id, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.competitors = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Collection<Competitor> getCompetitors() {
        return competitors;
    }

    public void setCompetitors(Collection<Competitor> competitors) {
        this.competitors = competitors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Competition)) return false;
        Competition that = (Competition) o;
        return getId() == that.getId() && getScore() == that.getScore() && Objects.equals(getName(), that.getName()) && Objects.equals(getCompetitors(), that.getCompetitors());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getScore(), getCompetitors());
    }
}
