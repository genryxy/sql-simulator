<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-group col-md-6">
        <form action="/teacher/team/create" method="get">
            <button type="submit" class="btn btn-primary ml-2">Create new Team</button>
        </form>
    </div>
    <div>
        <#list teams as team>
            <div class="list-group">
                <a href="/teacher/team/${team.id}/info" class="list-group-item list-group-item-action" style="margin-top: 1%">
                    <h6>${team.name}</h6> ${team.invitation}
                </a>
            </div>
        <#else>
            No team
        </#list>
    </div>
</@c.page>