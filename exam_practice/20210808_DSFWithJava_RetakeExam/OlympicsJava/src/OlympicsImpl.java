import java.util.*;
import java.util.stream.Collectors;

public class OlympicsImpl implements Olympics {
    private Map<Integer, Competitor> competitorsById;
    private Map<Integer, Competition> competitionsById;

    public OlympicsImpl() {
        this.competitorsById = new HashMap<>();
        this.competitionsById = new HashMap<>();
    }


    @Override
    public void addCompetitor(int id, String name) {
        if (isCompetitorExist(id)) {
            throw new IllegalArgumentException();
        }

        competitorsById.put(id, new Competitor(id, name));
    }

    @Override
    public void addCompetition(int id, String name, int score) {
        if (isCompetitionExist(id)) {
            throw new IllegalArgumentException();
        }

        competitionsById.put(id, new Competition(name, id, score));
    }

    @Override
    public void compete(int competitorId, int competitionId) {
        if (!isCompetitorExist(competitorId) || !isCompetitionExist(competitionId)) {
            throw new IllegalArgumentException();
        }

        Competition competition = competitionsById.get(competitionId);

        Competitor competitor = competitorsById.get(competitorId);

        competition.getCompetitors().add(competitor);
        //
//        competition.setScore((int) (competition.getScore() + competitor.getTotalScore()));

        competitor.setTotalScore(competitor.getTotalScore() + competition.getScore());
    }

    @Override
    public void disqualify(int competitionId, int competitorId) {
        Competition competition = competitionsById.get(competitionId);
        Competitor competitor = competitorsById.get(competitorId);

        if (competition == null || competitor == null) {
            throw new IllegalArgumentException();
        }

        boolean isRemoved = competition.getCompetitors().remove(competitor);

        if (!isRemoved) {
            throw new IllegalArgumentException();
        }

        competitor.setTotalScore(competitor.getTotalScore() - competition.getScore());
    }

    @Override
    public Iterable<Competitor> getByName(String name) {
        List<Competitor> competitorsByName = competitorsById.values().stream()
                .filter(competitor -> competitor.getName().equals(name))
                .sorted(Comparator.comparingInt(Competitor::getId))
                .collect(Collectors.toList());

        if (competitorsByName.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return competitorsByName;
    }

    @Override
    public Iterable<Competitor> findCompetitorsInRange(long min, long max) {
        List<Competitor> competitorsInRange = competitorsById.values().stream()
                .filter(competitor -> competitor.getTotalScore() > min && competitor.getTotalScore() <= max)
                .collect(Collectors.toList());

        return competitorsInRange;
    }

    @Override
    public Iterable<Competitor> searchWithNameLength(int minLength, int maxLength) {
        List<Competitor> competitorsWithNameLength = competitorsById.values().stream()
                .filter(competitor -> competitor.getName().length() >= minLength && competitor.getName().length() <= maxLength)
                .sorted(Comparator.comparingInt(Competitor::getId))
                .collect(Collectors.toList());

        return competitorsWithNameLength;
    }

    @Override
    public Boolean contains(int competitionId, Competitor comp) {
        Competition competition = getCompetition(competitionId);

        HashSet<Competitor> competitors = getCompetitorsOfCompetition(competition);

        return competitors.contains(comp);
    }

    @Override
    public Competition getCompetition(int id) {
        Competition competition = competitionsById.get(id);

        if (competition == null) {
            throw new IllegalArgumentException();
        }

        return competition;
    }

    @Override
    public int competitorsCount() {
        return competitorsById.size();
    }

    @Override
    public int competitionsCount() {
        return competitionsById.size();
    }

    private boolean isCompetitorExist(int id) {
        return competitorsById.containsKey(id);
    }

    private boolean isCompetitionExist(int id) {
        return competitionsById.containsKey(id);
    }

    private HashSet<Competitor> getCompetitorsOfCompetition(Competition competition) {
        return (HashSet<Competitor>) competition.getCompetitors();
    }
}
