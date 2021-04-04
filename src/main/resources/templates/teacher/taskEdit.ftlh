<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <form name="task" method="post" action="/teacher/task/${task.id}/edit">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="hidden" name="authorId" value="${currentUserId}">
        Name:
        <input type="text" name="name" value="${task.name}" class="form-inline" style="width: 50%" required>

        <div style="margin-top: 1%">Description:</div>
        <input type="text" name="text" value="${task.text}" class="form-inline"
               style="width: 100%" required>

        <div style="margin-top: 1%">DDL Script:</div>
        <input type="text" name="ddlScript" value="${task.ddlScript}" class="form-inline"
               style="width: 100%" required>

        <div style="margin-top: 1%">Correct Query:</div>
        <input type="text" name="correctQuery" value="${task.correctQuery}" class="form-inline"
               style="width: 100%" required>

        <div style="margin-top: 1%">Points:</div>
        <input type="int" name="points" value="${task.points}" required>

        <input type="hidden" name="isPrivate" value="${task.isPrivate?c}">
        <select class="form-select form-select-sm" name="category" aria-label=".form-select-sm example">
            <#list categories as category>
                <option value="${category.id}">${category.name}</option>
            </#list>
        </select>
        <button type="submit" class="btn btn-primary ml-2">Save</button>
    </form>
</@c.page>