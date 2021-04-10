<#import "../parts/common.ftlh" as c>
<#import "typeSelection.ftlh" as ts>
<#import "../parts/student/tasksView.ftlh" as tv>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div class="row">
        <div class="col-7">
            <div class="text-dark mb-4 h5">Tasks for practice: ${practice.name}</div>
            <@tv.showTasks tasks "/result/practice/${practice.id}/task"/>
        </div>
    </div>
</@c.page>
