/* global blockTimeout, onUnblockFunction, blockMessage */

$(function() {
    $.ajaxSetup({
        statusCode: {
            403: function(param){ // FORBIDDEN - sessão expirada
                bloqueiaTelaTimeout(2300, param.responseText, null);
            },
            404: function(param){ // Página não encontrada
                toastr.warning('Recurso não encontrado!', {timeOut: 3500});
//                setTimeout(redirectToIndex, 2500);
            },
            500: function(param){ // Erro interno do servidor
                console.log(param);
                toastr.error('Erro interno do servidor!', {timeOut: 3500});
            }
        }
    });
});


function bloqueiaTelaTimeout(blockTimeout, blockMessage, onUnblockFunction){
    $.blockUI({
        message: blockMessage,
        css: {
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        }
    }); 

    setTimeout(function() {
        $.unblockUI({
            onUnblock: onUnblockFunction !== null ? onUnblockFunction : function(){redirectToLogin();} 
        }); 
    }, blockTimeout); 
};

function bloqueiaTelaSimples(mensagem){
    $.blockUI({
        message: mensagem,
        css: {
            border: 'none', 
            padding: '15px', 
            backgroundColor: '#000', 
            '-webkit-border-radius': '10px', 
            '-moz-border-radius': '10px', 
            opacity: .5, 
            color: '#fff' 
        }
    });
}

function desbloqueiaTela(){
    $.unblockUI();
}

function redirectToLogin() {
    window.location.href = "./login";
};

function redirectToIndex() {
    window.location.href = "./";
};

function getModalHtml(){
    return $('\
    <div class="modal fade" id="prodInfoModal">'+
        '<div class="modal-dialog" role="document">'+
            '<div class="modal-content">'+
                '<div class="modal-header">'+
                    '<h4 class="modal-title"></h4>'+
                    '<button type="button" class="close" data-dismiss="modal" aria-label="Fechar">'+
                        '<span aria-hidden="true">&times;</span>'+
                    '</button>'+
                '</div>'+
                '<div class="modal-body">'+
                '</div>'+
                '<div class="modal-footer">'+
                    '<button type="button" class="btn btn-secondary" data-dismiss="modal">Fechar</button>'+
                '</div>'+
            '</div>'+
        '</div>'+
    '</div>');
}

function showModal(titulo, conteudo) {
    var modal = getModalHtml();
    modal.find('.modal-title').text(titulo);
    modal.find('.modal-body').html(conteudo);
    modal.modal('show');
}

function showModalAjax(titulo, url, data){
    $.ajax({
        url: url,
        data: data,
        headers: {
            'Ajax': true
        },
        success: function (result) {
            var modal = getModalHtml();
            modal.find('.modal-title').text(titulo);
            modal.find('.modal-body').html(result);
            modal.modal('show');
        }
    });
};

function sendAjax(url, funcao, data){
    $.ajax({
        url: url,
        data: data,
        headers: {
            'Ajax': true
        },
        success: ((funcao !== null && typeof funcao !== 'undefined') ? funcao : function (result) {$('#content').html(result);})
    });
}
