package team.sugarsmile.cprms.dto;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

/**
 * @author XiMo
 */

@Data
@Builder
public class PaginationDto<T> {
    private int pageNum;
    private int pageSize;
    private int total;
    private ArrayList<T> list;
}
