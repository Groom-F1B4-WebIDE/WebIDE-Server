package f1b4.webide_server.compile;

public class JavaCodeUtil {
    public static String changeClassNameToSolution(String code){
        return code.replace("public class \\w+", "public class Solution");
    }
}
