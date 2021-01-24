package scarpetOsUtils.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.StringValue;
import carpet.script.value.Value;

public class OsExecutor {
    public static void apply(Expression expr) {
        expr.addLazyFunction("os_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec(command);
        });

        expr.addLazyFunction("powershell_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("powershell -c "+command);
        });

        expr.addLazyFunction("cmd_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("cmd /c "+command);
        });

        expr.addLazyFunction("bash_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("bash -c "+command);
        });

        expr.addLazyFunction("sh_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("sh -c "+command);
        });



    }

    private static LazyValue os_exec(String command) {
        try {

            Process process = Runtime.getRuntime().exec(command);

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                return (cc, tt) -> StringValue.of(output.toString());
            } else {
                return (cc, tt) -> Value.NULL;
            }
        } catch (IOException | InterruptedException e) {
            throw new InternalExpressionException(e.getMessage());
        }
    }

}
