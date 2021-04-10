<#macro btnByState kind name>
    <a href="/result/${kind}" class="list-group-item list-group-item-action">
        <h6>${name}</h6>
    </a>
</#macro>

<#macro selectionMenu>
    <div class="text-dark mt-4 mb-2 h5">Select type of result</div>
    <div class="row list-group mt-4">
        <@btnByState "submission" "Results by submissions"/>
        <@btnByState "practice" "Results by practices"/>
        <@btnByState "task" "Results by tasks"/>
    </div>
</#macro>