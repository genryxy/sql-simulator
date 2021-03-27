<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    TASK
    <h5 class="card-title">${task.name}</h5>
    <p class="card-text">${task.text}</p>
    <p class="card-text">${task.ddlScript}</p>
    <p class="card-text">${task.correctQuery}</p>
    <p class="card-text">${task.points}</p>
    <p class="card-text">${task.categoryId}</p>
</@c.page>