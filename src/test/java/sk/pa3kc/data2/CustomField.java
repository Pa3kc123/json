package sk.pa3kc.data2;

import sk.pa3kc.json.ann.JsonKey;

public class CustomField {
    private Long id;
    private Name name;

    @JsonKey("internal_name")
    private String internalName;
    @JsonKey("field_format")
    private FieldFormat fieldFormat;
    private String value;

    public Long getId() { return id; }
    public void setId(Long value) { this.id = value; }

    public Name getName() { return name; }
    public void setName(Name value) { this.name = value; }

    public String getInternalName() { return internalName; }
    public void setInternalName(String value) { this.internalName = value; }

    public FieldFormat getFieldFormat() { return fieldFormat; }
    public void setFieldFormat(FieldFormat value) { this.fieldFormat = value; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
