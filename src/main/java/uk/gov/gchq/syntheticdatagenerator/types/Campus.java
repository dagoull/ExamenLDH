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

import java.util.Random;

public enum Campus {
    ESCUELA_DE_DOCTORADO_Y_ESTUDIOS_DE_POSGRADO,
    ESCUELA_POLITECNICA_SUPERIOR_DE_INGENIERIA,
    ESCUELA_SUPERIOR_DE_INGENIERIA_Y_TECNOLOGIA,
    FACULTAD_DE_BELLAS_ARTES,
    FACULTAD_DE_CIENCIAS,
    FACULTAD_DE_CIENCIAS_DE_LA_SALUD,
    FACULTAD_DE_CIENCIAS_SOCIALES_Y_DE_LA_COMUNICACION,
    FACULTAD_DE_DERECHO,
    FACULTAD_DE_ECONOMIA,
    FACULTAD_DE_EDUCACUION,
    FACULTAD_DE_FARMACIA,
    FACULTAD_DE_HUMANIDADES,
    FACULTAD_DE_PSICOLOGIA;

    public static Campus generate(final Random random) {
        return Campus.values()[random.nextInt(Campus.values().length)];
    }
}
