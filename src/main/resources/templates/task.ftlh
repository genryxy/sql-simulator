<#import "parts/common.ftlh" as c>

<@c.page>
    <h5>You can resolve tasks:</h5>

    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/task/create" class="form-inline">
                <button type="submit" class="btn btn-primary ml-2">create task</button>
            </form>
            <form method="get" action="/task/all" class="form-inline">
                <button type="submit" class="btn btn-primary ml-2">available tasks</button>
            </form>
        </div>
    </div>
    <#include "parts/taskList.ftlh"/>
</@c.page>