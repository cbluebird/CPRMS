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
    DepartmentType departmentType;
    String name;
    Boolean social;
    Boolean official;

    @AllArgsConstructor
    @Getter
    public enum DepartmentType {
        ADMINISTRATION(1),
        DIRECT(2),
        COLLEGE(3);

        private final Integer value;

        public static DepartmentType getType(Integer value) {
            for (DepartmentType v : DepartmentType.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid Department Type value: " + value);
        }
    }
}