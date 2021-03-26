<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    Task list
    <#if result??>
        <div class="alert alert-${type}" role="alert">
            ${result}
        </div>
    </#if>
    <div>
        <#list tasks as task>
            <div class="list-group">
                <a href="/practice/${practice.id}/task/${task.id}"
                    class="list-group-item list-group-item-action
                        list-group-item-<#if task.state.value() == "correct">success<#elseif
                        task.state.value() == "wrong">danger</#if>">
                    Task name: ${task.name}
                </a>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>