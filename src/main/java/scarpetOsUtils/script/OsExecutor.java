package scarpetOsUtils.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import carpet.script.Expression;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.StringValue;
import carpet.script.value.Value;

public class OsExecutor {
    public static void apply(Expression expr) {
        expr.addLazyFunction("os_exec", 1, (c, t, lv) -> {

            String command = lv.get(0).evalValue(c).getString();

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
        });
    }

}
