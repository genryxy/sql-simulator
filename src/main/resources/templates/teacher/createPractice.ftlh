<#import '../parts/common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <h5>Create new Practice: </h5>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form name="task" method="post" action="/teacher/practice/create" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="text" name="name" placeholder="name">
                <input type="text" name="description" placeholder="description">
                <input type="hidden" name="authorId" value="${currentUserId}">
                Task list
                <div>
                    <#list tasks as task>
                        <div class="list-group">
                            <a href="/practice/task/${task.id}" class="list-group-item list-group-item-action">
                                Task name: ${task.name}
                            </a>
                            <input class="form-check-input" type="checkbox" name="checkBox${task.id}">
                        </div>
                    <#else>
                        No messages
                    </#list>
                </div>
                <button type="submit" class="btn btn-primary ml-2">save</button>
            </form>
        </div>
    </div>

    <div class="form-group col-md-6">
        <form method="get" action="/teacher/task/create" class="form-inline">
            <button type="submit" class="btn btn-primary ml-2">Add new Task</button>
        </form>
    </div>
</@c.page>