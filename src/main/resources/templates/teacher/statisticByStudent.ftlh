<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    Task list
    <div>
        <#list submissions as submission>
            <div class="list-group">
                <a href="/teacher" class="list-group-item list-group-item-action">
                    ${submission.user.username} \t ${submission.sendDate} \t
                </a>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>