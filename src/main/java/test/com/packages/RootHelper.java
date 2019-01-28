package test.com.packages;

import android.support.annotation.Nullable;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class RootHelper {


    public static boolean uninstall(String packageName) {
        String output = executeCommand("pm uninstall " + packageName);
        if (output != null && output.toLowerCase().contains("success")) {
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    //поможет нам в выполнении команд
    private static String executeCommand(String command) {
        List<String> stdout = Shell.SU.run(command);
        if (stdout == null) {
            return null;
        }
        //конвертирует список строк в одну строку
        StringBuilder stringBuilder = new StringBuilder();
        for (String line : stdout) {
            stringBuilder.append(line).append("\n");
        }
        return stringBuilder.toString();
    }





}
