<#import "../parts/common.ftlh" as c>

<@c.page>
    Task execution

    <form action="/task" method="post">
        <div class="container-fluid">
            <div class="row flex-nowrap">
                <div class="col">
                    <label>
                        <input type="text" name="text">
                    </label>
                </div>
            </div>
        </div>

        <input type="hidden" value="${user.id}" name="userId">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit">Submit task</button>
    </form>

</@c.page>