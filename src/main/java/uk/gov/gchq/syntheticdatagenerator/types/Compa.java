package uk.gov.gchq.syntheticdatagenerator.types;

public interface Compa {
    String getUid();
    void setUid(final String uid);
    String getCompaType();
    void setCompaType(final String compaType);
    Compa[] getCompa();
    void setCompa(final Compa[] compa);
}
