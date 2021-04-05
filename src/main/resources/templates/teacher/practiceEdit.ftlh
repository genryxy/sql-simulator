<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <form name="task" method="post" action="/teacher/practice/${practice.id}/edit">
        <input type="hidden" name="_csrf" value="${_csrf.token}">

        <div class="input-group-prepend" style="margin-top: 1%">
            <div class="input-group-text">Name</div>
            <textarea class="form-control" rows="1" name="name"
                      required>${practice.name}
            </textarea>
        </div>

        <div class="input-group-prepend" style="margin-top: 1%">
            <div class="input-group-text">Description</div>
            <textarea class="form-control" rows="4" name="description"
                      required>${practice.description}
            </textarea>
        </div>

        <input type="hidden" name="authorId" value="${currentUserId}">
        <div class="form-inline" style="margin-top: 1%">
            <#if deadline??>
                <input type="date" name="date" class="form-control" value="${deadline.toLocalDate()}"
                       required>
                <input type="time" name="time" class="form-control" value="${deadline.toLocalTime()}"
                       style="margin-left: 1%" required>
                <input type="checkbox" class="form-check-input" id="exampleCheck1"
                       style="margin-left: 1%" name="sendingAfterDeadLine">
                <label class="form-check-label" for="exampleCheck1">Solving after deadline</label>
            </#if>
            <button type="submit" class="btn btn-primary ml-2" style="margin-left: 1%">Save</button>
        </div>
    </form>
</@c.page>