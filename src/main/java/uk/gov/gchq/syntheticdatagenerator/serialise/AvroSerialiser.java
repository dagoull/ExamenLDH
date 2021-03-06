/*
 * Copyright 2018-2021 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.gchq.syntheticdatagenerator.serialise;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileStream;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.syntheticdatagenerator.types.Person;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

/**
 * An {@code AvroInputStreamSerialiser} is used to serialise and deserialise Avro files.
 * Converts an avro {@link InputStream} to/from a {@link Stream} of domain objects ({@link O}s).
 *
 * @param <O> the domain object type
 */

class MyReflectDatumWriter<O> extends ReflectDatumWriter<O> implements Serialiser<O>{
    public MyReflectDatumWriter(Schema schema) {
        super(schema);
    }

    @Override
    public void serialise(Stream<Person> objects, OutputStream output) throws IOException {
        // Empty Function
    }

    @Override
    public Stream<O> deserialise(InputStream stream) throws IOException {
        return null;
    }
}

/*****************************************************************************
 * @class AvroSerialiser
 * @brief Clase encargada de pasar los datos en Stream a formato avro
 * @details Haciendo uso de el serialiser de apache se transformara un stream de objetos a formato avro. Implementa a la interfaz Serialiser
 * @version 1.0
 ****************************************************************************/
public class AvroSerialiser<O> implements Serialiser<O> {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = LoggerFactory.getLogger(AvroSerialiser.class);
    private final MyReflectDatumWriter<O> datumWriter;

    private final Class<O> domainClass;
    private final Schema schema;

    /**
     * @brief Constructor de la clase, almacena valores necesarios para la serializacion en formato avro
     * @param domainClass Dominio de la clase
     */
    @JsonCreator
    public AvroSerialiser(@JsonProperty("domainClass") final Class<O> domainClass) {
        requireNonNull(domainClass, "domainClass is required");
        this.domainClass = domainClass;
        this.schema = ReflectData.AllowNull.get().getSchema(domainClass);
        this.datumWriter = new MyReflectDatumWriter<>(schema);
    }

    /**
     * @brief Cambia un stream de entrada en formato avro a un stream entendible
     * @param input Stream de entrada
     * @return Stream de salida
     * @throws IOException Fallo en la deserializacion
     */
    @Override
    public Stream<O> deserialise(final InputStream input) throws IOException {
        DataFileStream<O> in;
        in = new DataFileStream<>(input, new ReflectDatumReader<>(schema));

        //Don't use try-with-resources here! This input stream needs to stay open until it is closed manually by the
        //stream it is feeding below
        return StreamSupport.stream(in.spliterator(), false);
    }

    /**
     * @brief Metodo que sera usado para serializar en formato avro
     * @param objects El stream de objetos que van a a ser serializados
     * @param output  El stream de salida que se usara para escribir en formato json
     * @throws IOException Fallo en la serializacion
     */
    @Override
    public void serialise(final Stream<Person> objects, final OutputStream output) throws IOException {
        requireNonNull(output, "output");
        if (nonNull(objects)) {
            //create a data file writer around the output stream
            //since we didn't create the output stream, we shouldn't close it either, someone else might want it afterwards!
            final DataFileWriter<O> dataFileWriter = new DataFileWriter<>(datumWriter);
            LOGGER.debug("Creating data file writer");
            try {
                dataFileWriter.create(schema, output);
                //iterate and append items -- we can't use forEach on the stream as the lambda can't throw an IOException
                Iterator<O> objectIt = (Iterator<O>) objects.iterator();

                while (objectIt.hasNext()) {
                    O next = objectIt.next();
                    dataFileWriter.append(next);
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    dataFileWriter.flush();
                } catch (IOException e) {
                    LOGGER.warn("Unable to flush Avro DataFileWriter", e);
                }
                dataFileWriter.close();
            }
        }
    }

    public Class<O> getDomainClass() {
        return domainClass;
    }
}
