<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
        <#list students as student>
            <div class="list-group">
                <a href="/teacher/statistic/${practice.id}/team/${team.id}/student/${student.user.id}" class="list-group-item list-group-item-action">
                    <h6>${student.user.username}</h6>
                    ${student.user.email}
                </a>
            </div>
        <#else>
            No submissions
        </#list>
</@c.page>