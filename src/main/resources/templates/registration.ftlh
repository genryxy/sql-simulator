<#import "parts/common.ftlh" as c>
<#import "parts/authentication.ftlh" as auth>

<@c.page>
    <div class="text-dartk mb-1 h5"> Create an account </div>
    <#if message??>
       ${message!}
    </#if>
    <@auth.login "/registration" true/>
</@c.page>