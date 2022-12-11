import java.util.*;
import java.util.stream.Collectors;

public class BoardImpl implements Board {
    Map<String, Card> cardsByNames;
    List<String> deathCardsByNames;

    public BoardImpl() {
        cardsByNames = new HashMap<>();
        deathCardsByNames = new ArrayList<>();
    }

    @Override
    public void draw(Card card) {
        if (this.contains(card.getName())) {
            throw new IllegalArgumentException();
        }

        cardsByNames.put(card.getName(), card);
    }

    @Override
    public Boolean contains(String name) {
        return cardsByNames.containsKey(name);
    }

    @Override
    public int count() {
        return cardsByNames.size();
    }

    @Override
    public void play(String attackerCardName, String attackedCardName) {
        Card attacker = cardsByNames.get(attackerCardName);
        Card target = cardsByNames.get(attackedCardName);

        if (attacker == null || target == null || attacker.getLevel() != target.getLevel()) {
            throw new IllegalArgumentException();
        }

        if (!isDeath(target)) {
            target.setHealth(target.getHealth() - attacker.getDamage());

            if (isDeath(target)) {
                attacker.setScore(attacker.getScore() + target.getLevel());
                deathCardsByNames.add(target.getName());
            }
        }
    }

    @Override
    public void remove(String name) {
        Card remove = cardsByNames.remove(name);

        if (remove == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void removeDeath() {
//        cardsByNames.entrySet().removeIf(e -> e.getValue().getHealth() <= 0);
        deathCardsByNames.forEach(c -> cardsByNames.remove(c));
        deathCardsByNames.clear();
    }

    @Override
    public Iterable<Card> getBestInRange(int start, int end) {
        return cardsByNames.values().stream()
                .filter(c -> c.getScore() >= start && c.getScore() <= end)
                .sorted(Comparator.comparingInt(Card::getLevel).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Card> listCardsByPrefix(String prefix) {
        return cardsByNames.values().stream()
                .filter(c -> c.getName().startsWith(prefix))
                .sorted((c1, c2) -> {
                    String c1ReversedName = new StringBuilder(c1.getName()).reverse().toString();
                    String c2ReversedName = new StringBuilder(c2.getName()).reverse().toString();
                    int result = c1ReversedName.compareTo(c2ReversedName);

                    if (result == 0) {
                        result = Integer.compare(c1.getLevel(), c2.getLevel());
                    }

                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Card> searchByLevel(int level) {
        return cardsByNames.values().stream()
                .filter(c -> c.getLevel() == level)
                .sorted(Comparator.comparingInt(Card::getScore).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void heal(int health) {
        Card cardWithMinHealth = cardsByNames.values().stream().min(Comparator.comparingInt(Card::getHealth)).orElse(null);

        int newHealth = cardWithMinHealth.getHealth() + health;

        if (isDeath(cardWithMinHealth) && newHealth > 0) {
            deathCardsByNames.remove(cardWithMinHealth.getName());
        }

        cardWithMinHealth.setHealth(newHealth);
    }

    private boolean isDeath(Card card) {
        return card.getHealth() <= 0;
    }
}
