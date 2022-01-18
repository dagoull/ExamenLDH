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
import uk.gov.gchq.syntheticdatagenerator.utils.DateHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.StringJoiner;

/*****************************************************************************
 * @class Pas
 * @brief Clase encargada de generar los datos sinteticos del PAS de la ULL
 * @details Clase que segun unos parametros predeterminados y otros aleatorios genera datos sinteticos que simulan ser un trabajador del PAS de la ULL
 * @version 1.0
 ****************************************************************************/

public class Pas implements Serializable,  Person {
    private static final long serialVersionUID = 1L;
    private static final int MIN_MANGERS_TREE_HEIGHT = 2;
    private static final int EXTRA_MATES_TREE_HEIGHT_RANGE = 3;
    private static final int MIN_EXPEDIENTE = 1248;
    private static final int EXTRA_EXPEDIENTE_RANGE = 1_000;
    private static final int PRODUCTIVIDAD_BONUS = 2_500;
    private static final String TAX_CODE = "11500L";

    private String uid;
    private String name;
    private String dateOfBirth;
    private PhoneNumber[] contactNumbers;
    private EmergencyContact[] emergencyContacts;
    private Address address;
    private BankDetails bankDetails;
    private String taxCode;
    private Nationality nationality;
    private Mate[] mate;
    private String entradaULLDate;
    private Grade grade;
    private Campus campus;
    private int expedienteAmount;
    private int productividadBonus;
    private BirthLocation birthLocation;
    private Sex sex;

    /**
     * @brief Constructor por defecto
     */
    public Pas() {

    }

    public Pas(Pas pas) {
        this.setName(pas.name);
        this.setDateOfBirth(pas.dateOfBirth);
        this.setContactNumbers(pas.contactNumbers);
        this.setEmergencyContacts(pas.emergencyContacts);
        this.setAddress(pas.address);
        this.setBankDetails(pas.bankDetails);
        this.setTaxCode(pas.taxCode);
        this.setNationality(pas.nationality);
        this.setMate(pas.mate);
        this.setEntradaULLDate(pas.entradaULLDate);
        this.setGrade(pas.grade);
        this.setCampus(pas.campus);
        this.setExpedienteAmount(pas.expedienteAmount);
        this.setProductividadBonus(pas.productividadBonus);
        this.setBirthLocation(pas.birthLocation);
        this.setSex(pas.sex);
    }

    /**
     * @brief Genera datos relacionados con el PAS
     * @param random Numero aleatorio que hara que los datos generados tambien sean aleatorios
     * @return Al individuo generado
     */
    public static Pas generate(final Random random) {
        Pas pas = new Pas();
        //Genera datos que solo son de españoles, funciona medio raro
        Faker faker = new Faker(new Locale("es"));
        pas.setUid(generateUID(random));
        Name pasName = faker.name();
        pas.setName(pasName.firstName() + " " + pasName.lastName()); // we are storing name as a string not a Name
        pas.setDateOfBirth(DateHelper.generateDateOfBirth(random));
        pas.setContactNumbers(PhoneNumber.generateMany(random));
        pas.setEmergencyContacts(EmergencyContact.generateMany(faker, random));
        pas.setAddress(Address.generate(faker));
        pas.setBankDetails(BankDetails.generate(random));
        pas.setTaxCode(generateTaxCode());
        pas.setNationality(Nationality.generate(random));
        pas.setMate(Mate.generateMany(random, MIN_MANGERS_TREE_HEIGHT + random.nextInt(EXTRA_MATES_TREE_HEIGHT_RANGE)));
        pas.setEntradaULLDate(DateHelper.generateHireDate(pas.dateOfBirth, random));
        pas.setGrade(Grade.generate(random));
        pas.setCampus(Campus.generate(random));
        pas.setExpedienteAmount(MIN_EXPEDIENTE + random.nextInt(EXTRA_EXPEDIENTE_RANGE));
        pas.setProductividadBonus(random.nextInt(PRODUCTIVIDAD_BONUS));
        pas.setBirthLocation(BirthLocation.generate(faker, random));
        pas.setSex(Sex.generate(random));

        return pas;
    }

    /**
     * @brief Crea un UID aleatorio
     * @param random Numero aleatorio que hara que los datos generados tambien sean aleatorios
     * @return El UID
     */
    public static String generateUID(final Random random) {
        return "pas" + random.nextInt(Integer.MAX_VALUE);
    }

    /**
     * @brief Crea un numero TAX aleatorio
     * @return TAX_CODE
     */
    private static String generateTaxCode() {
        return TAX_CODE;
    }

    /**
     * @brief Devuelve el UID
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @brief Utiliza el UID generado aleatoriamente para el individuo
     * @param uid Codigo unico del usuario
     */
    public void setUid(final String uid) {
        this.uid = uid;
    }

    /**
     * @brief Devuelve el nombre de la persona
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @brief Asigna un nombre a la persona
     * @param name Nombre de la persona
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @brief Devuelve la fecha de nacimiento de la persona
     * @return dateOfBirth
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @brief Asigna una fecha de nacimiento a la persona
     * @param dateOfBirth Fecha de nacimiento a asignar
     */
    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @brief Devuelve una lista de contactos asignados a la persona
     * @return contactNumbers
     */
    public PhoneNumber[] getContactNumbers() {
        return contactNumbers;
    }

