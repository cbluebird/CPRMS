package team.sugarsmile.cprms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Builder
public class Admin {
    private Integer id;
    private AdminType adminType;
    private String userName;
    private String password;
    private LocalDate passwordUpdateTime;
    private String name;
    private String phone;
    private Integer departmentID;

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
