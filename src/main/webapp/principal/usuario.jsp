<%-- 
    Document   : principal
    Created on : 5 de jun. de 2022, 11:50:05
    Author     : lucia
--%>

<%@page import="model.Login"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"%>

<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

    <jsp:include page="template/head.jsp" ></jsp:include>

        <body>
            <!-- Pre-loader start -->
        <jsp:include page="template/theme-loader.jsp" ></jsp:include>

            <!-- Pre-loader end -->
            <div id="pcoded" class="pcoded">
                <div class="pcoded-overlay-box"></div>
                <div class="pcoded-container navbar-wrapper">
                <jsp:include page="template/navbar.jsp" ></jsp:include>

                    <div class="pcoded-main-container">
                        <div class="pcoded-wrapper">
                        <jsp:include page="template/navbarmainmenu.jsp" ></jsp:include>

                            <div class="pcoded-content">
                                <!-- Page-header start -->
                            <jsp:include page="template/pageheader.jsp" ></jsp:include>

                                <!-- Page-header end -->
                                <div class="pcoded-inner-content">
                                    <!-- Main-body start -->
                                    <div class="main-body">
                                        <div class="page-wrapper">
                                            <!-- Page-body start -->
                                            <div class="page-body">

                                                <div class="row">
                                                    <div class="col-sm-12">
                                                        <!-- Basic Form Inputs card start -->
                                                        <div class="card">
                                                            <div class="card-block">
                                                                <h4 class="sub-title">Cadastro de Usu�rio</h4>

                                                                <form class="form-material" action="<%= request.getContextPath()%>/ServletUsuarioController" method="post" id="formUser" enctype="multipart/form-data">
                                                                <input type="hidden" name="acao" id="acao" value="" />

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="login" id="login" class="form-control" placeholder="Informe o Login" required="required" maxlength="20" autocomplete="off" value="${modelLogin.login}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Login</label>
                                                                </div>                                        

                                                                <p>Imagem</p>
                                                                <div class="form-group form-default input-group mb-4">
                                                                    <div class="input-group-prepend" >
                                                                        <c:if test="${modelLogin.fotoUser != null
                                                                                      && modelLogin.fotoUser != ''}">
                                                                              <!--Se tem imagem:-->
                                                                              <!--envolvido o objeto img num link (a) para fazer a parte do download da imagem-->
                                                                              <!--Componente href vai sempre po doGet da servlet-->
                                                                              <a href="<%= request.getContextPath()%>/ServletUsuarioController?acao=downloadFoto&login=${modelLogin.login}">
                                                                                  <img id="fotoBase64" alt="Imagem User" src="${modelLogin.fotoUser}" width="100px;" />                                                                        
                                                                              </a>
                                                                        </c:if>
                                                                        <c:if test="${modelLogin.fotoUser == null
                                                                                      || modelLogin.fotoUser == ''}">
                                                                              <!--Se n�o tem imagem:-->
                                                                              <img id="fotoBase64" alt="Imagem User" src="assets/images/User_font_awesome.svg.png" width="100px;" />                                                                        
                                                                        </c:if>
                                                                    </div>
                                                                    <!--Aceitar apenas imagens: >> accept="image/*"-->
                                                                    <input id="fileFoto" name="fileFoto" type="file" accept="image/*" onchange="visualizarImagem('fotoBase64', 'fileFoto');" class="form-control-file" style="margin-top: 15px; margin-left: 5px;"/>
                                                                </div>

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="password" name="senha" id="senha" class="form-control" placeholder="Informe a Senha" required="required" maxlength="20" autocomplete="off" value="${modelLogin.senha}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Senha</label>
                                                                </div>
                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="password" name="confirmSenha" id="confirmSenha" class="form-control" placeholder="Confirma��o da Senha" required="required" maxlength="20" autocomplete="off" value="${modelLogin.confirmSenha}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Confirma��o da Senha</label>
                                                                </div>

                                                                <div class="form-group form-default form-static-label">
                                                                    <select class="form-control" aria-label="Default select example" name="perfil" required="required">
                                                                        <option disabled="disabled">[Selecione o Perfil do Usu�rio]</option>
                                                                        <option value="ADMIN" <%
                                                                            Login loginCheck = (Login) request.getAttribute("modelLogin");

                                                                            if (loginCheck != null
                                                                                    && loginCheck.getPerfil() != null
                                                                                    && loginCheck.getPerfil().equals("ADMIN")) {
                                                                                out.print(" ");
                                                                                out.print("selected=\"selected\"");
                                                                                out.print(" ");
                                                                            }
                                                                                %>>Administrador</option>
                                                                        <option value="SECRETARIA" <%
                                                                            loginCheck = (Login) request.getAttribute("modelLogin");

                                                                            if (loginCheck != null
                                                                                    && loginCheck.getPerfil() != null
                                                                                    && loginCheck.getPerfil().equals("SECRETARIA")) {
                                                                                out.print(" ");
                                                                                out.print("selected=\"selected\"");
                                                                                out.print(" ");
                                                                            }
                                                                                %>>Secret�ria</option>
                                                                        <option value="AUXILIAR" <%
                                                                            loginCheck = (Login) request.getAttribute("modelLogin");

                                                                            if (loginCheck != null
                                                                                    && loginCheck.getPerfil() != null
                                                                                    && loginCheck.getPerfil().equals("AUXILIAR")) {
                                                                                out.print(" ");
                                                                                out.print("selected=\"selected\"");
                                                                                out.print(" ");
                                                                            }
                                                                                %>>Auxiliar</option>
                                                                    </select>    
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Perfil</label>
                                                                </div>

                                                                <div class="form-group form-default form-static-label">
                                                                    <p>Sexo</p>
                                                                    <input type="radio" name="sexo" value="M" class="radio-sexo" required="required" <%
                                                                        loginCheck = (Login) request.getAttribute("modelLogin");

                                                                        if (loginCheck != null
                                                                                && loginCheck.getSexo() != null
                                                                                && loginCheck.getSexo().equals("M")) {
                                                                            out.print(" ");
                                                                            out.print("checked=\"checked\"");
                                                                            out.print(" ");
                                                                        }
                                                                           %>>Masculino</>
                                                                    <input type="radio" name="sexo" value="F" class="radio-sexo" required="required" <%
                                                                        loginCheck = (Login) request.getAttribute("modelLogin");

                                                                        if (loginCheck != null
                                                                                && loginCheck.getSexo() != null
                                                                                && loginCheck.getSexo().equals("F")) {
                                                                            out.print(" ");
                                                                            out.print("checked=\"checked\"");
                                                                            out.print(" ");
                                                                        }
                                                                           %>>Feminino</>
                                                                </div>

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="nome" id="nome" class="form-control" placeholder="Informe o Nome" required="required" maxlength="100" autocomplete="off" value="${modelLogin.nome}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Nome</label>
                                                                </div>                                                                    
                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="email" name="email" id="email" class="form-control" placeholder="Informe o Email" required="required" maxlength="100" autocomplete="off" value="${modelLogin.email}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Email</label>
                                                                </div>

                                                                <div class="form-group form-default form-static-label">
                                                                    <input onblur="pesquisaCep();" type="text" name="cep" id="cep" class="form-control" placeholder="Informe o CEP" required="required" maxlength="12" autocomplete="off" value="${modelLogin.cep}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">CEP</label>
                                                                </div>   

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="logradouro" id="logradouro" class="form-control" placeholder="Informe o Logradouro" required="required" maxlength="100" autocomplete="off" value="${modelLogin.logradouro}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Logradouro</label>
                                                                </div>   

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="bairro" id="bairro" class="form-control" placeholder="Informe o Bairro" maxlength="60" required="required" autocomplete="off" value="${modelLogin.bairro}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Bairro</label>
                                                                </div>   

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="localidade" id="localidade" class="form-control" placeholder="Informe a Localidade" required="required" maxlength="60" autocomplete="off" value="${modelLogin.localidade}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Localidade</label>
                                                                </div>   

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="uf" id="uf" class="form-control" placeholder="Informe o Estado" maxlength="2" required="required" autocomplete="off" value="${modelLogin.uf}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">Estado</label>
                                                                </div>   

                                                                <div class="form-group form-default form-static-label">
                                                                    <input type="text" name="numero" id="numero" class="form-control" placeholder="Informe o N�mero" required="required" maxlength="15" autocomplete="off" value="${modelLogin.numero}">
                                                                    <span class="form-bar"></span>
                                                                    <label class="float-label">N�mero</label>
                                                                </div>           

                                                                <button type="submit" class="btn btn-primary waves-effect waves-light">Salvar</button>
                                                                <button type="submit" class="btn btn-success waves-effect waves-light">Salvar/Novo</button>
                                                                <button type="button" class="btn btn-danger waves-effect waves-light" onclick="deletarComAjax();">Excluir</button>
                                                                <button type="button" class="btn btn-inverse waves-effect waves-light" onclick="limpar();">Limpar</button>
                                                                <button type="button" class="btn btn-info" data-toggle="modal" data-target="#modalPesquisaUsuario" onclick="limpaCamposPesquisa();">Pesquisar</button>
                                                            </form>
                                                        </div>

                                                        <div class="card-footer">
                                                            <span><code id="msg" style="${msg == null or empty msg ? 'display: none;' : ''}">${msg}</code></span>
                                                        </div>

                                                        <div class="card-footer">
                                                            <span><code id="msgListaUser" style="${msgListaUser == null or empty msgListaUser ? 'display: none;' : ''}">${msgListaUser}</code></span>
                                                        </div>

                                                        <!--<div style="height: 500px; overflow: scroll;">-->
                                                        <table class="table" id="tabelaListarUsuarios">
                                                            <thead class="thead-dark">
                                                                <tr>
                                                                    <th scope="col">Login</th>
                                                                    <th scope="col">Nome</th>
                                                                    <th scope="col">Email</th>
                                                                    <th scope="col">Editar</th>
                                                                    <th scope="col">Excluir</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <c:forEach items="${listModelLogin}" var="modelLoginAux">
                                                                    <tr>
                                                                        <td><c:out value="${modelLoginAux.login}"></c:out></td>
                                                                        <td><c:out value="${modelLoginAux.nome}"></c:out></td>
                                                                        <td><c:out value="${modelLoginAux.email}"></c:out></td>
                                                                        <td><button type="button" class="btn btn-info" onclick="selEditar('${modelLoginAux.login}');">Editar</button></td>
                                                                        <td><button type="button" class="btn btn-danger" onclick="deletarComAjaxDiretoLista('${modelLoginAux.login}');">Excluir</button></td>
                                                                    </tr>
                                                                </c:forEach>
                                                            </tbody>
                                                        </table>
                                                        <span id="totalResListaUsuario" style="color: black; font-weight: bold;">${totalResListaUsuario}</span>
                                                        <!--</div>-->
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Page-body end -->
                                        </div>
                                        <div id="styleSelector"> </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Required Jquery -->
            <jsp:include page="template/javascript-file.jsp" ></jsp:include>

            <!--Modal para pesquisa de usuario:-->
            <!-- Modal -->
            <div class="modal fade" id="modalPesquisaUsuario" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog" role="document" style="max-width: 800px;">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Pesquisa de Usu�rio</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="input-group mb-3">
                                <input type="text" id="nomePesquisa" class="form-control" placeholder="Nome do Usu�rio" aria-label="Nome" aria-describedby="basic-addon2">
                                <div class="input-group-append">
                                    <button class="btn btn-success" type="button" onclick="buscarUsuario();">Pesquisar</button>
                                </div>
                            </div>
                            <div style="height: 300px; overflow: scroll;">
                                <table class="table" id="tabelaUsuarioPesquisa">
                                    <thead class="thead-dark">
                                        <tr>
                                            <th scope="col">Login</th>
                                            <th scope="col">Nome</th>
                                            <th scope="col">Email</th>
                                            <th scope="col">Selecionar</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <!--Sera preenchido pelo javascript na m�o sempre as linhas e colunas-->
                                    </tbody>
                                </table>
                            </div>
                            <span id="totalResPesquisa" style="color: black; font-weight: bold;"></span>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>
                        </div>
                    </div>
                </div>
            </div>


            <script type="text/javascript" >
                function pesquisaCep() {
                    var cep = $("#cep").val();

                    //Busca no WebService por jQuery mesmo:
                    if (cep !== null
                            && cep !== '') {
                        //Express�o regular para validar o CEP.
                        var validacep = /^[0-9]{8}$/;

                        //Valida o formato do CEP.
                        if (validacep.test(cep)) {
                            $.getJSON("https://viacep.com.br/ws/" + cep + "/json/?callback=?", function (dados) {
                                if (!("erro" in dados)) {
                                    //Atualiza os campos com os valores da consulta.
                                    if (dados.cep !== null) {
                                        $("#cep").val(dados.cep.toUpperCase());
                                    }

                                    if (dados.logradouro !== null) {
                                        $("#logradouro").val(dados.logradouro.toUpperCase());
                                    }

                                    if (dados.bairro !== null) {
                                        $("#bairro").val(dados.bairro.toUpperCase());
                                    }

                                    if (dados.localidade !== null) {
                                        $("#localidade").val(dados.localidade.toUpperCase());
                                    }

                                    if (dados.uf !== null) {
                                        $("#uf").val(dados.uf.toUpperCase());
                                    }
                                } else {
                                    //CEP pesquisado n�o foi encontrado.
                                    limpa_formul�rio_cep();
                                    alert("CEP n�o encontrado.");
                                }
                            });
                        } else {
                            //cep � inv�lido.
                            limpa_formul�rio_cep();
                            alert("Formato de CEP inv�lido.");
                        }
                    }
                }

                function limpa_formul�rio_cep() {
                    // Limpa valores do formul�rio de cep.
                    $("#logradouro").val("");
                    $("#bairro").val("");
                    $("#localidade").val("");
                    $("#uf").val("");
                }

                function visualizarImagem(fotoBase64, fileFoto) {
                    var preview = document.getElementById(fotoBase64); //Campo Imagem do Html
                    var fileUser = document.getElementById(fileFoto).files[0]; //Campo File do Html 
                    var reader = new FileReader();
                    reader.onloadend = function () {
                        preview.src = reader.result; //Carrega a Foto na tela
                    };

                    if (fileUser) {
                        reader.readAsDataURL(fileUser); //Preview da Imagem
                    } else {
                        preview.src = '';
                    }
                }

                function limpar() {
                    var elementos = document.getElementById("formUser").elements; //Retorno os elementos HTML  dentro do form

                    for (p = 0; p < elementos.length; p++) {
                        if (elementos[p].type !== null
                                && elementos[p].type !== 'radio') {
                            elementos[p].value = '';
                        }
                    }

                    //Limpar a Imagem para a padr�o:
                    if (document.getElementById("fotoBase64") !== null
                            && document.getElementById("fotoBase64") !== '') {
                        document.getElementById("fotoBase64").src = "assets/images/User_font_awesome.svg.png";
                    }
                }

                function limpaCamposPesquisa() {
                    document.getElementById("nomePesquisa").value = "";
                    document.getElementById("totalResPesquisa").textContent = "Total de Registros: 0";
                    //Remove todos os resultados da tabela (pesquisa anteriores)
                    $('#tabelaUsuarioPesquisa > tbody > td').remove();
                    $('#tabelaUsuarioPesquisa > tbody > tr').remove();
                }

                function deletar() {
                    //Aparecer um dialog para o usuario confirmar a exclusao
                    if (confirm('Deseja realmente excluir o usu�rio ?')) {
                        //Trocar para o method get o formul�rio:
                        document.getElementById("formUser").method = 'get';
                        //Atributo acao recebe o valor 'deletar' para trabalhar com ele no metodo doGet dentro do Servlet
                        document.getElementById("acao").value = 'deletar';
                        //Envia o formul�rio
                        document.getElementById("formUser").submit();
                    }
                }

                function deletarComAjax() {
                    //Aparecer um dialog para o usuario confirmar a exclusao
                    if (confirm('Deseja realmente excluir o usu�rio ?')) {
                        //Pega a URLAction do formul�rio para cair no doGet (servlet utilizado)
                        var urlAction = document.getElementById("formUser").action;
                        var idLogin = document.getElementById("login").value;

                        $.ajax({
                            method: "get",
                            url: urlAction,
                            contentType: "application/x-www-form-urlencoded;charset=utf-8",
                            //Parametros fica no data
                            data: "login=" + idLogin + "&acao=deletarajax",
                            success: function (response) {
                                //roda o limpar formulario se tudo ok:
                                limpar();
                                //Adicionando a resposta dentro do componente msg (das mensagens)
                                document.getElementById("msg").textContent = response;
//                                alert(response);
//                                
                                //Redirecionar com ajax carregando a listas de usuarios:
                                window.location.href = urlAction + "?acao=listarUsuarios&msgAuxiliar=" + response;
                            }
                        }).fail(function (xhr, status, errorThrow) {
                            document.getElementById("msg").textContent = xhr.responseText;
//                            alert("Erro ao deletar usu�rio!\n" + xhr.responseText);
                        });
                    }
                }

                function deletarComAjaxDiretoLista(loginSel) {
                    //Aparecer um dialog para o usuario confirmar a exclusao
                    if (confirm('Deseja realmente excluir o usu�rio ?')) {
                        //Pega a URLAction do formul�rio para cair no doGet (servlet utilizado)
                        var urlAction = document.getElementById("formUser").action;

                        $.ajax({
                            method: "get",
                            url: urlAction,
                            contentType: "application/x-www-form-urlencoded;charset=utf-8",
                            //Parametros fica no data
                            data: "login=" + loginSel + "&acao=deletarajax",
                            success: function (response) {
                                //roda o limpar formulario se tudo ok:
                                limpar();

                                document.getElementById("msg").textContent = response;

                                if (response !== null
                                        && response !== "") {
                                    document.getElementById("msg").style.display = 'block';
                                } else {
                                    document.getElementById("msg").style.display = 'none';
                                }
//                                
                                //Redirecionar com ajax carregando a listas de usuarios:
                                window.location.href = urlAction + "?acao=listarUsuarios&msgAuxiliar=" + response;
                            }
                        }).fail(function (xhr, status, errorThrow) {
                            document.getElementById("msg").textContent = xhr.responseText;
                        });
                    }
                }

                function buscarUsuario() {
                    var nomePesquisa = document.getElementById("nomePesquisa").value;

                    if (nomePesquisa !== null
                            && nomePesquisa !== ""
                            && nomePesquisa.trim() !== "") {
                        //Pega a URLAction do formul�rio para cair no doGet (servlet utilizado)
                        var urlAction = document.getElementById("formUser").action;

                        $.ajax({
                            method: "get",
                            url: urlAction,
                            contentType: "application/x-www-form-urlencoded;charset=utf-8",
                            //Parametros fica no data
                            data: "nomePesquisa=" + nomePesquisa + "&acao=consultarajax",
                            success: function (response) {
                                //Remove todos os resultados da tabela (pesquisa anteriores)
                                $('#tabelaUsuarioPesquisa > tbody > td').remove();
                                $('#tabelaUsuarioPesquisa > tbody > tr').remove();

                                if (response !== null
                                        && response !== "") {
                                    try {
                                        var json = JSON.parse(response);

                                        console.info(json);

                                        for (var i = 0; i < json.length; i++) {
                                            $('#tabelaUsuarioPesquisa > tbody').append("<tr>");
                                            $('#tabelaUsuarioPesquisa > tbody').append("<td>" + json[i].login + "</td>");
                                            $('#tabelaUsuarioPesquisa > tbody').append("<td>" + json[i].nome + "</td>");
                                            $('#tabelaUsuarioPesquisa > tbody').append("<td>" + json[i].email + "</td>");
                                            //Na String do button, para passar aspas duplas e n�o dar pal na string, colocar assim \' com isso funciona normalmente?
                                            $('#tabelaUsuarioPesquisa > tbody').append('<td><button type="button" onclick="selEditar(\'' + json[i].login + '\');" class="btn btn-info">Selecionar</button></td>');
                                            $('#tabelaUsuarioPesquisa > tbody').append("</tr>");
                                        }

                                        document.getElementById("totalResPesquisa").textContent = "Total de Registros: " + json.length;
                                    } catch (e) {
                                        document.getElementById("totalResPesquisa").textContent = "Total de Registros: 0";
                                        //Remove todos os resultados da tabela (pesquisa anteriores)
                                        $('#tabelaUsuarioPesquisa > tbody > td').remove();
                                        $('#tabelaUsuarioPesquisa > tbody > tr').remove();

                                        alert(response);
                                    }
                                }
                            }
                        }).fail(function (xhr, status, errorThrow) {
                            document.getElementById("totalResPesquisa").textContent = "Total de Registros: 0";
                            //Remove todos os resultados da tabela (pesquisa anteriores)
                            $('#tabelaUsuarioPesquisa > tbody > td').remove();
                            $('#tabelaUsuarioPesquisa > tbody > tr').remove();

                            alert("Erro ao pesquisar usu�rio!\n" + xhr.responseText);
                        });
                    } else {
                        document.getElementById("totalResPesquisa").textContent = "Total de Registros: 0";
                        //Remove todos os resultados da tabela (pesquisa anteriores)
                        $('#tabelaUsuarioPesquisa > tbody > td').remove();
                        $('#tabelaUsuarioPesquisa > tbody > tr').remove();

                        alert("Digite alguma informa��o para habilitar a pesquisa!");
                    }
                }

                function selEditar(loginSel) {
                    //Pega a URLAction do formul�rio para cair no doGet (servlet utilizado)
                    var urlAction = document.getElementById("formUser").action;

                    //Redirecionar com ajax:
                    window.location.href = urlAction + "?acao=selecionarUsuario&loginSel=" + loginSel;
                }
            </script>
    </body>

</html>

