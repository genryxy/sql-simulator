<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <h5>Creating new Task</h5>
    <div class="input-group">
        <#include "../parts/alertMessage.ftlh"/>
        <form name="task" method="post" action="create" style="width: 100%">
            <div class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="author" value="${currentUserId}">
                <textarea rows="1" name="name" placeholder="Name"
                          style="width: 30%"
                          required></textarea>
                <input type="number" name="Points" placeholder="points" required style="margin-left: 1%; width: 10%"
                       onkeypress='return charCode >= 48 && charCode <= 57'>
                <select class="form-select form-select-sm" name="category" aria-label=".form-select-sm example"
                        class="form-control" style="margin-left: 1%">
                    <#list categories as category>
                        <option value="${category.id}">${category.name}</option>
                    </#list>
                </select>
                <input type="checkbox" class="form-check-input" id="isPrivate" name="isPrivate" style="margin-left: 1%">
                <label class="form-check-label">Is Private?</label>
            </div>

            <textarea class="form-control" rows="2" name="text" placeholder="Description" style="margin-top: 1%"
                      required></textarea>
            <textarea class="form-control" rows="8" name="ddlScript" placeholder="DDL Script" style="margin-top: 1%"
                      required></textarea>
            <textarea class="form-control" rows="8" name="correctQuery" placeholder="Correct query"
                      style="margin-top: 1%" required></textarea>
            <button type="submit" class="btn btn-primary ml-2" style="margin-top: 1%; margin-bottom: 1%">Save</button>
        </form>
    </div>
</@c.page>