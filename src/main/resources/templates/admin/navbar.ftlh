<#include "../parts/security.ftlh">
<#import "../parts/authentication.ftlh" as auth>

<nav class="navbar nav-tabs navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/">Sql-simulator</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/main">Home</a>
            </li>
            <#if user?? && name!="unknown">
                <li class="nav-item">
                    <a class="nav-link" href="/user/profile">Profile</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/user">User list</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Role
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <a class="dropdown-item" href="/">Student</a>
                        <a class="dropdown-item" href="/teacher/practice">Teacher</a>
                        <a class="dropdown-item" href="/admin/user">Admin</a>
                    </div>
                </li>
            </#if>

        </ul>

        <div class="navbar-text mr-3">
            <#if user??>${name}<#else>Please, login</#if>
        </div>
        <@auth.logout />
    </div>
</nav>