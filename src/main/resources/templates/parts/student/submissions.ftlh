<#macro showSubmissions subms>
    <div class="container-fluid mt-2 mb-lg-5">
            <#list subms as subm>
                <div class="list-group mt-1">
                    <a href="/result/submission/${subm.id}" class="list-group-item list-group-item-action">
                        <b>Send date: ${subm.sendDate}</b>
                        <div class="row">
                            <p class="mb-1 col-sm-2">ID: ${subm.id}</p>
                            <p class="mb-1 col-sm">Task: ${subm.task.name}</p>
                        </div>
                    </a>
                </div>
            <#else>
                No submissions
            </#list>
    </div>
</#macro>