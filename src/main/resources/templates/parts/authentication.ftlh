<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Username :</label>
            <div class="col-sm-5">
                <input type="text" name="username"
                       value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="Username"/>
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label"> Password:</label>
            <div class="col-sm-5">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password"/>
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>

        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Password:</label>
                <div class="col-sm-5">
                    <input type="password" name="passwordConfirm"
                           class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}"
                           placeholder="Retype password"/>
                    <#if passwordConfirmError??>
                        <div class="invalid-feedback">
                            ${passwordConfirmError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> First name:</label>
                <div class="col-sm-5">
                    <input type="text" name="firstname"
                           value="<#if user??>${user.firstname}</#if>"
                           class="form-control ${(firstnameError??)?string('is-invalid', '')}"
                           placeholder="Firstname"/>
                    <#if firstnameError??>
                        <div class="invalid-feedback">
                            ${firstnameError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Last name:</label>
                <div class="col-sm-5">
                    <input type="text" name="lastname"
                           value="<#if user??>${user.lastname}</#if>"
                           class="form-control ${(lastnameError??)?string('is-invalid', '')}"
                           placeholder="Lastname"/>
                    <#if lastnameError??>
                        <div class="invalid-feedback">
                            ${lastnameError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Email:</label>
                <div class="col-sm-5">
                    <input type="email" name="email"
                           value="<#if user??>${user.email}</#if>"
                           class="form-control ${(emailError??)?string('is-invalid', '')}"
                           placeholder="example@example.com"/>
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label"> Role:</label>
                <div class="btn-group mt-2" role="group">
                    <input type="radio" class="btn-check mr-2" name="btnradio" id="student" value="student"
                           autocomplete="off" checked>
                    <label class="text-dark mr-5" for="student">Student</label>

                    <input type="radio" class="btn-check ml-5" name="btnradio" id="teacher" value="teacher"
                           autocomplete="off">
                    <label class="text-dark ml-2" for="teacher">Teacher</label>
                </div>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
        <div class="row">
            <#if !isRegisterForm>
                <a class="nav-link ml-5" href="/registration">Create an account</a>
            </#if>
        </div>
        <button class="btn btn-primary ml-5 mt-2" type="submit">
            <#if isRegisterForm>Create<#else>Sign In</#if>
        </button>
    </form>
</#macro>

<#macro logout>
    <#include "security.ftlh"/>
    <form action="/logout" method="post">
        <button class="btn btn-primary" type="submit"><#if user??>Sign out<#else>Log in</#if></button>
        <input type="hidden" name="_csrf" value="${_csrf.token}">
    </form>
</#macro>