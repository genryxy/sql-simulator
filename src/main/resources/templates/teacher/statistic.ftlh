<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>

    <form method="get" action="/teacher/statistic/${practice.id}/table" style="margin-left: 1%">
        <button type="submit" class="btn btn-primary my-2">Table</button>
    </form>

    <div class="row">
        <div class="col-sm">
            <h5>By Tasks</h5>
            <div>
                <#list tasks as task>
                    <div class="list-group">
                        <a href="/teacher/statistic/${practice.id}/task/${task.id}" class="list-group-item list-group-item-action">
                            ${task.name}
                        </a>
                    </div>
                <#else>
                    No messages
                </#list>
            </div>
        </div>
        <div class="col-sm">
            <h5>By Groups</h5>
            <div>
                <#list teams as team>
                    <div class="list-group">
                        <a href="/teacher/statistic/${practice.id}/team/${team.id}/student" class="list-group-item list-group-item-action">
                            ${team.name}
                        </a>
                    </div>
                <#else>
                    No messages
                </#list>
            </div>
        </div>
    </div>

</@c.page>