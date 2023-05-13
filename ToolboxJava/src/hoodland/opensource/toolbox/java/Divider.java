package hoodland.opensource.toolbox.java;

import hoodland.opensource.toolbox.DividerTypes;
import hoodland.opensource.toolbox.SymbolsKt;

public class Divider {
    public static final int DEFAULT_DIVIDER_LENGTH = SymbolsKt.DEFAULT_DIVIDER_LENGTH;
    public static final hoodland.opensource.toolbox.DividerTypes DIVIDER_TYPE_SINGLE = DividerTypes.SINGLE;
    public static final hoodland.opensource.toolbox.DividerTypes DIVIDER_TYPE_DOUBLE = DividerTypes.DOUBLE;

    public static String get(hoodland.opensource.toolbox.DividerTypes typeToUse, int length) {
        return SymbolsKt.divider(typeToUse, length);
    }

    public static String get(hoodland.opensource.toolbox.DividerTypes typeToUse) {
        return get(typeToUse, DEFAULT_DIVIDER_LENGTH);
    }

    public static String get(int length) {
        return get (DIVIDER_TYPE_SINGLE, length);
    }

    public static String get() {
        return get(DIVIDER_TYPE_SINGLE, DEFAULT_DIVIDER_LENGTH);
    }
}
