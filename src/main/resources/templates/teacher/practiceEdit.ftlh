<#import 'common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <div class="form-row">
        <#include "../parts/alertMessage.ftlh"/>
        <div class="form-row">
            <div class="form-group col-md-6">
                <form name="task" method="post" action="/teacher/practice/${practice.id}/edit" class="form-inline">
                    <input type="hidden" name="_csrf" value="${_csrf.token}">
                    <input type="text" name="name" value="${practice.name}" required>
                    <input type="text" name="description" value="${practice.description} required">
                    <input type="hidden" name="authorId" value="${currentUserId}">
                    <#if deadline??>
                        <div class="form-group">
                            <input type="date" name="date" class="form-control" value="${deadline.toLocalDate()}" required>
                            <label>Input Date</label>
                        </div>
                        <div class="md-form mx-5 my-5">
                            <input type="time" name="time" class="form-control" value="${deadline.toLocalTime()}" required>
                            <label>Choose DEADLINE</label>
                        </div>
                        <div class="form-check">
                            <input type="checkbox" class="form-check-input" id="exampleCheck1"
                                   name="sendingAfterDeadLine">
                            <label class="form-check-label" for="exampleCheck1">Solving after deadline</label>
                        </div>
                    </#if>
                    <button type="submit" class="btn btn-primary ml-2">save</button>
                </form>
            </div>
        </div>
    </div>
</@c.page>