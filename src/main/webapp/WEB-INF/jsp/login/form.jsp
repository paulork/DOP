<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <link href="./assets/css/bootstrap.min.css" rel="stylesheet">
        <link href="./assets/css/login.css" rel="stylesheet">
        <script src="./assets/js/jquery-3.1.0.min.js"></script>
        <link href="./assets/css/toastr.min.css" rel="stylesheet"/>
        <script src="./assets/js/toastr.min.js"></script>
    </head>
    
    <body>
        <div class="container form-group">

            <form class="form-signin" id="frmLogin">
                <h2 class="form-signin-heading">Por favor, faça login</h2>
                <p></p>
                <label for="inputEmail" class="sr-only">Email</label>
                <input type="text" id="inputEmail" name="usuario" class="form-control" placeholder="paulo" autofocus>
                <p></p>
                <label for="inputPassword" class="sr-only">Senha</label>
                <input type="password" id="inputPassword" name="senha" class="form-control" placeholder="12">
                <p></p>
                <button class="btn btn-lg btn-primary btn-block" id="btnSubmit">Entrar</button>
            </form>

        </div>
    </body>
    
    <script type="text/javascript">
        $('#btnSubmit').click(function (event) {
            event.preventDefault();
            $.ajax({
                type: "POST",
                data: $('#frmLogin').serialize(),
                url: './login/check',
                success: function (result) {
                    toastr.options.preventDuplicates = true;
                    if (result === 'ok') {
                        toastr.success('Login efetuado! Redirecionanto...', {timeOut: 1500});
                        setTimeout(redirect, 1500);
                    } else if (result === 'erro') {
                        toastr.error('Login ou senha inválidos!', {timeOut: 3000});
                    }
                }
            });
        });
        
        function redirect () {
              window.location.href = "./";
        }
    </script>
</html>

