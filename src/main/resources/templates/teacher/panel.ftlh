<#import "../parts/common.ftlh" as c>

<@c.page>
    Practice list
    <div class="card-columns">
        <#list practices as practice>
            <div class="card my-3">
                <div class="card-body">
                    <h5 class="card-title">${practice.name}</h5>
                    <p class="card-text">${practice.description}</p>
                    <a href="/practice/${practice.id}" class="btn btn-primary">Start</a>
                </div>
            </div>
        <#else>
            No available practice
        </#list>

        <div class="form-group col-md-6">
            <form method="get" action="/teacher/practice/create" class="form-inline">
                <button type="submit" class="btn btn-primary ml-2">Create practice</button>
            </form>
        </div>
    </div>
</@c.page>