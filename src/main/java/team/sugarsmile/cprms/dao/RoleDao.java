package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoleDao {
    public ArrayList<String> getRoleByType(Admin.AdminType type) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT path FROM role where admin_type = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, type.getValue());
            rs = stmt.executeQuery();
            ArrayList<String> roleList = new ArrayList<String>();
            while (rs.next()) {
                roleList.add(rs.getString("path"));
            }
            return roleList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }
}
