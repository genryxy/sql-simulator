<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5>Creating new Practice </h5>
    <form name="task" method="post" action="/teacher/practice/create">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <textarea rows="1" name="name" placeholder="Name"
                  style="width: 30%"
                  required></textarea>
        <textarea class="form-control" rows="2" name="description" placeholder="Description" style="margin-top: 1%"
                  required></textarea>
        <input type="hidden" name="author" value="${currentUserId}">
        <h6 style="margin-top: 2%">Choose tasks</h6>
        <div>
            <#list tasks as task>
                <div class="list-group" style="margin-left: 3%; margin-top: 1%">
                    <a href="/teacher/task/${task.id}/info" class="list-group-item list-group-item-action" style="margin-left: 40px">
                        Task name: ${task.name}
                    </a>
                    <input class="form-check-input" type="checkbox" name="checkBox${task.id}" style="width: 40px; height: 40px;">
                </div>
            <#else>
                No messages
            </#list>
        </div>
        <button type="submit" class="btn btn-primary ml-2" style="margin-top: 2%; margin-bottom: 2%">save</button>
    </form>
</@c.page>