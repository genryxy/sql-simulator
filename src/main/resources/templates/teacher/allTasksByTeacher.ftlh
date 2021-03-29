<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    Task list
    <div>
        <#list tasks as task>
            <div class="list-group">
                <a href="/teacher/task/${task}" class="list-group-item list-group-item-action">
                    Task name: ${task.name}
                </a>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>