let currentProductId = null;

$(function () {
  const slug = getSlug();
  if (!slug) return;

  $.ajax({
    url: '/api/products/' + encodeURIComponent(slug),
    method: 'GET',
    success: function (res) {
      if (!res || res.success !== true) {
        showError('상품 정보를 불러오지 못했습니다.');
        return;
      }

      renderProduct(res.data);
    },
    error: function (xhr) {
      console.log('product api error:', xhr.status, xhr.responseText);
      showError(xhr?.responseJSON?.error?.message || '서버 오류');
    }
  });

});

/* ==========================
   PRODUCT RENDER
========================== */

function renderProduct(p) {

  currentProductId = p.id;

  $('#productName').text(p.name);
  $('#productBrand').text(p.brand.name);
  $('#productBrandName').text(p.brand.name);
  $('#productCategory').text(p.category.name);

  $('#productPrice').text(formatPrice(p.price));

  if (p.thumbnailUrl) {
    $('#productImage').html(
      `<img src="${escapeHtml(p.thumbnailUrl)}"
            style="width:100%; height:100%; object-fit:cover;">`
    );
  }

  // 구매 버튼
  $('#buyBtn').off('click').on('click', function () {
    alert('구매 기능은 다음 단계에서!');
  });

  // 찜 버튼
  $('#likeBtn').off('click').on('click', function () {
    toggleLike();
  });

  // 로그인 상태면 찜 상태 조회
  checkLikeStatus();
}

/* ==========================
   LIKE API
========================== */

function checkLikeStatus() {

  const token = localStorage.getItem('accessToken');
  if (!token) return; // 비로그인 → 기본 🤍

  $.ajax({
    url: '/api/products/' + currentProductId + '/like',
    method: 'GET',
    headers: {
      Authorization: 'Bearer ' + token
    },
    success: function (res) {
      if (res && res.success) {
        updateLikeUI(res.data.liked);
      }
    }
  });
}

function toggleLike() {

  const token = localStorage.getItem('accessToken');

  if (!token) {
    alert('로그인이 필요합니다.');
    location.href = '/login';
    return;
  }

  $.ajax({
    url: '/api/products/' + currentProductId + '/like',
    method: 'POST',
    headers: {
      Authorization: 'Bearer ' + token
    },
    success: function (res) {

      if (!res || !res.success) {
        alert('찜 처리 실패');
        return;
      }

      updateLikeUI(res.data.liked);
    },
    error: function (xhr) {
      alert(xhr?.responseJSON?.error?.message || '서버 오류');
    }
  });
}

function updateLikeUI(liked) {

  const $btn = $('#likeBtn');

  if (liked) {
    $btn.text('❤️');
    $btn.css({
      borderColor: '#ff4d4f',
      color: '#ff4d4f'
    });
  } else {
    $btn.text('🤍');
    $btn.css({
      borderColor: '#ddd',
      color: '#000'
    });
  }
}

/* ==========================
   UTIL
========================== */

function getSlug() {
  const path = window.location.pathname;
  const parts = path.split('/').filter(Boolean);
  // /products/{slug}
  return parts.length === 2 ? parts[1] : null;
}

function formatPrice(price) {
  const n = typeof price === 'number' ? price : Number(price);
  if (Number.isFinite(n)) return n.toLocaleString('ko-KR');
  return price;
}

function showError(msg) {
  $('#productName').text(msg);
}

function escapeHtml(str) {
  if (!str) return '';
  return String(str)
    .replaceAll('&', '&amp;')
    .replaceAll('<', '&lt;')
    .replaceAll('>', '&gt;')
    .replaceAll('"', '&quot;')
    .replaceAll("'", '&#039;');
}