<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form name="task" method="post" action="create" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="authorId" value="${currentUserId}">
                <input type="text" name="name" placeholder="name">
                <input type="text" name="text" placeholder="text">
                <input type="text" name="ddlScript" placeholder="ddlScript">
                <input type="text" name="correctQuery" placeholder="correctQuery">
                <input type="int" name="points" placeholder="points">
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="isPrivate" name="isPrivate">
                    <label class="form-check-label" for="isPrivate">is Private</label>
                </div>
                <select class="form-select form-select-sm" name="category" aria-label=".form-select-sm example">
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
                <button type="submit" class="btn btn-primary ml-2">save</button>
            </form>
        </div>
        <#include "../parts/alertResult.ftlh"/>
    </div>
</@c.page>