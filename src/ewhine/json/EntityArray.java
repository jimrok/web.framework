package ewhine.json;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

public class EntityArray  {
	
	final static private Logger LOG = LoggerFactory.getLogger(EntityArray.class
			.getName());
	private Collection<Entity> _items = null;
	
	public EntityArray(Collection<Entity> items) {
		this._items  = items;
	}
	
	public String toJson() {

		JsonFactory jfactory = new JsonFactory();
		ByteArrayOutputStream jsonOutput = new ByteArrayOutputStream();
		try {
			JsonGenerator gen = jfactory.createGenerator(jsonOutput,
					JsonEncoding.UTF8);
			
			gen.writeStartArray();
			int i = 0;
			for (Entity e : _items) {
				
				if (i > 0) {
					gen.writeRaw(",");
				}
				gen.writeRaw(e.toJson());
				i++;
			}
			gen.writeEndArray();
			gen.flush();
			
			gen.close();
			

		} catch (Exception e) {

			if(LOG.isErrorEnabled())
				LOG.error("json output exeception", e);
		}

		byte[] out = jsonOutput.toByteArray();
		String result = new String(out);
		
		return result;

	}

}
