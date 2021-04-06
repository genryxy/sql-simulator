<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<#macro btnByState state name>
    <a href="/practice/?status=${state}" class="list-group-item list-group-item-action">
        <h6>${name}</h6>
    </a>
</#macro>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <p class="text-dark h5">Available practices</p>
    <div class="container-fluid">
    <div class="row">
        <div class="col-8">
            <div class="card-columns">
                <#list practices as practice>
                    <div class="card my-3">
                        <div class="card-body">
                            <h5 class="card-title">${practice.name}</h5>
                            <p class="card-text">${practice.description}</p>
                            <a href="/practice/${practice.id}" class="btn btn-primary">Open</a>
                        </div>
                    </div>
                <#else>
                    No available practice
                </#list>
            </div>
        </div>
        <div class="col-3">
            <div class="row list-group mt-4">
                <a href="/practice" class="list-group-item list-group-item-action"><h6>All practices</h6></a>
                <@btnByState "active" "Active practices"/>
                <@btnByState "archive" "Finished practices"/>
            </div>
        </div>
    </div>
</@c.page>