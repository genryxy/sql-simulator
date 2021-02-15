<#import "../parts/common.ftlh" as c>

<@c.page>
    <b>Task execution</b>
    <form method="post">
        <div class="container-fluid">
            <div class="row flex-nowrap">
                <div class="col">
                    <label>${task.text}</label>
                </div>
            </div>
            <div class="form-group">
                <label>Please, enter the answer below</label>
                <textarea class="form-control" name="query" rows="3"></textarea>
            </div>
            <button class="btn btn-primary" type="submit">Submit task</button>
        </div>
        <input type="hidden" value="${task.id}" name="task">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>

</@c.page>