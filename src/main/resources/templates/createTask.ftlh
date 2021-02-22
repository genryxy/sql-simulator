<#import 'parts/common.ftlh' as c>

<@c.page>
    <h5>Input Data: </h5>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="post" action="/task/create" class="form-inline">
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <input type="text" name="text" placeholder="text" >
                <input type="text" name="ddlScript" placeholder="ddlScript" >
                <input type="text" name="name" placeholder="name" >
                <input type="text" name="correctQuery" placeholder="correctQuery">
                <input type="int" name="points" placeholder="points">
                <input type="long" name="categoryId" placeholder="categoryId">
                <button type="submit" class="btn btn-primary ml-2">save</button>
            </form>
        </div>
    </div>
</@c.page>