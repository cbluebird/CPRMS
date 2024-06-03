package team.sugarsmile.cprms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.sql.Date;

@Data
@Builder
public class Admin {
    private Integer id;
    private String name;
    private String phone;
    private String password;
    private AdminType adminType;
    private Integer departmentID;
    private Date date;
    private String userName;

    @AllArgsConstructor
    @Getter
    public enum AdminType {
        SYSTEM(1),
        SCHOOL(2),
        DEPARTMENT(3),
        AUDIT(4);

        private final Integer value;

        public static AdminType getType(Integer value) {
            for (AdminType v : AdminType.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid Admin Type value: " + value);
        }
    }
}
