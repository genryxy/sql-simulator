<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <h5 class="card-title">${task.name}</h5>
    <p class="card-text">${task.text}</p>
    <p class="card-text">DDL script: ${task.ddlScript}</p>
    <p class="card-text">${task.correctQuery}</p>
    <p class="card-text">${task.points}</p>
    <p class="card-text">${task.category.name}</p>

    <form action="practice/{practice}/task/{task}" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary ml-2">Edit</button>
    </form>
</@c.page>