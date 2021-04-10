<#import "../parts/common.ftlh" as c>
<#import "../parts/student/submissions.ftlh" as sbmns>

<@c.page>
    <div class="text-dark mt-5">
        <h5>Submissions</h5>
        <@sbmns.showSubmissions submissions/>
    </div>
</@c.page>