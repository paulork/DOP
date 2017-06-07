<form>
    <div class="form-group">
        <label for="inputTitulo">Título</label>
        <input type="text" id="inputTitulo" class="form-control">
    </div>
    <div class="form-group">
        <label for="inputDescricao">Descrição</label>
        <input type="text" id="inputDescricao" class="form-control">
    </div>
    <div class="form-group">
        <label for="inputLocal">Local</label>
        <input type="text" id="inputLocal" class="form-control">
    </div>
    <div class="form-group">
        <label for="inputDTIni">Data/Hora Inicial</label>
        <input type="datetime-local" id="inputDTIni" class="form-control">
    </div>
    <div class="form-group">
        <label for="inputDTFim">Data/Hora Final</label>
        <input type="datetime-local" id="inputDTFim" class="form-control">
    </div>
    <br/>
    <button class="btn btn-lg btn-primary btn-block" id="btnSubmit">Salvar</button>
</form>

<script type="text/javascript">
    $('#btnSubmit').click(function (event) {
        event.preventDefault();

        var dados = new Object();
        dados.titulo = $('#inputTitulo').val();
        dados.descricao = $('#inputDescricao').val();
        dados.local = $('#inputLocal').val();
        dados.dataIni = moment($('#inputDTIni').val()).utc().format();
        dados.dataFim = moment($('#inputDTFim').val()).utc().format();

        $.ajax({
            type: 'POST',
            url: './atividade/salvar',
            contentType: 'application/json',
            data: JSON.stringify(dados),
            success: function (result) {
                if (result === "erro") {
                    toastr.error('Erro ao salvar atividade!!', {timeOut: 3000});
                } else {
                    $('#calendarModal').modal('toggle'); 
                    toastr.info('Salvo com sucesso!!', {timeOut: 3000});
                }
            }
        });
    });
</script>