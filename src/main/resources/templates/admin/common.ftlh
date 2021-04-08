<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <#include "../parts/head.ftlh"/>
    <body>
    <#include "navbar.ftlh">
    <div class="container mt-5">
        <#nested>
    </div>
    <#include "../parts/jsSettings.ftlh"/>
    </body>
    </html>
</#macro>