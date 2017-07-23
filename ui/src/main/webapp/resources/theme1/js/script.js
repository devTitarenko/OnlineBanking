function searchFormSpaceTrimmer() {
    var inputField;
    inputField = document.getElementById("search:firstName");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("search:lastName");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("search:mobile");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("search:accountNumber");
    inputField.value = inputField.value.trim();
}

function customerFormSpaceTrimmer() {
    var inputField;
    inputField = document.getElementById("createEditCustomerForm:accordion:firstName");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:lastName");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:tin");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:passport");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:zip");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:state");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:city");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:address");
    inputField.value = inputField.value.trim();

    inputField = document.getElementById("createEditCustomerForm:accordion:phone");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:email");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:codeword");
    inputField.value = inputField.value.trim();

    inputField = document.getElementById("createEditCustomerForm:accordion:zip2");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:state2");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:city2");
    inputField.value = inputField.value.trim();
    inputField = document.getElementById("createEditCustomerForm:accordion:address2");
    inputField.value = inputField.value.trim();
}

function hideCustomerErrMsg(variable) {
    var inputGField, msg, bottomPageErrorMsg;
    inputGField = document.getElementById("createEditCustomerForm:accordion:" + variable);
    msg = document.getElementById("createEditCustomerForm:accordion:m_" + variable);
    bottomPageErrorMsg = document.getElementById("createEditCustomerForm:g_" + variable);
    if (inputGField.value === "") {
        msg.innerHTML = "";
    }
    if (variable === "passDef") {
        document.getElementById("createEditCustomerForm:accordion:passConf").value = "";
    }
    try {
        if ((bottomPageErrorMsg.value !== null) && (inputGField.value === "")) {
            bottomPageErrorMsg.innerHTML = "";
        }
    } catch (err) {
    }
}

function hideCustomerCalendarErrMsg(variable, widgetvar) {
    var inputField, msg;
    inputField = PF(widgetvar).input;
    msg = document.getElementById("createEditCustomerForm:accordion:m_" + variable);
    if (inputField.val() === "") {
        msg.innerHTML = "";
    }
}

function hideAccountErrMsg(variable) {
    var inputField, msg;
    inputField = document.getElementById("createEditViewAccountForm:" + variable);
    msg = document.getElementById("createEditViewAccountForm:m_" + variable);
    if (inputField.value === "" && msg.hasChildNodes()) {
        msg.innerHTML = "";
    }
}

function hideAccountCalendarErrMsg(variable, widgetvar) {
    var inputField, msg;
    inputField = PF(widgetvar).input;
    msg = document.getElementById("createEditViewAccountForm:m_" + variable);
    if (inputField.val() === "") {
        msg.innerHTML = "";
    }
}

function hideCardErrMsg(variable) {
    var inputField, msg;
    inputField = document.getElementById("createEditViewCardForm:" + variable);
    msg = document.getElementById("createEditViewCardForm:m_" + variable);
    if (inputField.value === "" && msg.hasChildNodes()) {
        msg.innerHTML = "";
    }
}

function hideCardCalendarErrMsg(variable, widgetvar) {
    var inputField, msg;
    inputField = PF(widgetvar).input;
    msg = document.getElementById("createEditViewCardForm:m_" + variable);
    if (inputField.val() === "") {
        msg.innerHTML = "";
    }
}


function hideLoginErrMsg(variableLog) {
    var inputField, passwordInputField, loginErrMsg, downPageErrMsg;
    inputField = document.getElementById("loginForm:" + variableLog);
    loginErrMsg = document.getElementById("loginForm:m_" + variableLog);
    passwordInputField = document.getElementById("loginForm:password");

    if (inputField.value === "") {
        loginErrMsg.innerHTML = "";
    }
    try {
        downPageErrMsg = document.getElementById("loginForm:message");
        if ((inputField.value === "") && (passwordInputField.value === "")) {
            downPageErrMsg.innerHTML = "";
        }
    } catch (err) {}
}
hideSearchErrMsg

function hideSearchErrMsg(variable) {
    var inputField, msg;
    inputField = document.getElementById("search:" + variable);
    msg = document.getElementById("search:m_" + variable);
    if (inputField.value === "" && msg.hasChildNodes()) {
        msg.innerHTML = "";
    }
}

function hideChangePasswordErrMsg(variable) {
    var inputField, msg;
    inputField = document.getElementById("changePasswordForm:" + variable);
    msg = document.getElementById("changePasswordForm:m_" + variable);
    if (inputField.value === "" && msg.hasChildNodes()) {
        msg.innerHTML = "";
    }
}

function hideSearchCalendarErrMsg(variable, widgetvar) {
    var inputField, msg;
    inputField = PF(widgetvar).input;
    msg = document.getElementById("search:m_" + variable);
    if (inputField.val() === "") {
        msg.innerHTML = "";
    }
}

function hideReportsErrMsg(variable) {
    var inputField, msg;
    inputField = document.getElementById("reports:" + variable);
    msg = document.getElementById("reports:m_" + variable);
    if (inputField.value === "" && msg.hasChildNodes()) {
        msg.innerHTML = "";
    }
}

function hideReportsCalendarErrMsg(variable, widgetvar) {
    var inputField, msg;
    inputField = PF(widgetvar).input;
    msg = document.getElementById("reports:m_" + variable);
    if (inputField.val() === "") {
        msg.innerHTML = "";
    }
}
