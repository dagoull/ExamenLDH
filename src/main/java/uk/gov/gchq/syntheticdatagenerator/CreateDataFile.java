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
import uk.gov.gchq.syntheticdatagenerator.types.Alumno;
import uk.gov.gchq.syntheticdatagenerator.types.Manager;
import uk.gov.gchq.syntheticdatagenerator.types.UserId;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public final class CreateDataFile implements Callable<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDataFile.class);
    // When a large number of Alumnos are requested, print the progress as feedback that the process has not frozen
    private static final long PRINT_EVERY = 100_000L;

    private final long numberOfAlumnos;
    private final Random random;
    private final File outputFile;

    public CreateDataFile(final long numberOfAlumnos, final long seed, final File outputFile) {
        this.numberOfAlumnos = numberOfAlumnos;
        this.random = new Random(seed);
        this.outputFile = outputFile;
    }

    public Boolean call() {
        if (!outputFile.getParentFile().exists()) {
            boolean mkdirSuccess = outputFile.getParentFile().mkdirs();
            if (!mkdirSuccess) {
                LOGGER.warn("Failed to create parent directory {}", outputFile.getParent());
            }
        }
        try (OutputStream out = new FileOutputStream(outputFile)) {
            AvroSerialiser<Alumno> AlumnoAvroSerialiser = new AvroSerialiser<>(Alumno.class);

            // Need at least one Alumno
            Alumno firstAlumno = Alumno.generate(random);
            Manager[] managers = firstAlumno.getManager();
            managers[0].setUid("Bob");
            firstAlumno.setManager(managers);

            // Create more Alumnos if needed
            Stream<Alumno> AlumnoStream = Stream.of(firstAlumno);
            if (numberOfAlumnos > 1) {
                AlumnoStream = Stream.concat(AlumnoStream, generateStreamOfAlumnos());
            }

            // Serialise stream to output
            AlumnoAvroSerialiser.serialise(AlumnoStream, out);
            return true;
        } catch (IOException ex) {
            LOGGER.error("IOException when serialising Alumno to Avro", ex);
            return false;
        }
    }

    private Stream<Alumno> generateStreamOfAlumnos() {
        LOGGER.info("Generating {} Alumnos", numberOfAlumnos);
        final AtomicLong counter = new AtomicLong(0);
        Stream<Alumno> AlumnoStream = Stream.generate(() -> {
            if (counter.incrementAndGet() % PRINT_EVERY == 0) {
                LOGGER.info("Processing {} of {}", counter.get(), numberOfAlumnos);
            }
            return Alumno.generate(random);
        });
        // Excluding the one Alumno we had to generate above
        return AlumnoStream.limit(numberOfAlumnos - 1);
    }
}