    /**
     * @brief Asigna una lista de contactos a la persona
     * @param contactNumbers Lista de contactos generados automaticamente
     */
    public void setContactNumbers(final PhoneNumber[] contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    /**
     * @brief Devuelve una lista de contactos de emergencia asignados a la persona
     * @return emergencyContacts
     */
    public EmergencyContact[] getEmergencyContacts() {
        return emergencyContacts;
    }

    /**
     * @brief Asigna una lista de contactos a la persona
     * @param emergencyContacts Lista de contactos de emergencia asignados a la persona
     */
    public void setEmergencyContacts(final EmergencyContact[] emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    /**
     * @brief Devuelve la direccion del hogar de la persona
     * @return address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @brief Asigna una direccion aleatoria a la persona
     * @param address Direccion del hogar generada aleatoriamente
     */
    public void setAddress(final Address address) {
        this.address = address;
    }

    /**
     * @brief Devuelve los datos de la cuenta bancaria de la persona
     * @return bankDetails
     */
    public BankDetails getBankDetails() {
        return bankDetails;
    }

    /**
     * @brief Asigna unos datos bancarios a la persona
     * @param bankDetails Numero generado aleatoriamente asignado a la persona
     */
    public void setBankDetails(final BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    /**
     * @brief Devuelve el codigo TAX de la persona
     * @return taxVode
     */
    public String getTaxCode() {
        return taxCode;
    }

    /**
     * @brief Asigna un codigo de TAX a la persona
     * @param taxCode codigo TAX generado automaticamente
     */
    public void setTaxCode(final String taxCode) {
        this.taxCode = taxCode;
    }

    /**
     * @brief Devuelve la nacionalidad de la persona
     * @return nationality
     */
    public Nationality getNationality() {
        return nationality;
    }

    /**
     * @brief Asigna una nacionalidad a la persona
     * @param nationality nacionalidad a asignar
     */
    public void setNationality(final Nationality nationality) {
        this.nationality = nationality;
    }

    /**
     * @brief Devuelve la lista de compañeros asignados a la persona
     * @return mate
     */
    public Mate[] getMate() {
        return mate;
    }

    /**
     * @brief Asigna una lista de compañeros a la persona
     * @param mate lista compleja de compañeros
     */
    public void setMate(final Mate[] mate) {
        this.mate = mate;
    }

    /**
     * @brief Devuelve la fecha de la entrada de la persona a la ULL
     * @return entradaULLDate
     */
    public String getEntradaULLDate() {
        return entradaULLDate;
    }

    /**
     * @brief Asigna una fecha de entrada
     * @param entradaULLDate fecha exacta
     */
    public void setEntradaULLDate(final String entradaULLDate) {
        this.entradaULLDate = entradaULLDate;
    }

    /**
     * @brief Devuelve el tipo de grado
     * @return grade
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * @brief Asigna un tipo de grado
     * @param grade nivel de grado
     */
    public void setGrade(final Grade grade) {
        this.grade = grade;
    }

    /**
     * @brief Devuelve el campus donde trabaja
     * @return campus
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * @brief Asigna el campus donde trabaja
     * @param campus campus de la ULL
     */
    public void setCampus(final Campus campus) {
        this.campus = campus;
    }

    /**
     * @brief Devuelve la cantidad de dinero que cobra
     * @return expedienteAmount
     */
    public int getExpedienteAmount() {
        return expedienteAmount;
    }

    /**
     * @brief Asigna la cantidad de dinero que cobra
     * @param expedienteAmount cantidad generada aleatoriamente
     */
    public void setExpedienteAmount(final int expedienteAmount) {
        this.expedienteAmount = expedienteAmount;
    }

    /**
     * @brief Devuelve el bonus que gana al mes gracias a su productividad
     * @return productividadBonus
     */
    public int getProductividadBonus() {
        return productividadBonus;
    }

    /**
     * @brief Asigna el bonus de productividad que ganara la persona al mes
     * @param productividadBonus El bonus que cobrara
     */
    public void setProductividadBonus(final int productividadBonus) {
        this.productividadBonus = productividadBonus;
    }

    /**
     * @brief Devuelve el lugar de nacimiento de la persona
     * @return birthLocation
     */
    public BirthLocation getBirthLocation() {
        return birthLocation;
    }

    /**
     * @brief Asigna un lugar de nacimiento
     * @param birthLocation Lugar concreto de nacimiento
     */
    public void setBirthLocation(final BirthLocation birthLocation) {
        this.birthLocation = birthLocation;
    }

    /**
     * @brief Devuelve el genero de la persona
     * @return sex
     */
    public Sex getSex() {
        return sex;
    }

    /**
     * @brief Asigna un genero a la persona
     * @param sex genero
     */
    public void setSex(final Sex sex) {
        this.sex = sex;
    }

    /**
     * @brief Cambia todos los atributos de la personal a String
     * @return los datos de la persona
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Pas.class.getSimpleName() + "[", "]")
                .add("uid=" + uid)
                .add("name='" + name + "'")
                .add("dateOfBirth='" + dateOfBirth + "'")
                .add("contactNumbers=" + Arrays.toString(contactNumbers))
                .add("emergencyContacts=" + Arrays.toString(emergencyContacts))
                .add("address=" + address)
                .add("bankDetails=" + bankDetails)
                .add("taxCode='" + taxCode + "'")
                .add("nationality=" + nationality)
                .add("Mate=" + Arrays.toString(mate))
                .add("entradaULLDate='" + entradaULLDate + "'")
                .add("grade=" + grade)
                .add("campus=" + campus)
                .add("expedienteAmount=" + expedienteAmount)
                .add("productividadBonus=" + productividadBonus)
                .add("birthLocation=" + birthLocation)
                .add("sex=" + sex)
                .toString();
    }
}
