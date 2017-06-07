<div class="row">
    <div class="col-xs-10">
        <form>
            <div class="col-xs-12">
                <div class="form-group">
                    <label for="inputID">ID</label>
                    <input type="number" id="inputID" class="form-control" disabled="true" value="${produto.id}">
                </div>
                <div class="form-group">
                    <label for="inputDescr">Descrição</label>
                    <input type="text" id="inputDescr" class="form-control" value="${produto.nome}">
                </div>
                <div class="form-group">
                    <label for="inputValor">Valor</label>
                    <input type="number" id="inputValor" class="form-control" value="${produto.valor}">
                </div>
                <div class="form-group">
                    <label for="inputQnt">Quantidade</label>
                    <input type="number" id="inputQnt" class="form-control" value="${produto.quantidade}">
                </div>
            </div>
            <br/>
            <div class="col-xs-12">
                <div class="form-group">
                    <button class="btn btn-lg btn-primary btn-block" id="btnSubmit">Salvar</button>
                </div>
            </div>
        </form>
    </div>
</div>
                
<script type="text/javascript">
    $('#btnSubmit').click(function (event) {
        event.preventDefault();

        var dados = new Object();
        dados.id = $('#inputID').val();
        dados.nome = $('#inputDescr').val();
        dados.valor = $('#inputValor').val();
        dados.quantidade = $('#inputQnt').val();

        $.ajax({
            type: 'POST',
            url: './produto/salvar',
            contentType: 'application/json',
            data: JSON.stringify(dados),
            success: function (result) {
                if (result === "erro") {
                    toastr.error('Erro ao salvar produto!!', {timeOut: 3000});
                } else {
                    $('#content').html(result);
                }
            }
        });
    });
</script>