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
import uk.gov.gchq.syntheticdatagenerator.types.Pas;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class AlumnoTest {

    @Test(expected = Test.None.class)
    public void generateAlumno() {
        ArrayList<Alumno> alumnos = new ArrayList<>();
        int counter = 0;
        long startTime = System.currentTimeMillis();
        Random random = new Random(0);
        for (int i = 0; i < 100; i++) {
            Alumno t = Alumno.generate(random);
            alumnos.add(t);
            counter++;
        }
        assert (alumnos.size() == counter);
        long endTime = System.currentTimeMillis();
        System.out.println("Took " + (endTime - startTime) + "ms to create 100 Alumnos");
    }

    @Test(expected = Test.None.class)
    public void generatePas() {
        ArrayList<Pas> miembros = new ArrayList<>();
        int counter = 0;
        long startTime = System.currentTimeMillis();
        Random random = new Random(0);
        for (int i = 0; i < 100; i++) {
            Pas p = Pas.generate(random);
            miembros.add(p);
            counter++;
        }
        assert (miembros.size() == counter);
        long endTime = System.currentTimeMillis();
        System.out.println("Took " + (endTime - startTime) + "ms to create 100 Pas");
    }

    @Test(expected = Test.None.class)
    public void generateAvroDataAlumno() {
        try {
            assert (CreateData.main(new String[]{"data", "50", "-avro", "1", "alumno"}) == 0);
        } finally {
            FileUtils.deleteQuietly(new File(".data"));
        }
    }

    @Test(expected = Test.None.class)
    public void generateJSONDataAlumno() {
        try {
            assert (CreateData.main(new String[]{"data", "50", "-json", "1", "alumno"}) == 0);
        } finally {
            FileUtils.deleteQuietly(new File(".data"));
        }
    }

    @Test(expected = Test.None.class)
    public void generateAvroDataPAS() {
        try {
            assert (CreateData.main(new String[]{"data", "50", "-avro", "1", "pas"}) == 0);
        } finally {
            FileUtils.deleteQuietly(new File(".data"));
        }
    }

    @Test(expected = Test.None.class)
    public void generateJSONData() {
        try {
            assert (CreateData.main(new String[]{"data", "50", "-json", "1", "pas"}) == 0);
        } finally {
            FileUtils.deleteQuietly(new File(".data"));
        }
    }

}
