<#import "parts/common.ftlh" as c>
<#import "parts/authentication.ftlh" as auth>

<@c.page>
    <div class="mb-1"> Add new user </div>
    <#if message??>
       ${message}
    </#if>
    <@auth.login "/registration" true/>
</@c.page>