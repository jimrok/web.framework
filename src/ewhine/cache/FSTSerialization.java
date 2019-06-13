package ewhine.cache;

import org.nustaq.serialization.FSTConfiguration;

public class FSTSerialization {
	static FSTConfiguration singletonConf = FSTConfiguration.createDefaultConfiguration();
    public static FSTConfiguration getInstance() {
        return singletonConf;
    }
    
    public static byte[] toByteArray(Object o) {
    	return FSTSerialization.getInstance().asByteArray(o);
    }
    
    public static Object asObject(byte[] data) {
    	return FSTSerialization.getInstance().asObject(data);
    }
    
}
