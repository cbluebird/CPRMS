package team.sugarsmile.cprms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Audit {
    private Integer id;
    private String operate;
    private Integer adminId;
    private LocalDateTime createTime;
    private AuditType type;
    private String HMAC;

    @AllArgsConstructor
    @Getter
    public enum AuditType {
        LOGIN(1),
        ADD(2),
        DELETE(3),
        UPDATE(4),
        QUERY(5);

        private final int value;

        public static AuditType fromValue(int value) {
            for (AuditType type : AuditType.values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Audit Type value: " + value);
        }
    }
}
