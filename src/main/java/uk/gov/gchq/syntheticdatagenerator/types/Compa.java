package uk.gov.gchq.syntheticdatagenerator.types;


import java.io.Serializable;

public interface Compa extends Serializable {
    String getUid();
    void setUid(final String uid);
    String getCompaType();
    void setCompaType(final String compaType);
    Compa[] getCompa();
    void setCompa(final Compa[] compa);
}
