<#import "parts/common.ftlh" as c>
<#import "parts/authentication.ftlh" as auth>

<@c.page>
    Add new user
    <#if message??>
       ${message}
    </#if>
    <@auth.login "/registration" />
</@c.page>