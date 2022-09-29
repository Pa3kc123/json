package sk.pa3kc.data_sample;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSequentialList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Root {
    // Primitive types
    private boolean booleanPrimitive;
    private byte bytePrimitive;
    private char charPrimitive;
    private short shortPrimitive;
    private int intPrimitive;
    private long longPrimitive;
    private float floatPrimitive;
    private double doublePrimitive;
    private Object nullPrimitive;
    private String[] stringArray;

    // Wrappers
    private Boolean booleanWrapper;
    private Byte byteWrapper;
    private Character charWrapper;
    private Short shortWrapper;
    private Integer intWrapper;
    private Long longWrapper;
    private Float floatWrapper;
    private Double doubleWrapper;

    // Frequently used
    private Date date;
    private LocalDate localDate;
    private OffsetDateTime offsetDateTime;

    private BigInteger bigInteger;
    private BigDecimal bigDecimal;

    // JDK Interfaces
    private CharSequence charSequence;

    private Iterable<String> stringIterable;
    private Queue<String> stringQueue;
    private Set<String> stringSet;
    private Map<String, String> stringStringMap;
}
