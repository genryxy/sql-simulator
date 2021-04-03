<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${task.name}</h5>
    <p class="card-text">${task.text}</p>
    <p class="card-text">DDL script:${task.ddlScript}</p>
    <p class="card-text">${task.correctQuery}</p>
    <p class="card-text">${task.points}</p>
    <p class="card-text">${task.category.name}</p>


    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
        <div class="btn-group mr-2" role="group" aria-label="First group">
            <form action="/teacher/task/${task.id}/edit" method="get">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <button type="submit" class="btn btn-primary ml-2">Edit</button>
            </form>
            <@cr.modal "All student submissions will be deleted" "/teacher/task/${task.id}/remove"/>
        </div>
    </div>
</@c.page>