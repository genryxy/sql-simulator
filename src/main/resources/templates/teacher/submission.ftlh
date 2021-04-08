<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div class="text-dark mt-5">
        <h5 class="mt-2">${submission.task.name}</h5>
        <h5 class="mt-2">${submission.sendDate}</h5>
        <div class="text-dark mt-2 mb-2">
            ${submission.task.text}
        </div>
        <textarea class="form-control" name="query" rows="${countOfLength}" disabled="disabled">
            ${submission.query}
        </textarea>
    </div>
</@c.page>