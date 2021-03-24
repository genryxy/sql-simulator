<#import "../parts/common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-group col-md-6">
        <form action="/teacher/team/${practiceId}/create" method="get">
            <button type="submit" class="btn btn-primary ml-2">Create new Team</button>
        </form>
    </div>
    <div class="container">
        <div class="row">
            <div class="col-sm">
                Available teams
                <div>
                    <#list teams2 as team>
                        <div class="card my-3">
                            <div class="card-body">
                                <h5 class="card-title">${team.name}</h5>
                                <p class="card-text">${team.invitation}</p>
                            </div>
                            <form action="/teacher/team/assign" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <input type="hidden" name="practiceId" value="${practiceId}">
                                <input type="hidden" name="teamId" value="${team.id}">
                                <button type="submit" class="btn btn-primary ml-2">Assign</button>
                            </form>
                        </div>
                    <#else>
                        No available teams
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                Assigned groups
                <div>
                    <#list teams as team>
                        <div class="card my-3">
                            <div class="card-body">
                                <h5 class="card-title">${team.name}</h5>
                                <p class="card-text">${team.invitation}</p>
                            </div>
                            <form action="/teacher/team/throw" method="post">
                                <input type="hidden" name="_csrf" value="${_csrf.token}">
                                <input type="hidden" name="practiceId" value="${practiceId}">
                                <input type="hidden" name="teamId" value="${team.id}">
                                <button type="submit" class="btn btn-primary ml-2">Throw</button>
                            </form>
                        </div>
                    <#else>
                        No available teams
                    </#list>
                </div>
            </div>
            <div class="col-sm">
                DeadLine
                <div>
                    <form action="/teacher/team/${practiceId}/start" method="post">
                        <div class="form-group">
                            <input type="date" name="date" class="form-control">
                            <label>Введите дату:</label>
                        </div>
                        <div class="md-form mx-5 my-5">
                            <input type="time" name="time" class="form-control">
                            <label>Choose your time</label>
                        </div>
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <input type="hidden" name="practiceId" value="${practiceId}">
                        <button type="submit" class="btn btn-primary ml-2">Ready!</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@c.page>