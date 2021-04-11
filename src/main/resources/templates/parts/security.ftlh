<#assign
known = Session.SPRING_SECURITY_CONTEXT??
>
<#if known>
    <#assign
    user = Session.SPRING_SECURITY_CONTEXT.authentication.principal
    name = user.getUsername()
    firstname = user.getUsername()
    isAdmin = user.isAdmin()
    isTeacher = user.isTeacher()
    currentUserId = user.getId()
    >
<#else>
    <#assign
    name = "unknown"
    firstname = "unknown"
    isAdmin = false
    isTeacher = false
    currentUserId = -1
    >
</#if>