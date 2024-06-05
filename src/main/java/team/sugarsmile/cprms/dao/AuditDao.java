package team.sugarsmile.cprms.dao;

import team.sugarsmile.cprms.exception.ErrorCode;
import team.sugarsmile.cprms.exception.SystemException;
import team.sugarsmile.cprms.model.Audit;
import team.sugarsmile.cprms.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AuditDao {
    public void insert(Audit audit) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "INSERT INTO audit (operate, admin_id, create_time, type, hmac) VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, audit.getOperate());
            stmt.setLong(2, audit.getAdminId());
            stmt.setDate(3, audit.getCreateTime());
            stmt.setInt(4, audit.getType().getValue());
            stmt.setString(5, audit.getHMAC());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public Audit findById(Long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM audit WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return Audit.builder()
                        .id(rs.getInt("id"))
                        .operate(rs.getString("operate"))
                        .adminId(rs.getInt("admin_id"))
                        .createTime(rs.getDate("create_time"))
                        .type(Audit.AuditType.fromValue(rs.getInt("type")))
                        .HMAC(rs.getString("hmac"))
                        .build();
            }
            return null;
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
    }

    public ArrayList<Audit> findAll(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Audit> auditList = new ArrayList<>();
        try {
            conn = JDBCUtil.getConnection();
            String sql = "SELECT * FROM audit LIMIT ?, ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, (pageNum - 1) * pageSize);
            stmt.setInt(2, pageSize);
            rs = stmt.executeQuery();
            while (rs.next()) {
                auditList.add(Audit.builder()
                        .id(rs.getInt("id"))
                        .operate(rs.getString("operate"))
                        .adminId(rs.getInt("admin_id"))
                        .createTime(rs.getDate("create_time"))
                        .type(Audit.AuditType.fromValue(rs.getInt("type")))
                        .HMAC(rs.getString("hmac"))
                        .build());
            }
            return auditList;
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
            String sql = "SELECT COUNT(*) FROM audit";
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

    public ArrayList<Audit> searchAudit(String operate, Integer type, Integer adminID, String createDate, int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Audit> auditList = new ArrayList<>();
        try {
            conn = JDBCUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM audit WHERE 1=1");
            if (createDate != null && !createDate.isEmpty()) {
                sql.append(" AND DATE(create_time) = ?");
            }
            if (operate != null && !operate.isEmpty()) {
                sql.append(" AND operate LIKE ?");
            }
            if (type != null) {
                sql.append(" AND type = ?");
            }
            if (adminID != null) {
                sql.append(" AND admin_id = ?");
            }
            sql.append(" LIMIT ?, ?");

            stmt = conn.prepareStatement(sql.toString());

            int index = 1;
            if (createDate != null && !createDate.isEmpty()) {
                stmt.setString(index++, createDate);
            }
            if (operate != null && !operate.isEmpty()) {
                stmt.setString(index++, operate);
            }
            if (type != null) {
                stmt.setInt(index++, type);
            }
            if (adminID != null) {
                stmt.setInt(index++, adminID);
            }
            stmt.setInt(index++, (pageNum - 1) * pageSize);
            stmt.setInt(index, pageSize);

            rs = stmt.executeQuery();
            while (rs.next()) {
                auditList.add(Audit.builder()
                        .id(rs.getInt("id"))
                        .operate(rs.getString("operate"))
                        .adminId(rs.getInt("admin_id"))
                        .createTime(rs.getDate("create_time"))
                        .type(Audit.AuditType.fromValue(rs.getInt("type")))
                        .HMAC(rs.getString("hmac"))
                        .build());
            }
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt, rs);
        }
        return auditList;
    }

    public int countForSearch(String operate, Integer type, Integer adminID, String createDate) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtil.getConnection();
            StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM audit where 1=1");
            if (createDate != null && !createDate.isEmpty()) {
                sql.append(" AND DATE(create_time) = ?");
            }
            if (operate != null && !operate.isEmpty()) {
                sql.append(" AND operate LIKE ?");
            }
            if (type != null) {
                sql.append(" AND type = ?");
            }
            if (adminID != null) {
                sql.append(" AND admin_id = ?");
            }

            stmt = conn.prepareStatement(sql.toString());

            int index = 1;
            if (createDate != null && !createDate.isEmpty()) {
                stmt.setString(index++, createDate);
            }
            if (operate != null && !operate.isEmpty()) {
                stmt.setString(index++, operate);
            }
            if (type != null) {
                stmt.setInt(index++, type);
            }
            if (adminID != null) {
                stmt.setInt(index, adminID);
            }
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

    public void update(Audit audit) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "UPDATE audit SET operate = ?, admin_id = ?, create_time = ?, type = ?,hmac = ? WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, audit.getOperate());
            stmt.setLong(2, audit.getAdminId());
            stmt.setDate(3, audit.getCreateTime());
            stmt.setInt(4, audit.getType().getValue());
            stmt.setString(5, audit.getHMAC());
            stmt.setLong(6, audit.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }

    public void delete(Long id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtil.getConnection();
            String sql = "DELETE FROM audit WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SystemException(ErrorCode.DB_ERROR.getCode(), e.getMessage(), e);
        } finally {
            JDBCUtil.close(conn, stmt);
        }
    }
}
