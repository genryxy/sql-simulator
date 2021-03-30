<#import "common.ftlh" as c>

<@c.page>
    Archive practices
    <div>
        <#list practices as practice>
            <div class="card my-3">
                <div class="card-body">
                    <h5 class="card-title">${practice.name}</h5>
                    <p class="card-text">${practice.description}</p>
                    <a href="/teacher/practice/${practice.id}" class="btn btn-primary">Information</a>
                </div>
            </div>
        <#else>
            No available practice
        </#list>
    </div>

</@c.page>