package sk.pa3kc.data2;

import com.fasterxml.jackson.annotation.JsonAlias;

import sk.pa3kc.json.ann.JsonKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomField {
    private Long id;
    private Name name;
    @JsonKey("internal_name")
    @JsonAlias("internal_name")
    private String internalName;
    @JsonKey("field_format")
    @JsonAlias("field_format")
    private FieldFormat fieldFormat;
    private String value;
}
