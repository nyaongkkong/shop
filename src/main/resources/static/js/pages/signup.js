$(document).ready(function () {
    $("#signupBtn").click(function () {
        const username = $("#username").val().trim();
        const password = $("#password").val().trim();
        const nickname = $("#nickname").val().trim();
        const messageBox = $("#messageBox");

        if (!username || !password || !nickname) {
            messageBox.removeClass("success").addClass("error")
                .text("아이디, 비밀번호, 닉네임을 모두 입력하세요.")
                .show();
            return;
        }

        $.ajax({
            url: "/api/auth/signup",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ username, password, nickname }),

            success: function (res) {
                messageBox.removeClass("error").addClass("success")
                    .text("회원가입이 완료되었습니다. 로그인 페이지로 이동합니다.")
                    .show();

                setTimeout(function () {
                    window.location.href = "/login";
                }, 1000);
            },

            error: function (xhr) {
                let msg = "회원가입 중 오류가 발생했습니다.";

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

                messageBox.removeClass("success").addClass("error")
                    .text(msg).show();
            }
        });
    });

});
