package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.User;
import model.utils.PasswordEncryptor;

public class MySQLUserDAO implements UserDAO {

    @Override
    public boolean save(User user) throws ModelException {
        DBHandler db = new DBHandler();

        String sql = "INSERT INTO users (nome, sexo, email, senha) "
                   + "VALUES (?, ?, ?, ?)";

        db.prepareStatement(sql);
        db.setString(1, user.getName());
        db.setString(2, user.getGender());
        db.setString(3, user.getEmail());

        String senha = user.getPasswordHash();

        if (senha == null || senha.isBlank()) {
            senha = PasswordEncryptor.hashPassword(user.getPassword());
        }

        db.setString(4, senha);

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean update(User user) throws ModelException {
        String senha = user.getPasswordHash();

        if (senha == null || senha.isBlank()) {
            String senhaInformada = user.getPassword();

            if (senhaInformada != null && !senhaInformada.isBlank()) {
                senha = PasswordEncryptor.hashPassword(senhaInformada);
            } else {
                User usuarioExistente = findById(user.getId());

                if (usuarioExistente == null) {
                    return false;
                }

                senha = usuarioExistente.getPasswordHash();
            }
        }

        DBHandler db = new DBHandler();

        String sql = "UPDATE users SET nome = ?, sexo = ?, "
                   + "email = ?, senha = ? WHERE id = ?";

        db.prepareStatement(sql);
        db.setString(1, user.getName());
        db.setString(2, user.getGender());
        db.setString(3, user.getEmail());
        db.setString(4, senha);
        db.setInt(5, user.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public boolean delete(User user) throws ModelException {
        DBHandler db = new DBHandler();

        String sql = "DELETE FROM users WHERE id = ?";

        db.prepareStatement(sql);
        db.setInt(1, user.getId());

        return db.executeUpdate() > 0;
    }

    @Override
    public List<User> listAll() throws ModelException {
        DBHandler db = new DBHandler();
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users ORDER BY id";

        db.createStatement();
        db.executeQuery(sql);

        while (db.next()) {
            users.add(createUser(db));
        }

        return users;
    }

    @Override
    public User findById(int id) throws ModelException {
        DBHandler db = new DBHandler();

        String sql = "SELECT * FROM users WHERE id = ?";

        db.prepareStatement(sql);
        db.setInt(1, id);
        db.executeQuery();

        User user = null;

        if (db.next()) {
            user = createUser(db);
        }

        return user;
    }

    @Override
    public User findByEmail(String email) throws ModelException {
        DBHandler db = new DBHandler();

        String sql = "SELECT * FROM users WHERE email = ?";

        db.prepareStatement(sql);
        db.setString(1, email);
        db.executeQuery();

        User user = null;

        if (db.next()) {
            user = createUser(db);
        }

        return user;
    }

    private User createUser(DBHandler db) throws ModelException {
        User user = new User(db.getInt("id"));

        user.setName(db.getString("nome"));
        user.setGender(db.getString("sexo"));
        user.setEmail(db.getString("email"));

        // O login utiliza o hash armazenado na coluna senha.
        user.setPassword("");
        user.setPasswordHash(db.getString("senha"));

        return user;
    }
}