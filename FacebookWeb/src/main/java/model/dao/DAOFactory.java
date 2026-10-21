
package model.dao;

import java.util.HashMap;
import java.util.Map;

public class DAOFactory {

    private static final Map<Class<?>, Object> listDAOsInterfaces =
            new HashMap<>();

    private DAOFactory() {
    }

    static {
        listDAOsInterfaces.put(PostDAO.class, new MySQLPostDAO());
        listDAOsInterfaces.put(UserDAO.class, new MySQLUserDAO());
        listDAOsInterfaces.put(SponsorDAO.class, new MySQLSponsorDAO());
        listDAOsInterfaces.put(CommentDAO.class, new MySQLCommentDAO());
    }

    @SuppressWarnings("unchecked")
    public static <T> T createDAO(Class<?> entity) {
        Object dao = listDAOsInterfaces.get(entity);

        if (dao == null) {
            throw new IllegalArgumentException(
                    "DAO não registrado: " + entity.getName());
        }

        return (T) dao;
    }
}
