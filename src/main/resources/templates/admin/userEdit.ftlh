<#import "../parts/common.ftlh" as c>

<@c.page>
    <p class="text-dark h5 mb-4">User editor</p>
    <form action="/admin/user" method="post">
        <div class="row mb-2">
            <div class="col-1">
                <b>Name:</b>
            </div>
            <div class="row">
                <label>
                    <input class="mb-2" type="text" name="username" value="${user.username}">
                </label>
            </div>
        </div>
        <#list roles as role>
            <div>
                <label>
                    <input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}>
                    ${role}
                </label>
            </div>
        </#list>
        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button class="btn btn-primary" type="submit">Save</button>
    </form>

</@c.page>