<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div class="text-dark mt-5">
        <h6>Practice: ${submission.practice.name}</h6>
        <h6 class="mt-2">Task: ${submission.task.name}</h6>
        <h6 class="mt-2">Send date: ${submission.sendDate}</h6>
        <div class="text-dark mt-2 mb-2">
            ${submission.task.text}
        </div>
        <textarea class="form-control" name="query" rows="3" disabled="disabled">
            ${submission.query}
        </textarea>
    </div>
</@c.page>