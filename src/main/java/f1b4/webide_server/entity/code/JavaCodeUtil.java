package f1b4.webide_server.entity.code;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCodeUtil {
    private static final Pattern CLASS_PATTERN = Pattern.compile("public\\s+class\\s+(\\w+)");

    public static String changeClassNameToUserCode(String code) {
        Matcher matcher = CLASS_PATTERN.matcher(code);
        if (matcher.find()) {
            String originalClassName = matcher.group(1);
            return code.replace("public class " + originalClassName, "public class UserCode");
        }
        return code;
    }
}

