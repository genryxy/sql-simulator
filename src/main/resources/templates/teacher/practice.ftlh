<#import "common.ftlh" as c>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <form method="get" action="/teacher/practice/create">
        <button type="submit" class="btn btn-primary ml-2">Create new practice</button>
    </form>
    <form method="get" action="/teacher/practice/archive" style="margin-top: 1%">
        <button type="submit" class="btn btn-primary ml-2">Archive</button>
    </form>
    <div class="container" style="margin-top: 3%">
        <div class="row">
            <div class="col-sm">
                <h5 class="card-title">Ready to assign practices</h5>
                <div>
                    <#list practices as practice>
                        <div class="card my-3">
                            <div class="card-body">
                                <h5 class="card-title">${practice.name}</h5>
                                <p class="card-text">${practice.description}</p>
                                <a href="/teacher/practice/${practice.id}/info" class="btn btn-primary">Information</a>
                                <a href="/teacher/team/${practice.id}" class="btn btn-primary">Start</a>
                            </div>
                        </div>
                    <#else>
                        No available practice
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                <h5>Practices in process of solving</h5>
                <#list practicesInProcess as practice>
                    <div class="card my-3">
                        <div class="card-body">
                            <h5 class="card-title">${practice.name}</h5>
                            <p class="card-text">${practice.description}</p>
                            <a href="/teacher/practice/${practice.id}/info" class="btn btn-primary">Information</a>
                        </div>
                    </div>
                <#else>
                    No available practice
                </#list>
            </div>
        </div>
    </div>

</@c.page>