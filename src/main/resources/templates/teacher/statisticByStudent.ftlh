<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div>
        <#list submissions as submission>
            <div class="list-group">
                <a href="/teacher/statistic/${practice.id}/team/${team.id}/student/${submission.user.id}/submission/${submission.id}" class="list-group-item list-group-item-action" style="background: <#if submission.correct>rgba(12,255,27,0.31)
                        <#else>red</#if>">
                    <h6>${submission.user.username}</h6>
                    <h6>${submission.task.name}</h6>
                    ${submission.sendDate}
                </a>
            </div>
        <#else>
            No submissions
        </#list>
    </div>
</@c.page>