package uk.gov.gchq.syntheticdatagenerator.serialise;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.syntheticdatagenerator.types.Person;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/*****************************************************************************
 * @class JSONSerialiser
 * @brief Clase encargada de pasar los datos en Stream a formato JSON
 * @details Haciendo uso de el serialiser de jackson se transformara un stream de objetos a formato JSON. Implementa a la interfaz Serialiser
 * @version 1.0
 ****************************************************************************/
public class JSONSerialiser<O> implements Serialiser<O>{

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONSerialiser.class);

    /**
     * @brief Constructor de la clase, almacena el dominio de la clase
     * @param domainClass Dominio de la clase
     */
    public JSONSerialiser(@JsonProperty("domainClass") final Class<O> domainClass) {
        requireNonNull(domainClass, "domainClass is required");
    }

    /**
     * @brief Metodo que sera usado para serializar en formato json
     * @param objects El stream de objetos que van a a ser serializados
     * @param output  El stream de salida que se usara para escribir en formato json
     * @throws IOException Fallo en la serializacion
     */
    @Override
    public void serialise(Stream<Person> objects, OutputStream output) throws IOException {
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
                Iterator<O> objectIt = (Iterator<O>) objects.iterator();

                while (objectIt.hasNext()) {
                    O next = objectIt.next();
                    mapper.writeValue(jGenerator, next);
                }

            } catch (Exception ex) {
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
