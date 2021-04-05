<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">
<#import "../parts/student/submissions.ftlh" as sbmns>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <@sbmns.showSubmissions submissions />
</@c.page>