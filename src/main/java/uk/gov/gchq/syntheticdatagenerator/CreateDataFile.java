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

package uk.gov.gchq.syntheticdatagenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.gov.gchq.syntheticdatagenerator.serialise.AvroSerialiser;
import uk.gov.gchq.syntheticdatagenerator.serialise.JSONSerialiser;
import uk.gov.gchq.syntheticdatagenerator.serialise.Serialiser;
import uk.gov.gchq.syntheticdatagenerator.types.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/*****************************************************************************
 * @class CreateDataFile
 * @brief Clase encargada de crear el fichero de salida de los resultados
 * @details Crea un fecho de salida avro o json gracias a los serialise implementados
 * @version 1.0
 ****************************************************************************/
public final class CreateDataFile implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDataFile.class);
    // When a large number of Alumnos are requested, print the progress as feedback that the process has not frozen
    private static final long PRINT_EVERY = 100_000L;
    private static final String ALUMNO = "alumno";

    private final long numberOfPersons;
    private final SecureRandom random;
    private final File outputFile;
    private final String extension;
    private final String type;

    /**
     * @brief Constructor de la clase, asigna varios de los valores pasados por la linea de comandos y la semilla que generara los datos
     * @param numberOfPersons Numero de alumnos a generar
     * @param seed Semilla que se utilizara para generar los datos aleatorios
     * @param outputFile Fichero de salida
     * @param extension Extension del fichero de salida
     */
    public CreateDataFile(final long numberOfPersons, final long seed, final File outputFile, final String extension, final String type) {
        this.numberOfPersons = numberOfPersons;
        this.random = new SecureRandom(longToBytes(seed));
        this.outputFile = outputFile;
        this.extension = extension;
        this.type = type;
    }

    /**
     * @brief Llamada que se utiliza para crear el fichero de salida y generar los datos de la persona
     * @return True si la operacion fue exitosa, false en caso contrario
     */
    public Boolean call() {
        if (!outputFile.getParentFile().exists()) {
            boolean mkdirSuccess = outputFile.getParentFile().mkdirs();
            if (!mkdirSuccess) {
                LOGGER.warn("Failed to create parent directory {}", outputFile.getParent());
            }
        }
        try (OutputStream out = new FileOutputStream(outputFile)) {
            Serialiser<Person> alumnoSerialiser = null;
            if (extension.equals(".avro")) {
                alumnoSerialiser = new AvroSerialiser<>(Person.class);
            } else if (extension.equals(".json")) {
                alumnoSerialiser = new JSONSerialiser<>(Person.class);
            }

            // Need at least one Alumno or one Pas
            Stream<Person> personStream = null;

            if (type.equals(ALUMNO)) {
                Alumno firstPerson = Alumno.generate(random);
                Profesor[] profesors = firstPerson.getProfesor();
                profesors[0].setUid("Bob");
                firstPerson.setProfesor(profesors);
                personStream = Stream.of(firstPerson);
            } else {
                Pas firstPerson = Pas.generate(random);
                Mate[] mates = firstPerson.getMate();
                mates[0].setUid("Bob");
                firstPerson.setMate(mates);
                personStream = Stream.of(firstPerson);
            }

            // Create more Alumnos/Pas if needed
            if (numberOfPersons > 1) {
                personStream = Stream.concat(personStream, generateStreamOfPersons());
            }

            // Serialise stream to output
            assert alumnoSerialiser != null;
            alumnoSerialiser.serialise(personStream, out);
            return true;
        } catch (IOException ex) {
            LOGGER.error("IOException when serialising Alumno to Avro", ex);
            return false;
        }
    }

    /**
     * @brief Genera el numero de alumnos pedidos por la linea de comandos
     * @return Devuelve todos los datos que se han generado en forma de Stream
     */
    private Stream<Person> generateStreamOfPersons() {
        LOGGER.info("Generating {} Persons", numberOfPersons);
        final AtomicLong counter = new AtomicLong(0);
        Stream<Person> personStream = Stream.generate(() -> {
            if (counter.incrementAndGet() % PRINT_EVERY == 0) {
                LOGGER.info("Processing {} of {}", counter.get(), numberOfPersons);
            }
            return type.equals(ALUMNO) ? Alumno.generate(random) : Pas.generate(random);
        });
        // Excluding the one Alumno we had to generate above
        return personStream.limit(numberOfPersons - 1);
    }

    /**
     * @brief Cambia un valor en long a bytes
     * @param x Numero long
     * @return El valor del long en bytes
     */
    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}
