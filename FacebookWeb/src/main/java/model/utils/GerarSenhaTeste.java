
package model.utils;

public class GerarSenhaTeste {

    public static void main(String[] args) {
        String senha = "Teste@123";

        String hashGerado = PasswordEncryptor.hashPassword(senha);

        String hashBanco =
            "a3qKQOtC2bZZOTDApMqZRnkuI1cJvAdMoFbZrvqje2E=";

        System.out.println("Hash gerado: " + hashGerado);
        System.out.println("Hash do banco: " + hashBanco);
        System.out.println(
            "Senhas correspondem: " +
            PasswordEncryptor.checkPassword(senha, hashBanco)
        );
    }
}
