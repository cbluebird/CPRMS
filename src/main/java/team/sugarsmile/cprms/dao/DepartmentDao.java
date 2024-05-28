package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.Department;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author XiMo
 */

public class DepartmentDao {
    public void insert(Department department) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO department (type, name, public, business) VALUES (?, ? ,? ,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, department.getType().getValue());
            stmt.setString(2, department.getName());
            stmt.setBoolean(3, department.getSocial());
            stmt.setBoolean(4, department.getBusiness());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public void delete(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM department WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public void updateNameAndType(Department department) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE department SET type = ?, name = ? ,public=?,business=? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, department.getType().getValue());
            stmt.setString(2, department.getName());
            stmt.setLong(5, department.getId());
            stmt.setBoolean(3,department.getSocial());
            stmt.setBoolean(4,department.getBusiness());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }


    public Department findById(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM department WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Department.builder()
                        .id(rs.getInt("id"))
                        .type(Department.Type.getType(rs.getInt("type")))
                        .name(rs.getString("name"))
                        .social(rs.getBoolean("public"))
                        .business(rs.getBoolean("business"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public Department findByName(String name) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM department WHERE name = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Department.builder()
                        .id(rs.getInt("id"))
                        .type(Department.Type.getType(rs.getInt("type")))
                        .name(rs.getString("name"))
                        .social(rs.getBoolean("public"))
                        .business(rs.getBoolean("business"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public ArrayList<Department> findByPage(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM department LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            ArrayList<Department> departmentList = new ArrayList<Department>();
            while (rs.next()) {
                departmentList.add(Department.builder()
                        .id(rs.getInt("id"))
                        .type(Department.Type.getType(rs.getInt("type")))
                        .name(rs.getString("name"))
                        .social(rs.getBoolean("public"))
                        .business(rs.getBoolean("business"))
                        .build());
            }
            return departmentList;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public ArrayList<Department> getAll() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM department ";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            ArrayList<Department> departmentList = new ArrayList<Department>();
            while (rs.next()) {
                departmentList.add(Department.builder()
                        .id(rs.getInt("id"))
                        .type(Department.Type.getType(rs.getInt("type")))
                        .name(rs.getString("name"))
                        .social(rs.getBoolean("public"))
                        .business(rs.getBoolean("business"))
                        .build());
            }
            System.out.println(departmentList.size());
            return departmentList;
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
            String sql = "SELECT COUNT(*) FROM department";
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
}
