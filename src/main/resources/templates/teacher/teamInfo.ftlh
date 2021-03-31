<#import "common.ftlh" as c>
<#include "../parts/security.ftlh">

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <h5 class="card-title">${team.name}</h5>

    <form action="/teacher/team/${team.id}/edit" method="get">
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <button type="submit" class="btn btn-primary ml-2">Edit</button>
    </form>

    <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">
        Remove
    </button>
    <div class="modal fade" id="exampleModal" tabindex="-1" role="dialog"
         aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="exampleModalLabel">All student submissions will be deleted</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    <form method="post" action="/teacher/team/${team.id}/remove" class="form-inline">
                        <input type="hidden" name="_csrf" value="${_csrf.token}">
                        <button type="submit" class="btn btn-primary ml-2">
                            Remove
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@c.page>