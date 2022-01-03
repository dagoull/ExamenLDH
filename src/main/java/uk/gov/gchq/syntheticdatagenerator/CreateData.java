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

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public final class CreateData {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateData.class);
    // Varargs indices
    private static final int MINIMUM_ARGS = 4;
    private static final int OUT_PATH_ARG = 0;
    private static final int NUM_ALUMNOS_ARG = 1;
    private static final int NUM_FILES_ARG = 3;
    private static final int OPC_JSON = 2;
    private static final int NUM_THREADS_ARG = 4;

    private CreateData() {
    }

    public static void main(final String... args) {
        if (args.length < MINIMUM_ARGS) {
            LOGGER.warn("Este metodo necesita al menos 4 argumentos. La direccion del directorio para guardar los archivos, el numero de alumnos para generar, 0 para formato avro o 1 para json y el numero de archivos que se utilizaran para dividir la informacion. El cuarto argumento es opcional y se trata del numero de hilos estando 1 por defecto.");
        } else {
            String outputFilePath = args[OUT_PATH_ARG];
            // Required minimal arguments
            long numberOfAlumnos = Long.parseLong(args[NUM_ALUMNOS_ARG]);
            int numberOfFiles = Integer.parseInt(args[NUM_FILES_ARG]);
            // Default values
            int numberOfThreads = numberOfFiles;
            // avro o json
            int opcion = Integer.parseInt(args[OPC_JSON]);
            String extension;
            if(opcion == 0) {
                extension = ".avro";
            } else {
                extension = ".json";
            }
            // Optional additional arguments overriding default values
            if (args.length > MINIMUM_ARGS) {
                numberOfThreads = Integer.parseInt(args[NUM_THREADS_ARG]);
            }
            long startTime = System.currentTimeMillis();
            ExecutorService executors = Executors.newFixedThreadPool(numberOfThreads, createDaemonThreadFactory());
            CreateDataFile[] tasks = new CreateDataFile[numberOfFiles];
            long alumnosPerFile = numberOfAlumnos / numberOfFiles;
            for (int i = 0; i < numberOfFiles; i++) {
                tasks[i] = new CreateDataFile(alumnosPerFile, i, new File(outputFilePath + "/alumnos_file" + i + extension), extension);
            }
            try {
                List<Future<Boolean>> responses = executors.invokeAll(Arrays.asList(tasks));
                for (Future<Boolean> response : responses) {
                    response.get();
                }
            } catch (final Exception e) {
                LOGGER.error(e.getLocalizedMessage());
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("Took {}ms to create {} alumnos", (endTime - startTime), numberOfAlumnos);
        }
    }

    /**
     * Create a {@link ThreadFactory} that creates daemon threads that don't prevent JVM exit.
     *
     * @return a daemon thread factory
     */
    public static ThreadFactory createDaemonThreadFactory() {
        //set up a thread to watch this
        final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
        //ensure thread is daemon
        return runnable -> {
            Thread t = defaultFactory.newThread(runnable);
            t.setDaemon(true);
            return t;
        };
    }
}
