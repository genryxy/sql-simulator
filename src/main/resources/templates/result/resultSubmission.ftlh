<#import "../parts/common.ftlh" as c>
<#import "../parts/student/submissions.ftlh" as sbmns>
<#import "typeSelection.ftlh" as ts>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div class="row">
        <div class="col-7">
            <div class="text-dark mb-4 h5">Results by submissions</div>
            <@sbmns.showSubmissions submissions />
        </div>
        <div class="col-4 ml-2">
            <a href="/result/submission/export" class="btn btn-primary mb-2 mt-2">Export CSV</a>
            <@ts.selectionMenu />
        </div>
    </div>
</@c.page>
