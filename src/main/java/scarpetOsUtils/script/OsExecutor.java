package scarpetOsUtils.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;

public class OsExecutor {
    public static void apply(Expression expr) {
        expr.addLazyFunction("os_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec(command, false);
        });

        expr.addLazyFunction("os_exec_ignore_errors", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec(command, true);
        });

        expr.addLazyFunction("powershell_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("powershell -c "+command, false);
        });

        expr.addLazyFunction("cmd_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("cmd /c "+command, false);
        });

        expr.addLazyFunction("bash_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("bash -c "+command, false);
        });

        expr.addLazyFunction("sh_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

            return os_exec("sh -c "+command, false);
        });



    }

    private static LazyValue os_exec(String command, boolean ignoreError) {
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
            if(ignoreError){
                return (cc, tt) -> StringValue.of(output.toString());
            }
            if (exitVal == 0) {
                return (cc, tt) -> StringValue.of(output.toString());
            } else {
                return (cc, tt) -> NumericValue.of(exitVal);
            }
        } catch (IOException | InterruptedException e) {
            throw new InternalExpressionException(e.getMessage());
        }
    }

}
