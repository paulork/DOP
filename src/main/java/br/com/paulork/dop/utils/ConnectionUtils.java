package br.com.paulork.dop.utils;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paulo R. Kraemer <paulork10@gmail.com>
 */
public class ConnectionUtils {

    private static void carregaDriver() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    public static Connection getConnection(String ip, String porta, String banco, String usuario, String senha) throws SQLException, ClassNotFoundException {
        return getConnection(ip, porta, banco, usuario, senha, null);
    }

    public static Connection getConnection(String ip, String porta, String banco, String usuario, String senha, String current_schema) throws SQLException, ClassNotFoundException {
        carregaDriver();
        return DriverManager.getConnection("jdbc:postgresql://" + ip + ":" + porta + "/" + banco + (current_schema != null && !current_schema.isEmpty() ? "?currentSchema=" + current_schema : ""), usuario, senha);
    }

    public static boolean testaConexao(String ip, String porta, String banco, String usuario, String senha) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection(ip, porta, banco, usuario, senha);
        return (connection != null);
    }

    public static boolean deleteSchema(String schema, boolean cascade, Connection conn) throws SQLException {
        boolean ok = false;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DROP SCHEMA " + schema + (cascade ? " CASCADE " : ""));
            ok = true;
        } catch (SQLException ex) {
            System.out.println("Erro ao deletar schema [" + schema + "]. " + ex.getMessage());
            throw new SQLException("Erro ao deletar schema [" + schema + "].", ex);
        }
        return ok;
    }
    
    public static boolean criaSchema(String schema, String owner, Connection conn) throws SQLException {
        boolean ok = false;
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE SCHEMA " + schema + " AUTHORIZATION " + owner);
            ok = true;
        } catch (SQLException ex) {
            System.out.println("Erro ao criar schema [" + schema + "]. " + ex.getMessage());
            throw new SQLException("Erro ao criar schema [" + schema + "].", ex);
        }
        return ok;
    }

    public static boolean criaSchema(String schema, String hostname, String port, String databaseName, String owner, String senha) throws ClassNotFoundException, SQLException {
        Connection conn = getConnection(hostname, port, databaseName, owner, senha);
        return criaSchema(schema, owner, conn);
    }

    public static boolean existeSchema(String schema, Connection conn) {
        boolean existeSchema = false;
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT 1 as existe FROM information_schema.schemata WHERE schema_name = '" + schema + "'")) {
            while (rs.next()) {
                existeSchema = true;
            }
        } catch (SQLException ex) {
            existeSchema = false;
            System.out.println("Erro ao checar schema [" + schema + "]. " + ex.getMessage());
        }
        return existeSchema;
    }
    
    public static boolean existeTabela(String tabela, String schema, Connection conn) {
        boolean tabExists = false;
        try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT to_regclass('"+schema+"."+tabela+"');")) {
            while (rs.next()) {
                tabExists = true;
            }
        } catch (SQLException ex) {
            tabExists = false;
            System.out.println("Erro ao checar tabela [" + tabela + "] no schema ["+schema+"]. " + ex.getMessage());
        }
        return tabExists;
    }

    public static boolean existeSchema(String schema, String hostname, String port, String owner, String senha) throws ClassNotFoundException, SQLException {
        Connection conn = getConnection(hostname, port, "postgres", owner, senha);
        return existeSchema(schema, conn);
    }

    public static boolean criaTabelas(String schema, String hostname, String port, String databaseName, String owner, String senha) throws ClassNotFoundException, SQLException, IOException {
        Connection conn = getConnection(hostname, port, databaseName, owner, senha, schema);
        return criaTabelas(conn);
    }

    public static boolean criaTabelas(String schema_to_set, Connection conn) throws SQLException, IOException {
        conn.setSchema(schema_to_set);
        return criaTabelas(conn);
    }

    public static boolean criaTabelas(Connection conn) throws SQLException, IOException {
        conn.setAutoCommit(false);
        File sql = new File(ConnectionUtils.class.getResource("/commons").getPath(), "schema.sql");
        String arquivo = FileUtils.toText(sql);
        conn.createStatement().executeUpdate(arquivo);
        conn.commit();
        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> List<T> resultSetToObject(ResultSet rs, Class<T> clazz) throws SQLException, InstantiationException, IllegalAccessException {
        List<T> outputList = null;
        try {
            if (rs != null) {
                ResultSetMetaData rsmd = rs.getMetaData();
                Field[] fields = clazz.getDeclaredFields();

                while (rs.next()) {
                    T bean = (T) clazz.newInstance();
                    for (int _iterator = 0; _iterator < rsmd.getColumnCount(); _iterator++) {
                        String columnName = rsmd.getColumnName(_iterator + 1);
                        Object columnValue = rs.getObject(_iterator + 1);

                        for (Field field : fields) {
                            if (field.getName().equalsIgnoreCase(columnNameAjust(columnName)) && columnValue != null) {
                                ConnectionUtils.setProperty(bean, field.getName(), columnValue);
                                break;
                            }
                        }
                    }
                    if (outputList == null) {
                        outputList = new ArrayList<T>();
                    }

                    outputList.add(bean);
                }
            } else {
                return null;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return outputList;
    }

    private static String columnNameAjust(String columnName) {
        if (columnName.contains("_")) {
            return LOWER_UNDERSCORE.to(LOWER_CAMEL, columnName);
        }
        return columnName;
    }

    private static void setProperty(Object clazz, String fieldName, Object columnValue) {
        try {
            Field field = clazz.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(clazz, columnValue);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
