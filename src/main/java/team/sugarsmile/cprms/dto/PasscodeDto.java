package team.sugarsmile.cprms.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author XiMo
 */

@Data
@Builder
public class PasscodeDto {
    private String name;
    private String idCard;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}
