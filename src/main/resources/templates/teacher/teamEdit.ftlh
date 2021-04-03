<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-row">
        <#include "../parts/alertMessage.ftlh"/>
        <div class="form-group col-md-6">
            <form name="task" method="post" action="/teacher/team/${team.id}/edit" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="authorId" value="${currentUserId}">
                <input type="text" name="name" value="${team.name}" required>
                <button type="submit" class="btn btn-primary ml-2">Save</button>
            </form>
        </div>
    </div>
</@c.page>