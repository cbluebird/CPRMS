package team.sugarsmile.cprms.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnforcerConfigProperties {
    private String url;

    private String driverClassName;

    private String username;

    private String password;

    private String modelPath;

}
