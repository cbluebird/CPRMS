package team.sugarsmile.cprms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * @author XiMo
 */

@Data
@Builder
public class Department {
    Integer id;
    Type type;
    String name;
    Boolean pubic;
    Boolean business;

    @AllArgsConstructor
    @Getter
    public enum Type {
        ADMINISTRATION(1),
        DIRECT(2),
        COLLEGE(3);

        private final Integer value;

        public static Type getType(Integer value) {
            for (Type v : Type.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid DepartmentType value: " + value);
        }
    }
}