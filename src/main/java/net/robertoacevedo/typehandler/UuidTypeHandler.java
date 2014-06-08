package net.robertoacevedo.typehandler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.UUID;

/**
 * Created by roberto on June 08, 2014.
 */
public class UuidTypeHandler implements TypeHandler<UUID> {

    @Override
    public void setParameter(PreparedStatement ps, int i, UUID
            parameter, JdbcType jdbcType) throws SQLException {
        ps.setObject(i, parameter.toString(), Types.OTHER);
    }

    @Override
    public UUID getResult(ResultSet rs, String columnName) throws SQLException {
        return UUID.fromString(rs.getString(columnName));
    }

    @Override
    public UUID getResult(ResultSet rs, int i) throws SQLException {
        return UUID.fromString(rs.getString(i));
    }

    @Override
    public UUID getResult(CallableStatement cs, int columnIndex) throws
            SQLException {
        return UUID.fromString(cs.getString(columnIndex));
    }
}
