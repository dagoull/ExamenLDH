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

package uk.gov.gchq.syntheticdatagenerator.types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

import static java.util.Objects.requireNonNull;
/*****************************************************************************
 * @class Profesor
 * @brief Clase encargada de crear una lista de profesores
 * @details Genera una lista de profesores, los cuales a su vez tambien tienen profesores, pueden tener tres titulos
 * @version 1.0
 ****************************************************************************/
public class Profesor implements Serializable {
    private String uid;
    private Profesor[] profesors;
    private String profesorType;

    /**
     * @brief Genera los profesores de los tres titulos que pueden tener asignados
     * @param random Numero aleatorio que creara datos aleatorios
     * @param chain Profundidad que puede tener la generacion recursiva
     * @return Lista de profesores
     */
    public static Profesor[] generateMany(final Random random, final int chain) {
        return new Profesor[]{
                generateRecursive(random, chain, "Profesor Adjunto"),
                generateRecursive(random, chain, "Profesor de Laboratorio"),
                generateRecursive(random, chain, "Profesor Titular")
        };
    }

    /**
     * @brief Metodo que se encarga de controlar que la lista es lo suficientemente profunda
     * @param random Numero aleatorio que creara datos aleatorios
     * @param chain Profundidad que puede tener la generacion recursiva
     * @param profesorType Titulo asignado al profesor
     * @return Profesor
     */
    public static Profesor generateRecursive(final Random random, final int chain, final String profesorType) {
        Profesor profesor = Profesor.generate(random, profesorType);
        if (chain <= 1) {
            profesor.setProfesor(null);
        } else {
            profesor.setProfesor(Profesor.generateMany(random, chain - 1));
        }
        return profesor;
    }

    /**
     * @brief Genera los datos aleatorios de los profesores
     * @param random Numero aleatorio que creara datos aleatorios
     * @param profesorType Titulo asignado al profesor
     * @return Compañero
     */
    public static Profesor generate(final Random random, final String profesorType) {
        Profesor profesor = new Profesor();
        profesor.setUid(Alumno.generateUID(random));
        profesor.setProfesorType(profesorType);

        return profesor;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        requireNonNull(uid);
        this.uid = uid;
    }

    public String getProfesorType() {
        return profesorType;
    }

    public void setProfesorType(final String profesorType) {
        requireNonNull(profesorType);
        this.profesorType = profesorType;
    }

    public Profesor[] getProfesor() {
        if (null == profesors) {
            return new Profesor[0];
        } else {
            return profesors.clone();
        }
    }

    public void setProfesor(final Profesor[] profesors) {
        if (null == profesors) {
            this.profesors = null;
        } else {
            this.profesors = profesors.clone();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Profesor.class.getSimpleName() + "[", "]")
                .add("uid=" + uid)
                .add("Profesor=" + Arrays.toString(profesors))
                .add("ProfesorType='" + profesorType + "'")
                .toString();
    }
}
