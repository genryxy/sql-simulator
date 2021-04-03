<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <h5>Input Data: </h5>
    <div class="input-group">
        <#include "../parts/alertMessage.ftlh"/>
        <form name="task" method="post" action="create" style="width: 100%" >
            <div class="row">
                <input type="hidden" name="_csrf" value="${_csrf.token}" style="margin-left: 10%">
                <input type="hidden" name="authorId" value="${currentUserId}" style="margin-left: 10%">
                <input name="text" placeholder="Name" required style="margin-left: 10%">
                <input type="int" name="Points" placeholder="Points" required style="margin-left: 10%">
                <select class="form-select form-select-sm" name="category" aria-label=".form-select-sm example"
                        class="form-control" style="margin-left: 10%">
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
                <div class="form-check">
                    <input type="checkbox" class="form-check-input" id="isPrivate" name="isPrivate">
                    <label class="form-check-label" for="isPrivate">is Private</label>
                </div>
            </div>
            <textarea class="form-control" rows="2" name="text" placeholder="Description" style="margin-top: 1%" required></textarea>
            <textarea class="form-control" rows="8" name="ddlScript" placeholder="DDL Script" style="margin-top: 1%" required></textarea>
            <textarea class="form-control" rows="8" name="correctQuery" placeholder="Correct query" style="margin-top: 1%" required></textarea>
            <button type="submit" class="btn btn-primary ml-2" style="margin-top: 1%;">Save</button>
        </form>
    </div>
</@c.page>