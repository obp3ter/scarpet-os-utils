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
            ProcessBuilder processBuilder = execProcess(command);

            try {

                Process process = processBuilder.start();

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

    private static ProcessBuilder execProcess(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String os = System.getProperty("os.name");
        if (os.equals("Windows 10")) { processBuilder.command("powershell.exe", "-c", command); }
        else if (os.matches("Windows .*")) { processBuilder.command("cmd.exe", "/c", command); }
        else if (os.matches("Ubuntu .*")) { processBuilder.command("bash", "-c", command); }
        else { processBuilder.command("sh", "-c", command); }
        return processBuilder;
    }

}
