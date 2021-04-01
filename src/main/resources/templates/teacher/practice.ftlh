<#import "common.ftlh" as c>

<@c.page>
    <div class="form-group col-md-6">
        <form method="get" action="/teacher/practice/create" class="form-inline">
            <button type="submit" class="btn btn-primary ml-2">Create new practice</button>
        </form>
    </div>
    <div class="form-group col-md-6">
        <form method="get" action="/teacher/practice/archive" class="form-inline">
            <button type="submit" class="btn btn-primary ml-2">Archive</button>
        </form>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-sm">
                Ready to assign practices
                <div>
                    <#list practices as practice>
                        <div class="card my-3">
                            <div class="card-body">
                                <h5 class="card-title">${practice.name}</h5>
                                <p class="card-text">${practice.description}</p>
                                <small class="card-text"></small>
                                <a href="/teacher/practice/${practice.id}/info" class="btn btn-primary">Info</a>
                                <a href="/teacher/team/${practice.id}" class="btn btn-primary">Start</a>
                            </div>
                        </div>
                    <#else>
                        No available practice
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                Practices in process of solving
                <#list practicesInProcess as practice>
                    <div class="card my-3">
                        <div class="card-body">
                            <h5 class="card-title">${practice.name}</h5>
                            <p class="card-text">${practice.description}</p>
                            <a href="/teacher/practice/${practice.id}/info" class="btn btn-primary">Info</a>
                        </div>
                    </div>
                <#else>
                    No available practice
                </#list>
            </div>
        </div>
    </div>

</@c.page>