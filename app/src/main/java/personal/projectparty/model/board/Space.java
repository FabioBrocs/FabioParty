package personal.projectparty.model.board;

public class Space {
    private final int id;
    private SpaceType type;

    public Space(int id, SpaceType type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public SpaceType getType() {
        return type;
    }

    public void changeType(SpaceType newType) {
        this.type = newType;
    }

    public String getSymbol() {
        return switch (type) {
            case START -> "s";
            case BLUE -> "B";
            case RED -> "R";
            case EVENT -> "E";
            case ITEM -> "I";
            case STAR -> "S";
        };
    }

    @Override
    public String toString() {
        return "Posizione " + id + ": " + type;
    }
}
