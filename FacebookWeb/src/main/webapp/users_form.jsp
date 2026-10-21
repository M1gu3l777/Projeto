
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cadastro de usuário - FacebookWeb</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">

    <style>
        body {
            background: #f3f5f9;
        }

        .form-container {
            max-width: 650px;
            margin: 50px auto;
            padding: 30px;
            background: white;
            border-radius: 14px;
            box-shadow: 0 4px 18px rgba(0, 0, 0, .08);
        }
    </style>
</head>

<body>
<div class="container">
    <div class="form-container">

        <h2 class="mb-4">
            <c:choose>
                <c:when test="${not empty user}">
                    Editar usuário
                </c:when>
                <c:otherwise>
                    Cadastrar usuário
                </c:otherwise>
            </c:choose>
        </h2>

        <form action="${pageContext.request.contextPath}/users/save"
              method="post">

            <input type="hidden"
                   name="user_id"
                   value="${user.id}">

            <div class="mb-3">
                <label for="user_name" class="form-label">Nome</label>
                <input type="text"
                       class="form-control"
                       id="user_name"
                       name="user_name"
                       value="<c:out value='${user.name}'/>"
                       required>
            </div>

            <div class="mb-3">
                <label for="user_gender" class="form-label">Sexo</label>
                <select class="form-select"
                        id="user_gender"
                        name="user_gender"
                        required>

                    <option value="">Selecione</option>

                    <option value="M"
                        <c:if test="${user.gender == 'M'}">selected</c:if>>
                        Masculino
                    </option>

                    <option value="F"
                        <c:if test="${user.gender == 'F'}">selected</c:if>>
                        Feminino
                    </option>
                </select>
            </div>

            <div class="mb-3">
                <label for="user_email" class="form-label">E-mail</label>
                <input type="email"
                       class="form-control"
                       id="user_email"
                       name="user_email"
                       value="<c:out value='${user.email}'/>"
                       required>
            </div>

            <div class="mb-4">
                <label for="user_pw" class="form-label">Senha</label>
                <input type="password"
                       class="form-control"
                       id="user_pw"
                       name="user_pw"
                       autocomplete="new-password"
                       <c:if test="${empty user}">required</c:if>>

                <c:if test="${not empty user}">
                    <div class="form-text">
                        Deixe em branco para manter a senha atual.
                    </div>
                </c:if>
            </div>

            <button type="submit" class="btn btn-primary">
                Salvar
            </button>

            <a href="${pageContext.request.contextPath}/users"
               class="btn btn-secondary">
                Cancelar
            </a>
        </form>

    </div>
</div>
</body>
</html>
