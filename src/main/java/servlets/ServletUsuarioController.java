/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.DAOUsuarioRepository;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.Login;
import modeldto.GraficoSalarioUsuarioDTO;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import servlets.util.ServletUtil;
import util.ReportUtil;

/**
 *
 * @author lucia
 */
@MultipartConfig
@WebServlet(name = "ServletUsuarioController", urlPatterns = {"/principal/ServletUsuarioController", "/ServletUsuarioController"})
public class ServletUsuarioController extends HttpServlet {

    DAOUsuarioRepository daoUsuarioRepository = new DAOUsuarioRepository();

    ServletUtil servletUtil = new ServletUtil();

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String acao = request.getParameter("acao");
            String msgAuxiliar = request.getParameter("msgAuxiliar");

            if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("deletar")) {
                //Rotina para carregar Lista de Usuarios sempre que abrir a tela de usuario:
                List<Login> listModelLoginPaginada = daoUsuarioRepository.consultarTodosUsuariosPaginada(servletUtil.getUsuarioLogado(request), 0);
                List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuarios(servletUtil.getUsuarioLogado(request));
                //Passa o objeto como parâmetro para tela de volta:
                request.setAttribute("listModelLogin", listModelLoginPaginada);
                request.setAttribute("totalPagina", daoUsuarioRepository.getTotalPaginasConsultaTodosUsuarios(servletUtil.getUsuarioLogado(request)));
                request.setAttribute("paginaAtual", 1L); //Adicinando parametro para trabalhar com os botoes anterior e proximo

                if (listModelLoginGeral != null
                        && !listModelLoginGeral.isEmpty()) {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: " + listModelLoginGeral.size());
                    request.setAttribute("msgListaUser", "Listagem de Usuários");
                } else {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: 0");
                    request.setAttribute("msgListaUser", "Nenhum usuário cadastrado");
                }

                String login = request.getParameter("login");
                String senha = request.getParameter("senha");
                String confirmSenha = request.getParameter("confirmSenha");
                String nome = request.getParameter("nome");
                String email = request.getParameter("email");
                String perfil = request.getParameter("perfil");
                String sexo = request.getParameter("sexo");
                String cep = request.getParameter("cep");
                String logradouro = request.getParameter("logradouro");
                String bairro = request.getParameter("bairro");
                String localidade = request.getParameter("localidade");
                String uf = request.getParameter("uf");
                String numero = request.getParameter("numero");

                if (login != null
                        && !login.isEmpty()) {
                    Login newLogin = new Login();
                    newLogin.setLogin(login);
                    newLogin.setSenha(senha);
                    newLogin.setConfirmSenha(confirmSenha);
                    newLogin.setNome(nome);
                    newLogin.setEmail(email);
                    newLogin.setPerfil(perfil);
                    newLogin.setSexo(sexo);
                    newLogin.setCep(cep);
                    newLogin.setLogradouro(logradouro);
                    newLogin.setBairro(bairro);
                    newLogin.setLocalidade(localidade);
                    newLogin.setUf(uf);
                    newLogin.setNumero(numero);

                    Login consultaLogin = daoUsuarioRepository.consultarUsuario(login, servletUtil.getUsuarioLogado(request));

                    if (consultaLogin != null) {
                        daoUsuarioRepository.deletarUsuario(login);

                        request.setAttribute("msg", "Usuário excluído com sucesso!");

                        //Redireciona
                        RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                        //Passa um novo objeto como parâmetro para tela de volta:
                        request.setAttribute("modelLogin", new Login());
                        redirecionar.forward(request, response);
                    } else {
                        request.setAttribute("msg", "Usuário não existe ainda para ser deletado!");

                        //Redireciona
                        RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                        //Passa o próprio objeto como parâmetro para tela de volta:
                        request.setAttribute("modelLogin", newLogin);
                        redirecionar.forward(request, response);
                    }
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("deletarajax")) {
                //Ajax não atualizar o formulário... nao tem redirecionamento!
                String login = request.getParameter("login");

                if (login != null
                        && !login.isEmpty()) {
                    String strWriter = getWriterMsgDelete(login);
                    Login consultaLogin = daoUsuarioRepository.consultarUsuario(login, servletUtil.getUsuarioLogado(request));

                    if (consultaLogin != null) {
                        daoUsuarioRepository.deletarUsuario(login);

                        //Resposta para o Ajax:
                        response.setContentType("text/html; charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        strWriter = strWriter + " excluído com sucesso!";
                        response.getWriter().write(strWriter);
                    } else {
                        //Resposta para o Ajax:
                        response.setContentType("text/html; charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        strWriter = strWriter + " não existe ainda para ser deletado!";
                        response.getWriter().write(strWriter);
                    }
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("consultarajax")) {
                //Consultar Ajax:
                request.setCharacterEncoding("UTF-8");
                //Ajax não atualizar o formulário... nao tem redirecionamento!
                String nomePesquisa = request.getParameter("nomePesquisa");

                if (nomePesquisa != null
                        && !nomePesquisa.isEmpty()) {
                    List<Login> listLogin = daoUsuarioRepository.consultarUsuarioPorNome(nomePesquisa, servletUtil.getUsuarioLogado(request));
                    Long totalPaginaAjax = daoUsuarioRepository.consultarUsuarioPorNomeTotalPorPagina(nomePesquisa, servletUtil.getUsuarioLogado(request));
                    Long totalRegistrosConsultaNome = daoUsuarioRepository.consultarUsuarioPorNomeTotalRegistros(nomePesquisa, servletUtil.getUsuarioLogado(request));

                    if (listLogin != null
                            && !listLogin.isEmpty()) {
                        //Resposta para o Ajax em json (Transforma lista em String json):
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonResposta = mapper.writeValueAsString(listLogin);

                        response.setContentType("text/html; charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        //Adiciona um parametro no cabeçario para pegar no evento ajax
                        //Passar sempre o addHeader antes do Writer se nao nao chega o parametro no header
                        response.addHeader("totalPaginaAjax", totalPaginaAjax == null ? "1" : String.valueOf(totalPaginaAjax));
                        response.addHeader("paginaAtualAjax", String.valueOf(1)); //Adicinando parametro para trabalhar com os botoes anterior e proximo
                        response.addHeader("totalRegistrosConsultaNome", totalRegistrosConsultaNome == null ? "0" : String.valueOf(totalRegistrosConsultaNome));
                        response.getWriter().write(jsonResposta);
                    } else {
                        //Resposta para o Ajax:
                        response.setContentType("text/html; charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        //Adiciona um parametro no cabeçario para pegar no evento ajax
                        //Passar sempre o addHeader antes do Writer se nao nao chega o parametro no header
                        response.addHeader("totalPaginaAjax", totalPaginaAjax == null ? "1" : String.valueOf(totalPaginaAjax));
                        response.addHeader("paginaAtualAjax", String.valueOf(1)); //Adicinando parametro para trabalhar com os botoes anterior e proximo
                        response.addHeader("totalRegistrosConsultaNome", totalRegistrosConsultaNome == null ? "0" : String.valueOf(totalRegistrosConsultaNome));
                        response.getWriter().write("Nenhum Usuário encontrado!");
                    }
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("consultarajaxPage")) {
                //Consultar Ajax (Paginacao):
                //Ajax não atualizar o formulário... nao tem redirecionamento!
                String nomePesquisa = request.getParameter("nomePesquisa");
                
                Integer offset = 0;
                if(request.getParameter("paginaAjax") != null) {
                    offset = Integer.valueOf(request.getParameter("paginaAjax"));
                }
                
                Long paginaAtualAjax = 0L;
                if(request.getParameter("paginaAtualAjax") != null) {
                    paginaAtualAjax = Long.valueOf(request.getParameter("paginaAtualAjax"));
                }
                
                Long totalRegistrosConsultaNome = daoUsuarioRepository.consultarUsuarioPorNomeTotalRegistros(nomePesquisa, servletUtil.getUsuarioLogado(request));

                if (nomePesquisa != null
                        && !nomePesquisa.isEmpty()) {
                    List<Login> listLogin = daoUsuarioRepository.consultarUsuarioPorNomePaginada(nomePesquisa, servletUtil.getUsuarioLogado(request), offset);
                    Long totalPaginaAjax = daoUsuarioRepository.consultarUsuarioPorNomeTotalPorPagina(nomePesquisa, servletUtil.getUsuarioLogado(request));

                    if (listLogin != null
                            && !listLogin.isEmpty()) {
                        //Resposta para o Ajax em json (Transforma lista em String json):
                        ObjectMapper mapper = new ObjectMapper();
                        String jsonResposta = mapper.writeValueAsString(listLogin);

                        response.setContentType("text/html; charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        //Adiciona um parametro no cabeçario para pegar no evento ajax
                        response.addHeader("totalPaginaAjax", totalPaginaAjax == null ? "1" : String.valueOf(totalPaginaAjax));
                        response.addHeader("paginaAtualAjax", String.valueOf(paginaAtualAjax)); //Adicinando parametro para trabalhar com os botoes anterior e proximo
                        response.addHeader("totalRegistrosConsultaNome", totalRegistrosConsultaNome == null ? "0" : String.valueOf(totalRegistrosConsultaNome));
                        response.getWriter().write(jsonResposta);
                    } else {
                        //Resposta para o Ajax:
                        response.setContentType("text/html; charset=UTF-8");
                        response.setCharacterEncoding("UTF-8");
                        //Adiciona um parametro no cabeçario para pegar no evento ajax
                        response.addHeader("totalPaginaAjax", totalPaginaAjax == null ? "1" : String.valueOf(totalPaginaAjax));
                        response.addHeader("paginaAtualAjax", String.valueOf(paginaAtualAjax)); //Adicinando parametro para trabalhar com os botoes anterior e proximo
                        response.addHeader("totalRegistrosConsultaNome", totalRegistrosConsultaNome == null ? "0" : String.valueOf(totalRegistrosConsultaNome));
                        response.getWriter().write("Nenhum Usuário encontrado!");
                    }
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("selecionarUsuario")) {
                String loginSel = request.getParameter("loginSel");

                if (loginSel != null
                        && !loginSel.isEmpty()) {
                    //Rotina para carregar Lista de Usuarios sempre que abrir a tela de usuario:
                    List<Login> listModelLoginPaginada = daoUsuarioRepository.consultarTodosUsuariosPaginada(servletUtil.getUsuarioLogado(request), 0);
                    List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuarios(servletUtil.getUsuarioLogado(request));
                    //Passa o objeto como parâmetro para tela de volta:
                    request.setAttribute("listModelLogin", listModelLoginPaginada);
                    request.setAttribute("totalPagina", daoUsuarioRepository.getTotalPaginasConsultaTodosUsuarios(servletUtil.getUsuarioLogado(request)));
                    request.setAttribute("paginaAtual", 1L); //Adicinando parametro para trabalhar com os botoes anterior e proximo

                    if (listModelLoginGeral != null
                            && !listModelLoginGeral.isEmpty()) {
                        request.setAttribute("totalResListaUsuario", "Total de Registros: " + listModelLoginGeral.size());
                        request.setAttribute("msgListaUser", "Listagem de Usuários");
                    } else {
                        request.setAttribute("totalResListaUsuario", "Total de Registros: 0");
                        request.setAttribute("msgListaUser", "Nenhum usuário cadastrado");
                    }

                    Login consultaLogin = daoUsuarioRepository.consultarUsuario(loginSel, servletUtil.getUsuarioLogado(request));

                    if (consultaLogin != null) {
                        //Passa o objeto como parâmetro para tela de volta:
                        request.setAttribute("modelLogin", consultaLogin);
                        request.setAttribute("msg", "Alteração de Usuário");

                        //Redireciona
                        RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                        redirecionar.forward(request, response);
                    }
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("listarUsuarios")) {
                //Rotina para carregar Lista de Usuarios sempre que abrir a tela de usuario:
                List<Login> listModelLoginPaginada = daoUsuarioRepository.consultarTodosUsuariosPaginada(servletUtil.getUsuarioLogado(request), 0);
                List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuarios(servletUtil.getUsuarioLogado(request));
                //Passa o objeto como parâmetro para tela de volta:
                request.setAttribute("listModelLogin", listModelLoginPaginada);
                request.setAttribute("totalPagina", daoUsuarioRepository.getTotalPaginasConsultaTodosUsuarios(servletUtil.getUsuarioLogado(request)));
                request.setAttribute("paginaAtual", 1L); //Adicinando parametro para trabalhar com os botoes anterior e proximo

                if (listModelLoginGeral != null
                        && !listModelLoginGeral.isEmpty()) {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: " + listModelLoginGeral.size());
                    request.setAttribute("msgListaUser", "Listagem de Usuários");
                } else {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: 0");
                    request.setAttribute("msgListaUser", "Nenhum usuário cadastrado");
                }

                if (msgAuxiliar != null
                        && !msgAuxiliar.isEmpty()) {
                    request.setAttribute("msg", msgAuxiliar);
                }

                //Redireciona
                RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                redirecionar.forward(request, response);
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("downloadFoto")) {
                //Ajax não atualizar o formulário... nao tem redirecionamento!
                String login = request.getParameter("login");

                if (login != null
                        && !login.isEmpty()) {
                    Login consultaLogin = daoUsuarioRepository.consultarUsuario(login, servletUtil.getUsuarioLogado(request));

                    if (consultaLogin != null
                            && consultaLogin.getFotoUser() != null
                            && !consultaLogin.getFotoUser().isEmpty()
                            && consultaLogin.getExtensaoFotoUser() != null
                            && !consultaLogin.getExtensaoFotoUser().isEmpty()) {
                        //Enviar como resposta um arquivo para download:
                        response.setHeader("Content-Disposition", "attachment;filename=arquivo." + consultaLogin.getExtensaoFotoUser());
                        //Quebrar a String nas virgulas (split\\,) e pega a segunda string quebrada, joga fora a primeira parte (data:image/" + part.getContentType().split("\\/")[1] + ";base64,)
                        //vai pegar so o que vem depois do base64, >>> daqui em diante:
                        response.getOutputStream().write(new Base64().decode(consultaLogin.getFotoUser().split("\\,")[1]));
                    }
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("paginar")) {
                Integer offset = 0;
                if(request.getParameter("pagina") != null) {
                    offset = Integer.valueOf(request.getParameter("pagina"));
                }
                
                Long paginaAtual = 0L;
                if(request.getParameter("paginaAtual") != null) {
                    paginaAtual = Long.valueOf(request.getParameter("paginaAtual"));
                }

//                //Rotina para carregar Lista de Usuarios PAGINADA sempre que abrir a tela de usuario:
                List<Login> listModelLoginPaginada = daoUsuarioRepository.consultarTodosUsuariosPaginada(servletUtil.getUsuarioLogado(request), offset);
                //Executar sem paginacao para saber total de registros.. seria melhor por um count mesmo aqui
                List<Login> listTotalRegistros = daoUsuarioRepository.consultarTodosUsuarios(servletUtil.getUsuarioLogado(request));

                //Passa o objeto como parâmetro para tela de volta:
                request.setAttribute("listModelLogin", listModelLoginPaginada);
                request.setAttribute("totalPagina", daoUsuarioRepository.getTotalPaginasConsultaTodosUsuarios(servletUtil.getUsuarioLogado(request)));
                request.setAttribute("paginaAtual", paginaAtual); //Adicinando parametro para trabalhar com os botoes anterior e proximo

                if (listTotalRegistros != null
                        && !listTotalRegistros.isEmpty()) {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: " + listTotalRegistros.size());
                    request.setAttribute("msgListaUser", "Listagem de Usuários");
                } else {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: 0");
                    request.setAttribute("msgListaUser", "Nenhum usuário cadastrado");
                }

                if (msgAuxiliar != null
                        && !msgAuxiliar.isEmpty()) {
                    request.setAttribute("msg", msgAuxiliar);
                }
//                //Redireciona
                RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                redirecionar.forward(request, response);
            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("consultarRelUser")) {
                String dataInicial = request.getParameter("dataInicial");
                String dataFinal = request.getParameter("dataFinal");

                Boolean validDatas = validDatasConsultaRelUser(request, response, false);

                if (validDatas) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("dataInicial", dataInicial);
                    param.put("dataFinal", dataFinal);

                    List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuariosRel(servletUtil.getUsuarioLogado(request), param);
                    request.setAttribute("listModelLoginGeral", listModelLoginGeral);
                    request.setAttribute("totalResListaUsuario", "Total de Usuários: " + (listModelLoginGeral == null || listModelLoginGeral.isEmpty() ? "0" : listModelLoginGeral.size()));

                    //Redireciona para a mesma página de usuário:
                    request.setAttribute("dataInicial", dataInicial);
                    request.setAttribute("dataFinal", dataFinal);

                    RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/relatorio-usuario.jsp");
                    redirecionar.forward(request, response);
                    request.setAttribute("msg", null);
                } else {
                    request.setAttribute("listModelLoginGeral", null);
                    request.setAttribute("totalResListaUsuario", "Total de Usuários: 0");

                    //Redireciona para a mesma página de usuário:
                    request.setAttribute("dataInicial", dataInicial);
                    request.setAttribute("dataFinal", dataFinal);

                    RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/relatorio-usuario.jsp");
                    redirecionar.forward(request, response);
                }
            } else if (acao != null
                    && !acao.isEmpty()
                    && ((acao.equals("imprimirRelUserPdf")) || (acao.equals("imprimirRelUserExcel")))) {
                String dataInicial = request.getParameter("dataInicial");
                String dataFinal = request.getParameter("dataFinal");

                Boolean validDatas = validDatasConsultaRelUser(request, response, false);

                if (validDatas) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("dataInicial", dataInicial);
                    param.put("dataFinal", dataFinal);
                    param.put("TITULO_RELATORIO", "Usuários");

                    //Gera o byte[] com o relatorio pdf:
                    List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuariosRel(servletUtil.getUsuarioLogado(request), param);

                    byte[] relatorio = null;
                    String extensaoRel = null;
                    if (acao.equals("imprimirRelUserPdf")) {
                        //SubRelatorio:
                        String caminhoSubRelJasper = request.getServletContext().getRealPath("relatorio") + File.separator + "subrelatorio" + File.separator + "sub_report_user.jasper";
                        param.put("SUB_REPORT_TELEFONE", caminhoSubRelJasper);

                        relatorio = new ReportUtil().geraRelatorioPDF(listModelLoginGeral, "rel_usuario", param, request.getServletContext());
                        extensaoRel = "pdf";
                        response.setHeader("Content-Disposition", "attachment;filename=rel_usuario." + extensaoRel);
                    } else if (acao.equals("imprimirRelUserExcel")) {
                        //SubRelatorio:
                        String caminhoSubRelJasper = request.getServletContext().getRealPath("relatorio") + File.separator + "subrelatorio" + File.separator + "sub_report_user_excel.jasper";
                        param.put("SUB_REPORT_TELEFONE", caminhoSubRelJasper);

                        relatorio = new ReportUtil().geraRelatorioExcel(listModelLoginGeral, "rel_usuario_excel", param, request.getServletContext());
                        extensaoRel = "xls";
                        response.setHeader("Content-Disposition", "attachment;filename=rel_usuario_excel." + extensaoRel);
                    }
                    //Como resposta vamos enviar o arquivo byte em pdf ou excell
                    response.getOutputStream().write(relatorio);
                } else {
                    request.setAttribute("listModelLoginGeral", null);
                    request.setAttribute("totalResListaUsuario", "Total de Usuários: 0");

                    //Redireciona para a mesma página de usuário:
                    request.setAttribute("dataInicial", dataInicial);
                    request.setAttribute("dataFinal", dataFinal);

                    RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/relatorio-usuario.jsp");
                    redirecionar.forward(request, response);
                }

            } else if (acao != null
                    && !acao.isEmpty()
                    && acao.equals("graficoMediaSalarialUsuario")) {
                //Grafico Media Salarial Por Tipo Usuario:
                String dataInicial = request.getParameter("dataInicial");
                String dataFinal = request.getParameter("dataFinal");

                Boolean validDatas = validDatasConsultaRelUser(request, response, true);

                if (validDatas) {
                    Map<String, Object> param = new HashMap<>();
                    param.put("dataInicial", dataInicial);
                    param.put("dataFinal", dataFinal);

                    GraficoSalarioUsuarioDTO graficoSalarioUsuarioDto = daoUsuarioRepository.getListGraficoMediaSalarioPorTipo(servletUtil.getUsuarioLogado(request), param);
                    //Resposta para o Ajax em json (Transforma objeto em String json):
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResposta = mapper.writeValueAsString(graficoSalarioUsuarioDto);

                    response.setContentType("text/html; charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    //Adiciona um parametro no cabeçario para pegar no evento ajax
                    response.addHeader("msg", "Grafico gerado com sucesso!");
                    response.getWriter().write(jsonResposta);
                } else {
                    response.setContentType("text/html; charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    //Resposta para o Ajax em json (Transforma objeto em String json):
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonResposta = mapper.writeValueAsString(new GraficoSalarioUsuarioDTO());
                    response.getWriter().write(jsonResposta);
                }
            } else {
                //Rotina para carregar Lista de Usuarios sempre que abrir a tela de usuario:
                List<Login> listModelLoginPaginada = daoUsuarioRepository.consultarTodosUsuariosPaginada(servletUtil.getUsuarioLogado(request), 0);
                List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuarios(servletUtil.getUsuarioLogado(request));
                //Passa o objeto como parâmetro para tela de volta:
                request.setAttribute("listModelLogin", listModelLoginPaginada);
                request.setAttribute("totalPagina", daoUsuarioRepository.getTotalPaginasConsultaTodosUsuarios(servletUtil.getUsuarioLogado(request)));
                request.setAttribute("paginaAtual", 1L); //Adicinando parametro para trabalhar com os botoes anterior e proximo

                if (listModelLoginGeral != null
                        && !listModelLoginGeral.isEmpty()) {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: " + listModelLoginGeral.size());
                    request.setAttribute("msgListaUser", "Listagem de Usuários");
                } else {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: 0");
                    request.setAttribute("msgListaUser", "Nenhum usuário cadastrado");
                }

                //Não é delete nada continua o fluxo normal:
                RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                redirecionar.forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //Redirecionar para uma tela de erro (erro.jsp)
            RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
            request.setAttribute("msg", ex.getMessage());
            redirecionar.forward(request, response);
        }
    }

    private String getWriterMsgDelete(String login) {
        StringBuilder strWriter = new StringBuilder();
        if (login != null) {
            strWriter.append("Usuário ");
            strWriter.append(login);
            strWriter.append(" ");
        }

        return strWriter.toString();
    }

    private Boolean validDatasConsultaRelUser(HttpServletRequest request, HttpServletResponse response, Boolean respostaEmAjax) throws Exception {
        //Valida Datas filter:
        Boolean validDatas = true;

        //Formulario de impressão relatorio de usuário:
        String dataInicial = request.getParameter("dataInicial");
        String dataFinal = request.getParameter("dataFinal");

        if (dataInicial != null
                && !dataInicial.isEmpty()) {
            try {
                //Data Inicial:
                Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial)));
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                validDatas = false;

                if (respostaEmAjax) {
                    response.addHeader("msg", "Data Nascimento Inicial invalida!");
                } else {
                    request.setAttribute("msg", "Data Nascimento Inicial invalida!");
                }
            }
        }

        if (dataFinal != null
                && !dataFinal.isEmpty()) {
            try {
                //Data Final:
                Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal)));
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                validDatas = false;

                if (respostaEmAjax) {
                    response.addHeader("msg", "Data Nascimento Final invalida!");
                } else {
                    request.setAttribute("msg", "Data Nascimento Final invalida!");
                }
            }
        }

        //Valida Período:
        if (validDatas
                && dataInicial != null
                && !dataInicial.isEmpty()
                && dataFinal != null
                && !dataFinal.isEmpty()) {
            Date dateIniApur = Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataInicial)));
            Date dateFimApur = Date.valueOf(new SimpleDateFormat("yyyy-mm-dd").format(new SimpleDateFormat("dd/mm/yyyy").parse(dataFinal)));

            if (dateIniApur.after(dateFimApur)) {
                validDatas = false;

                if (respostaEmAjax) {
                    response.addHeader("msg", "Data Nascimento Final deve ser maior que a Inicial!");
                } else {
                    request.setAttribute("msg", "Data Nascimento Final deve ser maior que a Inicial!");
                }
            }
        }

        return validDatas;
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Request é o que vem da tela
            //Response é o que você vai mandar como resposta
            String login = request.getParameter("login");
            String senha = request.getParameter("senha");
            String confirmSenha = request.getParameter("confirmSenha");
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String perfil = request.getParameter("perfil");
            String sexo = request.getParameter("sexo");
            String cep = request.getParameter("cep");
            String logradouro = request.getParameter("logradouro");
            String bairro = request.getParameter("bairro");
            String localidade = request.getParameter("localidade");
            String uf = request.getParameter("uf");
            String numero = request.getParameter("numero");
            String dataNascimento = request.getParameter("dataNascimento");
            String rendaMensal = request.getParameter("rendaMensal");
            //Quebra em um vetor quando achar espaco (R$ ).. com isso vai pegar so o numero e tirar os pontos e decimal ficar com .
            rendaMensal = rendaMensal != null && !rendaMensal.isEmpty() ? rendaMensal.split("\\ ")[1].replaceAll("\\.", "").replaceAll("\\,", ".") : null;

            //Validação:
            if (login != null
                    && !login.isEmpty()
                    && senha != null
                    && !senha.isEmpty()
                    && nome != null
                    && !nome.isEmpty()
                    && email != null
                    && !email.isEmpty()
                    && confirmSenha != null
                    && !confirmSenha.isEmpty()
                    && perfil != null
                    && !perfil.isEmpty()
                    && sexo != null
                    && !sexo.isEmpty()
                    && cep != null
                    && !cep.isEmpty()
                    && logradouro != null
                    && !logradouro.isEmpty()
                    && bairro != null
                    && !bairro.isEmpty()
                    && localidade != null
                    && !localidade.isEmpty()
                    && uf != null
                    && !uf.isEmpty()
                    && numero != null
                    && !numero.isEmpty()
                    && rendaMensal != null
                    && !rendaMensal.isEmpty()) {
                Login newLogin = new Login();
                newLogin.setLogin(login);
                newLogin.setSenha(senha);
                newLogin.setConfirmSenha(confirmSenha);
                newLogin.setNome(nome);
                newLogin.setEmail(email);
                newLogin.setPerfil(perfil);
                newLogin.setSexo(sexo);
                newLogin.setCep(cep);
                newLogin.setLogradouro(logradouro);
                newLogin.setBairro(bairro);
                newLogin.setLocalidade(localidade);
                newLogin.setUf(uf);
                newLogin.setNumero(numero);
                newLogin.setDataNascimento(dataNascimento != null && !dataNascimento.isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").parse(dataNascimento) : null);
                newLogin.setRendaMensal(Double.parseDouble(rendaMensal));

                Boolean informouFoto = false;

                if (ServletFileUpload.isMultipartContent(request)) {
                    //Pegar Foto da Tela:
                    Part part = request.getPart("fileFoto");
                    if (part != null
                            && part.getSize() > 0) {
                        informouFoto = true;
                        //Converte Imagem para byte:
                        byte[] foto = org.apache.commons.compress.utils.IOUtils.toByteArray(part.getInputStream());
                        //Converte para String base 64:
                        String imagemBase64 = "data:image/" + part.getContentType().split("\\/")[1] + ";base64," + new Base64().encodeBase64String(foto);

                        //Seta informações da foto a gravar:
                        newLogin.setFotoUser(imagemBase64);
                        newLogin.setExtensaoFotoUser(part.getContentType().split("\\/")[1]);
                    }
                }

                Login consultaLogin = daoUsuarioRepository.consultarUsuarioGeralSaberSeInsert(login, servletUtil.getUsuarioLogado(request));

                if (!confirmSenha.equals(senha)) {
                    request.setAttribute("msg", "Senha e confirmação de senha devem ser iguais. Verifique!");
                } else if (consultaLogin != null) {
                    //Login já existente >> Update:
                    daoUsuarioRepository.salvarUsuario(newLogin, true, servletUtil.getUsuarioLogado(request));
                    request.setAttribute("msg", "Usuário atualizado com sucesso!");

                    //Caso der update e não alterou a foto, seta a existente para carregar em tela:
                    if (!informouFoto) {
                        newLogin.setFotoUser(consultaLogin.getFotoUser());
                        newLogin.setExtensaoFotoUser(consultaLogin.getExtensaoFotoUser());
                    }
                } else {
                    //Login Novo >> Insert:
                    daoUsuarioRepository.salvarUsuario(newLogin, false, servletUtil.getUsuarioLogado(request));
                    request.setAttribute("msg", "Usuário Incluído com sucesso!");
                }

                //Rotina para carregar Lista de Usuarios sempre que abrir a tela de usuario:
                List<Login> listModelLoginPaginada = daoUsuarioRepository.consultarTodosUsuariosPaginada(servletUtil.getUsuarioLogado(request), 0);
                List<Login> listModelLoginGeral = daoUsuarioRepository.consultarTodosUsuarios(servletUtil.getUsuarioLogado(request));
                //Passa o objeto como parâmetro para tela de volta:
                request.setAttribute("listModelLogin", listModelLoginPaginada);
                request.setAttribute("totalPagina", daoUsuarioRepository.getTotalPaginasConsultaTodosUsuarios(servletUtil.getUsuarioLogado(request)));
                request.setAttribute("paginaAtual", 1L); //Adicinando parametro para trabalhar com os botoes anterior e proximo

                if (listModelLoginGeral != null
                        && !listModelLoginGeral.isEmpty()) {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: " + listModelLoginGeral.size());
                    request.setAttribute("msgListaUser", "Listagem de Usuários");
                } else {
                    request.setAttribute("totalResListaUsuario", "Total de Registros: 0");
                    request.setAttribute("msgListaUser", "Nenhum usuário cadastrado");
                }

                Login loginCharged = daoUsuarioRepository.consultarUsuario(login, servletUtil.getUsuarioLogado(request));

                if (loginCharged != null) {
                    //Passa o objeto como parâmetro para tela de volta:
                    request.setAttribute("modelLogin", loginCharged);

                    //Redireciona
                    RequestDispatcher redirecionar = request.getRequestDispatcher("/principal/usuario.jsp");
                    redirecionar.forward(request, response);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //Redirecionar para uma tela de erro (erro.jsp)
            RequestDispatcher redirecionar = request.getRequestDispatcher("/erro.jsp");
            request.setAttribute("msg", ex.getMessage());
            redirecionar.forward(request, response);
        }
    }
}
