<#import "parts/common.ftlh" as c>
<#import "parts/authentication.ftlh" as auth>


<@c.page>
    <h5>Join team by entering an invitation code</h5>
    <#include "parts/alertMessage.ftlh"/>
    <form method="post" action="/student/team/" class="form-inline">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Invitation code:</label>
            <div class="col-sm-5">
                <input type="text" name="invitation"
                       placeholder="Invitation code"/>
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button class="btn btn-primary" type="submit">Join</button>
    </form>
    <h5 class="text-dark mt-4">My teams</h5>
    <div class="card-columns mt-2">
        <#list teams as team>
            <div class="card my-3">
                <div class="m-2">
                    <a class="nav-link" href="/practice/?team=${team.id}">${team.name}</a>
                </div>
                Creator: <b>${team.author.username}</b>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>