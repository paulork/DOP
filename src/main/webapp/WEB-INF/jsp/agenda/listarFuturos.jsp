<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
    <c:when test="${atividades != null && atividades.size() > 0}">
        <table class="table table-striped table-hover table-bordered" id="tblListagem">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Descrição</th>
                    <th>Data Inicial</th>
                    <th>Data Final</th>
                    <th>Ação</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${atividades}" var="ativ">
                    <tr>
                        <td>${ativ.id}</td>
                        <td>${ativ.titulo}</td>
                        <td>${ativ.descricao}</td>
                        <td><fmt:formatDate pattern = "dd-MM-yyyy - HH:mm" value = "${ativ.dataIni}" /></td>
                        <td><fmt:formatDate pattern = "dd-MM-yyyy - HH:mm" value = "${ativ.dataFim}" /></td>
                        <td>
                            <button class="btn btn-sm btn-danger" id="btnDeletar" onclick="deletar('atividade',${ativ.id})">Excluir</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info" role="alert">Sem atividades!</div>
    </c:otherwise>
</c:choose>
        
        <br/>
        <br/>
        
<c:choose>
    <c:when test="${lembretes != null && lembretes.size() > 0}">
        <table class="table table-striped table-hover table-bordered" id="tblListagem">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Descrição</th>
                    <th>Data</th>
                    <th>Ação</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${lembretes}" var="lemb">
                    <tr>
                        <td>${lemb.id}</td>
                        <td>${lemb.titulo}</td>
                        <td>${lemb.descricao}</td>
                        <td><fmt:formatDate pattern = "dd-MM-yyyy" value = "${lemb.data}" /></td>
                        <td>
                            <button class="btn btn-sm btn-danger" id="btnDeletar" onclick="deletar('lembrete',${lemb.id})">Excluir</button>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="alert alert-info" role="alert">Sem lembretes!</div>
    </c:otherwise>
</c:choose>
        
<script>
    function deletar(ref, id){
        event.preventDefault();
        $.ajax({
            type: "DELETE",
            url: './'+ref+'/deletar/'+id,
            success: function (result) {
                console.log(result);
                if (result === 'ok') {
                    sendAjax('./agenda/listar');
                } else if (result === 'erro') {
                    toastr.error('Falha ao excluir!', {timeOut: 3000});
                }
            }
        });
    }
</script>

