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
 * @class Alumno
 * @brief Clase encargada de generar los datos sinteticos de un alumno de la ULL
 * @details Clase que segun unos parametros predeterminados y otros aleatorios genera datos sinteticos que simulan ser un alumno de la ULL
 * @version 1.0
 ****************************************************************************/
public class Alumno implements Serializable, Person {
    private static final long serialVersionUID = 1L;
    private static final int MIN_MANGERS_TREE_HEIGHT = 2;
    private static final int EXTRA_PROFESORS_TREE_HEIGHT_RANGE = 3;
    private static final int MIN_MATRICULA = 900;
    private static final int EXTRA_MATRICULA_RANGE = 1_000;
    private static final int BECA_BONUS = 2_500;
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
    private Profesor[] profesor;
    private String entradaULLDate;
    private Grade grade;
    private Campus campus;
    private int matriculaAmount;
    private int becaBonus;
    private BirthLocation birthLocation;
    private Sex sex;

    /**
     * @brief Constructor por defecto
     */
    public Alumno() {

    }

    /**
     * @brief Constructor de copia
     * @param alumno Objeto Alumno a copiar
     */
    public Alumno(Alumno alumno) {
        this.setName(alumno.name);
        this.setDateOfBirth(alumno.dateOfBirth);
        this.setContactNumbers(alumno.contactNumbers);
        this.setEmergencyContacts(alumno.emergencyContacts);
        this.setAddress(alumno.address);
        this.setBankDetails(alumno.bankDetails);
        this.setTaxCode(alumno.taxCode);
        this.setNationality(alumno.nationality);
        this.setProfesor(alumno.profesor);
        this.setEntradaULLDate(alumno.entradaULLDate);
        this.setGrade(alumno.grade);
        this.setCampus(alumno.campus);
        this.setMatriculaAmount(alumno.matriculaAmount);
        this.setBecaBonus(alumno.becaBonus);
        this.setBirthLocation(alumno.birthLocation);
        this.setSex(alumno.sex);
    }

    /**
     * @brief Genera datos relacionados con un alumno
     * @param random Numero aleatorio que hara que los datos generados tambien sean aleatorios
     * @return Al individuo generado
     */
    public static Alumno generate(final Random random) {
        Alumno alumno = new Alumno();
        //Genera datos que solo son de españoles, funciona medio raro
        Faker faker = new Faker(new Locale("es"));
        alumno.setUid(generateUID(random));
        Name alumnoName = faker.name();
        alumno.setName(alumnoName.firstName() + " " + alumnoName.lastName()); // we are storing name as a string not a Name
        alumno.setDateOfBirth(DateHelper.generateDateOfBirth(random));
        alumno.setContactNumbers(PhoneNumber.generateMany(random));
        alumno.setEmergencyContacts(EmergencyContact.generateMany(faker, random));
        alumno.setAddress(Address.generate(faker));
        alumno.setBankDetails(BankDetails.generate(random));
        alumno.setTaxCode(generateTaxCode());
        alumno.setNationality(Nationality.generate(random));
        alumno.setProfesor(Profesor.generateMany(random, MIN_MANGERS_TREE_HEIGHT + random.nextInt(EXTRA_PROFESORS_TREE_HEIGHT_RANGE)));
        alumno.setEntradaULLDate(DateHelper.generateHireDate(alumno.dateOfBirth, random));
        alumno.setGrade(Grade.generate(random));
        alumno.setCampus(Campus.generate(random));
        alumno.setMatriculaAmount(MIN_MATRICULA + random.nextInt(EXTRA_MATRICULA_RANGE));
        alumno.setBecaBonus(random.nextInt(BECA_BONUS));
        alumno.setBirthLocation(BirthLocation.generate(faker, random));
        alumno.setSex(Sex.generate(random));

        return alumno;
    }

    /**
     * @brief Crea un UID aleatorio
     * @param random Numero aleatorio que hara que los datos generados tambien sean aleatorios
     * @return El UID
     */
    public static String generateUID(final Random random) {
        return "alu" + random.nextInt(Integer.MAX_VALUE);
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
     * @brief Devuelve la lista de profesores asignados a la persona
     * @return profesor
     */
    public Profesor[] getProfesor() {
        return profesor;
    }

    /**
     * @brief Asigna una lista de profesores a la persona
     * @param profesor lista compleja de profesores
     */
    public void setProfesor(final Profesor[] profesor) {
        this.profesor = profesor;
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
     * @brief Devuelve el campus donde estudia
     * @return campus
     */
    public Campus getCampus() {
        return campus;
    }

    /**
     * @brief Asigna el campus donde estudia
     * @param campus campus de la ULL
     */
    public void setCampus(final Campus campus) {
        this.campus = campus;
    }

    /**
     * @brief Devuelve la cantidad dinero que debe de pagar por matricularse un año en la ULL
     * @return matriculaAmount
     */
    public int getMatriculaAmount() {
        return matriculaAmount;
    }

    /**
     * @brief Asigna la cantidad de dinero que debe de pagar por la matricula
     * @param matriculaAmount cantidad generada aleatoriamente
     */
    public void setMatriculaAmount(final int matriculaAmount) {
        this.matriculaAmount = matriculaAmount;
    }

    /**
     * @brief Devuelve el dinero que debe recibir gracias a la beca
     * @return productividadBonus
     */
    public int getBecaBonus() {
        return becaBonus;
    }

    /**
     * @brief Asigna la cantidad de dinero que debe de recibir el alumno por la beca
     * @param becaBonus El bonus que cobrara
     */
    public void setBecaBonus(final int becaBonus) {
        this.becaBonus = becaBonus;
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
        return new StringJoiner(", ", Alumno.class.getSimpleName() + "[", "]")
                .add("uid=" + uid)
                .add("name='" + name + "'")
                .add("dateOfBirth='" + dateOfBirth + "'")
                .add("contactNumbers=" + Arrays.toString(contactNumbers))
                .add("emergencyContacts=" + Arrays.toString(emergencyContacts))
                .add("address=" + address)
                .add("bankDetails=" + bankDetails)
                .add("taxCode='" + taxCode + "'")
                .add("nationality=" + nationality)
                .add("Profesor=" + Arrays.toString(profesor))
                .add("entradaULLDate='" + entradaULLDate + "'")
                .add("grade=" + grade)
                .add("campus=" + campus)
                .add("matriculaAmount=" + matriculaAmount)
                .add("becaBonus=" + becaBonus)
                .add("birthLocation=" + birthLocation)
                .add("sex=" + sex)
                .toString();
    }
}
