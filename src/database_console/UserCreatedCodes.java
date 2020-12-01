package database_console;

/**

 @author R-Mule
 */
public enum UserCreatedCodes {
    OTC(856), POP(857), FRI(859), CAN(858);
    private final int value;

    private UserCreatedCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
