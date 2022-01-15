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

public class Pas implements Serializable {
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

    public static String generateUID(final Random random) {
        return "pas" + random.nextInt(Integer.MAX_VALUE);
    }

    private static String generateTaxCode() {
        return TAX_CODE;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public PhoneNumber[] getContactNumbers() {
        return contactNumbers;
    }

    public void setContactNumbers(final PhoneNumber[] contactNumbers) {
        this.contactNumbers = contactNumbers;
    }

    public EmergencyContact[] getEmergencyContacts() {
        return emergencyContacts;
    }

    public void setEmergencyContacts(final EmergencyContact[] emergencyContacts) {
        this.emergencyContacts = emergencyContacts;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(final Address address) {
        this.address = address;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(final BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(final String taxCode) {
        this.taxCode = taxCode;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(final Nationality nationality) {
        this.nationality = nationality;
    }

    public Mate[] getMate() {
        return mate;
    }

    public void setMate(final Mate[] mate) {
        this.mate = mate;
    }

    public String getEntradaULLDate() {
        return entradaULLDate;
    }

    public void setEntradaULLDate(final String entradaULLDate) {
        this.entradaULLDate = entradaULLDate;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(final Grade grade) {
        this.grade = grade;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(final Campus campus) {
        this.campus = campus;
    }

    public int getExpedienteAmount() {
        return expedienteAmount;
    }

    public void setExpedienteAmount(final int expedienteAmount) {
        this.expedienteAmount = expedienteAmount;
    }

    public int getProductividadBonus() {
        return productividadBonus;
    }

    public void setProductividadBonus(final int productividadBonus) {
        this.productividadBonus = productividadBonus;
    }

    public BirthLocation getBirthLocation() {
        return birthLocation;
    }

    public void setBirthLocation(final BirthLocation workLocation) {
        this.birthLocation = workLocation;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(final Sex sex) {
        this.sex = sex;
    }

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
