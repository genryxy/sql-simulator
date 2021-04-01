<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${team.name}</h5>

    <form action="/teacher/team/${team.id}/edit" method="get">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary ml-2">Edit</button>
    </form>

    <@cr.modal "message" "/teacher/team/${team.id}/remove"/>
</@c.page>