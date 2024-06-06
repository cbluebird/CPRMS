package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.Admin;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class AdminDao {
    public void insert(Admin admin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        Date date = new Date(new java.util.Date().getTime());
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO admin (admin_type, name, phone, password, department_id, password_update_time, user_name) VALUES (?, ? ,? ,?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, admin.getAdminType().getValue());
            stmt.setString(2, admin.getName());
            stmt.setString(3, admin.getPhone());
            stmt.setString(4, admin.getPassword());
            stmt.setInt(5, admin.getDepartmentID());
            stmt.setDate(6, date);
            stmt.setString(7, admin.getUserName());
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
                        .passwordUpdateTime(rs.getDate("password_update_time").toLocalDate())
                        .userName(rs.getString("user_name"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public Admin findByUserName(String userName) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM admin WHERE user_name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Admin.builder()
                        .id(rs.getInt("id"))
                        .adminType(Admin.AdminType.getType(rs.getInt("admin_type")))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .password(rs.getString("password"))
                        .departmentID(rs.getInt("department_id"))
                        .userName(rs.getString("user_name"))
                        .passwordUpdateTime(rs.getDate("password_update_time").toLocalDate())
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }


    public Admin findByUserPhone(String phone) {
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
                        .userName(rs.getString("user_name"))
                        .passwordUpdateTime(rs.getDate("password_update_time").toLocalDate())
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public void updatePasswordByID(Integer id, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE admin SET password = ?, password_update_time = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, password);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            stmt.setLong(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }

    }

    public ArrayList<Admin> findByPage(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM admin LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            ArrayList<Admin> adminList = new ArrayList<>();
            while (rs.next()) {
                adminList.add(Admin.builder()
                        .id(rs.getInt("id"))
                        .adminType(Admin.AdminType.getType(rs.getInt("admin_type")))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .password(rs.getString("password"))
                        .departmentID(rs.getInt("department_id"))
                        .userName(rs.getString("user_name"))
                        .passwordUpdateTime(rs.getDate("password_update_time").toLocalDate())
                        .build());
            }
            return adminList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public ArrayList<Admin> findByPageAndType(int pageNum, int pageSize, Admin.AdminType type) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM admin where admin_type = ? LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, type.getValue());
            stmt.setInt(2, (pageNum - 1) * pageSize);
            stmt.setInt(3, pageSize);
            rs = stmt.executeQuery();
            ArrayList<Admin> adminList = new ArrayList<>();
            while (rs.next()) {
                adminList.add(Admin.builder()
                        .id(rs.getInt("id"))
                        .adminType(Admin.AdminType.getType(rs.getInt("admin_type")))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .password(rs.getString("password"))
                        .departmentID(rs.getInt("department_id"))
                        .userName(rs.getString("user_name"))
                        .passwordUpdateTime(rs.getDate("password_update_time").toLocalDate())
                        .build());
            }
            return adminList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public ArrayList<Admin> findAuditAndSchoolAdminList(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM admin where admin_type = 2 OR admin_type = 4 LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            ArrayList<Admin> adminList = new ArrayList<>();
            while (rs.next()) {
                adminList.add(Admin.builder()
                        .id(rs.getInt("id"))
                        .adminType(Admin.AdminType.getType(rs.getInt("admin_type")))
                        .name(rs.getString("name"))
                        .phone(rs.getString("phone"))
                        .password(rs.getString("password"))
                        .departmentID(rs.getInt("department_id"))
                        .userName(rs.getString("user_name"))
                        .passwordUpdateTime(rs.getDate("password_update_time").toLocalDate())
                        .build());
            }
            return adminList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public int count() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM admin";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public int countAuditAndSchoolAdmin() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM admin where admin_type = 2 OR admin_type = 4";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public int countByType(Admin.AdminType type) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT COUNT(*) FROM admin where admin_type = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, type.getValue());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public void delete(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM admin WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public void updateAdminInfo(Admin admin) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE admin SET name = ?, user_name = ?, phone = ?, department_id = ?, admin_type = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, admin.getName());
            stmt.setString(2, admin.getUserName());
            stmt.setString(3, admin.getPhone());
            stmt.setLong(4, admin.getDepartmentID());
            stmt.setInt(5, admin.getAdminType().getValue());
            stmt.setLong(6, admin.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }
}
