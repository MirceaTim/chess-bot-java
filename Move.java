public class Move {
    Coordonate from;
    Coordonate to;

    Character promotion;

    public Move(Coordonate from, Coordonate to) {
        this.from = from;
        this.to = to;
    }

    public Move(Coordonate from, Coordonate to, Character promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "move " + from.toString() + to.toString() + (promotion == null ? "" : promotion);
    }
}
