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
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/*****************************************************************************
 * @class CreateData
 * @brief Clase encargada de iniciar el programa
 * @details Clase que segun parametros pasados por la linea de comandos inicializa varios hilos, crea distintos ficheros y organiza donde se crearan los datos sinteticos
 * @version 1.0
 ****************************************************************************/
public final class CreateData {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateData.class);
    // Varargs indices
    private static final int MINIMUM_ARGS = 5;
    private static final int OUT_PATH_ARG = 0;
    private static final int NUM_ALUMNOS_ARG = 1;
    private static final int NUM_FILES_ARG = 3;
    private static final int OPC_JSON = 2;
    private static final int NUM_THREADS_ARG = 5;
    private static final int PERSON_TYPE = 4;

    private CreateData() {
    }

    /**
     * @brief main del programa, dependiendo de los valores pasados por la linea de comandos creara datos para el PAS o para Alumnos en formato avro o JSON
     * @param args Argumentos pasados por la linea de comandos, intrucciones de uso en README.md
     */
    public static void main(final String... args) {
        if (args.length < MINIMUM_ARGS) {
            LOGGER.warn("Este metodo necesita al menos 5 argumentos. La direccion del directorio para guardar los archivos, " +
                    "el numero de alumnos para generar, -avro para formato avro o -json para json, el numero de archivos " +
                    "que se utilizaran para dividir la informacion y el tipo de persona a generar (pas / alumno). " +
                    "El quinto argumento es opcional y se trata del numero de hilos estando 1 por defecto.");
        } else {
            String outputFilePath = args[OUT_PATH_ARG];
            // Required minimal arguments
            long numberOfPersons = Long.parseLong(args[NUM_ALUMNOS_ARG]);
            int numberOfFiles = Integer.parseInt(args[NUM_FILES_ARG]);
            String personType = args[PERSON_TYPE];
            // Default values
            int numberOfThreads = numberOfFiles;
            // avro o json
            String opcion = args[OPC_JSON];
            String extension;
            if(Objects.equals(opcion, "-avro")) {
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
            long personsPerFile = numberOfPersons / numberOfFiles;
            for (int i = 0; i < numberOfFiles; i++) {
                tasks[i] = new CreateDataFile(personsPerFile, i, new File(outputFilePath + "/" + personType + "_file" + i + extension), extension, personType);
            }
            try {
                List<Future<Boolean>> responses = executors.invokeAll(Arrays.asList(tasks));
                for (Future<Boolean> response : responses) {
                    response.get();
                }
            } catch (final Exception e) {
                LOGGER.error(e.getLocalizedMessage());
                Thread.currentThread().interrupt();
            }
            long endTime = System.currentTimeMillis();
            LOGGER.info("Took {}ms to create {} persons" , (endTime - startTime), numberOfPersons);
        }
    }

    /**
     * @brief Crea un {@link ThreadFactory} que crea hilos daemon que previenen que JVM cierre
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
