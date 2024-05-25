package team.sugarsmile.cprms.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private Integer id;
    private Admin.AdminType type;
    private String path;
}
