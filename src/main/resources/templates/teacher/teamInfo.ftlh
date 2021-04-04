<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${team.name}</h5>
    <h5 class="card-title">${team.invitation}</h5>

    <#list team.students as student>
        <div class="card-body">
            <h5 class="card-title">${student.user.username}</h5>
            <p class="card-text">${student.user.email}</p>
            <@cr.modal "Student will be removed" "/teacher/team/${team.id}/remove/${student.user.id}"/>
        </div>
    <#else>
        No students...
    </#list>

    <form action="/teacher/team/${team.id}/edit" method="get">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary ml-2">Edit</button>
    </form>
    <@cr.modal "message" "/teacher/team/${team.id}/remove"/>
</@c.page>