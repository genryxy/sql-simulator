<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    Teams list
    <div class="card-columns">
        <#list teams as team>
            <div class="card my-3">
                <div class="card-body">
                    <h5 class="card-title">${team.name}</h5>
                    <p class="card-text">${team.invitation}</p>
                </div>
                <form action="/teacher/team/assign" method="post">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <input type="hidden" name="practiceId" value="${practiceId}">
                    <input type="hidden" name="teamId" value="${team.id}">
                    <button type="submit" class="btn btn-primary ml-2">Assign</button>
                </form>
            </div>
        <#else>
            No available teams
        </#list>
    </div>

    <div class="form-group col-md-6">
        <form action="/teacher/team/${practiceId}/create" method="get">
            <button type="submit" class="btn btn-primary ml-2">Create new</button>
        </form>
    </div>
</@c.page>