<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-group col-md-6">
        <form method="get" action="/teacher/task/create" class="form-inline">
            <button type="submit" class="btn btn-primary ml-2">Add new Task</button>
        </form>
    </div>
    <div>
        <#list tasks as task>
            <div class="list-group">
                <a href="/teacher/task/${task.id}" class="list-group-item list-group-item-action">
                    Task name: ${task.name} ${task.authorId}
                </a>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>