<#import "../parts/common.ftlh" as c>
<#import "../parts/student/submissions.ftlh" as sbmns>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div class="row">
        <div class="col-4 h5">Task execution</div>
        <div class="col-4">Number of attempts for all users: ${correctAttempts}/${totalAttempts}</div>
    </div>
    <form method="post">
        <div class="container-fluid">
            <div class="row flex-nowrap">
                <div class="col">
                    <label>${task.text}</label>
                </div>
            </div>
            <div class="form-group">
                <label>Please, enter the answer below</label>
                <textarea class="form-control" name="query" rows="3"
                          placeholder="Type your query ..."><#if sentQuery??>${sentQuery}</#if></textarea>
            </div>
            <button class="btn btn-primary" type="submit">Submit solution</button>
        </div>
        <input type="hidden" value="${task.id}" name="task">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>

    <div class="text-dark mt-5">
        <h5>Submissions</h5>
        <@sbmns.showSubmissions submissions/>
    </div>

</@c.page>