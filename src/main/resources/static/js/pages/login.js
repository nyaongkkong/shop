$(document).ready(function () {
    $("#loginBtn").click(function () {
        const username = $("#username").val().trim();
        const password = $("#password").val().trim();
        const errorBox = $("#errorBox");

        if (!username || !password) {
            errorBox.text("아이디와 비밀번호를 입력하세요.").show();
            return;
        }

        $.ajax({
            url: "/api/auth/login",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ username, password }),

            success: function (res) {
                const data = res.data;
                localStorage.setItem("accessToken", data.accessToken);
                localStorage.setItem("refreshToken", data.refreshToken);
                window.location.href = "/";
            },

            error: function (xhr) {
                let msg = "로그인에 실패했습니다.";

                if (xhr.responseJSON) {
                    const err = xhr.responseJSON.error;
                    if (err) {
                        msg = err.message || msg;

                        const fe = err.fieldErrors;
                        if (fe) {
                            const firstKey = Object.keys(fe)[0];
                            if (firstKey) msg = fe[firstKey];
                        }
                    }
                } else if (xhr.responseText) {
                    msg = xhr.responseText;
                }

                errorBox.text(msg).show();
            }
        });
    });

});
