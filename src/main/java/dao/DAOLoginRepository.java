/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import connection.SingleConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Login;

/**
 *
 * @author lucia
 */
public class DAOLoginRepository {

    private Connection connection;

    public DAOLoginRepository() {
        connection = SingleConnection.getConnection();
    }

    public Boolean getValidAutenticacao(Login login) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM public.login WHERE login = ? and senha = ?;");

        if (login != null
                && login.getLogin() != null
                && login.getSenha() != null) {
            try ( PreparedStatement pstaSel = connection.prepareStatement(sql.toString());) {
                pstaSel.setString(1, login.getLogin());
                pstaSel.setString(2, login.getSenha());

                try ( ResultSet rsSel = pstaSel.executeQuery();) {
                    if (rsSel.next()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public Login consultarUsuario(String login) throws Exception {
        Login modelLogin = null;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM public.login WHERE login = ?;");

        if (login != null) {
            try ( PreparedStatement pstaSel = connection.prepareStatement(sql.toString());) {
                pstaSel.setString(1, login);

                try ( ResultSet rsSel = pstaSel.executeQuery();) {
                    if (rsSel.next()) {
                        modelLogin = new Login();
                        modelLogin.setLogin(rsSel.getString("login"));
                        modelLogin.setSenha(rsSel.getString("senha"));
                        modelLogin.setConfirmSenha(rsSel.getString("senha"));
                        modelLogin.setEmail(rsSel.getString("email"));
                        modelLogin.setAdmin(rsSel.getBoolean("admin"));
                        modelLogin.setNome(rsSel.getString("nome"));
                        modelLogin.setUsuarioLogin(rsSel.getString("usuario_login"));
                        modelLogin.setPerfil(rsSel.getString("perfil"));
                        modelLogin.setSexo(rsSel.getString("sexo"));
                        modelLogin.setFotoUser(rsSel.getString("foto_user"));
                        modelLogin.setExtensaoFotoUser(rsSel.getString("extensao_foto_user"));
                        modelLogin.setCep(rsSel.getString("cep"));
                        modelLogin.setLogradouro(rsSel.getString("logradouro"));
                        modelLogin.setBairro(rsSel.getString("bairro"));
                        modelLogin.setLocalidade(rsSel.getString("localidade"));
                        modelLogin.setUf(rsSel.getString("uf"));
                        modelLogin.setNumero(rsSel.getString("numero"));
                        modelLogin.setDataNascimento(rsSel.getObject("data_nascimento") != null ? rsSel.getDate("data_nascimento") : null);
                        modelLogin.setRendaMensal(rsSel.getDouble("renda_mensal"));
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();

                if (connection != null) {
                    try {
                        connection.rollback();
                    } catch (SQLException ex1) {
                        ex1.printStackTrace();
                    }
                }

                throw new Exception(ex.getMessage());
            }
        } else {
            throw new Exception("Informe um Login para ser consultado!");
        }

        return modelLogin;
    }

}
