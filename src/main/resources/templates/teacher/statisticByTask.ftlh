<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div style="margin-bottom: 3%">
        <#list submissions as submission>
            <div class="text-dark mt-5">
                <h5>${submission.user.username}</h5>
                <h5 class="mt-2">${submission.sendDate}</h5>
                <textarea class="form-control" name="query" rows="3" disabled="disabled"
                          style="color: <#if submission.correct>darkgreen;
                                    <#else>indianred</#if>">
                    ${submission.query}
                </textarea>
            </div>
        <#else>
            No submissions
        </#list>
    </div>
</@c.page>