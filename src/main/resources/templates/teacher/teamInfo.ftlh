<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${team.name}</h5>
    <h6 class="card-text">${team.invitation}</h6>

    <div class="card-columns">
        <#list team.students as student>
            <div class="card my-3">
                <div class="card-body">
                    <h6 class="card-title">${student.user.username}</h6>
                    <p class="card-text">${student.user.email}</p>
                    <@cr.modal "Student will be removed" "/teacher/team/${team.id}/remove/${student.user.id}"/>
                </div>
            </div>
        <#else>
            No students...
        </#list>
    </div>

    <div class="form-inline" role="group" style="margin-top: 1%">
        <form action="/teacher/team/${team.id}/edit" method="get">
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit" class="btn btn-primary ml-2">Edit</button>
        </form>
        <@cr.modal "message" "/teacher/team/${team.id}/remove"/>
    </div>
</@c.page>