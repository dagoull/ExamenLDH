package uk.gov.gchq.syntheticdatagenerator.serialise;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class JSONSerialiser<O> implements Serialiser<O>{

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONSerialiser.class);
    //private final ReflectDatumWriter<O> datumWriter;

    private final Class<O> domainClass;
    //private final Schema schema;

    public JSONSerialiser(@JsonProperty("domainClass") final Class<O> domainClass) {
        requireNonNull(domainClass, "domainClass is required");
        this.domainClass = domainClass;
    }

    @Override
    public void serialise(Stream<O> objects, OutputStream output) throws IOException {
        requireNonNull(output, "output");
        if (nonNull(objects)) {
            //create a data file writer around the output stream
            //since we didn't create the output stream, we shouldn't close it either, someone else might want it afterwards!
            ObjectMapper mapper = new ObjectMapper();
            JsonFactory jfactory = new JsonFactory();
            JsonGenerator jGenerator = jfactory.createJsonGenerator(output, JsonEncoding.UTF8);
            LOGGER.debug("Creating data file writer");
            try {
                jGenerator.useDefaultPrettyPrinter();
                jGenerator.writeStartArray();
                //iterate and append items -- we can't use forEach on the stream as the lambda can't throw an IOException
                Iterator<O> objectIt = objects.iterator();

                while (objectIt.hasNext()) {
                    O next = objectIt.next();
                    mapper.writeValue(jGenerator, next);
                }

            } catch (Exception ex) {
                LOGGER.error("Error occurred: {}", ex.getMessage());
                throw new RuntimeException(ex);
            } finally {
                try {
                    jGenerator.writeEndArray();
                } catch (IOException e) {
                    LOGGER.warn("Unable to write JSON generator", e);
                }
                jGenerator.close();
            }
        }
    }

    @Override
    public Stream<O> deserialise(InputStream stream) throws IOException {
        return null;
    }
}
