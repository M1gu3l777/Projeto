```jsp
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Login - Facebook CRUD</title>

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/css/bootstrap.min.css">

    <style>
        body {
            margin: 0;
            min-height: 100vh;
            background: #f0f2f5;
        }

        .login-card {
            width: 100%;
            max-width: 400px;
            border: none;
            border-radius: 12px;
        }

        .login-title {
            font-weight: 700;
            color: #212529;
        }

        .login-button {
            font-weight: 600;
        }

        .login-footer {
            font-size: 14px;
        }
    </style>
</head>

<body>
    <div class="d-flex flex-column min-vh-100">

        <!-- Navbar -->
        <nav class="navbar navbar-dark bg-dark">
            <div class="container">
                <span class="navbar-brand mb-0 h1">
                    Facebook CRUD
                </span>
            </div>
        </nav>

        <!-- Formulário de login -->
        <main class="flex-grow-1 d-flex justify-content-center align-items-center p-3">

            <div class="card shadow-sm p-4 login-card">

                <h2 class="text-center mb-4 login-title">
                    Entrar
                </h2>

                <p class="text-center text-muted mb-4">
                    Informe seus dados para acessar o sistema.
                </p>

                <form action="${pageContext.request.contextPath}/login"
                      method="post">

                    <div class="mb-3">
                        <label for="user_login_id" class="form-label">
                            E-mail
                        </label>

                        <input
                            type="email"
                            class="form-control"
                            id="user_login_id"
                            name="user_login"
                            placeholder="Digite seu e-mail"
                            autocomplete="username"
                            required>
                    </div>

                    <div class="mb-3">
                        <label for="user_pw_id" class="form-label">
                            Senha
                        </label>

                        <input
                            type="password"
                            class="form-control"
                            id="user_pw_id"
                            name="user_pw"
                            placeholder="Digite sua senha"
                            autocomplete="current-password"
                            required>
                    </div>

                    <c:if test="${param.erro == 'true'}">
                        <div class="alert alert-danger py-2" role="alert">
                            Usuário ou senha inválidos.
                        </div>
                    </c:if>

                    <div class="d-grid mt-4">
                        <button type="submit"
                                class="btn btn-dark btn-lg login-button">
                            Entrar
                        </button>
                    </div>

                </form>
            </div>
        </main>

        <!-- Rodapé -->
        <footer class="bg-dark text-white text-center py-3 mt-auto login-footer">
            <div class="container">
                <jsp:useBean id="date" class="java.util.Date" />
                <fmt:formatDate value="${date}"
                                pattern="yyyy"
                                var="currentYear" />

                <p class="mb-0">
                    &copy; <c:out value="${currentYear}" />
                    Facebook CRUD. Todos os direitos reservados.
                </p>
            </div>
        </footer>

    </div>
</body>
</html>
```