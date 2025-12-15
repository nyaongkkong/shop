$(document).ready(function () {
    const $guest = $("#top-guest");
    const $user = $("#top-user");
    const $nick = $("#top-nickname");
    const $logout = $("#logoutBtn");

    function showGuest() {
        $guest.show();
        $user.hide();
        $nick.text("");
    }

    function showUser(nickname) {
        $nick.text(nickname + " 님");
        $guest.hide();
        $user.show();
    }

    function clearTokens() {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
    }

    function getErrorMessage(xhr, fallbackMsg) {
        let msg = fallbackMsg || "요청 처리 중 오류가 발생했습니다.";

        if (xhr && xhr.responseJSON && xhr.responseJSON.error) {
            const err = xhr.responseJSON.error;
            msg = err.message || msg;

            const fe = err.fieldErrors;
            if (fe) {
                const firstKey = Object.keys(fe)[0];
                if (firstKey) msg = fe[firstKey];
            }
        } else if (xhr && xhr.responseText) {
            msg = xhr.responseText;
        }
        return msg;
    }

    $logout.on("click", function (e) {
        e.preventDefault();

        const accessToken = localStorage.getItem("accessToken");

        if (!accessToken) {
            clearTokens();
            showGuest();
            window.location.href = "/";
            return;
        }

        $.ajax({
            url: "/api/auth/logout",
            method: "POST",
            headers: { "Authorization": "Bearer " + accessToken },

            complete: function () {
                clearTokens();
                showGuest();
                window.location.href = "/";
            }
        });
    });

    const accessToken = localStorage.getItem("accessToken");
    const refreshToken = localStorage.getItem("refreshToken");

    if (!accessToken) {
        showGuest();
        return;
    }

    callMeApi(accessToken, refreshToken);

    function callMeApi(token, refresh) {
        $.ajax({
            url: "/api/auth/me",
            method: "GET",
            headers: { "Authorization": "Bearer " + token },

            success: function (res) {
                showUser(res.data.nickname);
            },

            error: function (xhr) {
                if (xhr.status === 401 && refresh) {
                    refreshAccessToken(refresh);
                    return;
                }
                // console.warn(getErrorMessage(xhr, "인증 정보를 확인할 수 없습니다."));
                clearTokens();
                showGuest();
            }
        });
    }

    function refreshAccessToken(refreshToken) {
        $.ajax({
            url: "/api/auth/refresh",
            method: "POST",
            contentType: "application/json",
            data: JSON.stringify({ refreshToken }),

            success: function (res) {
                const data = res.data;
                localStorage.setItem("accessToken", data.accessToken);
                callMeApi(data.accessToken, refreshToken);
            },

            error: function (xhr) {
                // console.warn(getErrorMessage(xhr, "세션이 만료되었습니다. 다시 로그인해주세요."));
                clearTokens();
                showGuest();
            }
        });
    }
});
