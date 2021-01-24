package scarpetOsUtils.script;

import carpet.script.Expression;
import carpet.script.value.StringValue;

public class GetOs {
    public static void apply(Expression expr) {
        expr.addLazyFunction("get_os", 0, (c, t, lv) -> (cc, tt) -> StringValue.of(System.getProperty("os.name")));
    }
}
