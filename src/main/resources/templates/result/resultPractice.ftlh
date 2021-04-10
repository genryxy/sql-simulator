<#import "../parts/common.ftlh" as c>
<#import "typeSelection.ftlh" as ts>

<@c.page>
    <#include "../parts/alertMessage.ftlh"/>
    <div class="row">
        <div class="col-7">
            <div class="text-dark mb-4 h5">Results by practices</div>
            <div class="container-fluid mt-2 mb-lg-5">
                <#list results as res>
                    <div class="list-group mt-1">
                        <a href="/result/practice/${res.practice.id}" class="list-group-item list-group-item-action">
                            <b>${res.practice.name}</b>
                            <div class="row mb-1">
                                <p class="dark-text col-4">Author: ${res.practice.author.username}</p>
                                <p class="dark-text col-4">Result: ${res.score} / ${res.total}</p>
                            </div>
                        </a>
                    </div>
                <#else>
                    No results for practices
                </#list>
            </div>
        </div>
        <div class="col-4 ml-2">
            <@ts.selectionMenu />
        </div>
    </div>
</@c.page>
