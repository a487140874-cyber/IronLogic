package com.ironlogic.common.persistence.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

/**
 * MyBatis type handler that binds plain JSON strings as PostgreSQL {@code jsonb}.
 *
 * <p>The current MVP keeps JSON payloads as raw {@link String} values in the domain and DTO
 * layers because that is the easiest shape to read and debug while the schema is still moving.
 * PostgreSQL, however, does not accept a normal varchar parameter for a {@code jsonb} column.
 * This handler performs the minimal conversion needed for persistence without forcing the rest
 * of the codebase to adopt maps or custom JSON objects prematurely.
 */
public class JsonbStringTypeHandler extends BaseTypeHandler<String> {

    /**
     * Binds one non-null JSON string as a PostgreSQL {@code jsonb} value.
     *
     * @param ps prepared statement receiving the value
     * @param i parameter index
     * @param parameter raw JSON string
     * @param jdbcType jdbc type passed by MyBatis
     * @throws SQLException when the database driver rejects the value
     */
    @Override
    public void setNonNullParameter(
            PreparedStatement ps,
            int i,
            String parameter,
            JdbcType jdbcType
    ) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("jsonb");
        jsonObject.setValue(parameter);
        ps.setObject(i, jsonObject);
    }

    /**
     * Reads a JSON column back into its raw string form.
     *
     * @param rs result set containing the column
     * @param columnName column label
     * @return raw JSON string or {@code null}
     * @throws SQLException when the result set cannot be read
     */
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    /**
     * Reads a JSON column back into its raw string form.
     *
     * @param rs result set containing the column
     * @param columnIndex column index
     * @return raw JSON string or {@code null}
     * @throws SQLException when the result set cannot be read
     */
    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }

    /**
     * Reads a JSON column from a stored procedure result.
     *
     * @param cs callable statement containing the column
     * @param columnIndex column index
     * @return raw JSON string or {@code null}
     * @throws SQLException when the statement cannot be read
     */
    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex);
    }
}
