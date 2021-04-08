<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
        <#list resultsList as result>
            <div class="list-group">
                <a href="/teacher/statistic/${practice.id}/team/${team.id}/student/${result.student.user.id}" class="list-group-item list-group-item-action">
                    <h6>${result.student.user.username}</h6>
                    ${result.student.user.email}
                    <h6>Result: ${result.points} / ${totalSum}</h6>
                </a>
            </div>
        <#else>
            No submissions
        </#list>
</@c.page>