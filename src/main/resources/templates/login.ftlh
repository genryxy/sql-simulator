<#import "parts/common.ftlh" as c>
<#import "parts/authentication.ftlh" as auth>

<@c.page>
    Login page
    <@auth.login "/login" />
    <a href="/registration">Add new user</a>
</@c.page>