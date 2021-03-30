<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div>
        <#if deadLine??>
            <h5 class="card-title">DeadLine: ${deadLine}</h5>
        </#if>
    </div>

    <div class="container">
        <div class="row">
            <div class="col-sm">
                Task list
                <div>
                    <#list tasks as task>
                        <div class="list-group">
                            <a href="/teacher/task/${task.id}" class="list-group-item list-group-item-action">
                                Task name: ${task.name}
                            </a>
                        </div>
                    <#else>
                        No messages
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                <form method="get" action="/teacher/statistic/${practice.id}/" class="form-inline">
                    <button type="submit" class="btn btn-primary ml-2">Statistic</button>
                </form>
            </div>
        </div>
    </div>
</@c.page>