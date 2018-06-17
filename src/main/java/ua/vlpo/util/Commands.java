package ua.vlpo.util;

public enum Commands {
    HELP("/help"),
    START("/start"),
    STOCK("/stock");

    private final String value;

    Commands(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
