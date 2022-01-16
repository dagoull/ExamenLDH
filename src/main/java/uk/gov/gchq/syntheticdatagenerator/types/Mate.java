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
 * @class Mate
 * @brief Clase encargada de crear una lista de compañeros de trabajo
 * @details Genera una lista de compañeros, los cuales a su vez tambien tienen compañeros, pueden tener tres titulos
 * @version 1.0
 ****************************************************************************/
public class Mate implements Serializable {
    private String uid;
    private Mate[] mates;
    private String mateType;

    /**
     * @brief Genera los compañeros de los tres titulos que pueden tener asignados
     * @param random Numero aleatorio que creara datos aleatorios
     * @param chain Profundidad que puede tener la generacion recursiva
     * @return Lista de compañeros
     */
    public static Mate[] generateMany(final Random random, final int chain) {
        return new Mate[]{
                generateRecursive(random, chain, "Companiero Interino"),
                generateRecursive(random, chain, "Companiero Fijo"),
                generateRecursive(random, chain, "Companiero Titular")
        };
    }

    /**
     * @brief Metodo que se encarga de controlar que la lista es lo suficientemente profunda
     * @param random Numero aleatorio que creara datos aleatorios
     * @param chain Profundidad que puede tener la generacion recursiva
     * @param mateType Titulo asignado al compañero
     * @return Compañero
     */
    public static Mate generateRecursive(final Random random, final int chain, final String mateType) {
        Mate mate = Mate.generate(random, mateType);
        if (chain <= 1) {
            mate.setMate(null);
        } else {
            mate.setMate(Mate.generateMany(random, chain - 1));
        }
        return mate;
    }

    /**
     * @brief Genera los datos aleatorios de los compañeros
     * @param random Numero aleatorio que creara datos aleatorios
     * @param mateType Titulo asignado al compañero
     * @return Compañero
     */
    public static Mate generate(final Random random, final String mateType) {
        Mate mate = new Mate();
        mate.setUid(Pas.generateUID(random));
        mate.setmateType(mateType);

        return mate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        requireNonNull(uid);
        this.uid = uid;
    }

    public String getmateType() {
        return mateType;
    }

    public void setmateType(final String mateType) {
        requireNonNull(mateType);
        this.mateType = mateType;
    }

    public Mate[] getMate() {
        if (null == mates) {
            return new Mate[0];
        } else {
            return mates.clone();
        }
    }

    public void setMate(final Mate[] mates) {
        if (null == mates) {
            this.mates = null;
        } else {
            this.mates = mates.clone();
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Mate.class.getSimpleName() + "[", "]")
                .add("uid=" + uid)
                .add("Mate=" + Arrays.toString(mates))
                .add("mateType='" + mateType + "'")
                .toString();
    }
}
