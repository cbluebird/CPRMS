package team.sugarsmile.cprms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Builder
public class OfficialAppointment {
    private Long id;
    private String name;
    private String idCard;
    private String phone;
    private Campus campus;
    private LocalDate createTime;
    private LocalDate appointmentTime;
    private String unit;
    private Transportation transportation;
    private String licensePlate;
    private Integer departmentId;
    private String receptionist;
    private String reason;
    private Status status;

    @AllArgsConstructor
    @Getter
    public enum Campus {
        ZHAOHUI(1),
        PINGFENG(2),
        MOGANSHAN(3);

        private final Integer value;

        public static Campus getType(Integer value) {
            for (Campus v : Campus.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid Campus value: " + value);
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Transportation {
        WALKING(1),
        DRIVING(2);

        private final Integer value;

        public static Transportation getType(Integer value) {
            for (Transportation v : Transportation.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid Transportation value: " + value);
        }
    }

    @AllArgsConstructor
    @Getter
    public enum Status {
        UNREVIEWED(1),
        APPROVED(2),
        REJECTED(3);

        private final Integer value;

        public static Status getType(Integer value) {
            for (Status v : Status.values()) {
                if (value.equals(v.getValue())) {
                    return v;
                }
            }
            throw new IllegalArgumentException("Invalid Status value: " + value);
        }
    }
}
