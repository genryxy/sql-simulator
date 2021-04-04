<#import "common.ftlh" as c>
<#import "commonRemove.ftlh" as cr>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div style="width: 100%">
        <h5>${practice.name}</h5>
        <#if deadLine??>
            <div style="margin-left: 70%"><h5>DeadLine: ${deadLine.toLocalDate()} ${deadLine.toLocalTime()}</h5></div>
        </#if>
        <h7>${practice.description}</h7>
    </div>
    <div class="form-inline" role="group" style="margin-top: 1%">
        <form method="get" action="/teacher/statistic/${practice.id}/">
            <button type="submit" class="btn btn-primary my-2">Statistic</button>
        </form>
        <form method="get" action="/teacher/practice/${practice.id}/edit" style="margin-left: 1%">
            <button type="submit" class="btn btn-primary my-2">Edit</button>
        </form>
        <@cr.modal "All student submissions will be deleted" "/teacher/practice/${practice.id}/remove"/>
    </div>
    <div style="margin-bottom: 3%">
        <#list tasks as task>
            <div class="list-group">
                <a href="/teacher/task/${task.id}/info" class="list-group-item list-group-item-action"
                   style="margin-top: 1%">
                    Task name: ${task.name}
                </a>
            </div>
        <#else>
            No tasks...
        </#list>
    </div>
</@c.page>