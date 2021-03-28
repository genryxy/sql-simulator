<#import "../parts/common.ftlh" as c>

<#macro btnByState state name>
    <a href="/practice/1?task_status=${state}" class="list-group-item list-group-item-action">
        <h6>${name}</h6>
    </a>
</#macro>

<@c.page>
    <p class="text-break ml-3 h5">It's a common pool with tasks.<br/>
        You're able to solve these tasks unlimited number of times.</p>
    <#include "../parts/alertResult.ftlh"/>
    <#if category_filter??>
        <p class="text-dark h5 alert-info col-4 m-2 p-3">Category: ${category_filter.name}</p>
    </#if>
    <div class="container-fluid">
        <div class="row">
            <div class="col-8">
                <#include "../parts/student/tasks.ftlh"/>
            </div>
            <div class="col-4">
                <div class="row">
                    <select class="form-select form-select-lg mt-3 h5" name="category" onchange="location=this.value">
                        <option selected>Filter by category</option>
                        <#list categories as category>
                            <option value="/practice/1?category=${category.id}">${category.name}</option>
                        </#list>
                    </select>
                </div>
                <div class="row">
                    <a class="btn btn-primary ml-2 mt-2" href="/practice/1" role="button">Reset</a>
                </div>
                <div class="row list-group mt-4">
                    <a href="/practice/1" class="list-group-item list-group-item-action"><h6>All tasks</h6></a>
                    <@btnByState "correct" "Correct solved"/>
                    <@btnByState "wrong" "Wrong solved"/>
                    <@btnByState "not_solved" "Not solved"/>
                </div>
            </div>
        </div>
    </div>
</@c.page>