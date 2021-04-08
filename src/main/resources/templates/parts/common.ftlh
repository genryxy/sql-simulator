<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <#include "head.ftlh"/>
    <body>
    <#include "navbar.ftlh">
    <div class="container mt-5">
        <#nested>
    </div>
    <#include "jsSettings.ftlh"/>
    </body>
    </html>
</#macro>