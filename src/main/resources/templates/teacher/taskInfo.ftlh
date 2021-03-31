<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${task.name}</h5>
    <p class="card-text">${task.text}</p>
    <p class="card-text">DDL script: ${task.ddlScript}</p>
    <p class="card-text">${task.correctQuery}</p>
    <p class="card-text">${task.points}</p>
    <p class="card-text">${task.category.name}</p>

    <form action="/teacher/task/${task.id}/edit" method="get">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary ml-2">Edit</button>
    </form>
</@c.page>