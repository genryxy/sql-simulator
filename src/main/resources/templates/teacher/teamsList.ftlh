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
            </div>
        <#else>
            No available teams
        </#list>
    </div>

    <div class="form-group col-md-6">
        <form method="get" action="/teacher/team/${practiceId}/notIncludedTeams" class="form-inline">
            <button type="submit" class="btn btn-primary ml-2">Add Teams</button>
        </form>
    </div>
</@c.page>