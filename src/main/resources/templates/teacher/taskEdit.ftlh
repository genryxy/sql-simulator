<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-row">
        <#include "../parts/alertMessage.ftlh"/>
        <div class="form-group col-md-6">
            <form name="task" method="post" action="/teacher/task/${task.id}/edit" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="authorId" value="${currentUserId}">
                <input type="text" name="name" value="${task.name}" required>
                <input type="text" name="text" value="${task.text}" required>
                <input type="text" name="ddlScript" value="${task.ddlScript}" required>
                <input type="text" name="correctQuery" value="${task.correctQuery}" required>
                <input type="int" name="points" value="${task.points}" required>
                <input type="hidden" name="isPrivate" value="${task.isPrivate?c}">
                <select class="form-select form-select-sm" name="category" aria-label=".form-select-sm example">
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
                <button type="submit" class="btn btn-primary ml-2">save</button>
            </form>
        </div>
    </div>
</@c.page>