package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDao {
    public void insert(Admin admin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO admin (admin_type, name ,phone ,password,department_id) VALUES (?, ? ,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, admin.getAdminType().getValue());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getPhone());
            stmt.setString(4, admin.getPassword());
            stmt.setInt(5,admin.getDepartmentID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public Admin findByID(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM admin WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Admin.builder()
                        .id(rs.getInt("id"))
                        .adminType(Admin.AdminType.getType(rs.getInt("admin_type")))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .password(rs.getString("password"))
                        .departmentID(rs.getInt("department_id"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public Admin findByPhone(String phone) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM admin WHERE phone = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, phone);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Admin.builder()
                        .id(rs.getInt("id"))
                        .adminType(Admin.AdminType.getType(rs.getInt("admin_type")))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .password(rs.getString("password"))
                        .departmentID(rs.getInt("department_id"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }



}
