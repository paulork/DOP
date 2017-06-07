<form>
    <div class="form-group">
        <label for="inputTitulo">Título</label>
        <input type="text" id="inputTitulo" class="form-control" >
    </div>
    <div class="form-group">
        <label for="inputDescricao">Descrição</label>
        <input type="text" id="inputDescricao" class="form-control">
    </div>
    <div class="form-group">
        <label for="inputData">Data</label>
        <input type="date" id="inputData" class="form-control">
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
        dados.data = moment($('#inputData').val()).utc().format();

        $.ajax({
            type: 'POST',
            url: './lembrete/salvar',
            contentType: 'application/json',
            data: JSON.stringify(dados),
            success: function (result) {
                if (result === "erro") {
                    toastr.error('Erro ao salvar lembrete!!', {timeOut: 3000});
                } else {
                    $('#calendarModal').modal('toggle'); 
                    toastr.info('Salvo com sucesso!!', {timeOut: 3000});
                }
            }
        });
    });
</script>