<#import "../parts/common.ftlh" as c>

<@c.page>
    <h5>List of users</h5>
    <table class="table table-bordered" style="width: auto">
        <tr>
            <th>Name</th>
            <th>Role</th>
            <th>Editing</th>
        </tr>
        <tbody>
        <#list users as user>
            <tr>
                <td>${user.username!"null or missing"}</td>
                <td><#list user.roles as role>${role}<#sep>, </#list></td>
                <td><a class="btn btn-primary" href="/admin/user/${user.id}">Edit</a></td>
            </tr>
        </#list>
        </tbody>
    </table>
</@c.page>