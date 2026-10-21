
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import model.ModelException;

public class DBHandler {

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    public DBHandler() throws ModelException {
        connection = MySQLConnectionFactory.getConnection();
    }

    public void prepareStatement(String sqlInsert) throws ModelException {
        try {
            preparedStatement = connection.prepareStatement(sqlInsert);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao preparar a SQL", e);
        }
    }

    public void setString(int i, String value) throws ModelException {
        validatePreparedStatement();

        try {
            preparedStatement.setString(i, value);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao atribuir string", e);
        }
    }

    public void setInt(int i, int value) throws ModelException {
        validatePreparedStatement();

        try {
            preparedStatement.setInt(i, value);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao atribuir inteiro", e);
        }
    }

    public int executeUpdate() throws ModelException {
        validatePreparedStatement();

        try {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new ModelException("Erro ao executar SQL", e);
        } finally {
            close();
        }
    }

    public void createStatement() throws ModelException {
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new ModelException("Erro ao criar o statement", e);
        }
    }

    public void executeQuery(String sql) throws ModelException {
        validateStatement();

        try {
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao executar SQL", e);
        }
    }

    public void executeQuery() throws ModelException {
        validatePreparedStatement();

        try {
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao executar SQL", e);
        }
    }

    public boolean next() throws ModelException {
        validateResultSet();

        try {
            return resultSet.next();
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao chamar próximo registro", e);
        }
    }

    public int getInt(String column) throws ModelException {
        try {
            return resultSet.getInt(column);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao chamar getInt", e);
        }
    }

    public String getString(String column) throws ModelException {
        try {
            return resultSet.getString(column);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao chamar getString", e);
        }
    }

    public Date getDate(String column) throws ModelException {
        try {
            return resultSet.getDate(column);
        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao chamar getDate", e);
        }
    }

    // Lê data e hora de campos SQL DATETIME ou TIMESTAMP.
    public java.time.LocalDateTime getDateTime(String column)
            throws ModelException {
        try {
            java.sql.Timestamp timestamp = resultSet.getTimestamp(column);

            if (timestamp == null) {
                return null;
            }

            return timestamp.toLocalDateTime();

        } catch (SQLException e) {
            close();
            throw new ModelException("Erro ao chamar getDateTime", e);
        }
    }

    private void close() throws ModelException {
        try {
            if (resultSet != null) {
                resultSet.close();
                resultSet = null;
            }

            if (preparedStatement != null) {
                preparedStatement.close();
                preparedStatement = null;
            }

            if (statement != null) {
                statement.close();
                statement = null;
            }

            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }

        } catch (SQLException e) {
            throw new ModelException("Erro ao fechar recursos", e);
        }
    }

    private void validatePreparedStatement() throws ModelException {
        if (preparedStatement == null) {
            throw new ModelException(
                "PreparedStatement não foi criado"
            );
        }
    }

    private void validateStatement() throws ModelException {
        if (statement == null) {
            throw new ModelException("Statement não foi criado");
        }
    }

    private void validateResultSet() throws ModelException {
        if (resultSet == null) {
            throw new ModelException("ResultSet não foi criado");
        }
    }
}
