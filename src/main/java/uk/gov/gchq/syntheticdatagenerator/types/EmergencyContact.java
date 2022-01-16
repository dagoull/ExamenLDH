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

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

/*****************************************************************************
 * @class EmergencyContact
 * @brief Clase encargada de crear una lista de contactos de emergencia de una persona
 * @details Con javafaker se generara un nombre, relacion y nuemero de telefono de una persona y se le asignara como contacto de emergencia, puede tener un maximo de 4 contactos
 * @version 1.0
 ****************************************************************************/
public class EmergencyContact implements Serializable {
    private static final int MAX_EXTRA_CONTACTS = 4;
    private String contactName;
    private Relation relation;
    private PhoneNumber[] contactNumbers;

    /**
     * @brief Metodo para iniciar la generacion de datos aleatorios
     * @param faker Objeto que contiene los metodos para generar nombres, calles, numero de telefonos, ... aleatorios
     * @param random Numero aleatorio que creara datos aleatorios
     * @return Los contactos de emergencia
     */
    public static EmergencyContact generate(final Faker faker, final Random random) {
        EmergencyContact contact = new EmergencyContact();
        Name tempName = faker.name();
        contact.setContactName(tempName.firstName() + " " + tempName.lastName());
        contact.setRelation(Relation.generate(random));
        contact.setContactNumbers(PhoneNumber.generateMany(random));
        return contact;
    }

    /**
     * @brief Metodo para dar la posibilidad de generar contactos de emergencia extra
     * @param faker Objeto que contiene los metodos para generar nombres, calles, numero de telefonos, ... aleatorios
     * @param random Numero aleatorio que creara datos aleatorios
     * @return Los contactos de emergencia
     */
    public static EmergencyContact[] generateMany(final Faker faker, final Random random) {
        int numberOfExtraContacts = random.nextInt(MAX_EXTRA_CONTACTS);
        EmergencyContact[] emergencyContacts = new EmergencyContact[numberOfExtraContacts + 1];
        emergencyContacts[0] = EmergencyContact.generate(faker, random);
        for (int i = 1; i <= numberOfExtraContacts; i++) {
            emergencyContacts[i] = EmergencyContact.generate(faker, random);
        }
        return emergencyContacts;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(final String contactName) {
        this.contactName = contactName;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(final Relation relation) {
        this.relation = relation;
    }

    public PhoneNumber[] getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(final PhoneNumber[] contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", EmergencyContact.class.getSimpleName() + "[", "]")
                .add("contactName='" + contactName + "'")
                .add("relation=" + relation)
                .add("contactNumbers=" + Arrays.toString(contactNumbers))
                .toString();
    }
}
