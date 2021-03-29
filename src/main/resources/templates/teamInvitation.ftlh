<#import "parts/common.ftlh" as c>

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
</@c.page>