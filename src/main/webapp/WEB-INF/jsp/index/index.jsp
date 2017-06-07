<!DOCTYPE html>
<html>

    <jsp:include page="<%="../common/header.jsp"%>"/>

    <body>
        <jsp:include page="<%="../common/navbar.jsp"%>"/>

        <div class="container">
            <div id="content" class="starter-template">
                <!--<div class="col-md-1"></div>-->
                <div id="calendar"></div>
                <!--<div class="col-xs-1"></div>-->
            </div>
        </div>

        <div id="calendarModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span> <span class="sr-only">fechar</span></button>
                        <h4 id="title" class="modal-title"></h4>
                    </div>
                    <div id="modalBody" class="modal-body">
                        <div id="start"></div>
                        <div id="end"></div>
                        <div id="description">
                            <button type="button" class="btn btn-default" id="btnCadAtividade">Cadastrar atividade</button>
                             ou 
                            <button type="button" class="btn btn-default" id="btnCadLembrete">Cadastrar lembrete</button>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
                    </div>
                </div>
            </div>
        </div>
        
    </body>

    <script>
        $(document).ready(function () {
            $('#calendar').fullCalendar({
                header: {
                    left: 'prev,next,today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                selectable: true,
                select: function (start, end, allDay) {
                },
                defaultView: 'month',
                events: function(start, end, timezone, callback) {
                    $.ajax({
                        url: './agenda/buscar',
                        data: {
                            start: start.utc().format(),
                            end: end.utc().format()
                        },
                        success: function(doc) {
                            callback(doc);
                        }
                    });
                },
                eventClick: function (event) {
                    $('#title').html(event.title);
                    if(event.allDay !== true){
                        $('#start').html('Inicio: '+event.start.format('DD/MM/YYYY - HH:mm'));
                        $('#end').html('Término: '+event.end.format('DD/MM/YYYY - HH:mm'));
                    } else {
                        $('#start').html('Data: '+event.start.format('DD/MM/YYYY')+' (O dia inteiro)');
                    }
                    $('#description').html('Descrição: '+event.description);
                    $('#calendarModal').modal();
                },
                dayClick: function (date, jsEvent, view) {
                    $('#title').html('Cadastrar compromisso');
                    $('#calendarModal').modal();
                },
                eventAfterRender: function (event, element, view) {
                    if (event.allDay === true) {
                        element.css('background-color', '#FFB347');
                    } else {
                        element.css('background-color', '#77DD77');
                    }
                }
            });
        });
        
        $('#calendarModal').on('hidden.bs.modal', function () {
            location.reload();
        });

        $('#idCompromissosPassados').click(function (event) {
            sendAjax('./agenda/listar-passados');
        });
        
        $('#idCompromissosFuturos').click(function (event) {
            sendAjax('./agenda/listar-futuros');
        });
        
        $('#btnCadAtividade').click(function (event) {
            $('#title').html('Cadastrar atividade');
            $.ajax({
                url: './atividade/form',
                success: function (result) {
                    $('#description').html(result);
                }
            });
        });
        
        $('#btnCadLembrete').click(function (event) {
            $('#title').html('Cadastrar lembrete');
            $.ajax({
                url: './lembrete/form',
                success: function (result) {
                    $('#description').html(result);
                }
            });
        });
    </script>

</html>