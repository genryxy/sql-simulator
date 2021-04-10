<#include "../security.ftlh">
<#import "tasksView.ftlh" as tv>

<div>
    <@tv.showTasks tasks "/practice/${practice.id}/task"/>
</div>