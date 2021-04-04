<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="container" xmlns="http://www.w3.org/1999/html">
        <div class="row">
            <div class="col-sm">
                <h5 class="card-title">Available teams</h5>
                <div>
                    <#list allAnotherTeamsByAuthor as team>
                        <div class="card my-3">
                            <div class="card-body">
                                <h5 class="card-title">${team.name}</h5>
                                <br><small class="card-text">${team.invitation}</small></br>
                            </div>
                            <form action="/teacher/team/assign" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <input type="hidden" name="practiceId" value="${practiceId}">
                                <input type="hidden" name="teamId" value="${team.id}">
                                <button type="submit" class="btn btn-lg btn-block btn-outline-primary">Assign</button>
                            </form>
                        </div>
                    <#else>
                        No available teams
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                <h5 class="card-title">Assigned teams</h5>
                <div>
                    <#list teamsInPractice as team>
                        <div class="card my-3">
                            <div class="card-body">
                                <h5 class="card-title">${team.name}</h5>
                                <br><small class="card-text">${team.invitation}</small></br>
                            </div>
                            <form action="/teacher/team/remove" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <input type="hidden" name="practiceId" value="${practiceId}">
                                <input type="hidden" name="teamId" value="${team.id}">
                                <button type="submit" class="btn btn-lg btn-block btn-outline-primary">Remove</button>
                            </form>
                        </div>
                    <#else>
                        No available teams
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                <h5 class="card-title">DeadLine</h5>
                <div>
                    <form action="/teacher/team/${practiceId}/start" method="post">
                        <div class="form-group">
                            <input type="date" name="date" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <input type="time" name="time" class="form-control" required>
                        </div>
                        <label style="margin-left: 50%">
                            <input type="checkbox" class="form-check-input" id="exampleCheck1"
                                   name="sendingAfterDeadLine">
                            Solving after deadline
                        </label>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="hidden" name="practiceId" value="${practiceId}">
                        <button type="submit" class="btn btn-lg btn-primary btn-block">Ready!</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@c.page>