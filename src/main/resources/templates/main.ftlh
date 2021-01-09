<#import "parts/common.ftlh" as c>

<@c.page>
    <div class="form-row">
        <div class="form-group col-md-6">
            <form method="get" action="/main" class="form-inline">
                <input type="text" name="filter" class="form-control" value="${filter!}"
                       placeholder="Search by tag"/>
                <button type="submit" class="btn btn-primary ml-2">Search</button>
            </form>
        </div>
    </div>

    <a class="btn btn-primary" data-toggle="collapse" href="#collapseExample" role="button" aria-expanded="false"
       aria-controls="collapseExample">
        Add new message
    </a>
    <div class="collapse" id="collapseExample">
        <div class="form-group mt-3 col-md-8">
            <form method="post">
                <div class="form-group">
                    <input type="text" name="text" class="form-control" placeholder="Input message"/>
                </div>
                <div class="form-group">
                    <input type="text" name="tag" class="form-control" placeholder="Input tag"/>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}">
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">Add</button>
                </div>
            </form>
        </div>
    </div>

    <div class="card-columns">
        <#list messages as message>
            <div class="card my-3">
                <div class="m-2">
                    <span>${message.text}</span>
                </div>
                <i>${message.tag}</i>
                <div class="card-footer text-muted">
                    ${message.authorName}
                </div>
            </div>
        <#else>
            No messages
        </#list>
    </div>
</@c.page>