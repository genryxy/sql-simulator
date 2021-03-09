<#import '../parts/common.ftlh' as c>
<#include "../parts/security.ftlh">

<@c.page>
    <h5>Input Team: </h5>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form name="team" method="post" action="/teacher/team/${practiceId}/create" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="hidden" name="authorId" value="${currentUserId}">
                <input type="text" name="name" placeholder="name">
                <input type="text" name="invitation" placeholder="text" >
                <button type="submit" class="btn btn-primary ml-2">Save</button>
            </form>
        </div>
    </div>
</@c.page>