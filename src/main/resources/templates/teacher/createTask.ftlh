<#import '../parts/common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <h5>Input Data: </h5>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form name="task" method="post" action="create" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="authorId" value="${currentUserId}">
                <input type="text" name="name" placeholder="name">
                <input type="text" name="text" placeholder="text" >
                <input type="text" name="ddlScript" placeholder="ddlScript" >
                <input type="text" name="correctQuery" placeholder="correctQuery">
                <input type="int" name="points" placeholder="points">
                <input type="hidden" name="isPrivate" value="${false?c}">
                <input type="long" name="categoryId" placeholder="categoryId">
                <button type="submit" class="btn btn-primary ml-2">save</button>
            </form>
        </div>
    </div>

    <#if message??>
        <div class="alert alert-${type}" role="alert">
            ${message}
        </div>
    </#if>
</@c.page>