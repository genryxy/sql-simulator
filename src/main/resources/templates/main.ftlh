<#import "parts/common.ftlh" as c>
<#import "parts/authentication.ftlh" as auth>

<@c.page>
    <div>
        <@auth.logout />
        <span><a href="/user">User list</a> </span>
    </div>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Input message"/>
            <input type="text" name="tag" placeholder="Input tag"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Add</button>
        </form>
        <form method="get" action="/main">
            <input type="text" name="filter" placeholder="Input filter" value="${filter}"/>
            <button type="submit">Search</button>
        </form>
    </div>
    <div>Список сообщений</div>
    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
        </div>
    <#else>
        No messages
    </#list>
</@c.page>