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

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import uk.gov.gchq.syntheticdatagenerator.types.Alumno;

import java.io.File;
import java.util.Random;

public class AlumnoTest {

    @Test
    public void generateAlumno() {
        long startTime = System.currentTimeMillis();
        Random random = new Random(0);
        for (int i = 0; i < 100; i++) {
            Alumno t = Alumno.generate(random);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Took " + (endTime - startTime) + "ms to create 100 Alumnos");
    }

    @Test
    public void generateAvroData() {
        try {
            CreateData.main(new String[]{"data", "50", "-avro", "1"});
        } finally {
            FileUtils.deleteQuietly(new File(".data"));
        }
    }

    @Test
    public void generateJSONData() {
        try {
            CreateData.main(new String[]{"data", "50", "-json", "1"});
        } finally {
            FileUtils.deleteQuietly(new File(".data"));
        }
    }

}
