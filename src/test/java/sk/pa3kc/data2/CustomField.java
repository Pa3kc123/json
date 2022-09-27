package sk.pa3kc.data2;

public class CustomField {
    private Long id;
    private Name name;
    private Object internalName;
    private FieldFormat fieldFormat;
    private String value;

    public Long getID() { return id; }
    public void setID(Long value) { this.id = value; }

    public Name getName() { return name; }
    public void setName(Name value) { this.name = value; }

    public Object getInternalName() { return internalName; }
    public void setInternalName(Object value) { this.internalName = value; }

    public FieldFormat getFieldFormat() { return fieldFormat; }
    public void setFieldFormat(FieldFormat value) { this.fieldFormat = value; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
