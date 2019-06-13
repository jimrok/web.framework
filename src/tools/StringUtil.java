package tools;

public class StringUtil {

    public static String concat(String f, Object... args) {
        StringBuilder sb = null;
        if (f == null) {
        	sb = new StringBuilder();
        } else {
        	sb = new StringBuilder( f );
        }
        
        for (Object o : args) {
            sb.append( o );
        }
        return sb.toString();
    }

}
