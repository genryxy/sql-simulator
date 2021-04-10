<#macro showTasks tasks tasks_href>
    <#list tasks as task>
        <div class="list-group">
            <a href="${tasks_href}/${task.id}"
               class="list-group-item list-group-item-action
                        list-group-item-<#if task.state.value() == "correct">success<#elseif
               task.state.value() == "wrong">danger</#if>">
                <div class="d-flex w-100 justify-content-between">
                    <h5 class="mb-1"> ${task.name}</h5>
                    <small>${task.points} point(-s)</small>
                </div>
                <p class="mb-1">${task.category.name}</p>
            </a>
        </div>
    <#else>
        No tasks
    </#list>
</#macro>