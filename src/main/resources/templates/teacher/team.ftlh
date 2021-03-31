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
    </div>
</@c.page>