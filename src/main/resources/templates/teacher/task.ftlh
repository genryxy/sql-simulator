<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-group col-md-6">
        <form method="get" action="/teacher/task/create" class="form-inline">
            <button type="submit" class="btn btn-primary ml-2">Create new Task</button>
        </form>
    </div>
    <#include "../parts/alertMessage.ftlh"/>
    <div>
        <#list tasks as task>
            <div class="list-group">
                <a href="/teacher/task/${task.id}/info" class="list-group-item list-group-item-action" style="margin-top: 1%">
                    <h6>${task.name}</h6>
                    ${task.text}
                </a>
            </div>
        <#else>
            No tasks
        </#list>
    </div>
</@c.page>