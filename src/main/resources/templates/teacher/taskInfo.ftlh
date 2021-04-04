<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${task.name}</h5>
    <p class="card-text">${task.text}</p>

    <div class="jumbotron">
        <h6>DDL script:</h6>
        <p class="card-text">${task.ddlScript}</p>
    </div>

    <div class="jumbotron">
        <h6>Correct query:</h6>
        <p class="card-text">${task.correctQuery}</p>
    </div>

    <p class="card-text">Points: ${task.points}</p>
    <p class="card-text">Category name: ${task.category.name}</p>

    <div class="form-inline" role="group" style="margin-bottom: 3%">
        <form action="/teacher/task/${task.id}/edit" method="get">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary ml-2">Edit</button>
        </form>
        <@cr.modal "All student submissions will be deleted" "/teacher/task/${task.id}/remove"/>
    </div>
</@c.page>