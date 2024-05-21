package team.sugarsmile.cprms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
public class Admin {
    private Integer id;
    private String name;
    private String phone;
    private String password;
    private AdminType adminType;
    private Integer departmentID;
    @AllArgsConstructor
    @Getter
    public enum AdminType {
        SYSTEM(1),
        SCHOOL(2),
        DEPARTMENT(3),
        AUDIT(4);

        private final Integer value;

        public static Admin.AdminType getType(Integer value) {
            for (Admin.AdminType v : Admin.AdminType.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid AdminType value: " + value);
        }
    }
}
