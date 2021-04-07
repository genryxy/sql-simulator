<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <form name="task" method="post" action="/teacher/task/${task.id}/edit">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <input type="hidden" name="authorId" value="${currentUserId}">
        <div class="input-group-prepend">
            <div class="input-group-text">Name</div>
            <textarea rows="1" name="name"
                      style="width: 30%"
                      required>${task.name}
            </textarea>
        </div>

        <div class="input-group-prepend" style="margin-top: 1%">
            <div class="input-group-text">Description</div>
            <textarea class="form-control" rows="2" name="text"
                      required>${task.text}
            </textarea>
        </div>

        <div class="input-group-prepend" style="margin-top: 1%">
            <div class="input-group-text">DDL Script</div>
            <textarea class="form-control" rows="8" name="ddlScript"
                      required>${task.ddlScript}
            </textarea>
        </div>

        <div class="input-group-prepend" style="margin-top: 1%">
            <div class="input-group-text">Correct query</div>
            <textarea class="form-control" rows="8" name="correctQuery"
                      required>${task.correctQuery}
            </textarea>
        </div>

        <div class="input-group-prepend" style="margin-top: 1%">
            <div class="input-group-text">Points</div>
            <input type="int" name="points" value="${task.points}" style="width: 10%"
                   onkeypress='return charCode >= 48 && charCode <= 57'>
        </div>

        <input type="hidden" name="isPrivate" value="${task.isPrivate?c}" style="margin-left: 1%;">
        <select class="form-select form-select-sm" name="category" aria-label=".form-select-sm example" style="margin-top: 1%;">
            <#list categories as category>
                <option value="${category.id}">${category.name}</option>
            </#list>
        </select>

        <button type="submit" class="btn btn-primary ml-2" style="margin: 1%" >Save</button>
    </form>
</@c.page>