<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    Task list
    <div>
        <#list tasks as task>
            <div class="list-group">
                <a href="/teacher/practice/${practice.id}/task/${task.id}" class="list-group-item list-group-item-action">
                    Task name: ${task.name}
                </a>
            </div>
        <#else>
            No tasks
        </#list>
    </div>
</@c.page>