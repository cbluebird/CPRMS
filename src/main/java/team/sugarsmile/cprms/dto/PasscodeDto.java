package team.sugarsmile.cprms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author XiMo
 */

@Data
@Builder
public class PasscodeDto {
    private String name;
    private String idCard;
    private LocalDate appointmentTime;
    private LocalDateTime createTime;
}
