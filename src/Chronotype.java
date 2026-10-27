public enum Chronotype {
    OWL("Сова"),
    LARK("Жаворонок"),
    DOVE("Голубь");

    private final String russianName;

    Chronotype(String russianName) {
        this.russianName = russianName;
    }

    @Override
    public String toString() {
        return russianName;
    }
}