package soa.util;

import javax.naming.InitialContext;
import java.util.Properties;

public class JndiUtils {
    @SuppressWarnings("unchecked")
    public static <T> T getFromContext(Class<T> clazz, String path) {
        try {
            return (T) new InitialContext().lookup(path);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to retrieve item from context. Path = "+path);
        }
    }
}
