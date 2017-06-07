<nav class="navbar navbar-fixed-top navbar-dark bg-primary">
    <ul class="nav navbar-nav">
        <li class="nav-item active">
            <a id="idInicio" class="nav-link" href="./">Agenda</a>
        </li>
        <li class="nav-item">
            <a id="idCompromissosPassados" class="nav-link">Compromissos passados</a>
        </li>
        <li class="nav-item">
            <a id="idCompromissosFuturos" class="nav-link">Compromissos futuros</a>
        </li>
        <li class="nav-item navbar-right">
            <a id="idSair" class="nav-link" href="./logout">Sair</a>
        </li>
    </ul>
</nav>

<script>
    $(".nav-item").on("click", function () {
        $(".nav").find(".active").removeClass("active");
        $(this).addClass("active");
    });
</script>