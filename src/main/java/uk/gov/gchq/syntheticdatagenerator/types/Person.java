package uk.gov.gchq.syntheticdatagenerator.types;

public interface Person {
    String getUid();
    void setUid(final String uid);
    String getName();
    void setName(final String name);
    String getDateOfBirth();
    void setDateOfBirth(final String dateOfBirth);
    PhoneNumber[] getContactNumbers();
    void setContactNumbers(final PhoneNumber[] contactNumbers);
    EmergencyContact[] getEmergencyContacts();
    void setEmergencyContacts(final EmergencyContact[] emergencyContacts);
    Address getAddress();
    void setAddress(final Address address);
    BankDetails getBankDetails();
    void setBankDetails(final BankDetails bankDetails);
    String getTaxCode();
    void setTaxCode(final String taxCode);
    Nationality getNationality();
    void setNationality(final Nationality nationality);
    String getEntradaULLDate();
    void setEntradaULLDate(final String entradaULLDate);
    Grade getGrade();
    void setGrade(final Grade grade);
    Campus getCampus();
    void setCampus(final Campus campus);
    BirthLocation getBirthLocation();
    void setBirthLocation(final BirthLocation birthLocation);
    Sex getSex();
    void setSex(final Sex sex);
}
